package com.steam.pb09;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CountBytesOutputStream extends FilterOutputStream {
	private long bytesWritten = 0;

	/**
	 * @param out
	 */
	public CountBytesOutputStream(OutputStream out) {
		super(out);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		super.write(b, off, len);
		bytesWritten += len;
	}

	public long getBytesWritten() {
		return bytesWritten;
	}

}
