package org.hyperion.rs2.cache.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.hyperion.rs2.cache.stream.CBZip2OutputStream;

public class BZip2Compressor {

	public static final byte[] compress(byte[] data) {
		ByteArrayOutputStream compressedBytes = new ByteArrayOutputStream();
		try {
			CBZip2OutputStream out = new CBZip2OutputStream(compressedBytes);
			out.write(data);
			out.close();
			return compressedBytes.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
