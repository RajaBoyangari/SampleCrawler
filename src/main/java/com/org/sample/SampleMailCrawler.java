package com.org.sample;

import java.io.IOException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class SampleMailCrawler {
	public static void main(String[] args) throws IOException,
			InterruptedException {
		System.out.println("Provide the Domain URL : ");
		Scanner scanIn = new Scanner(System.in);
		String domainURL = scanIn.nextLine();// "http://mail-archives.apache.org/mod_mbox/maven-users/";
		System.out.println("Provide the Destination Type : ");
		String destination = scanIn.nextLine();// "File"
		System.out.println("Provide the Destination Path : ");
		String destinationPath = scanIn.nextLine();// "G://Assignments//2014//";
		ApacheMailCrawlerImpl apacheMailCrawler = new ApacheMailCrawlerImpl();
		apacheMailCrawler.mailCrawler(domainURL, destination, destinationPath);
	}
}
