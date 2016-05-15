package com.electronwill.json;

import java.io.Writer;

/**
 * A Writer that writes in a StringBuilder. It isn't thread-safe.
 */
public class FastStringWriter extends Writer {
	
	/**
	 * The underlying StringBuilder. Everything is appended to it.
	 */
	private final StringBuilder sb;
	
	/**
	 * Creates a new FastStringWriter with a default StringBuilder
	 */
	public FastStringWriter() {
		sb = new StringBuilder(64);
	}
	
	/**
	 * Creates a new FastStringWriter with a given StringBuilder. It will append everything to this StringBuilder.
	 * 
	 * @param sb the StringBuilder
	 */
	public FastStringWriter(final StringBuilder sb) {
		this.sb = sb;
	}
	
	/**
	 * Gets the underlying StringBuilder.
	 */
	public StringBuilder getBuilder() {
		return sb;
	}
	
	/**
	 * Gets the content of the underlying StringBuilder, as a String. Equivalent to {@link #getBuilder()#toString()}.
	 */
	@Override
	public String toString() {
		return sb.toString();
	}
	
	@Override
	public FastStringWriter append(char c) {
		sb.append(c);
		return this;
	}
	
	@Override
	public FastStringWriter append(CharSequence csq, int start, int end) {
		sb.append(csq, start, end);
		return this;
	}
	
	@Override
	public FastStringWriter append(CharSequence csq) {
		sb.append(csq);
		return this;
	}
	
	@Override
	public void write(String str, int off, int len) {
		sb.append(str, off, off + len);
	}
	
	@Override
	public void write(String str) {
		sb.append(str);
	}
	
	@Override
	public void write(char[] cbuf, int off, int len) {
		sb.append(cbuf, off, len);
	}
	
	@Override
	public void write(int c) {
		sb.append(c);
	}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void flush() {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void close() {}
	
}
