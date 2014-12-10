package com.org.sample;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException,
			InterruptedException {
		String domainURL = "http://mail-archives.apache.org/mod_mbox/maven-users/";
		Connection conn = Jsoup.connect(domainURL);
		HashSet<String> urlSet = new LinkedHashSet<String>();

		Document doc = conn.get();
		// get all links and recursively call the processPage method
		System.out.println(doc.getElementsByClass("Contents").text());

		Elements links = doc.select("a[href]");
		for (Element link : links) {
			if (link.attr("href").contains("2014")) {
				// System.out.println(link.attr("abs:href"));
				urlSet.add(link.attr("abs:href"));
			}
		}
		for (String urlString : urlSet) {
			processURL(urlString);
		}

		System.out.println(urlSet);
	}

	private static void processURL(String urlString) throws IOException,
			InterruptedException {
		try {
			Connection conn = Jsoup.connect(urlString);
			String directoryName = urlString.split("2014")[1];
			Document doc = conn.get();
			Elements links = doc.select("a[href]");
			String location = "G://Assignments//2014//"
					+ directoryName.substring(0, 5);
			File directory = new File(location);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			for (Element link : links) {
				if (link.attr("href").startsWith("%")) {
					MyUtility.extractMailandWritetoFileLocation(link, location);
				} else if (link.attr("href").contains("thread?")
						&& link.text().contains("Next")) {
					processURL(link.attr("abs:href"));
				}
			}
		} catch (SocketTimeoutException socketTimeOutException) {
			System.out.println("\nTime Out Occurred");
			System.out.println("\n\n...Resuming the download...");
			Thread.sleep(5000);
			processURL(urlString);
		}
	}
}
