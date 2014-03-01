package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.util.ArrayList;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.SPECIALCHARS)
public class SpecialCharRule implements TokenizerRule {

@Override
public void apply(TokenStream stream) throws TokenizerException 
{
	if (stream != null) 
	{
		String buffer;	
		while (stream.hasNext())
		{  
			buffer=stream.next();
			
			if(buffer.matches("\\s*\\W"))
					{
				if(!buffer.equals("-"))
				{
				stream.previous();
				stream.remove();
				}
					}
			
			else if(buffer.matches("\\__*\\S*"))
			{
				if(!buffer.equals("-"))
				{
				stream.previous();
				stream.remove();	
				}
			}
			else 
			{
			String [] token1=buffer.split("[^\\w\\.-]+");
			String[] myTokens=new String[token1.length];
			int i=0;
			for(String tok:token1)
			{
				if(!tok.equals(""))
				{
					myTokens[i]=tok;
					i++;
				}
			}
			stream.previous();
			stream.set(myTokens);
			stream.next();
			}
		}
		}
	stream.reset();
	}
}