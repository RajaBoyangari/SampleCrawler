package com.sample.extractor;

import org.jsoup.nodes.Document;

public class ApacheMailContentExtractor {

	public static String extractMailContents(Document doc) {
		return doc.getElementsByClass("Contents").text();
	}
}
