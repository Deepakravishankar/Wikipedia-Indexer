/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nikhillo
 * An abstract class that represents a dictionary object for a given index
 */
public abstract class Dictionary implements Writeable 
{
	Properties props;
	INDEXFIELD field;
	Hashtable<String,Integer> dictionary;
	PrintWriter writer;
	
	public Dictionary (Properties props, INDEXFIELD field) 
	{
		//TODO Implement this method
		this.props=props;
		this.field=field;
		dictionary=new Hashtable<String,Integer>();
		writer=null;
	}
	
	/* (non-Javadoc)
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#writeToDisk()
	 */
	public void writeToDisk() throws IndexerException 
	{
	// TODO Implement this method
		for(Object key:dictionary.keySet())
		{
			writer.println(key.toString()+"->"+dictionary.get(key.toString()));
		}
		writer.flush();
		writer.close();
	}

	/* (non-Javadoc)
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#cleanUp()
	 */
	public void cleanUp() 
	{
		// TODO Implement this method
		dictionary=new Hashtable<String,Integer>();
	}
	
	/**
	 * Method to check if the given value exists in the dictionary or not
	 * Unlike the subclassed lookup methods, it only checks if the value exists
	 * and does not change the underlying data structure
	 * @param value: The value to be looked up
	 * @return true if found, false otherwise
	 */
	public boolean exists(String value) 
	{
		//TODO Implement this method
		if(dictionary.containsKey(value	))
			return true;
		else
		return false;
	}
	
	/**
	 * MEthod to lookup a given string from the dictionary.
	 * The query string can be an exact match or have wild cards (* and ?)
	 * Must be implemented ONLY AS A BONUS
	 * @param queryStr: The query string to be searched
	 * @return A collection of ordered strings enumerating all matches if found
	 * null if no match is found
	 */
	public Collection<String> query(String queryStr) 
	{
		//TODO: Implement this method (FOR A BONUS)
		ArrayList<String> myCollection=new ArrayList<String>();
		if(!queryStr.contains("*") && !queryStr.contains("?"))
			{
			if(dictionary.containsKey(queryStr))
			{
				myCollection.add(queryStr);
				return myCollection;
			}
			else
			return null;
			}
		
		String query=queryStr.replace("*", ".*").replace("?",".?");
		for(String key:dictionary.keySet())
		{
		if(key.matches(query))
			myCollection.add(key);
		}
		Collections.sort(myCollection);
		if(myCollection.size()==0)
			return null;
		else
		return myCollection;
	}
	
	/**
	 * Method to get the total number of terms in the dictionary
	 * @return The size of the dictionary
	 */
	public int getTotalTerms() 
	{
		return dictionary.size();
	}
}
