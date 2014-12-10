package com.org.sample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MyUtility {
	public static void extractMailandWritetoFileLocation(Element link,
			String location) throws IOException {
		try {
			Connection connection = Jsoup.connect(link.attr("abs:href"));
			Document doc = connection.get();
			String mailContent = doc.getElementsByClass("Contents").text();
			String fromUser = doc.getElementsByClass("from").get(0)
					.getElementsByClass("right").text().split("<")[0]
					.replaceAll("[\"-+.^:,?<>@\\/]", "");
			/*
			 * String subject = doc.getElementsByClass("subject").get(0)
			 * .getElementsByClass("right").text() .replaceAll("[\"-+.^:,?]",
			 * "");
			 */
			String date = doc.getElementsByClass("Date").get(0)
					.getElementsByClass("right").text()
					.replaceAll("[\"-+.^:,?]", "");
			String fileName = fromUser + "_" + date + ".txt";
			System.out.println(fileName);
			File file = new File(location, fileName);
			file.createNewFile();
			FileWriter fw = new FileWriter(file);
			fw.write(mailContent);
			fw.close();
		} catch (SocketTimeoutException socketTimeOutException) {
			System.out.println("Time Out Occurred");
			System.out.println("\n\nResuming the download...");
			extractMailandWritetoFileLocation(link, location);
		}

	}

}
