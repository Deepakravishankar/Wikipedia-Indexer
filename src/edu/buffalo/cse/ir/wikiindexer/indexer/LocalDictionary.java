/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * @author nikhillo
 * This class represents a subclass of a Dictionary class that is
 * local to a single thread. All methods in this class are
 * assumed thread safe for the same reason.
 */
public class LocalDictionary extends Dictionary 
{
	
	
	int id; 
	/**
	 * Public default constructor
	 * @param props: The properties file
	 * @param field: The field being indexed by this dictionary
	 */
	public LocalDictionary(Properties props, INDEXFIELD field) {
		super(props, field);
		// TODO Auto-generated constructor stub
		id=0;
		try 
		{
			File file=new File(props.getProperty("tmp.dir")+field.toString()+"DICT");
			if(!file.exists()) //if the file does not exists
			{
					file.createNewFile(); //create the file
			} 
			writer=new PrintWriter(new FileOutputStream(file),false);//append mode
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
	 * Method to lookup and possibly add a mapping for the given value
	 * in the dictionary. The class should first try and find the given
	 * value within its dictionary. If found, it should return its
	 * id (Or hash value). If not found, it should create an entry and
	 * return the newly created id.
	 * @param value: The value to be looked up
	 * @return The id as explained above.
	 */
	public int lookup(String value) 
	{
		//TODO Implement this method
		if(exists(value))//if the value exists in the dictionary..
		{
			return dictionary.get(value);//return its id
		}
		else//add it to the dictionary and..
		{
			id++;//increment the id
			dictionary.put(value,id);//put it into the dictionary
			return id;//return the id
		}
	}
}
