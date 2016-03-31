package com.electronwill.json;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Parses Json data.
 *
 * @author TheElectronWill
 */
public class JsonReader {
	
	protected char[] chars;
	protected int i = 0;
	protected int size;
	protected Reader reader;
	protected int blockSize;
	
	public JsonReader(String s) {
		this.chars = s.toCharArray();
		reader = null;
		size = chars.length;
		blockSize = -1;
	}
	
	public JsonReader(char[] chars, int from, int to) {
		this.chars = Arrays.copyOfRange(chars, from, to);
		reader = null;
		size = chars.length;
		blockSize = -1;
	}
	
	public JsonReader(char[] chars) {
		this.chars = chars;
		reader = null;
		size = chars.length;
		blockSize = -1;
	}
	
	public JsonReader(char[] chars, boolean direct) {
		if (direct) {
			this.chars = chars;
		} else {
			this.chars = Arrays.copyOf(chars, chars.length);
		}
		reader = null;
		size = chars.length;
		blockSize = -1;
	}
	
	public JsonReader(Reader reader) throws IOException {
		this.reader = reader;
		blockSize = 8192;
		chars = new char[blockSize];
		size = 0;
	}
	
	public JsonReader(Reader reader, int blockSize) throws IOException {
		this.reader = reader;
		this.blockSize = blockSize;
		chars = new char[blockSize];
		size = 0;
	}
	
	/**
	 * Reads the Json structure in {@link #chars}.
	 *
	 * @return
	 * @throws java.io.IOException
	 */
	public Object parse() throws IOException {
		if (reader != null && i == size) {
			fill();
		}
		char first = nextUseful();// Le tout premier caractère utile des données JSON.
		switch (first) {
			case '{':
				return nextObject();
			case '[':
				return nextArray();
			default:
				throw new JsonException("Invalid character n°" + i + " " + first + " at the beginning of a structure. Expected { or [");
		}
	}
	
	/**
	 * Read all the Json structures in {@link #chars}.
	 *
	 * @return
	 * @throws java.io.IOException
	 */
	public List<Object> parseAll() throws IOException {
		ArrayList<Object> list = new ArrayList<>();
		while (true) {
			Object structure = parse();
			list.add(structure);
			while (true) {
				if (i == size) {
					if (reader == null || !tryRefill()) {
						return list;
					}
				}
				char c = chars[i++];
				if (isUseful(c) && c != '{' && c != '[') {
					throw new JsonException("Invalid structure " + list.size() + ". A structure begins with { or [");
				}
			}
		}
	}
	
	/**
	 * Reads a Json object.
	 *
	 * @return the corresponding {@code Map<String, Object>}
	 */
	public Map<String, Object> parseObject() {
		char first = nextUseful();
		if (first != '{') {
			throw new JsonException("Invalid character n°" + i + " " + first + " at the beginning of a Json object. Expected {");
		}
		return nextObject();
	}
	
	/**
	 * Reads a Json array
	 *
	 * @return the corresponding <code>Object[]</code>
	 */
	public Object[] parseArray() {
		char first = nextUseful();
		if (first != '[') {
			throw new JsonException("Invalid character n°" + i + " " + first + " at the beginning of a Json array. Expected [");
		}
		return nextArray();
	}
	
	/**
	 * Reads the next Json Object until a '}' character is read. The '{' character must have been read BEFORE calling
	 * this method.<br>
	 * This method DOES read the last '}' character.
	 *
	 * @return the corresponding {@code Map<String, object>}
	 */
	protected Map<String, Object> nextObject() {
		HashMap<String, Object> objectMap = new HashMap<>();
		// boolean nameExpected = true;//Si on a lu une virgule et que donc on devrait lire un nom de paire nom/valeur
		// boolean valueExpected = false;//Si on a lu deux points et que donc on devrait lire une valeur de paire
		// nom/valeur
		while (true) {
			char nameFirst = nextUseful();// Premier caractère du nom
			if (nameFirst != '\"') {
				throw new JsonException("character", nameFirst, " at the beginning of a name", i, "\"");
			}
			String name = nextString();
			
			char separator = nextUseful();
			if (separator != ':') {
				throw new JsonException("separator", separator, " between a name and a value", i, ":");
			}
			char valueFirst = nextUseful();// Premier caractère de la valeur
			Object value = nextValue(valueFirst);
			objectMap.put(name, value);
			
			char afterEntry = nextUseful();
			if (afterEntry == '}') {
				return objectMap;
			}
			if (afterEntry != ',') {
				throw new JsonException("separator", afterEntry, " between two name-value pairs", i, ",");
			}
		}
	}
	
	/**
	 * Reads the next Json Array until a ']' character is read. The '[' character must have been read BEFORE calling
	 * this method.<br>
	 * This method DOES read the last ']' character.
	 *
	 * @return the corresponding <code>Object[]</code>
	 */
	protected Object[] nextArray() {
		ArrayList<Object> list = new ArrayList<>();
		while (true) {
			char c = nextUseful();
			Object value = nextValue(c);
			list.add(value);
			
			char afterEntry = nextUseful();// La virgule après, ou le crochet si l'array est terminé.
			if (afterEntry == ']') {
				return list.toArray();// C'est fini, on retourne le résultat.
			}
			if (afterEntry != ',') {
				throw new JsonException("separator", afterEntry, " between two name-value pairs", i, ",");
			}
			// Si on vient de lire une virgule, alors on attend un valeur juste après:
		}
		
	}
	
	/**
	 * Read the next value. The first character of the value must have been read BEFORE calling this method<br>
	 * Cette méthode fait comme si elle n'avait pas lu la virgule, l'accolade ou le crochet qui se trouve après la
	 * valeur
	 *
	 * @param first le premier caractère de la valeur.
	 * @return
	 */
	protected Object nextValue(char first) {
		char c2, c3, c4;
		switch (first) {
			case '-':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return nextNumber(first);
			case '"':
				return nextString();
			case '[':
				return nextArray();
			case '{':
				return nextObject();
			case 'n':// Doit être "null"
				c2 = next();// normalement 'u'
				c3 = next();// normalement 'l'
				c4 = next();// normalement 'l'
				if (c2 != 'u' || c3 != 'l' || c4 != 'l') {
					throw new JsonException("Invalid value " + first + c2 + c3 + c4);
				}
				return null;
			case 't':// Doit être "true"
				c2 = next();// normalement 'r'
				c3 = next();// normalement 'u'
				c4 = next();// normalement 'e'
				if (c2 != 'r' || c3 != 'u' || c4 != 'e') {
					throw new JsonException("Invalid value " + first + c2 + c3 + c4);
				}
				return true;
			case 'f':// Doit être "false":
				c2 = next();// normalement 'a'
				c3 = next();// normalement 'l'
				c4 = next();// normalement 's'
				char c5 = next();// normalement 'e'
				if (c2 != 'a' || c3 != 'l' || c4 != 's' || c5 != 'e') {
					throw new JsonException("Invalid value " + first + c2 + c3 + c4 + c5);
				}
				return false;
			default:
				throw new JsonException("character", first, "at the beginning of a value", i,
						"[, {, \", null, true, false, - (minus) or a digit.");
		}
	}
	
	/**
	 * Lit tous les caractères jusqu'à ce qu'un espace, une virgule, un crochet fermant ou une accolade fermante soit
	 * rencontrée. <br>
	 * Cette méthode fait comme si elle n'avait pas lu la virgule, le crochet ou l'accolade terminant le nombre.
	 *
	 * @param first le premier caractère qui a été lu.
	 * @return
	 */
	protected Number nextNumber(char first) {
		StringBuilder sb = new StringBuilder();
		sb.append(first);
		while (true) {
			char c = next();
			if ((c >= '0' && c <= '9') || c == '.' || c == '-' || c == 'e' || c == 'E' || c == '+') {
				sb.append(c);
			} else if (isUseless(c)) {// fin du nombre
				break;
			} else if (c == ',' || c == ']' || c == '}') {// fin du nombre
				i--;// Comme si on n'avait pas lu le token.
				break;
			} else {
				throw new JsonException("character", c, "in a number value", i);
			}
		}
		String nString = sb.toString();
		double d = Double.parseDouble(nString);
		long l = (long) d;
		if (l == d) {// Pas de partie décimale
			int n = (int) l;
			return n == l ? n : l;
		}
		return d;
	}
	
	/**
	 * Lit tous les caractères suivants jusqu'à ce que des guillemets (") soient rencontré. Les premiers guillemets
	 * doivent déjà avoir été lus avant d'appeler cette méthode. <br>
	 * La chaîne de caractère retournée ne contient <b>pas</b> les guillemets. Les caractères réservés peuvent être
	 * utilisés avec \ devant.<br>
	 * Cette méthode lit les guillemets qui terminent la chaîne de caractères.
	 *
	 * @return un objet String
	 */
	protected String nextString() {
		StringBuilder sb = new StringBuilder();
		boolean escape = false;// true si le prochain caractère est échappé
		char[] hexaCode = new char[4];// stockage des chiffres hexadécimaux
		int leftHexa = 0;// les x prochains chiffres hexadécimaux comme un code de caractère.
		while (true) {
			char c = next();
			if (leftHexa > 0) {
				hexaCode[4 - leftHexa] = c;
				leftHexa--;
				if (leftHexa == 0) {
					String hexaCodeString = new String(hexaCode);
					try {
						int codePoint = Integer.parseInt(hexaCodeString, 16);// Lit le nombre hexadécimal
						char unicode = (char) codePoint;
						sb.append(unicode);
					} catch (NumberFormatException ex) {
						throw new JsonException("Invalid hexadecimal codepoint \\u" + hexaCodeString, ex);
					}
				}
				continue;
			}
			if (c == '\\') {
				escape = true;
				continue;
			}
			if (escape) {
				escape = false;
				// Note: \' n'est PAS un échappement valide en JSON car les chaînes de caractères Json sont délimitées
				// par des guillemets ".
				switch (c) {
					case '\\':// backshlash \
						sb.append('\\');
						break;
					case '\"':// guillemets "
						sb.append('\"');
						break;
					case '/':// JSON permet d'échapper les slashs avec '\/'
						sb.append('/');
						break;
					case 'b':
						sb.append('\b');
						break;
					case 'f':
						sb.append('\f');
						break;
					case 'n':
						sb.append('\n');
						break;
					case 'r':
						sb.append('\r');
						break;
					case 't':
						sb.append('\t');
						break;
					case 'u':// Les 4 prochains caractères doivent former un code de caractère héxadécimal
						leftHexa = 4;
						break;
					default:// C'est pas un échappement valide !
						throw new JsonException("Invalid escape sequence \\" + c);
				}
			} else if (c == '\"') {// Fin de la chaîne de caractères
				return sb.toString();
			} else {
				sb.append(c);
			}
			
		}
	}
	
	/**
	 * Fills all the buffer by reading from the InputStream.
	 *
	 * @throws IOException
	 */
	protected void fill() throws IOException {
		int read = reader.read(chars);
		if (read == -1) {
			throw new IOException("End of stream");
		}
		size = read;
		i = 0;
	}
	
	protected boolean tryFill() {
		try {
			fill();
			return true;
		} catch (IOException ex) {
			return false;
		}
	}
	
	/**
	 * Keep the remaining data, and read further bytes from the Reader.
	 *
	 * @throws IOException
	 */
	protected void refill() throws IOException {
		if (i == size) {
			i = size - 1;// On garde le dernier caractère, au cas où.
		}
		int left = size - i;// Nombre de caractères restants.
		// On décale tout au début:
		System.arraycopy(chars, i, chars, 0, left);
		
		int read = reader.read(chars, left, blockSize - left);// caractères lus
		if (read == -1) {
			throw new IOException("End of stream.");
		}
		size = left + read;// cactères restants + caractères lus.
		i = 0;
	}
	
	/**
	 * Essaie de lire plus de caractères.
	 *
	 * @return <tt>true</tt> si plus de caractères ont été lus, sinon <tt>false</tt> (et ce quelque soit la raison de
	 *         l'échec).
	 */
	protected boolean tryRefill() {
		try {
			refill();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * donne le prochain caractère et incrémente la position <tt>i</tt>. Cette méthode tente si besoin de lire plus de
	 * données avec le Reader, s'il existe.
	 *
	 * @return le prochain caractère
	 */
	protected char next() {
		if (i == size) {
			if (reader == null) {
				throw new JsonException("Not enough chars available !");
			} else {
				try {
					refill();
				} catch (IOException ex) {
					throw new JsonException("Not enough chars available !", ex);
				}
			}
		}
		return chars[i++];
	}
	
	/**
	 * donne le prochain caractère sans avancer la position.
	 *
	 * @return le prochain caractère, ou {@code <tt>Optional.empty()</tt>} s'il n'y en a pas.
	 */
	protected Optional<Character> lookAtNext() {
		if (i == size) {
			if (reader == null) {
				return Optional.empty();
			} else {
				try {
					refill();
				} catch (IOException ex) {
					return Optional.empty();
				}
			}
		}
		return Optional.of(chars[i]);
	}
	
	/**
	 * le prochain caractère qui n'est ni un espace ni une tabulation (\t) ni un saut de ligne (\r ou \c).
	 *
	 * @return
	 */
	protected char nextUseful() {
		while (true) {
			char c = next();
			if (!(c == ' ' || c == '\t' || c == '\n' || c == '\r')) {
				return c;
			}
		}
		
	}
	
	protected boolean isUseful(char c) {
		return !(c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
	
	protected boolean isUseless(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
	
}
