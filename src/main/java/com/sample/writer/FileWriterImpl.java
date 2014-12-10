package com.sample.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterImpl implements WriterIntf {
	public void writeMailContent(String mailContent, String location,
			String fileName) {
		createDirectory(location);
		System.out.println(fileName);
		File file = new File(location, fileName);
		FileWriter fw = null;
		try {
			file.createNewFile();
			fw = new FileWriter(file);
			fw.write(mailContent);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void createDirectory(String location) {
		File directory = new File(location);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}
}
