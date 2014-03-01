/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nikhillo
 * This class implements Wikipedia markup processing.
 * Wikipedia markup details are presented here: http://en.wikipedia.org/wiki/Help:Wiki_markup
 * It is expected that all methods marked "todo" will be implemented by students.
 * All methods are static as the class is not expected to maintain any state.
 */
public class WikipediaParser {
	/* TODO */
	/**
	 * Method to parse section titles or headings.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Sections
	 * @param titleStr: The string to be parsed
	 * @return The parsed string with the markup removed
	 */
	public static String parseSectionTitle(String titleStr) 
	{
		if(titleStr!=null)
		{
		String output=titleStr.replaceAll("=","").trim().replaceAll(" +", " ");;//remove the = symbol and trim the leading and trailing spaces
		return output;
		}
		return titleStr;
	}
	
	/* TODO */
	/**
	 * Method to parse list items (ordered, unordered and definition lists).
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Lists
	 * @param itemText: The string to be parsed
	 * @return The parsed string with markup removed
	 */
	public static String parseListItem(String itemText) 
	{
		if(itemText!=null)
		{
			String output=itemText.replaceAll("[*#;:]","").trim().replaceAll(" +", " ");;//remove the *,#,; and : symbols and trim spaces
			return output;
		}
		return itemText;
	}
	
	/* TODO */
	/**
	 * Method to parse text formatting: bold and italics.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Text_formatting first point
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed
	 */
	public static String parseTextFormatting(String text) 
	{
		StringBuffer parsedContents=new StringBuffer();
		if(text!=null)
		{
			Pattern regex=Pattern.compile("('{2,5}.+'{2,5})");
			Matcher matcher=regex.matcher(text);
			while(matcher.find())
			{
				String output=matcher.group(1).replace("'", "");
				matcher.appendReplacement(parsedContents, output.replace("$",""));
			}
			matcher.appendTail(parsedContents);
			return parsedContents.toString().replaceAll(" +", " ").trim();
		}
		return text;
	}
	
	/* TODO */
	/**
	 * Method to parse *any* HTML style tags like: <xyz ...> </xyz>
	 * For most cases, simply removing the tags should work.
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed.
	 */
	public static String parseTagFormatting(String text) 
	{
		String regex="</?\\w+((\\s+\\w+(\\s*=\\s*(?:\".*?\"|\'.*?\'|[^\'\">\\s]+))?)+\\s*|\\s*)/?>";
		if(text!=null)
		{
		text=text.replaceAll("&lt;","<").replaceAll("&gt;",">").replaceAll("&amp","&").replaceAll("&quot;","\"").replaceAll("&apos;", "'");
		String output=text.toString().replaceAll(regex,"").trim().replaceAll(" +", " ");
		output= output.replaceAll("(?s)<!--.*?-->", "");//also remove the html comments  
		return output;
		}
		return text;
	}
	
	/* TODO */
	/**
	 * Method to parse wikipedia templates. These are *any* {{xyz}} tags
	 * For most cases, simply removing the tags should work.
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed
	 */
	public static String parseTemplates(String text) 
	{
		StringBuffer output=new StringBuffer();
		int braceCount=0;
	      if(text!=null)
	      {
           for(char ch:text.toCharArray())
           {
        	   if(ch=='{')
        	   {
        		   braceCount++;
        	   }
        	   else if(ch=='}')
        	   {
        		   braceCount--;
        	   }
        	   else if(braceCount==0)
        	   {
        		   output.append(ch);
        	   }
           }
	      }
		return output.toString().trim();	
	}
	
	
	/* TODO */
	/**
	 * Method to parse links and URLs.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Links_and_URLs
	 * @param text: The text to be parsed
	 * @return An array containing two elements as follows - 
	 *  The 0th element is the parsed text as visible to the user on the page
	 *  The 1st element is the link url
	 */
	public static String[] parseLinks(String text) 
	{
		String[] links=new String[]{"",""};
		String[] pipedParts=null;
		String textBeforeLinks=null;
		String textAfterLinks=null;
		String textWithinParantheses=null;
		Pattern regex=null;
		Matcher regexMatcher=null;
		boolean namespaceFlag=false;
		if(text!=null && text!="")
		{
			if(text.matches(".*\\[\\[.*\\]\\].*"))
			{
			//first we will separate the text content that is beginning of [[]] parantheses
			regex=Pattern.compile("(.*)\\[\\[.*\\]\\]");
			regexMatcher=regex.matcher(text);
			if (regexMatcher.find())
			textBeforeLinks = regexMatcher.group(1);
			
			//then we will separate the text contents within out parantheses
			//which is the actual link that needs to be parsed
			regex = Pattern.compile(".*\\[\\[(.*)\\]\\].*");
			regexMatcher = regex.matcher(text);
			if (regexMatcher.find())
			textWithinParantheses=regexMatcher.group(1);
			links[1]=textWithinParantheses;//when the input link is a normal link then there is no parsing necessary
			
			
			//first we will separate the text content that is after the [[]] parantheses
			regex=Pattern.compile(".*\\[\\[.*\\]\\](.*)");
			regexMatcher=regex.matcher(text);
			if (regexMatcher.find())
			textAfterLinks = regexMatcher.group(1);
			textAfterLinks=textAfterLinks.replaceAll("<nowiki( )*/>","");//just skip the nowiki tag..			
			
			//whenever the text within parantheses contains a namespace
			if(textWithinParantheses.matches("(.+:)+.*\\|*.*"))
			{
				links[1]="";//discard actual links
				namespaceFlag=true;//indicates that this link has a namespace
			}
			
			//now we need to parse the textWithinParantheses
			if(textWithinParantheses.matches(".*\\|.*"))//piped links
			{
				if(namespaceFlag)//if name space and there if piped
				{
					if(!textWithinParantheses.matches(".*#.+\\|.*"))//if namespace has section links then we need not remove namespace stuff 
					textWithinParantheses=textWithinParantheses.substring(textWithinParantheses.indexOf(':')+1);
					
				}
				
				if(textWithinParantheses.matches(".*\\|.+")&& !textWithinParantheses.matches(".*\\| +"))//links with alias names
				{
					pipedParts=textWithinParantheses.split("\\|");
					if(!namespaceFlag)
					links[1]=pipedParts[0];//the actual link
					links[0]=pipedParts[pipedParts.length-1];//what it appears to be	
				}
				else//links without alias names
				{
					if(!namespaceFlag)
					links[1]=textWithinParantheses.replace("|","");//the actual link
					
					
					else if(textWithinParantheses.matches("Category:.*"))//if the namespace is category
					{
						textWithinParantheses=textWithinParantheses.substring(textWithinParantheses.indexOf(':')+1);
					}
					
					
					links[0]=textWithinParantheses.replace("|","").replaceAll("\\(.*\\)","");//hide stuff in parantheses
					links[0]=links[0].replaceAll(",.*","");//automatically hide comma and the subsequent stuff	
				}
			}
			else//unpiped links..no need to parse stuff except for file name space
			{
				links[0]=textWithinParantheses;
				if(!namespaceFlag)//if not namespace
				{
					links[1]=textWithinParantheses;
				}
				else//if namespace
				{
				if(textWithinParantheses.matches("File:.*"))//if the namespace is file
				{
					links[0]="";//file name spaces is not our concern..it appears only as image and not as text			
				}
				else if(textWithinParantheses.matches(":?Category:.*"))//if the namespace is category
				{
					links[0]=textWithinParantheses.substring(textWithinParantheses.indexOf(':')+1);
				}
				}
			}
			}	
			
			else if(text.matches(".*\\[.*\\].*"))//external links
			{
				//first we will separate the text content that is beginning of [[]] parantheses
				regex=Pattern.compile("(.*)\\[.*\\]");
				regexMatcher=regex.matcher(text);
				if (regexMatcher.find())
				textBeforeLinks = regexMatcher.group(1);
				
				//then we will separate the text contents within out parantheses
				//which is the actual link that needs to be parsed
				regex = Pattern.compile(".*\\[(.*)\\].*");
				regexMatcher = regex.matcher(text);
				if (regexMatcher.find())
				textWithinParantheses=regexMatcher.group(1);
				links[1]=textWithinParantheses;//when the input link is a normal link then there is no parsing necessary
				
				
				//first we will separate the text content that is after the [[]] parantheses
				regex=Pattern.compile(".*\\[.*\\](.*)");
				regexMatcher=regex.matcher(text);
				if (regexMatcher.find())
				textAfterLinks = regexMatcher.group(1);
				
				links[1]="";//does not care about the external links
				if(textWithinParantheses.matches(".* +.*"))//if piped (external links use space)
				{
				pipedParts=textWithinParantheses.split(" ",2);
				links[0]=pipedParts[1];
				}
				else//if not piped
					links[0]="";
			}
			
			//capitalisation and replacing spaces with underscores
			if(links[1]!="")//outside namespaces we are not interested
			links[1]=(Character.toUpperCase(links[1].charAt(0))+links[1].substring(1).replace(" ", "_")).trim();
			links[0]=textBeforeLinks+(links[0].trim())+textAfterLinks;//the name of the link = text before links + what the link appears to be
		}
		return links;
	}
	
	
	
}
