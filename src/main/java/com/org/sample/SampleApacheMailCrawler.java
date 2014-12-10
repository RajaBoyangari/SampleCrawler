package com.org.sample;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sample.builder.DocumentBuilder;
import com.sample.extractor.MailContentExtractor;
import com.sample.writer.WriterIntf;
import com.sample.writer.WriterFactory;

/**
 * Hello world!
 *
 */
public class SampleApacheMailCrawler {
	public static void main(String[] args) throws IOException,
			InterruptedException {
		String domainURL = "http://mail-archives.apache.org/mod_mbox/maven-users/";
		String destination = "File";
		String destinationPath = "G://Assignments//2014//";
		HashSet<String> urlSet = new LinkedHashSet<String>();

		Document doc = DocumentBuilder.buildDocument(domainURL);
		// get all links and recursively call the processPage method
		// System.out.println(doc.getElementsByClass("Contents").text());

		Elements links = doc.select("a[href]");
		for (Element link : links) {
			if (link.attr("href").contains("2014")) {
				urlSet.add(link.attr("abs:href"));
			}
		}
		for (String urlString : urlSet) {
			processURL(urlString, destination, destinationPath);
		}

		System.out.println(urlSet);
	}

	private static void processURL(String urlString, String destination,
			String destinationPath) throws IOException, InterruptedException {
		Document doc = DocumentBuilder.buildDocument(urlString);
		Elements links = doc.select("a[href]");

		for (Element link : links) {
			if (link.attr("href").startsWith("%")) {
				if (destination.equalsIgnoreCase("File")) {
					WriterIntf writer = WriterFactory.getWriter(destination);
					String newURLString = link.attr("abs:href");
					doc = DocumentBuilder.buildDocument(newURLString);
					String mailContent = MailContentExtractor
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
