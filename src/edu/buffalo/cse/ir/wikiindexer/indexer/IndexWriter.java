/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

/**
 * @author nikhillo
 * This class is used to write an index to the disk
 * 
 */
public class IndexWriter implements Writeable 
{
	Hashtable<Object,LinkedList<SimpleEntry<Object,Integer>>> index;
	Properties props;
	INDEXFIELD keyField;
	INDEXFIELD valueField;
	boolean isForward;
	PrintWriter writer;
	int currentPartition;
	
	
	/**
	 * Constructor that assumes the underlying index is inverted
	 * Every index (inverted or forward), has a key field and the value field
	 * The key field is the field on which the postings are aggregated
	 * The value field is the field whose postings we are accumulating
	 * For term index for example:
	 * 	Key: Term (or term id) - referenced by TERM INDEXFIELD
	 * 	Value: Document (or document id) - referenced by LINK INDEXFIELD
	 * @param props: The Properties file
	 * @param keyField: The index field that is the key for this index
	 * @param valueField: The index field that is the value for this index
	 */
	public IndexWriter(Properties props, INDEXFIELD keyField, INDEXFIELD valueField) 
	{
		this(props, keyField, valueField, false);
	}
	
	/**
	 * Overloaded constructor that allows specifying the index type as
	 * inverted or forward
	 * Every index (inverted or forward), has a key field and the value field
	 * The key field is the field on which the postings are aggregated
	 * The value field is the field whose postings we are accumulating
	 * For term index for example:
	 * 	Key: Term (or term id) - referenced by TERM INDEXFIELD
	 * 	Value: Document (or document id) - referenced by LINK INDEXFIELD
	 * @param props: The Properties file
	 * @param keyField: The index field that is the key for this index
	 * @param valueField: The index field that is the value for this index
	 * @param isForward: true if the index is a forward index, false if inverted
	 */
	public IndexWriter(Properties props, INDEXFIELD keyField, INDEXFIELD valueField, boolean isForward) 
	{
		//TODO: Implement this method
		this.props=props;
		this.keyField=keyField;
		this.valueField=valueField;
		this.isForward=isForward;
		this.currentPartition=0;//intially set it as 0
		File file;
		index=new Hashtable<Object, LinkedList<SimpleEntry<Object,Integer>>>();
		
		//this is the case for TERM,AUTHOR AND CATEGORY
		if(isForward)//For every doc key represented by its integer id...we have a linked list postings of terms/authors/category(strings)
			{
			file=new File(props.getProperty("tmp.dir")+keyField.toString()+"FWDINDEX");
			}
		else//for every string key (term/author/category) we have a postings which is a linked list of docs id(integers)
			{
			file=new File(props.getProperty("tmp.dir")+keyField.toString()+"INVINDEX");
			}
		try 
		{
			writer=new PrintWriter(new FileOutputStream(file),false);//not append mode
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to make the writer self aware of the current partition it is handling
	 * Applicable only for distributed indexes.
	 * @param pnum: The partition number
	 */
	public void setPartitionNumber(int pnum) 
	{
		//TODO: Optionally implement this method
	}
	
	/**
	 * Method to add a given key - value mapping to the index
	 * @param keyId: The id for the key field, pre-converted
	 * @param valueId: The id for the value field, pre-converted
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(int keyId, int valueId, int numOccurances) throws IndexerException 
	{
		//TODO: Implement this method
		Integer key=keyId;
		Integer value=valueId;
		if(index.containsKey(key))//if the key is already in the index
		{
			SimpleEntry<Object,Integer> simpleEntry=new SimpleEntry<Object, Integer>(value,numOccurances);
			index.get(key).add(simpleEntry);//add it to the postings list
		}
		else//if the key is not in the index
		{
			SimpleEntry<Object,Integer> simpleEntry=new SimpleEntry<Object, Integer>(value,numOccurances);//create a simple entry
			LinkedList<SimpleEntry<Object,Integer>> linkedList=new LinkedList<SimpleEntry<Object,Integer>>();//create a linkedList of simple entries
			linkedList.add(simpleEntry);//add the simple entry to the linked list
			index.put(keyId, linkedList);//add the linkedlist to the corresponding key in the index
		}
	}
	
	/**
	 * Method to add a given key - value mapping to the index
	 * @param keyId: The id for the key field, pre-converted
	 * @param value: The value for the value field
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(int keyId, String value, int numOccurances) throws IndexerException 
	{
		//TODO: Implement this method
		Integer key=keyId;
		if(index.containsKey(key))//if the key is already in the index
		{
			SimpleEntry<Object,Integer> simpleEntry=new SimpleEntry<Object, Integer>(value,numOccurances);
			index.get(key).add(simpleEntry);//add it to the postings list
		}
		else//if the key is not in the index
		{
			SimpleEntry<Object,Integer> simpleEntry=new SimpleEntry<Object, Integer>(value,numOccurances);//create a simple entry
			LinkedList<SimpleEntry<Object,Integer>> linkedList=new LinkedList<SimpleEntry<Object,Integer>>();//create a linkedList of simple entries
			linkedList.add(simpleEntry);//add the simple entry to the linked list
			index.put(keyId, linkedList);//add the linkedlist to the corresponding key in the index
		}
	}
	
	/**
	 * Method to add a given key - value mapping to the index
	 * @param key: The key for the key field
	 * @param valueId: The id for the value field, pre-converted
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(String key, int valueId, int numOccurances) throws IndexerException 
	{
		//TODO: Implement this method
		Integer value=valueId;
		if(index.containsKey(key))//if the key is already in the index
		{
			SimpleEntry<Object,Integer> simpleEntry=new SimpleEntry<Object, Integer>(value,numOccurances);
			index.get(key).add(simpleEntry);//add it to the postings list
		}
		else//if the key is not in the index
		{
			SimpleEntry<Object,Integer> simpleEntry=new SimpleEntry<Object, Integer>(value,numOccurances);//create a simple entry
			LinkedList<SimpleEntry<Object,Integer>> linkedList=new LinkedList<SimpleEntry<Object,Integer>>();//create a linkedList of simple entries
			linkedList.add(simpleEntry);//add the simple entry to the linked list
			index.put(key, linkedList);//add the linkedlist to the corresponding key in the index
		}
	}
	
	/**
	 * Method to add a given key - value mapping to the index
	 * @param key: The key for the key field
	 * @param value: The value for the value field
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(String key, String value, int numOccurances) throws IndexerException 
	{
		//TODO: Implement this method
		if(index.containsKey(key))//if the key is already in the index
		{
			SimpleEntry<Object,Integer> simpleEntry=new SimpleEntry<Object, Integer>(value,numOccurances);
			index.get(key).add(simpleEntry);//add it to the postings list
		}
		else//if the key is not in the index
		{
			SimpleEntry<Object,Integer> simpleEntry=new SimpleEntry<Object, Integer>(value,numOccurances);//create a simple entry
			LinkedList<SimpleEntry<Object,Integer>> linkedList=new LinkedList<SimpleEntry<Object,Integer>>();//create a linkedList of simple entries
			linkedList.add(simpleEntry);//add the simple entry to the linked list
			index.put(key, linkedList);//add the linkedlist to the corresponding key in the index
		}
	}

	/* (non-Javadoc)
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#writeToDisk()
	 */
	public void writeToDisk() throws IndexerException 
	{
		// TODO Implement this method
		for(Object key:index.keySet())
		{
			writer.print(key.toString()+"->");
			LinkedList<SimpleEntry<Object,Integer>> linkedList=index.get(key);
			Iterator<SimpleEntry<Object,Integer>> iterator=linkedList.iterator();
			while(iterator.hasNext())
			{
				SimpleEntry<Object,Integer> simpleEntry=iterator.next();
				writer.print(simpleEntry.getKey()+"-"+simpleEntry.getValue()+",");
			}
			writer.println();
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
		index=new Hashtable<Object, LinkedList<SimpleEntry<Object,Integer>>>();
	}

}
