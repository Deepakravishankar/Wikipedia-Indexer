package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.STOPWORDS)
public class StopWordsRule implements TokenizerRule {
	@Override
	public void apply(TokenStream stream) throws TokenizerException 
	{
		if (stream != null) 
		{
			String buffer;	
			String[] stopWords={"a","all","also","am","an","and","any","are","as","at","be","because","been","but","by","can","cannot","could","did","do","does","either","else","ever","every","for","from","get","got","had","has","have","he","her","hers","him","his","how","however","i","if","in","into","is","it","its","just","least","let","like","likely","may","me","might","most","must","my","neither","no","nor","not","of","off","often","on","only","or","other","our","own","rather","s","said","say","says","she","should","since","so","some","than","that","the","their","them","then","there","these","they","this","tis","to","too","us","wants","was","we","were","what","when","","where","which","while","who","whom","why","will","with","would","yet","you","your"};
			while (stream.hasNext())
			{  
			buffer=stream.next();
			for(String token:stopWords)
			{
				if(buffer.toLowerCase().equals(token))
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