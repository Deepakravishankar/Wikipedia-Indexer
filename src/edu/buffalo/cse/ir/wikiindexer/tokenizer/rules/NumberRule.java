package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.NUMBERS)
public class NumberRule implements TokenizerRule 
{
	
@Override
public void apply(TokenStream stream) throws TokenizerException 
{
	if (stream!= null) 
	{
		String buffer;
		while (stream.hasNext())
		{ 
		buffer=stream.next();
		if(!buffer.matches("\\d{8,}") && !buffer.matches("\\d+:\\d+:\\d+"))
		{
		if(buffer.matches("[\\d\\,]+"))
		{
		stream.previous();
		stream.remove();
		}
		else if(buffer.matches("\\d+\\.*"))
		{
			stream.previous();
			stream.remove();
		}
	else if(buffer.matches("\\d+\\.\\d+\\W+"))
	{
		int i=0;
		String token1=buffer.replaceAll("[\\d+.]","");
		String tok[]=new String[buffer.length()];
		if(!token1.equals(""))
		{
			tok[i]=token1;
			i++;
		}
		stream.previous();
		stream.set(tok);
		stream.next();
	}
			  
	else if(buffer.matches("\\d+\\W+\\d+"))		  
	{	
	String token1=buffer.replaceAll("\\d+","");
	stream.previous();
	stream.set(token1);
	stream.next();
	}		
	else if(buffer.matches("\\d+"))
	{
		stream.previous();
		stream.remove();
	}
}
}
}
	stream.reset();
}
}