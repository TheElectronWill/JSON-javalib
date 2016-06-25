package com.electronwill.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Writes json data.
 *
 * @author TheElectronWill
 */
public final class JsonWriter {

	private static String escape(char c) {
		switch (c) {
			case '\b':
				return "\\b";
			case '\t':
				return "\\t";
			case '\n':
				return "\\n";
			case '\\':
				return "\\\\";
			case '\r':
				return "\\r";
			case '\f':
				return "\\f";
			case '"':
				return "\\\"";
			default:
				return new StringBuilder(1).append(c).toString();
		}
	}

	private final Writer writer;
	private final boolean humanFriendly;
	private int indentation = 0;

	public JsonWriter(Writer writer, boolean humanFriendly) {
		this.writer = writer;
		this.humanFriendly = humanFriendly;
	}

	public void writeArray(byte[] array) throws IOException {
		writer.write('[');
		for (int i = 0; i < array.length; i++) {
			byte b = array[i];
			writer.write(String.valueOf(b));
			if (i < array.length - 1) {
				writer.write(',');
			}
		}
		writer.write(']');
	}

	public void writeArray(double[] array) throws IOException {
		writer.write('[');
		for (int i = 0; i < array.length; i++) {
			double d = array[i];
			writer.write(String.valueOf(d));
			if (i < array.length - 1) {
				writer.write(',');
			}
		}
		writer.write(']');
	}

	public void writeArray(float[] array) throws IOException {
		writer.write('[');
		for (int i = 0; i < array.length; i++) {
			float f = array[i];
			writer.write(String.valueOf(f));
			if (i < array.length - 1) {
				writer.write(',');
			}
		}
		writer.write(']');
	}

	public void writeArray(int[] array) throws IOException {
		writer.write('[');
		for (int i = 0; i < array.length; i++) {
			int n = array[i];
			writer.write(String.valueOf(n));
			if (i < array.length - 1) {
				writer.write(',');
			}
		}
		writer.write(']');
	}

	/**
	 * Writes a json array.
	 */
	public void writeArray(List<Object> jsonArray) throws IOException {
		Iterator<Object> it = jsonArray.iterator();
		writer.write('[');
		while (it.hasNext()) {
			Object value = it.next();
			if (value instanceof Map) {
				writeLine();
			}
			writeValue(value);
			if (it.hasNext()) {
				writer.write(',');
			}
		}
		writer.write(']');
	}

	public void writeArray(long[] array) throws IOException {
		writer.write('[');
		for (int i = 0; i < array.length; i++) {
			long n = array[i];
			writer.write(String.valueOf(n));
			if (i < array.length - 1) {
				writer.write(',');
			}
		}
		writer.write(']');
	}

	/**
	 * Writes a json array.
	 */
	public void writeArray(Object[] jsonArray) throws IOException {
		writer.write('[');
		for (int i = 0; i < jsonArray.length; i++) {
			Object value = jsonArray[i];
			if (value instanceof Map) {
				writeLine();
			}
			writeValue(value);
			if (i < jsonArray.length - 1) {
				writer.write(',');
			}
		}
		writer.write(']');
	}

	public void writeArray(short[] array) throws IOException {
		writer.write('[');
		for (int i = 0; i < array.length; i++) {
			short s = array[i];
			writer.write(String.valueOf(s));
			if (i < array.length - 1) {
				writer.write(',');
			}
		}
		writer.write(']');
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
			writer.write("  ");
		}
	}

	protected void writeLine() throws IOException {
		if (!humanFriendly) {
			return;
		}
		writer.write('\n');
	}

	/**
	 * Writes a json object.
	 */
	public void writeObject(Map<String, Object> jsonObject) throws IOException {
		Iterator<Map.Entry<String, Object>> iterator = jsonObject.entrySet().iterator();
		writer.write('{');
		if (humanFriendly) {
			writeLine();
		}
		indentation++;
		while (iterator.hasNext()) {
			Map.Entry<String, Object> entry = iterator.next();
			writeIndentation();

			// Write the key
			String key = entry.getKey();
			writeString(key);

			// Write the separator
			writer.write(':');
			writeSpace();

			// Write the value
			Object value = entry.getValue();
			writeValue(value);

			// Write the separator
			if (iterator.hasNext()) {
				writer.write(',');
			}
			writeLine();
		}
		indentation--;
		writeIndentation();
		writer.write('}');
	}

	protected void writeSpace() throws IOException {
		if (!humanFriendly) {
			return;
		}
		writer.write(' ');
	}

	private void writeString(CharSequence str) throws IOException {
		writer.write('"');
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			writer.write(escape(c));
		}
		writer.write('"');
	}

	private void writeValue(Object value) throws IOException {
		if (value instanceof Map) {
			writeObject((Map) value);
		} else if (value instanceof List) {
			writeArray((List) value);
		} else if (value instanceof String) {
			writeString((String) value);
		} else if (value instanceof byte[]) {
			writeArray((byte[]) value);
		} else if (value instanceof short[]) {
			writeArray((short[]) value);
		} else if (value instanceof int[]) {
			writeArray((int[]) value);
		} else if (value instanceof long[]) {
			writeArray((long[]) value);
		} else if (value instanceof float[]) {
			writeArray((float[]) value);
		} else if (value instanceof double[]) {
			writeArray((double[]) value);
		} else if (value instanceof Object[]) {
			writeArray((Object[]) value);
		} else {
			writer.write(String.valueOf(value));
		}
	}

}
