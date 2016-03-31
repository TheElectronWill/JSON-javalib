package com.electronwill.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

/**
 *
 * @author TheElectronWill
 */
public final class UnicodeDetector {
	
	public static final Charset UTF8 = Charset.forName("UTF-8");
	public static final Charset UTF16BE = Charset.forName("UTF-16BE");
	public static final Charset UTF16LE = Charset.forName("UTF-16LE");
	
	public static Charset getEncoding(InputStream in) throws IOException {
		Charset encoding;
		PushbackInputStream pushbackInputStream = new PushbackInputStream(in);
		byte[] bom = new byte[3];
		int read = pushbackInputStream.read(bom);
		int back;// Bytes to unread
		
		if (read < 2) {
			return UTF8;
		}
		if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF)) {
			encoding = UTF8;
			back = read - 3;
		} else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF)) {
			encoding = UTF16BE;
			back = read - 2;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)) {
			encoding = UTF16LE;
			back = read - 2;
		} else {
			// No BOM found, unread all bytes.
			encoding = UTF8;
			back = read;
		}
		
		if (back > 0) {
			pushbackInputStream.unread(bom, (read - back), back);
		}
		
		return encoding;
	}
	
	public static InputStreamReader getReader(InputStream in) throws IOException {
		Charset encoding = getEncoding(in);
		CharsetDecoder decoder = encoding.newDecoder();
		decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
		return new InputStreamReader(in, decoder);
	}
	
	public static InputStreamReader getReader(InputStream in, CodingErrorAction onUnmappableAction) throws IOException {
		Charset encoding = getEncoding(in);
		CharsetDecoder decoder = encoding.newDecoder();
		decoder.onUnmappableCharacter(onUnmappableAction);
		return new InputStreamReader(in, decoder);
	}
	
	private UnicodeDetector() {}
	
}
