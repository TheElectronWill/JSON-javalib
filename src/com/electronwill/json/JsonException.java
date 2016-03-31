package com.electronwill.json;

/**
 * Thrown when a problem occur during parsing or writing Json data.
 *
 * @author TheElectronWill
 */
public class JsonException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public JsonException() {}
	
	public JsonException(String message) {
		super(message);
	}
	
	public JsonException(Throwable cause) {
		super(cause);
	}
	
	public JsonException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public JsonException(String thingName, Object thing, String location, int charLocation, String expected) {
		super(new StringBuilder()
				.append("Invalid ")
				.append(thingName)
				.append(" ")
				.append(thing)
				.append(" ")
				.append(location)
				.append(".Expected ")
				.append(expected)
				.append("; at position")
				.append(charLocation)
				.toString());
	}
	
	public JsonException(String thingName, Object thing, String location, int charLocation) {
		super(new StringBuilder()
				.append("Invalid ")
				.append(thingName)
				.append(" ")
				.append(thing)
				.append(" ")
				.append(location)
				.append("; at position")
				.append(charLocation)
				.toString());
	}
	
}
