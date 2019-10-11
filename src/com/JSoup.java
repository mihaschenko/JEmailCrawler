package com;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.net.URL;

class JSoup
{
	public static void main(String[] args) throws IOException
	{
		String givenURL = "https://www.xing.com/companies";
		String authority; // host name
		
		{
			URL mainURL = new URL(givenURL);
			authority = mainURL.getAuthority();
		}
		
		String regex = "[a-zA-Z0-9\\.\\-\\_]+@[a-zA-Z]+[\\.]{1}[a-zA-Z]{2,4}";
		Pattern pattern = Pattern.compile(regex);
		
		ArrayList<String> listOfURL = new ArrayList<String>();
		ArrayList<String> listOfEmail = new ArrayList<String>();
		listOfURL.add(givenURL);
		
		// main process/crawler loop
		for(int i = 0; i < listOfURL.size(); i++)
		{
			givenURL = listOfURL.get(i);
				System.out.print("Connect to " + givenURL + " ");
				
			Document doc = Jsoup.connect(givenURL).get();
			
			// search and save emails and their location
			String siteText = doc.text();
			Matcher matcher = pattern.matcher(siteText);
			while(matcher.find())
			{
				if(!listOfEmail.contains(matcher.group()))
					listOfEmail.add(matcher.group());
			}
			
			// search and save URLs without duplication
			Elements scrapedUrls = doc.select("a[href]");
			for(Element tag_a : scrapedUrls)
			{
				String str = tag_a.attr("abs:href");
				URL url = new URL(str);
				if(authority.equals(url.getAuthority()))
				{
					if(!listOfURL.contains(str))
						listOfURL.add(str);
				}
			}
			System.out.print(" --- Found links (" + scrapedUrls.size() + ") ");
			System.out.println("Saved links (" + listOfURL.size() + ") ");
		}
		System.out.println("Total links : " + listOfURL.size());
		System.out.println("Total Email Address : " + listOfEmail.size());

		/* 
		 * checking for duplicate links
		Object[] checkList = listOfURL.toArray();
		Arrays.sort(checkList);
		for(int i = 0; i < checkList.length && i < 50; i++)
			System.out.println(checkList[i]);
			
		 *  email output and their location
		for(int i = 0; i < listOfEmail.size() && i < 50; i++)
			System.out.println(listOfEmail.get(i) + " --- " + emailAddresses.get(i));
		*/
	}
}
