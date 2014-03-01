/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.AbstractMap.SimpleEntry;
/**
 * @author nikhillo
 * This class is used to introspect a given index
 * The expectation is the class should be able to read the index
 * and all associated dictionaries.
 */
public class IndexReader 
{
	Hashtable<Object,LinkedList<SimpleEntry<Object,Integer>>> index;
	Hashtable<String,Integer> sharedDictionary;
	
	Properties props;
	INDEXFIELD indexField;
	/**
	 * Constructor to create an instance 
	 * @param props: The properties file
	 * @param field: The index field whose index is to be read
	 */
	public IndexReader(Properties props, INDEXFIELD field) 
	{
		//TODO: Implement this method
		this.props=props;
		this.indexField=field;
		index=new Hashtable<Object, LinkedList<SimpleEntry<Object,Integer>>>();
		sharedDictionary=new Hashtable<String, Integer>();
		
		try 
		{
			BufferedReader indexReader;
			
			if(field.equals(INDEXFIELD.LINK))
				indexReader = new BufferedReader(new FileReader(props.getProperty("tmp.dir")+field.toString()+"FWDINDEX"));
			else
				indexReader = new BufferedReader(new FileReader(props.getProperty("tmp.dir")+field.toString()+"INVINDEX"));
			
			while(true)
			{
			String line = indexReader.readLine();
			if(line==null || line.equals(""))
				break;
			String[] parts=line.split("->");
			String[] postings=parts[1].split(",");
			LinkedList<SimpleEntry<Object,Integer>> linkedList=new LinkedList<SimpleEntry<Object,Integer>>();
			index.put(parts[0],linkedList);
			for(String posting:postings)
			{
				if(posting!=null && !posting.equals(""))
				{
				String values[]=posting.split("-");
				SimpleEntry<Object,Integer> simpleEntry=new SimpleEntry<Object,Integer>(values[0],Integer.parseInt(values[1]));
				linkedList.add(simpleEntry);
				}
			}
			}
			indexReader.close();
			
			BufferedReader dictionaryReader= new BufferedReader(new FileReader(props.getProperty("tmp.dir")+"LINKDICT"));
			while(true)
			{
			String line=dictionaryReader.readLine();
			if(line==null || line.equals(""))
					break;	
			String[] parts=line.split("->");
			sharedDictionary.put(parts[0],Integer.parseInt(parts[1]));
			}
			dictionaryReader.close();	
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method to get the total number of terms in the key dictionary
	 * @return The total number of terms as above
	 */
	public int getTotalKeyTerms() 
	{
		//TODO: Implement this method
		return index.size();
	}
	
	/**
	 * Method to get the total number of terms in the value dictionary
	 * @return The total number of terms as above
	 */
	public int getTotalValueTerms() 
	{
		//TODO: Implement this method
		return sharedDictionary.size();
	}
	
	/**
	 * Method to retrieve the postings list for a given dictionary term
	 * @param key: The dictionary term to be queried
	 * @return The postings list with the value term as the key and the
	 * number of occurrences as value. An ordering is not expected on the map
	 */
	public Map<String, Integer> getPostings(String key) 
	{
		//TODO: Implement this method
		HashMap<String,Integer> map=new HashMap<String,Integer>();
		LinkedList<SimpleEntry<Object,Integer>> linkedList;
		if(indexField.equals(INDEXFIELD.LINK))
			linkedList=index.get(sharedDictionary.get(key));
		else
			linkedList=index.get(key);
		Iterator<SimpleEntry<Object,Integer>> iterator=linkedList.iterator();
		while(iterator.hasNext())
		{
			SimpleEntry<Object,Integer> simpleEntry=iterator.next();
			for(Map.Entry<String, Integer> entry : sharedDictionary.entrySet())
			{
				if(entry.getValue()==Integer.parseInt(simpleEntry.getKey().toString()))
				{
					map.put(entry.getKey(),simpleEntry.getValue());
					break;
				}
			}
		}
		return map;
	}
	
	/**
	 * Method to get the top k key terms from the given index
	 * The top here refers to the largest size of postings.
	 * @param k: The number of postings list requested
	 * @return An ordered collection of dictionary terms that satisfy the requirement
	 * If k is more than the total size of the index, return the full index and don't 
	 * pad the collection. Return null in case of an error or invalid inputs
	 */
	public Collection<String> getTopK(int k)
	{
		//TODO: Implement this method
		LinkedList<SimpleEntry<Object,Integer>> linkedList;
		Hashtable<String,Integer> myHashTable=new Hashtable<String, Integer>();
		for(Object key:index.keySet())
		{
			linkedList=index.get(key);
			if(indexField.equals(INDEXFIELD.LINK))
			{
			for(Map.Entry<String, Integer> entry : sharedDictionary.entrySet())
			{
			if(entry.getValue()==Integer.parseInt(key.toString()))
			{
				myHashTable.put(entry.getKey(),linkedList.size());
				break;
			}
			}
			}
			else
			{
			myHashTable.put(key.toString(),linkedList.size());
			}
		}
		List<Map.Entry> a = new ArrayList<Map.Entry>(myHashTable.entrySet());
		Collections.sort(a,
		         new Comparator() {
		             public int compare(Object o1, Object o2) 
		             {
		                 Map.Entry e1 = (Map.Entry) o1;
		                 Map.Entry e2 = (Map.Entry) o2;
		                 return ((Comparable) e2.getValue()).compareTo(e1.getValue());
		             }
		         });
		
		LinkedList<String> returningCollection=new LinkedList<String>(); 
		Iterator<Map.Entry> iterator=a.iterator();
			int count=0;
			while(iterator.hasNext())
			{
			if(count==k)
				return returningCollection;
			else
			{
			returningCollection.add(iterator.next().getKey().toString());
			count++;
			}
			}
		return returningCollection;
	}
	
	/**
	 * Method to execute a boolean AND query on the index
	 * @param terms The terms to be queried on
	 * @return An ordered map containing the results of the query
	 * The key is the value field of the dictionary and the value
	 * is the sum of occurrences across the different postings.
	 * The value with the highest cumulative count should be the
	 * first entry in the map.
	 */
	public Map<String, Integer> query(String... terms) 
	{
		//TODO: Implement this method (FOR A BONUS)
		Map<String,Integer> mergedPostings=new Hashtable<String, Integer>();
		for(String term:terms)
		{
			Map<String,Integer> postings=getPostings(term);
			for(Map.Entry<String,Integer> entry:postings.entrySet()) 
			{
			  Integer count=mergedPostings.get(entry.getKey());
			  if(count==null)  
			    mergedPostings.put(entry.getKey(),entry.getValue());
			  else
			    mergedPostings.put(entry.getKey(),entry.getValue()+count);
			}
		}
		List<Map.Entry> a = new ArrayList<Map.Entry>(mergedPostings.entrySet());
		Collections.sort(a,
		         new Comparator() {
		             public int compare(Object o1, Object o2) 
		             {
		                 Map.Entry e1 = (Map.Entry) o1;
		                 Map.Entry e2 = (Map.Entry) o2;
		                 return ((Comparable) e2.getValue()).compareTo(e1.getValue());
		             }
		         });
		Map<String,Integer> sortedPostings=new LinkedHashMap<String, Integer>();
		Iterator<Map.Entry> iterator=a.iterator();
		while(iterator.hasNext())
		{
		Map.Entry entry=iterator.next();
		sortedPostings.put(entry.getKey().toString(),Integer.parseInt(entry.getValue().toString()));
		}
		return sortedPostings;
	}

	
	
}
