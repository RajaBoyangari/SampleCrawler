package com.sample.builder;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DocumentBuilder {

	public static Document buildDocument(String urlString) throws IOException,
			InterruptedException {
		Document doc = null;
		try {
			Connection conn = Jsoup.connect(urlString);
			doc = conn.get();
		} catch (SocketTimeoutException socketTimeOutException) {
			System.out.println("Time Out Occurred");
			System.out.println("\n\nResuming the download...");
			System.out.println(urlString);
			Thread.sleep(1500);
			buildDocument(urlString);
		}
		if (doc == null) {
			System.out.println("hello There !!");
			buildDocument(urlString);
			Thread.sleep(1500);
		}
		return doc;
	}

}
