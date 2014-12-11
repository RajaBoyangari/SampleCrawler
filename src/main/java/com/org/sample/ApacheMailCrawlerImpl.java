package com.org.sample;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sample.builder.DocumentBuilder;
import com.sample.extractor.ApacheMailContentExtractor;
import com.sample.writer.WriterFactory;
import com.sample.writer.WriterIntf;

public class ApacheMailCrawlerImpl {

	public void mailCrawler(String urlString, String destination,
			String destinationPath) throws InterruptedException, IOException {
		HashSet<String> urlSet = new LinkedHashSet<String>();
		Document doc = DocumentBuilder.buildDocument(urlString);
		Elements links = doc.select("a[href]");
		for (Element link : links) {
			if (link.attr("href").contains("2014")
					&& link.attr("href").contains(".mbox/thread")) {
				System.out.println(link.attr("abs:href"));
				urlSet.add(link.attr("abs:href"));
			}
		}
		System.out.println(urlSet);
		for (String urlStr : urlSet) {
			System.out.println(urlStr);
			Thread.sleep(2000);
			processURL(urlStr, destination, destinationPath);
		}

		System.out.println(urlSet);
	}

	public void processURL(String urlString, String destination,
			String destinationPath) throws IOException, InterruptedException {
		Document doc = DocumentBuilder.buildDocument(urlString);
		Elements links = doc.select("a[href]");
		for (Element link : links) {
			if (link.attr("href").startsWith("%")) {
				if (destination.equalsIgnoreCase("File")) {
					WriterIntf writer = WriterFactory.getWriter(destination);
					String newURLString = link.attr("abs:href");
					doc = DocumentBuilder.buildDocument(newURLString);
					String mailContent = ApacheMailContentExtractor
							.extractMailContents(doc);
					String directoryName = newURLString.split("2014")[1];
					String location = destinationPath
							+ directoryName.substring(0, 5);
					String fileName = getFileName(doc);
					writer.writeMailContent(mailContent, location, fileName);
				}
			} else if (link.attr("href").contains("thread?")
					&& link.text().contains("Next")) {
				processURL(link.attr("abs:href"), destination, destinationPath);
			}
		}
	}

	private static String getFileName(Document doc) {
		String fromUser = doc.getElementsByClass("from").get(0)
				.getElementsByClass("right").text().split("<")[0];

		String date = doc.getElementsByClass("Date").get(0)
				.getElementsByClass("right").text();
		String fileName = formatString(fromUser) + "_" + formatString(date)
				+ ".txt";
		return fileName;
	}

	private static String formatString(String str) {
		return str.replaceAll("[\"-+.^:,?<>@\\/]", "");
	}
}
