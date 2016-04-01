package com.electronwill.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonReader2 {
	private final String data;
	private int pos = 0;
	
	public JsonReader2(String data) {
		this.data = data;
	}
	
	public Map<String, Object> readObject() {
		char firstChar = nextUseful();
		if (firstChar != '{')
			throw new JsonException("Invalid character '" + firstChar + "' at the beginning of a JSON object, at position " + pos);
		return nextObject();
	}
	
	public List<Object> readArray() {
		char firstChar = nextUseful();
		System.out.println("firstChar: " + (int) firstChar);
		if (firstChar != '[')
			throw new JsonException("Invalid character '" + firstChar + "' at the beginning of a JSON array, at position " + pos);
		return nextArray();
	}
	
	private Map<String, Object> nextObject() {
		Map<String, Object> map = new HashMap<>();
		while (true) {
			char keyFirstChar = nextUseful();
			if (keyFirstChar != '"')
				throw new JsonException("Invalid character at the beginning of a key, at position " + pos);
			String key = nextString();
			
			char sep = nextUseful();
			if (sep != ':')
				throw new JsonException("Invalid separator '" + sep + "' at position " + pos);
				
			char valueFirstChar = nextUseful();
			Object value = nextValue(valueFirstChar, ',', '}', ' ', '\t', '\n', '\r');
			map.put(key, value);
			
			char after = nextUseful();
			if (after == '}') {
				return map;
			} else if (after != ',') {
				throw new JsonException("Invalid separator '" + after + "' at position " + pos);
			}
			
		}
	}
	
	private List<Object> nextArray() {
		ArrayList<Object> list = new ArrayList<>();
		while (true) {
			char firstChar = nextUseful();
			if (firstChar == ']') {// empty array
				list.trimToSize();
				return list;
			}
			Object value = nextValue(firstChar, ',', ']', ' ', '\t', '\n', '\r');
			list.add(value);
			char after = nextUseful();
			if (after == ']') {
				return list;
			} else if (after != ',') {
				throw new JsonException("Invalid separator '" + after + "'at position " + pos);
			}
		}
	}
	
	private Object nextValue(char firstChar, char... allowedEnds) {
		switch (firstChar) {
			case '+':
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
				pos--;// to include the first char in the string below
				String number = until(allowedEnds);
				if (number.indexOf('.') > 0)
					return Double.parseDouble(number);
				if (number.length() < 10)
					return Integer.parseInt(number);
				return Long.parseLong(number);
			case '"':
				return nextString();
			case 't':// true
			case 'f':// false
				pos--;// to include the first char in the string below
				String bool = until(allowedEnds);
				return Boolean.parseBoolean(bool);
			case 'n':// null
				String valNull = until(allowedEnds);
				if (valNull.equals("ull"))
					return null;
				throw new JsonException("Invalid value \"" + valNull + "\" at position " + pos);
			case '{':
				return nextObject();
			case '[':
				return nextArray();
			default:
				throw new JsonException("Invalid character '" + firstChar + "' at the beginning of the value at position " + pos);
		}
		
	}
	
	private String until(char... ends) {
		for (int i = pos; i < data.length(); i++) {
			char c = data.charAt(i);
			for (char end : ends) {
				if (c == end) {
					String str = data.substring(pos, i);
					pos = i;
					return str;
				}
			}
		}
		throw new JsonException("Invalid end of data");
	}
	
	private String nextString() {
		StringBuilder sb = new StringBuilder();
		boolean escape = false;
		while (hasNext()) {
			char c = next();
			if (escape) {
				sb.append(unescape(c));
				escape = false;
			} else if (c == '\\') {
				escape = true;
			} else if (c == '"') {// end of the string
				return sb.toString();
			} else {
				sb.append(c);
			}
		}
		throw new JsonException("Invalid String at position " + pos + ": it nerver ends");
	}
	
	private boolean hasNext() {
		return pos < data.length();
	}
	
	private char next() {
		return data.charAt(pos++);
	}
	
	private char nextUseful() {
		char c = ' ';
		while (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
			if (!hasNext())
				throw new JsonException("Invalid end of data: things are missing!");
			c = next();
		}
		return c;
	}
	
	private char unescape(char c) {
		switch (c) {
			case 'b':
				return '\b';
			case 't':
				return '\t';
			case 'n':
				return '\n';
			case 'f':
				return '\f';
			case 'r':
				return '\r';
			case '"':
				return '"';
			case '\\':
				return '\\';
			case 'u': {// unicode uXXXX
				if (data.length() - pos < 5)
					throw new JsonException("Invalid unicode code point at position " + pos);
				String unicode = data.substring(pos, pos + 4);
				pos += 4;
				try {
					int hexVal = Integer.parseInt(unicode, 16);
					return (char) hexVal;
				} catch (NumberFormatException ex) {
					throw new JsonException("Invalid unicode code point at position " + pos, ex);
				}
			}
			default:
				throw new JsonException("Invalid escape sequence: \"\\" + c + "\" at position " + pos);
		}
	}
	
}
