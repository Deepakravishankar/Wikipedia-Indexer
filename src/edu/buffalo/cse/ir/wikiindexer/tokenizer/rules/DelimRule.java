package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;


import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.DELIM)
public class DelimRule implements TokenizerRule
{
	String delim;
	
	public DelimRule(String delim)
	{
		this.delim=delim;
	}
	
	public void apply(TokenStream stream) throws TokenizerException 
	{	
		if (stream != null) 
		{
			String buffer;
			while (stream.hasNext())
			{  
					buffer=stream.next();
					String[] token=buffer.split(delim);
					String[] token1=new String[token.length];
					int i=0;
					for(String tok:token)
					{
						if(tok!=null&&tok!="")
					{
						token1[i]=tok;
						i++;
					}
					}
					stream.previous();
					stream.set(token1);
					stream.next();
			}
		}
		stream.reset();
	}
}