package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.PUNCTUATION)
public class PunctuationRule implements TokenizerRule {

@Override
public void apply(TokenStream stream) throws TokenizerException 
{
	if (stream != null) 
	{
		String buffer;	
		while (stream.hasNext())
		{  
			buffer=stream.next();
			if(buffer.matches("[\\d+\\W+]+\\W+"))
			{ 
				String token=buffer.replaceAll("[^\\w.]+","");
				stream.previous();
				stream.set(token);
				stream.next();
		
			}
			else if(buffer.matches("[\\d+\\W+]+"))
			{
		
			}
			else if(buffer.matches("\\W+[a-z]+|\\W*[a-z]+\\W+[a-z]+\\W*"))
			{
				
			}
			else if(buffer.matches("[a-zA-Z]\\.+[a-zA-Z]\\.*"))
			{
				String[] token=buffer.split("\\.");
				StringBuffer token1=new StringBuffer();
				int i=0;
				for(String tok:token)
				{
					if(tok!=null&&tok!="")
					{
						token1.append(tok);
						i++;
					}
				}
				stream.previous();
				stream.set(token1.toString());
				stream.next();
			}
			else 
			{
				String token=buffer.replaceAll("(\\.|\\?|!)\\s*"," ").trim();
				stream.previous();
				stream.set(token);
				stream.next();
			}
		}	
	}
	stream.reset();
}
}