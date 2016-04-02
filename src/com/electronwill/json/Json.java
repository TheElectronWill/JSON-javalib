package com.electronwill.json;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading and writing JSON data. This class internally uses {@link JsonReader} and {@link JsonWriter}
 * .
 *
 * @author TheElectronWill
 */
public final class Json {
	
	/**
	 * Reads a json array from a Reader.
	 */
	public static List<Object> readArray(Reader reader) throws IOException {
		String data = readToString(reader);
		JsonReader jr = new JsonReader(data);
		return jr.readArray();
	}
	
	/**
	 * Reads a json array from a String.
	 */
	public static List<Object> readArray(String data) throws IOException {
		JsonReader jr = new JsonReader(data);
		return jr.readArray();
	}
	
	/**
	 * Reads a json object from a Reader.
	 */
	public static Map<String, Object> readObject(Reader reader) throws IOException {
		String data = readToString(reader);
		JsonReader jr = new JsonReader(data);
		return jr.readObject();
	}
	
	/**
	 * Reads a json object from a String.
	 */
	public static Map<String, Object> readObject(String data) throws IOException {
		JsonReader jr = new JsonReader(data);
		return jr.readObject();
	}
	
	/**
	 * Reads a json object or a json array, depending on the first non space (and non newline) character read.
	 */
	public static Object readObjectOrArray(Reader reader) throws IOException {
		String data = readToString(reader);
		JsonReader jr = new JsonReader(data);
		return jr.readObjectOrArray();
	}
	
	/**
	 * Reads a json object or a json array, depending on the first non space (and non newline) character read.
	 */
	public static Object readObjectOrArray(String data) throws IOException {
		JsonReader jr = new JsonReader(data);
		return jr.readObjectOrArray();
	}
	
	private static String readToString(Reader reader) throws IOException {
		StringBuilder sb = new StringBuilder(4096);
		char[] buf = new char[4096];
		int read;
		while ((read = reader.read(buf)) != -1) {
			sb.append(buf, 0, read);
		}
		return sb.toString();
	}
	
	/**
	 * Writes a json array to a Writer. This method is equivalent to {@code write(jsonArray, writer, false)}, so it
	 * isn't human friendly.
	 */
	public static void write(List<Object> jsonArray, Writer writer) throws IOException {
		write(jsonArray, writer, false);
	}
	
	/**
	 * Writes a json array to a Writer.
	 * 
	 * @param true to indent and space the output, false to compact everything
	 */
	public static void write(List<Object> jsonArray, Writer writer, boolean humanFriendly) throws IOException {
		JsonWriter jw = new JsonWriter(writer, humanFriendly);
		jw.writeArray(jsonArray);
	}
	
	/**
	 * Writes a json object to a Writer. This method is equivalent to {@code write(jsonObject, writer, false)}, so it
	 * isn't human friendly.
	 */
	public static void write(Map<String, Object> jsonObject, Writer writer) throws IOException {
		write(jsonObject, writer, false);
	}
	
	/**
	 * Writes a json object to a Writer.
	 * 
	 * @param true to indent and space the output, false to compact everything
	 */
	public static void write(Map<String, Object> jsonObject, Writer writer, boolean humanFriendly) throws IOException {
		JsonWriter jw = new JsonWriter(writer, humanFriendly);
		jw.writeObject(jsonObject);
	}
	
	/**
	 * Writes a json array to a String. This method is equivalent to {@code write(jsonArray, false)}, so it isn't human
	 * friendly.
	 */
	public static void writeToString(List<Object> jsonArray) throws IOException {
		writeToString(jsonArray, false);
	}
	
	/**
	 * Writes a json array to a String.
	 * 
	 * @param true to indent and space the output, false to compact everything
	 */
	public static void writeToString(List<Object> jsonArray, boolean humanFriendly) throws IOException {
		FastStringWriter stringWriter = new FastStringWriter();
		write(jsonArray, stringWriter, humanFriendly);
	}
	
	/**
	 * Writes a json object to a Writer. This method is equivalent to {@code write(jsonObject, writer, false)}, so it
	 * isn't human friendly.
	 */
	public static void writeToString(Map<String, Object> jsonObject) throws IOException {
		writeToString(jsonObject, false);
	}
	
	/**
	 * Writes a json object to a String.
	 * 
	 * @param true to indent and space the output, false to compact everything
	 */
	public static void writeToString(Map<String, Object> jsonObject, boolean humanFriendly) throws IOException {
		FastStringWriter stringWriter = new FastStringWriter();
		write(jsonObject, stringWriter, humanFriendly);
	}
	
	private Json() {}
	
}
