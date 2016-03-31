package com.electronwill.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Writes Json data.
 *
 * @author TheElectronWill
 */
public class JsonWriter {
	
	protected Writer writer;
	
	/**
	 * <tt>true</tt> to make the output human-friendy by adding spaces and linebreaks.
	 */
	protected boolean humanFriendly;
	
	/**
	 * The current indentation level.
	 */
	protected int indentation = 0;
	
	public JsonWriter() {
		this(new FastStringWriter(), false);
	}
	
	public JsonWriter(boolean humanFriendly) {
		this(new FastStringWriter(), humanFriendly);
	}
	
	public JsonWriter(Writer writer, boolean humanFriendly) {
		this.writer = writer;
		this.humanFriendly = humanFriendly;
	}
	
	/**
	 * Writes a json structure.
	 *
	 * @param structure
	 * @throws java.io.IOException
	 */
	public void write(final Object structure) throws IOException {
		if (structure instanceof Map) {
			writeObject((Map<Object, Object>) structure);
		} else if (structure instanceof byte[]) {
			writeArray((byte[]) structure);
		} else if (structure instanceof short[]) {
			writeArray((short[]) structure);
		} else if (structure instanceof int[]) {
			writeArray((int[]) structure);
		} else if (structure instanceof float[]) {
			writeArray((float[]) structure);
		} else if (structure instanceof double[]) {
			writeArray((double[]) structure);
		} else if (structure instanceof Object[]) {
			writeArray((Object[]) structure);
		} else {
			throw new JsonException(
					"Object " + structure + " of class " + structure.getClass().getName() + " is not a valid JSON structure !");
		}
	}
	
	protected void writeArray(byte[] array) throws IOException {
		writer.append('[');
		for (int i = 0; i < array.length; i++) {
			byte b = array[i];
			writer.append(String.valueOf(b));
			
			// Séparation:
			if (i < array.length - 1) {
				writer.append(',');
			}
		}
		writer.append(']');
	}
	
	protected void writeArray(short[] array) throws IOException {
		writer.append('[');
		for (int i = 0; i < array.length; i++) {
			short s = array[i];
			writer.append(String.valueOf(s));
			
			// Séparation:
			if (i < array.length - 1) {
				writer.append(',');
			}
		}
		writer.append(']');
	}
	
	protected void writeArray(int[] array) throws IOException {
		writer.append('[');
		for (int i = 0; i < array.length; i++) {
			int n = array[i];
			writer.append(String.valueOf(n));
			
			// Séparation:
			if (i < array.length - 1) {
				writer.append(',');
			}
		}
		writer.append(']');
	}
	
	protected void writeArray(float[] array) throws IOException {
		writer.append('[');
		for (int i = 0; i < array.length; i++) {
			float f = array[i];
			writer.append(String.valueOf(f));
			
			// Séparation:
			if (i < array.length - 1) {
				writer.append(',');
			}
		}
		writer.append(']');
	}
	
	protected void writeArray(double[] array) throws IOException {
		writer.append('[');
		for (int i = 0; i < array.length; i++) {
			double d = array[i];
			writer.append(String.valueOf(d));
			
			// Séparation:
			if (i < array.length - 1) {
				writer.append(',');
			}
		}
		writer.append(']');
	}
	
	protected void writeArray(Object[] array) throws IOException {
		// Si on saute des lignes après chaque valeur ou non:
		boolean breakLines = false;
		if (humanFriendly) {
			if (array.length > 9) {
				breakLines = true;
			}
			for (Object o : array) {
				if (o instanceof Map || o instanceof Object[]) {
					breakLines = true;
				}
			}
		}
		
		writer.append('[');
		if (breakLines) {
			writeLine();
			indentation++;
		}
		for (int i = 0; i < array.length; i++) {
			
			if (breakLines) {// Indentation:
				writeIndentation();
			}
			
			Object value = array[i];
			if (value instanceof Map) {
				writeObject((Map<Object, Object>) value);
			} else if (value instanceof byte[]) {
				writeArray((byte[]) value);
			} else if (value instanceof short[]) {
				writeArray((short[]) value);
			} else if (value instanceof int[]) {
				writeArray((int[]) value);
			} else if (value instanceof float[]) {
				writeArray((float[]) value);
			} else if (value instanceof double[]) {
				writeArray((double[]) value);
			} else if (value instanceof Object[]) {
				writeArray((Object[]) value);
			} else if (value instanceof List<?>) {
				writeArray(((List<?>) value).toArray());
			} else if (value instanceof CharSequence) {
				writeCharSequence((CharSequence) value);
			} else if (value instanceof Boolean || value instanceof Number) {
				writer.append(value.toString());
			} else if (value == null) {
				writer.append("null");
			} else {
				writeCharSequence(value.toString());
			}
			
			// Séparation:
			if (i < array.length - 1) {
				writer.append(',');
			}
			if (breakLines) {
				writeLine();
			}
		}
		if (breakLines) {
			indentation--;
			writeIndentation();
		}
		writer.append(']');
	}
	
	protected void writeObject(Map<Object, Object> object) throws IOException {
		Iterator<Map.Entry<Object, Object>> iterator = object.entrySet().iterator();
		writer.append('{');
		if (humanFriendly) {
			writeLine();
		}
		indentation++;
		while (iterator.hasNext()) {
			Map.Entry<Object, Object> entry = iterator.next();
			
			// Indentation:
			writeIndentation();
			
			// On écrit le nom de la valeur:
			CharSequence key = (entry.getKey() instanceof CharSequence) ? (CharSequence) entry.getKey() : entry.getKey().toString();
			String keyString = escapeSequence(key).toString();
			writeCharSequence(keyString);
			
			// Séparation:
			writer.append(':');
			writeSpace();
			
			// On écrit la valeur:
			Object value = entry.getValue();
			if (value instanceof Map) {
				writeObject((Map<Object, Object>) value);
			} else if (value instanceof byte[]) {
				writeArray((byte[]) value);
			} else if (value instanceof short[]) {
				writeArray((short[]) value);
			} else if (value instanceof int[]) {
				writeArray((int[]) value);
			} else if (value instanceof float[]) {
				writeArray((float[]) value);
			} else if (value instanceof double[]) {
				writeArray((double[]) value);
			} else if (value instanceof Object[]) {
				writeArray((Object[]) value);
			} else if (value instanceof List<?>) {
				writeArray(((List<?>) value).toArray());
			} else if (value instanceof Boolean || value instanceof Number) {
				writer.append(value.toString());
			} else if (value == null) {
				writer.append("null");
			} else {
				writeCharSequence(value.toString());
			}
			
			// Séparation:
			if (iterator.hasNext()) {
				writer.append(',');
			}
			// Nouvelle ligne:
			writeLine();
		}
		indentation--;
		writeIndentation();
		writer.append('}');
	}
	
	protected void writeSpace() throws IOException {
		if (!humanFriendly) {
			return;
		}
		writer.append(' ');
	}
	
	protected void writeLine() throws IOException {
		if (!humanFriendly) {
			return;
		}
		writer.append('\n');
	}
	
	/**
	 * Writes two spaces {@link #indentation} times.
	 *
	 * @throws java.io.IOException
	 */
	protected void writeIndentation() throws IOException {
		if (!humanFriendly) {
			return;
		}
		for (int i = 0; i < indentation; i++) {
			writer.append("  ");// Deux espaces à la suite
		}
	}
	
	protected void writeCharSequence(CharSequence s) throws IOException {
		StringBuilder sb = escapeSequence(s);
		String escaped = sb.toString();
		writer.append('"');
		writer.append(escaped);
		writer.append('"');
	}
	
	/**
	 * Escapes every character that need to be escaped in a CharSequence.
	 */
	protected StringBuilder escapeSequence(final CharSequence cs) {
		StringBuilder sb = new StringBuilder(cs.length());
		for (int i = 0; i < cs.length(); i++) {
			char c = cs.charAt(i);
			if (c == '\\' || c == '"') {// Le caractère est \ ou " et ne peut pas se trouver comme
										// ça dans la chaîne de caractère.
				sb.append('\\');// On ajoute \ devant le caractère
			}
			sb.append(c);
		}
		return sb;
	}
	
	/**
	 * If the writer is a FastStringWriter, returns the result of its toString() method. Otherwise, returns
	 * super.toString().
	 */
	@Override
	public String toString() {
		if (writer instanceof FastStringWriter) {
			return writer.toString();
		}
		return super.toString();
	}
	
}
