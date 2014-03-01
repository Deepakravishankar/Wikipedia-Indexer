/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.parsers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.ir.wikiindexer.wikipedia.MyWikipediaDocument;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaParser;

//we are importing the sax parser and DOM parser packages
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.*;
import org.xml.sax.Attributes;

/**
 * @author nikhillo
 *
 */

public class Parser{
	/* */
	private final Properties props;
	
	/**
	 * 
	 * @param idxConfig
	 * @param parser
	 */
	public Parser(Properties idxProps) {
		props = idxProps;
	}
	
	/* TODO: Implement this method */
	/**
	 * 
	 * @param filename
	 * @param docs
	 */
	//we are implementing the parser code here where i break the dump xml into wikipedia document instances	
	public void parse(String filename,Collection<WikipediaDocument> docs) 
	{
	SAXParserFactory parserFactory= SAXParserFactory.newInstance();//create sax parser factory
	ExecutorService exec=Executors.newFixedThreadPool(2);
	List<Callable<MyWikipediaDocument>> callables =new ArrayList<Callable<MyWikipediaDocument>>();
	MyHandler myHandler=new MyHandler(callables);
	try
	{
	SAXParser parser=parserFactory.newSAXParser();//create a parser using that factory
	parser.parse(filename,myHandler);//parse the file and handle the events
	
	try 
    {
    List<Future<MyWikipediaDocument>> myWikidocs=exec.invokeAll(callables);
    for(Future<MyWikipediaDocument> myWikidoc: myWikidocs) 
    {
    	add(myWikidoc.get(),docs);
    }
    } 
    catch (InterruptedException ex) 
    {
        ex.printStackTrace();
    } 
    catch (ExecutionException e) 
    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
    finally 
    {
        exec.shutdownNow();
    }
	
	
	}
	catch (Exception e) 
	{
		System.out.println("Parse Method:"+e.getMessage());
	}
	}
	
	class MyHandler extends DefaultHandler
	{
		Collection<WikipediaDocument> wikiDocs=null;
		List<Callable<MyWikipediaDocument>> callables;
		String contents=null;
		String timestamp=null,author=null,title=null;
		StringBuffer wmlContents=new StringBuffer();
		int id=0;
		int pageCount=0;
		Stack<String> myXMLElementsStack=new Stack<String>();//this tag maintains the tags i encounter in xml
		
		MyHandler(List<Callable<MyWikipediaDocument>> callables)
		{
			this.callables=callables;
		}

		//this method is called whenever a start element is encountered
		@Override
		public void startElement(String namespaceURI,String localName,String qName, Attributes atts)
		{
			myXMLElementsStack.push(qName);//whenever an element is encountered push it into stack
			if(qName=="text")//if the start element is a text element..then we have approached our wml contents
				wmlContents=new StringBuffer();
			
		}
		
		//this method is called whenever an end element is encountered
		@Override
		public void endElement(String namespaceURI,String localName,String qName)
		{
		myXMLElementsStack.pop();//whenever end element is encountered pop it from the stack
	    
		if(qName=="timestamp" && myXMLElementsStack.peek()=="revision")
			timestamp=contents;
		else if(qName=="title" && myXMLElementsStack.peek()=="page")
			title=contents;
		else if((qName=="username" || qName=="ip") && myXMLElementsStack.peek()=="contributor")
			author=contents;
		else if(qName=="id" && myXMLElementsStack.peek()=="page")
			id=Integer.parseInt(contents);
		//if we encounter the end page element then we have read through one wiki page
		//so we can create one wikipedia document object and add it to the collection
		else if(qName=="page")
		{
			class MyCallable implements Callable<MyWikipediaDocument> 
			{
	            private final int threadnumber;
	            MyWikipediaDocument doc;
	            StringBuffer wmlContents;

	            MyCallable(int threadnumber,int id,String timestamp,String author,String title,StringBuffer wmlContents)
	            {
	                this.threadnumber = threadnumber;
	                try 
	                {
						doc = new MyWikipediaDocument(id, timestamp, author, title);
						this.wmlContents=wmlContents;
					} 
	                catch (ParseException e) 
	                {
						e.printStackTrace();
					}
	            }

	            public MyWikipediaDocument call() 
	            {

	    			StringBuffer afterTemplateParsing=new StringBuffer();
					StringBuffer afterLinkParsing=new StringBuffer();
					StringBuffer afterTagParsing=new StringBuffer();
					StringBuffer afterTextParsing=new StringBuffer();
					StringBuffer afterListParsing=new StringBuffer();
					StringBuffer afterNestedLinkParsing=new StringBuffer();
					StringBuffer afterNestedParsing=new StringBuffer();
					Pattern regex;
					Matcher regexMatcher;
					ArrayList<String>categories=new ArrayList<String>();
					HashSet<String>links=new HashSet<String>();
					HashMap<String,String> langLinks=new HashMap<String,String>();
					
					
					
					//now we need to parse our wmlContents and add it to our wikipedia document object
					
					//we will extract out the templates first
					afterTemplateParsing=new StringBuffer(WikipediaParser.parseTemplates(wmlContents.toString()));

					//we will extract nested links first
					regex = Pattern.compile("(\\[\\[(File)|(Image):(.+\\[\\[.+\\]\\].+)+\\]\\])");
					regexMatcher = regex.matcher(afterTemplateParsing);
					while(regexMatcher.find())
					   {
						String outerLink=regexMatcher.group(1);
						Pattern nestedRegex=Pattern.compile("(\\[\\[.+?\\]\\])");
						Matcher nestedMatcher=nestedRegex.matcher(outerLink);
						while(nestedMatcher.find())
						{
							String[] nestedLinks=WikipediaParser.parseLinks(nestedMatcher.group(1));
							if(nestedLinks[1]!="")//if the link is not empty
								links.add(nestedLinks[1]);
							nestedMatcher.appendReplacement(afterNestedLinkParsing,nestedLinks[0]);
						}
						nestedMatcher.appendTail(afterNestedLinkParsing);
						regexMatcher.appendReplacement(afterNestedParsing,afterNestedLinkParsing.toString());
					   }
					regexMatcher.appendTail(afterNestedParsing);
					
					
					//we will extract out the links and categories
					regex = Pattern.compile("(\\[\\[?.*?\\]?\\])");
					regexMatcher = regex.matcher(afterNestedParsing);
					while(regexMatcher.find())
					   {
						String matchedLink=regexMatcher.group(1);
						String[] parsedLinks=WikipediaParser.parseLinks(matchedLink);

							if(matchedLink.matches("\\[\\[Category:.*\\]\\]"))//if the category does not have a colon it 
							{
								categories.add(parsedLinks[0]);//then we should add it to categories
								regexMatcher.appendReplacement(afterLinkParsing,"");//but does not have to go to index
							}
							
							else if(matchedLink.matches("\\[\\[:?Category:.*\\]\\]"))//if t matches this regex it is a category
							{
							regexMatcher.appendReplacement(afterLinkParsing,parsedLinks[0]);
							}
							
						else if(matchedLink.matches("\\[\\[\\w\\w:.*\\]\\]"))//if language links
						{
							regexMatcher.appendReplacement(afterLinkParsing,"");//replace those links with empty string
							langLinks.put(parsedLinks[0],parsedLinks[1]);//add it to langlinks..though it is not necessary
						}
						
						else//if normal links
						{
						regexMatcher.appendReplacement(afterLinkParsing,parsedLinks[0].replace("$",""));
						if(!parsedLinks[1].equals(""))//if the link is not empty
							links.add(parsedLinks[1]);
						}
					   }
					regexMatcher.appendTail(afterLinkParsing);	

					doc.addCategories(categories);
					doc.addLInks(links);
					doc.addLangLinks(langLinks);
					
					//we will parse all the tag formatting
					afterTagParsing=new StringBuffer(WikipediaParser.parseTagFormatting(afterLinkParsing.toString()));
					
					
					//we will parse all text formatting
					afterTextParsing=new StringBuffer(WikipediaParser.parseTextFormatting(afterTagParsing.toString()));
					
					
					//we will parse all text formatting
					afterListParsing=new StringBuffer(WikipediaParser.parseListItem(afterTextParsing.toString()));
					
					//now we will parse sections and its text out
					String sectionTitle="Default";
					StringBuffer sectionText=new StringBuffer();
					String matchedSection="";
					regex=Pattern.compile("(=+.+?=+)");
					regexMatcher=regex.matcher(afterListParsing);
					while(regexMatcher.find())
					{
						matchedSection=WikipediaParser.parseSectionTitle(regexMatcher.group(1));
						regexMatcher.appendReplacement(sectionText,"");//append all the text to sectionText
						((MyWikipediaDocument)doc).addSection(sectionTitle, sectionText.toString());//add the section to the document
						sectionTitle=matchedSection;//set the new section title
						sectionText=new StringBuffer("");//re initialize the section text
					}
					regexMatcher.appendTail(sectionText);
					
					
					doc.addSection(sectionTitle, sectionText.toString());
					return doc;
	        }
			}
			
			pageCount++;
            callables.add(new MyCallable(pageCount,id,timestamp,author,title,wmlContents));//whenever i encounter an end page element i add a new callable for parsing that page 
		}
		}
		
		//this method is called for the characters between the start element and end element
		@Override
		public void characters(char[] ch,int start,int length)
		{	
			contents=new String(ch,start,length);//collect the characters in a string
			//if the last encountered element is text..then the characters are wmlcontents
			if(myXMLElementsStack.peek()=="text")
			{
				wmlContents.append(contents);
			}
		}
		}
	
	
	/**
	 * Method to add the given document to the collection.
	 * PLEASE USE THIS METHOD TO POPULATE THE COLLECTION AS YOU PARSE DOCUMENTS
	 * For better performance, add the document to the collection only after
	 * you have completely populated it, i.e., parsing is complete for that document.
	 * @param doc: The WikipediaDocument to be added
	 * @param documents: The collection of WikipediaDocuments to be added to
	 */
	private synchronized void add(WikipediaDocument doc, Collection<WikipediaDocument> documents) {
		documents.add(doc);
	}
}
