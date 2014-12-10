package com.sample.writer;

public class WriterFactory {

	public static WriterIntf getWriter(String writerType) {
		if (writerType.equalsIgnoreCase("File")) {
			return new FileWriterImpl();
		}
		return null;
	}

}
