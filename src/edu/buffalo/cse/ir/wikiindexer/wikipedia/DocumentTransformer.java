/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.text.BreakIterator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.Tokenizer;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument.Section;

/**
 * A Callable document transformer that converts the given WikipediaDocument object
 * into an IndexableDocument object using the given Tokenizer
 * @author nikhillo
 *
 */
public class DocumentTransformer implements Callable<IndexableDocument> 
{
	WikipediaDocument doc;//the wikipedia document to be transformed
	Map<INDEXFIELD,Tokenizer> tokenizerMap;//map of indexfield and its corresponding tokenizer
	
	/**
	 * Default constructor, DO NOT change
	 * @param tknizerMap: A map mapping a fully initialized tokenizer to a given field type
	 * @param doc: The WikipediaDocument to be processed
	 */
	public DocumentTransformer(Map<INDEXFIELD, Tokenizer> tknizerMap, WikipediaDocument doc) 
	{
	//TODO: Implement this method
	this.doc=doc;	
	this.tokenizerMap=tknizerMap;
	}
	
	/**
	 * Method to trigger the transformation
	 * @throws TokenizerException Incase any tokenization error occurs
	 */
	public IndexableDocument call() throws TokenizerException 
	{
	// TODO Implement this method
	String author=doc.getAuthor();//has to go to author index
	String title=doc.getTitle();//has to go to term index
	List<String>categories=doc.getCategories();//has to go to category index
	int id=doc.getId();//this is the document identifier
	Map<String,String> langLinks=doc.getLangLinks();//there is no language index
	Set<String> links=doc.getLinks();//this has to go to link index
	Date date=doc.getPublishDate();
	List<Section> sections=doc.getSections();//all this has to go to term index
	
	IndexableDocument indexableDoc=new IndexableDocument(title);//create a new indexable document
	
	for(INDEXFIELD field:tokenizerMap.keySet())//for every INDEXFIELD key in the map
	{	
		switch(field)
		{
		case TERM:		Tokenizer termTokenizer=tokenizerMap.get(field);//get the corresponding tokenizer
						TokenStream termTokenStream=new TokenStream((String)null);//we will create a tokenStream
						TokenStream sentenceTokenStream=new TokenStream((String)null);
						ListIterator<Section> sectionIterator=sections.listIterator();
						while(sectionIterator.hasNext())
						{
							Section section=sectionIterator.next();
							String sectionTitle=section.getTitle();
							termTokenStream.append(sectionTitle);
							String sectionText=section.getText();
							BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.ENGLISH);
							iterator.setText(sectionText);
							int start = iterator.first();
							for (int end = iterator.next();end != BreakIterator.DONE;start = end, end = iterator.next()) 
							{
							  String sentence=sectionText.substring(start,end).trim();
							  sentenceTokenStream=new TokenStream((String)null);
							  sentenceTokenStream.append(sentence);
							  termTokenizer.tokenize(sentenceTokenStream);
							  termTokenStream.merge(sentenceTokenStream);
							}
						}
						indexableDoc.addField(field,termTokenStream);//add it to the indexable doc
						break;
			
		case AUTHOR:	Tokenizer authorTokenizer=tokenizerMap.get(field);//get the corresponding tokenizer
						TokenStream authorTokenStream=new TokenStream(author);//we will create a tokenStream
						authorTokenizer.tokenize(authorTokenStream);//tokenize the token stream
						indexableDoc.addField(field,authorTokenStream);//add it to the indexable doc
						break;
			
		case CATEGORY: 	Tokenizer categoryTokenizer=tokenizerMap.get(field);//get the corresponding tokenizer
						TokenStream categoryTokenStream=new TokenStream((String)null);//we will create a tokenStream
						ListIterator<String> categoryIterator=categories.listIterator();
						while(categoryIterator.hasNext())
						{
							categoryTokenStream.append(categoryIterator.next());
						}
						categoryTokenizer.tokenize(categoryTokenStream);//tokenize the token stream
						indexableDoc.addField(field,categoryTokenStream);//add it to theindexable doc
						break;
			
		case LINK:		Tokenizer linkTokenizer=tokenizerMap.get(field);//get the corresponding tokenizer
						TokenStream linkTokenStream=new TokenStream((String)null);//we will create a tokenStream
						Iterator<String> LinkIterator=links.iterator();
						while(LinkIterator.hasNext())
						{
							linkTokenStream.append(LinkIterator.next());
						}
						linkTokenizer.tokenize(linkTokenStream);//tokenize the token stream
						indexableDoc.addField(field,linkTokenStream);//add it to theindexable doc
						break;
		}
	}
	return indexableDoc;
	}
	
}
