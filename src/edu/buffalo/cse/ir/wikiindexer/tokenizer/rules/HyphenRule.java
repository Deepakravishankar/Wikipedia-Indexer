package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;


import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.HYPHEN)
public class HyphenRule implements TokenizerRule 
{

	@Override
	public void apply(TokenStream stream) throws TokenizerException
	{
	if (stream != null) 
	{
		String buffer;
		stream.reset();
			
		while (stream.hasNext()) 
		{
			buffer=stream.next();
			if(buffer.matches("(\\s*-+)+\\s*"))
			{
			stream.previous();
			stream.remove();
			}
			else if(buffer.matches("\\D+\\s*\\-+\\s*\\D+\\.*"))
				{
				stream.previous();
				stream.set(buffer.replaceAll("[\\-.]+"," ").trim());
				stream.next();
			}
		}
		}
	}
}