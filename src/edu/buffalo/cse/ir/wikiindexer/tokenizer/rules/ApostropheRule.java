package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.util.ArrayList;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.APOSTROPHE)
public class ApostropheRule implements TokenizerRule {
	public void apply(TokenStream stream) throws TokenizerException 
	{
		if (stream != null) 
		{
			String buffer;
			while (stream.hasNext())
			{  
				buffer=stream.next();
				if(buffer.matches("[a-z]+'+[a-z]+|\\w'\\w*|\\w*'\\w{2,}|\\w+'[d]"))
				{
					String[] token1=buffer.split("'");
					String[] token5=new String[token1.length];
					int i=0;
					for(String tok:token1)
					{
						if(!tok.equals(""))
						{
							token5[i]=tok;	
							i++;
						}
					}
					stream.previous();
					stream.set(token5);

					buffer=stream.next();
					

				if(buffer.equals("t"))
				{
					String token=buffer.replaceAll("[t]","not");
					stream.previous();
					stream.set(token);
					stream.next();
					stream.reset();
					buffer=stream.next();
					if(buffer.matches("[won]+|[shan]+"))
					{
						if(buffer.matches("[won]+"))
						{
						String token2=buffer.replaceAll("won","will");
						stream.previous();
						stream.set(token2);
						stream.next();
						}
						else
						{
							String token2=buffer.replaceAll("shan","shall");
							stream.previous();
							stream.set(token2);
							stream.next();
						}
						
					}
					else if(buffer.matches("\\w*[n]+"))
					{
						String token2=buffer.replaceAll("n","");
						stream.previous();
						stream.set(token2);
						stream.next();
					}
				}
				else if(buffer.equals("s"))
				{
					String token=buffer.replaceAll("[s]","us");
					stream.previous();
					stream.set(token);
					stream.next();
					stream.reset();
					buffer=stream.next();
				}
				else if(buffer.matches("m"))
				{
					String token=buffer.replaceAll("[m]","am");
					stream.previous();
					stream.set(token);
					stream.next();
					stream.reset();
					buffer=stream.next();	
				}
				else if(buffer.contains("re"))
				{
					String token=buffer.replaceAll("re","are");
					stream.previous();
					stream.set(token);
					stream.next();
					stream.reset();
					buffer=stream.next();	
				}
				else if(buffer.contains("ve"))
				{
					String token=buffer.replaceAll("ve","have");
					stream.previous();
					stream.set(token);
					stream.next();
					stream.reset();
					buffer=stream.next();	
				}
				else if(buffer.contains("d"))
				{
					String token=buffer.replaceAll("d","would");
					stream.previous();
					stream.set(token);
					stream.next();
					stream.reset();
					buffer=stream.next();	
				}
				else if(buffer.contains("ll"))
				{
					String token=buffer.replaceAll("ll","will");
					stream.previous();
					stream.set(token);
					stream.next();
					stream.reset();
					buffer=stream.next();
				}
				else if(buffer.contains("em"))
				{
					String token=buffer.replaceAll("em","them");
					stream.previous();
					stream.set(token);
					stream.next();
					stream.reset();
					buffer=stream.next();
				}
				else
				{
					String token=buffer.replaceAll("'w*","");
					stream.previous();
					stream.set(token);
					stream.next();
				}
				}
		
				else if(buffer.matches("\\w+'\\s*\\w*"))
				{
					String token=buffer.replaceAll("'\\w*","");
					stream.previous();
					stream.set(token);
					stream.next();
				}
				else
				{
					String token=buffer.replaceAll("'+","");
					stream.previous();
					stream.set(token);
					stream.next();
				}
		}
		
		}
		stream.reset();
	}
}