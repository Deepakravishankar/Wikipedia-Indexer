package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.CAPITALIZATION)
public class CapitalizationRule implements TokenizerRule  {
	public void apply(TokenStream stream) throws TokenizerException 
	{
		if (stream != null) 
		{
			String buffer;	
			int count=0;
			while (stream.hasNext())
			{  
				buffer=stream.next();
				if(buffer.matches("\\w+")&&count==0)
				{
					count=1;
					String token=buffer.toLowerCase();
					stream.previous();
					stream.set(token);
					stream.next();
				}
				
			}
			count=0;	
		}
		stream.reset();
	}
}