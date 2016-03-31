package com.electronwill.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 * Utility class for reading and writing JSON data. This class internally uses {@link JsonReader} and {@link JsonWriter}
 * .
 *
 * @author TheElectronWill
 */
public final class Json {
	
	private Json() {}
	
	/**
	 * Dumps a given Json Structure (i.e. either an Object[] or a Map) into a String.
	 *
	 * @param jsonStructure the structure to dump
	 * @param humanFriendly <code>true</true> if we should indent our output, <code>false</code> otherwise.
	 * @return a String which represents this structure
	 * @throws JsonException if a problem occur
	 */
	public static String dump(Object jsonStructure, boolean humanFriendly) {
		JsonWriter writer = new JsonWriter(humanFriendly);
		try {
			writer.write(jsonStructure);
		} catch (IOException ex) {
			throw new JsonException(ex);
		}
		return writer.toString();
	}
	
	/**
	 * Dumps all the given Json structures into a String.
	 *
	 * @param jsonStructures the several structures to dump
	 * @param humanFriendly <code>true</true> if we should indent our output, <code>false</code> otherwise.
	 * @return a String which represents this structure
	 * @throws JsonException if a problem occur
	 */
	public static String dumpAll(Iterable<Object> jsonStructures, boolean humanFriendly) {
		JsonWriter writer = new JsonWriter(humanFriendly);
		for (Object structure : jsonStructures) {
			try {
				writer.write(structure);
			} catch (IOException ex) {
				throw new JsonException(ex);
			}
		}
		return writer.toString();
	}
	
	/**
	 * Dumps a given Json structures into a Writer.
	 *
	 * @param jsonStructure the several structures to dump
	 * @param humanFriendly <code>true</true> if we should indent our output, <code>false</code> otherwise.
	 * @param output the output Writer
	 * @throws JsonException if a problem occur
	 */
	public static void dump(Object jsonStructure, Writer output, boolean humanFriendly) {
		JsonWriter writer = new JsonWriter(output, humanFriendly);
		try {
			writer.write(jsonStructure);
		} catch (IOException ex) {
			throw new JsonException(ex);
		}
		
	}
	
	/**
	 * Dumps all the given Json structures into a Writer.
	 *
	 * @param jsonStructures the several structures to dump
	 * @param humanFriendly <code>true</true> if we should indent our output, <code>false</code> otherwise.
	 * @param output the output Writer
	 * @throws JsonException if a problem occur
	 */
	public static void dumpAll(Iterable<Object> jsonStructures, Writer output, boolean humanFriendly) {
		JsonWriter writer = new JsonWriter(output, humanFriendly);
		for (Object structure : jsonStructures) {
			try {
				writer.write(structure);
			} catch (IOException ex) {
				throw new JsonException(ex);
			}
		}
	}
	
	/**
	 * Reads a Json structure from a String.
	 *
	 * @param json a String containing (at least) a Json structure
	 * @return the corresponding Json structure: eighter a {@code Map<String, Object>} or an <code>Object[]</code>.
	 * @throws JsonException if a problem occur
	 */
	public static Object load(String json) {
		JsonReader parser = new JsonReader(json);
		try {
			return parser.parse();
		} catch (IOException ex) {
			throw new JsonException(ex);
		}
	}
	
	/**
	 * Reads a Json structure from an Inputstream.
	 *
	 * @param in an InputStream containing a Json structure
	 * @return the corresponding Json structure: eighter a {@code Map<String, Object>} or an <code>Object[]</code>.
	 * @throws JsonException if a problem occur
	 */
	public static Object load(InputStream in) {
		try {
			JsonReader parser = new JsonReader(new InputStreamReader(in));
			return parser.parse();
		} catch (IOException ex) {
			throw new JsonException(ex);
		}
	}
	
	/**
	 * Reads a Json structure with the given Reader.
	 *
	 * @param reader a Reader which can read a Json structure.
	 * @return the corresponding Json structure: eighter a {@code Map<String, Object>} or an <code>Object[]</code>.
	 * @throws JsonException if a problem occur
	 */
	public static Object load(Reader reader) {
		try {
			JsonReader parser = new JsonReader(reader);
			return parser.parse();
		} catch (IOException ex) {
			throw new JsonException(ex);
		}
	}
	
	/**
	 * Reads all the Json structures in a given String.
	 *
	 * @param json a String containing at least one Json structure.
	 * @return the corresponding Json structure: eighter a {@code Map<String, Object>} or an <code>Object[]</code>.
	 */
	public static List<Object> loadAll(String json) {
		JsonReader parser = new JsonReader(json);
		try {
			return parser.parseAll();
		} catch (IOException ex) {
			throw new JsonException(ex);
		}
	}
	
	/**
	 * Reads all the Json structures in a given InputStream.
	 *
	 * @param in an InputStream containing at least one Json structure.
	 * @return {@code List<Map<String, Object>>} ou {@code List<Object[]>}.@return the corresponding Json structure:
	 *         eighter a {@code Map<String, Object>} or an <code>Object[]</code>.
	 * @throws JsonException if a problem occur
	 */
	public static List<Object> loadAll(InputStream in) {
		try {
			JsonReader parser = new JsonReader(new InputStreamReader(in));
			return parser.parseAll();
		} catch (IOException ex) {
			throw new JsonException(ex);
		}
	}
	
	/**
	 * Reads all the Json structures with the given Reader.
	 *
	 * @param reader an Reader which can read at least one Json structure.
	 * @return {@code List<Map<String, Object>>} ou {@code List<Object[]>}.@return the corresponding Json structure:
	 *         eighter a {@code Map<String, Object>} or an <code>Object[]</code>.
	 * @throws JsonException if a problem occur
	 */
	public static List<Object> loadAll(Reader reader) {
		try {
			JsonReader parser = new JsonReader(reader);
			return parser.parseAll();
		} catch (IOException ex) {
			throw new JsonException(ex);
		}
	}
	
}
