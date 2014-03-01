/**
* 
*/
package edu.buffalo.cse.ir.wikiindexer.tokenizer;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
* This class represents a stream of tokens as the name suggests.
* It wraps the token stream and provides utility methods to manipulate it
* @author nikhillo
*
*/
public class TokenStream implements Iterator<String>
{
LinkedList<String> tokenStream;
int currentPosition=0;
/**
* Default constructor
* @param bldr: THe stringbuilder to seed the stream
*/
public TokenStream(StringBuilder bldr)
{
//TODO: Implement this method
tokenStream=new LinkedList<String>();
if(bldr!=null && !bldr.equals(""))
{
tokenStream.add(bldr.toString());
//	currentPosition++;
}
}

/**
* Overloaded constructor
* @param bldr: THe stringbuilder to seed the stream
*/
public TokenStream(String string) {
tokenStream=new LinkedList<String>();
if(string!=null && string!="")
{
tokenStream.add(string);
//currentPosition++;
}
}

/**
* Method to append tokens to the stream
* @param tokens: The tokens to be appended
*/
public void append(String... tokens) {
//TODO: Implement this method
if(tokens!=null)
{
for(String token: tokens)
{
if(token!="" && token !=null)
tokenStream.add(token);
}
}
}

/**
* Method to retrieve a map of token to count mapping
* This map should contain the unique set of tokens as keys
* The values should be the number of occurrences of the token in the given stream
* @return The map as described above, no restrictions on ordering applicable
*/
public Map<String, Integer> getTokenMap() {
Map<String, Integer> tokenmap = new HashMap<String, Integer>();
for(int i=0;i<tokenStream.size();i++)
{

boolean flag=tokenmap.containsKey(tokenStream.get(i));
if(flag==false)
{
tokenmap.put(tokenStream.get(i), 1);
}
else
{
int value=tokenmap.get(tokenStream.get(i));
tokenmap.put(tokenStream.get(i), value+1);
}

}
if(tokenmap.size()==0)
tokenmap=null;
return tokenmap;
}

/**
* Method to get the underlying token stream as a collection of tokens
* @return A collection containing the ordered tokens as wrapped by this stream
* Each token must be a separate element within the collection.
* Operations on the returned collection should NOT affect the token stream
*/
public Collection<String> getAllTokens() {
//TODO: Implement this method
LinkedList<String> myCollection= new LinkedList<String>();
if(tokenStream.size()==0)
return null;
for(String list:tokenStream)
myCollection.add(list);

return myCollection;
}

/**
* Method to query for the given token within the stream
* @param token: The token to be queried
* @return: THe number of times it occurs within the stream, 0 if not found
*/
public int query(String token) {
int numberoftimes=0;
for(int i=0;i<tokenStream.size();i++){
if(token.equals(tokenStream.get(i)))
numberoftimes++;	
}
return numberoftimes;
}

/**
* Iterator method: Method to check if the stream has any more tokens
* @return true if a token exists to iterate over, false otherwise
*/
public boolean hasNext() {
if((currentPosition<=(tokenStream.size()-1)) && (currentPosition>=0))
return true;
else
return false;
}

/**
* Iterator method: Method to check if the stream has any more tokens
* @return true if a token exists to iterate over, false otherwise
*/
public boolean hasPrevious() {
//TODO: Implement this method
if((currentPosition>=1 )&& (currentPosition<=tokenStream.size()))
{
return true;
}
else
return false;

}

/**
* Iterator method: Method to get the next token from the stream
* Callers must call the set method to modify the token, changing the value
* of the token returned by this method must not alter the stream
* @return The next token from the stream, null if at the end
*/
public String next() {
// TODO: Implement this method

if(hasNext())
{

String token=tokenStream.get(currentPosition);
currentPosition++;
return token;
}
return null;
}

/**
* Iterator method: Method to get the previous token from the stream
* Callers must call the set method to modify the token, changing the value
* of the token returned by this method must not alter the stream
* @return The next token from the stream, null if at the end
*/
public String previous() {
//TODO: Implement this method
if(hasPrevious())
{
currentPosition--;
String token=tokenStream.get(currentPosition);
return token;
}
else
return null;
}


/**
* Iterator method: Method to remove the current token from the stream
*/
public void remove() {
// TODO: Implement this method
if(tokenStream.size()!=0 && currentPosition!=tokenStream.size())
tokenStream.remove(currentPosition);	

}

/**
* Method to merge the current token with the previous token, assumes whitespace
* separator between tokens when merged. The token iterator should now point
* to the newly merged token (i.e. the previous one)
* @return true if the merge succeeded, false otherwise
*/
public boolean mergeWithPrevious() {
//TODO: Implement this method
if(tokenStream.size()!=0 && currentPosition!=0 && tokenStream.size()!=1)
{
String index=tokenStream.get(currentPosition);
currentPosition--;
String previous=tokenStream.get(currentPosition);
if(index!=null && previous!=null)
{
String merge=previous+" "+index;
tokenStream.set(currentPosition, merge);
tokenStream.remove(currentPosition+1);
return true;
}
}
return false;
}

/**
* Method to merge the current token with the next token, assumes whitespace
* separator between tokens when merged. The token iterator should now point
* to the newly merged token (i.e. the current one)
* @return true if the merge succeeded, false otherwise
*/
public boolean mergeWithNext() {
//TODO: Implement this method
if(tokenStream.size()!=0 && currentPosition!=tokenStream.size() && tokenStream.size()!=1)
{
String index=tokenStream.get(currentPosition);
currentPosition++;
String next=tokenStream.get(currentPosition);
if(index!=null && next!=null)
{
currentPosition--;
String merge=index+" "+next;
tokenStream.set(currentPosition,merge);
tokenStream.remove(currentPosition+1);
return true;
}
}
return false;
}

/**
* Method to replace the current token with the given tokens
* The stream should be manipulated accordingly based upon the number of tokens set
* It is expected that remove will be called to delete a token instead of passing
* null or an empty string here.
* The iterator should point to the last set token, i.e, last token in the passed array.
* @param newValue: The array of new values with every new token as a separate element within the array
*/
public void set(String... newValue)
{
//TODO: Implement this method
LinkedList<String> newList=new LinkedList<String>();
if(tokenStream.size()!=0 &&currentPosition!=tokenStream.size())
{
for(int i=0;i< newValue.length;i++)
{ 
if(newValue[i]!=null && newValue[i]!="")
newList.add(newValue[i]);
}	

if(newList.size()!=0)
{
tokenStream.remove(currentPosition);
tokenStream.addAll(currentPosition,newList);
currentPosition=currentPosition+newList.size()-1;
}
}
}

/**
* Iterator method: Method to reset the iterator to the start of the stream
* next must be called to get a token
*/
public void reset() {
currentPosition=0;
//TODO: Implement this method
}

/**
* Iterator method: Method to set the iterator to beyond the last token in the stream
* previous must be called to get a token
*/
public void seekEnd() {
int size=tokenStream.size();
currentPosition=size;
}

/**
* Method to merge this stream with another stream
* @param other: The stream to be merged
*/
public void merge(TokenStream other) {
//TODO: Implement this method
if(other!=null)
{
if(other.tokenStream.size()!=0)
tokenStream.addAll(other.tokenStream);
}
}
}