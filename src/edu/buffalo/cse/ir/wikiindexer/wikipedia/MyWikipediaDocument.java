package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.text.ParseException;
import java.util.Collection;
import java.util.Map;

public class MyWikipediaDocument extends WikipediaDocument 
{

	public MyWikipediaDocument(int idFromXml, String timestampFromXml,String authorFromXml, String ttl) throws ParseException 
	{
		super(idFromXml, timestampFromXml, authorFromXml, ttl);
	}
	
	//this method takes in the section title and text and adds it to the wikipedia document object
	public void addSection(String title, String text)
	{
	 super.addSection(title, text);
	}
	
	public void addLink(String link) {
		super.addLink(link);
	}
	
	public void addLInks(Collection<String> links) {
		super.addLInks(links);
	}
	
	public void addCategory(String category) {
		super.addCategory(category);
	}
	
	/**
	 * Method to bulk add categories to the list of categories classifying this document
	 * @param categories: The collection of categories to be added
	 */
	public void addCategories(Collection<String> categories) {
		super.addCategories(categories);
	}
	

	public void addLangLink(String langCode, String langLink) {
		super.addLangLink(langCode, langLink);
	}
	

	public void addLangLinks(Map<String, String> links) {
		super.addLangLinks(links);
	}

}
