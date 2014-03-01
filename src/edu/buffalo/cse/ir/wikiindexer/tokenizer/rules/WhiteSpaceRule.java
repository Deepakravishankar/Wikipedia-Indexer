package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.WHITESPACE)
public class WhiteSpaceRule implements TokenizerRule 
{

	@Override
	public void apply(TokenStream stream) throws TokenizerException 
	{	
		if (stream != null)
		{
		String buffer[];	
		while (stream.hasNext())
		{ 
		String token=stream.next().replaceAll("[ \n\r]+"," ").trim();
		buffer = token.split(" ");
		stream.previous();
		stream.set(buffer);
		stream.next();
		}
	}
		stream.reset();	
	}
}