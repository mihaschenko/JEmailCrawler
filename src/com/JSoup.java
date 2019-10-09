package com;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.URL;

class JSoup
{
	public static void main(String[] args) throws IOException
	{
		System.out.println("START!");
		
		String strURL = "https://www.xing.com/companies";
		URL mainURL = new URL(strURL);
		
		String regex = "[a-zA-Z0-9\\.\\-\\_]+@[a-zA-Z]+[\\.]{1}[a-zA-Z]{2,4}";
		Pattern pattern = Pattern.compile(regex);
		
		ArrayList<String> listOfURL = new ArrayList<String>();
		ArrayList<String> listOfEmail = new ArrayList<String>();
		ArrayList<String> emailSite = new ArrayList<String>();
		listOfURL.add(mainURL.toString());
		
		for(int i = 0; i < listOfURL.size() && i < 20; i++)
		{
			//Поиск url-ссылок и email-адресов на странице
			strURL = listOfURL.get(i);
				System.out.print("Подключение к " + strURL + " ");
				
			Document doc = Jsoup.connect(strURL).get();
			Elements scraped_urls = doc.select("a[href]");
			
			String text = doc.text();
			Matcher matcher = pattern.matcher(text);
			while(matcher.find())
			{
				if(!listOfEmail.contains(matcher.group()))
				{
					listOfEmail.add(matcher.group());
					emailSite.add(strURL);
				}
			}
			
				//System.out.print(list.size() + " ");
			
			for(Element adr : scraped_urls)
			{
				// Сохранение ссылок (без дублирования)
				String str = adr.attr("abs:href");
				URL url = new URL(str);
				if(mainURL.getAuthority().equals(url.getAuthority()))
				{
					if(!listOfURL.contains(str))
						listOfURL.add(str);
				}
			}
			//System.out.println(listOfURL.size());
		}
		System.out.println("Всего ссылок : " + listOfURL.size());
		System.out.println("Всего почтовых адресов : " + listOfEmail.size());
		
		// Проверка массива на дублирования ссылок
		Object[] ss = listOfURL.toArray();
		Arrays.sort(ss);
		for(int i = 0; i < ss.length && i < 50; i++)
			System.out.println(ss[i]);
		// Вывод найденных email-адресов
		for(int i = 0; i < listOfEmail.size() && i < 50; i++)
			System.out.println(listOfEmail.get(i) + " --- " + emailSite.get(i));
	}
}