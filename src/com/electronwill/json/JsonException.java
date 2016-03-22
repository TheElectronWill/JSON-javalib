/*
 * Copyright (C) 2015 ElectronWill
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.electronwill.json;

/**
 * Thrown when a problem occur during parsing or writing Json data.
 *
 * @author TheElectronWill
 */
public class JsonException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates an empty JSONExceptipon.
	 */
	public JsonException() {}
	
	/**
	 * Creates a new JSONException with the given message.
	 *
	 * @param message the error message
	 */
	public JsonException(String message) {
		super(message);
	}
	
	/**
	 * Creates a new JSONException with the given cause.
	 *
	 * @param cause the cause of this exception
	 */
	public JsonException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Creates a new JSONException with the given message and cause.
	 *
	 * @param message the error message
	 * @param cause the cause
	 */
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
