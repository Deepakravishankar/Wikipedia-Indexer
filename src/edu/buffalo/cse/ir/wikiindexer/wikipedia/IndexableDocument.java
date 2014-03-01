/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;

/**
 * A simple map based token view of the transformed document
 * @author nikhillo
 *
 */
public class IndexableDocument 
{
	
	Map<INDEXFIELD,TokenStream> indexableDoc;//the map representation of the transformed document
	String documentIdentifier;
	
	
	/**
	 * Default constructor
	 */
	public IndexableDocument() 
	{
	//TODO: Init state as needed
	indexableDoc=new EnumMap<INDEXFIELD,TokenStream>(INDEXFIELD.class);
	documentIdentifier="";
	}
	
	public IndexableDocument(String id)
	{
		indexableDoc=new EnumMap<INDEXFIELD,TokenStream>(INDEXFIELD.class);
		documentIdentifier=id;
	}
	
	/**
	 * MEthod to add a field and stream to the map
	 * If the field already exists in the map, the streams should be merged
	 * @param field: The field to be added
	 * @param stream: The stream to be added.
	 */
	public void addField(INDEXFIELD field, TokenStream stream) 
	{
		//TODO: Implement this method
		if(indexableDoc.containsKey(field))//if the field already exists
		{
			TokenStream oldStream=indexableDoc.get(field);//get the currentstream present
			oldStream.merge(stream);//merge the incoming stream with it
			indexableDoc.put(field,oldStream);
		}
		else//if the field does not exist in the map
		{
			indexableDoc.put(field,stream);
		}
	}
	
	/**
	 * Method to return the stream for a given field
	 * @param key: The field for which the stream is requested
	 * @return The underlying stream if the key exists, null otherwise
	 */
	public TokenStream getStream(INDEXFIELD key) 
	{
		//TODO: Implement this method
		if(indexableDoc.containsKey(key))
		return indexableDoc.get(key);
		else
			return null;
	}
	
	/**
	 * Method to return a unique identifier for the given document.
	 * It is left to the student to identify what this must be
	 * But also look at how it is referenced in the indexing process
	 * @return A unique identifier for the given document
	 */
	public String getDocumentIdentifier() 
	{
	//TODO: Implement this method
	return documentIdentifier;
	}
	
}
