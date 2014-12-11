package com.sample.builder;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DocumentBuilder {
	
	public static Document buildDocument(String urlString) throws IOException,
			InterruptedException {
		try {
			Connection conn = Jsoup.connect(urlString);
			conn.timeout(2000);
			return conn.get();
		} catch (SocketTimeoutException socketTimeOutException) {
			System.out.println("Connection Timed out");
			System.out.println("Retrying the connection");
			Thread.sleep(1000);
			DocumentBuilder.buildDocument(urlString);
		}
		return null;
	}

}
