package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.ACCENTS)
public class AccentRule implements TokenizerRule{
	@Override
	public void apply(TokenStream stream) throws TokenizerException 
	{
		if (stream != null) 
		{
			Map<Character,String> token=new HashMap<Character,String>();
			token.put('À', "A");
			token.put('Á',"A");
			token.put('Â',"A");
			token.put('Ã',"A");
			token.put('Ä',"A");
			token.put('Å',"A");
			token.put('Æ',"AE");
			token.put('Ç',"C");
			token.put('È',"E");
			token.put('É',"E");
			token.put('Ê',"E");
			token.put('Ë',"E");
			token.put('Ì',"I");
			token.put('Í',"I");
			token.put('Î',"I");
			token.put('Ï',"I");
			token.put('Ĳ',"IJ");
			token.put('Ð',"D");
			token.put('Ò',"O");
			token.put('Ó',"O");
			token.put('Ô',"O");
			token.put('Õ',"O");
			token.put('Ö',"O");
			token.put('Ø',"O");
			token.put('Œ',"OE");
			token.put('Ù',"U");
			token.put('Ú',"U");
			token.put('Û',"U");
			token.put('Ü',"U");
			token.put('Ý',"Y");
			token.put('Ÿ',"Y");
			token.put('à',"a");
			token.put('á',"a");
			token.put('â',"a");
			token.put('ã',"a");
			token.put('ä',"a");
			token.put('å',"a");
			token.put('æ',"ae");
			token.put('ç',"c");
			token.put('è',"e");
			token.put('é',"e");
			token.put('ê',"e");
			token.put('ë',"e");
			token.put('ì',"i");
			token.put('í',"i");
			token.put('î',"i");
			token.put('ï',"i");
			token.put('ĳ',"ij");
			token.put('ð',"d");
			token.put('ñ',"n");
			token.put('ò',"o");
			token.put('ó',"o");
			token.put('ô',"o");
			token.put('õ',"o");
			token.put('ö',"o");
			token.put('ø',"o");
			token.put('œ',"oe");
			token.put('ß',"ss");
			token.put('þ',"th");
			token.put('ù',"u");
			token.put('ú',"u");
			token.put('û',"u");
			token.put('ü',"u");
			token.put('ý',"y");
			token.put('ÿ',"y");
			token.put('ﬀ',"ff");
			token.put('ﬁ',"fi");
			token.put('ﬂ',"fl");
			token.put('ﬃ',"ffi");
			token.put('ﬄ',"ffl");
			token.put('ﬅ',"ft");
			token.put('ﬆ',"st");
			token.put((char)768,"");
			token.put((char)1072,"a");
			Map<String,String> tokenMap=new HashMap<String,String>();
			tokenMap.put("nа̀ра","naра");
			tokenMap.put("nара̀","napa");
			String buffer;	
			while (stream.hasNext())
			{  
				buffer=stream.next();
				if(buffer.matches("[\\w*\\W+\\w*]+"))
				{
				String newtok=null;
				StringBuffer newchar=new StringBuffer();
				if(tokenMap.containsKey(buffer))
				{
					newtok=tokenMap.get(buffer);
					newchar.append(newtok);
				}
				else
				{
				for(Character tok:buffer.toCharArray())
				{
				if(token.containsKey(tok))
				{	
					 newtok=token.get(tok);
					 newchar.append(newtok);
				}
				else
				{
					newchar.append(tok);
				}
				}
				}
				stream.previous();
				stream.set(newchar.toString());
				stream.next();
				}		
			}
		}
		stream.reset();
	}
}