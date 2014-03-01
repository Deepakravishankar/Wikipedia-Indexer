package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.util.HashMap;
import java.util.Map;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.DATES)
public class DateRule implements TokenizerRule
{
	@Override
	public void apply(TokenStream stream) throws TokenizerException 
	{ 
		
		Map<String,String> tokenMap=new HashMap<String,String>();
		StringBuffer strbuf=new StringBuffer();
		String buffer;
		String day=null;
		String month=null;
		String year=null;
		int commacount=0;
		boolean bcadflag=false;
		String hours=null;
		String mins=null;
		String seconds=null;
		boolean pmtag=false;
		boolean adbcflag=false;
		boolean monthflag=false;
		int noofyears=0;
		tokenMap.put("January","01");
		tokenMap.put("February","02");
		tokenMap.put("March","03");
		tokenMap.put("April","04");
		tokenMap.put("May","05");
		tokenMap.put("June","06");
		tokenMap.put("July","07");
		tokenMap.put("August","08");
		tokenMap.put("September","09");
		tokenMap.put("October","10");
		tokenMap.put("November","11");
		tokenMap.put("December","12");
		
		if (stream!= null) 
		{
			while (stream.hasNext())
			{
				buffer=stream.next();
				int bcadcount=0;
				int adbccount=0;
				bcadcount=stream.query("BC");
				adbccount=stream.query("AD.");
				if(bcadcount>0)
				{
					bcadflag=true;
				}
				if(adbccount>0)
				{
					adbcflag=true;
				}
				boolean flag=false;
				if(tokenMap.containsKey(buffer))
				{
					flag=true;
				}
				if((buffer.matches("\\d+")&&(bcadflag==true)))
				{

					if(buffer.matches("\\d+"))
					{
						if(buffer.matches("\\d{4,}"))
						{
							year=buffer;
							month="01";
							day="01";
						}
						else if(buffer.matches("\\d{3,}"))
						{
							year="-0"+buffer;
							month="01";
							day="01";
						}
						else if(buffer.matches("\\d{2,}"))
						{
							year="-00"+buffer;
							month="01";
							day="01";
						}
						else 
						{
							year="-000"+buffer;
							month="01";
							day="01";
						}
						
						strbuf.append(year);
						strbuf.append(month);
						strbuf.append(day);
					}
				}
				else if((buffer.matches("\\d+")&&adbcflag==true))
				{

					if(buffer.matches("\\d+"))
					{
						if(buffer.matches("\\d{4,}"))
						{
							year=buffer;
							month="01";
							day="01";
						}
						else if(buffer.matches("\\d{3,}"))
						{
							year="0"+buffer;
							month="01";
							day="01";
						}
						else if(buffer.matches("\\d{2,}"))
						{
							year="00"+buffer;
							month="01";
							day="01";
						}
						else 
						{
							year="000"+buffer;
							month="01";
							day="01";
						}	
						strbuf.append(year);
						strbuf.append(month);
						strbuf.append(day+".");
					}
				}
				else
				{
					stream.previous();
					stream.set(buffer);
					stream.next();
				}
				if((buffer.matches("[\\d,]+|\\d+")||(flag==true))&&bcadflag==false&&adbcflag==false)
				{
					if(year==null)
					{
					year="1900";
					}
				if(buffer.matches("\\d{3,}|[\\d]{3,},"))
				{	
					if(buffer.matches("\\d{3,}"))
					{
				
						if(buffer.matches("\\d{4,}"))
						{
						year=buffer;
						noofyears=stream.query(year);
						}
						else
						{
						year="0"+buffer;
						noofyears=stream.query(year);
						}
					}
					else
					{
					String dummy=buffer.replaceAll("\\W+","");
					stream.previous();
					stream.set(dummy);
					buffer=stream.next();
					year=buffer;
					commacount++;
					}
				}
				else if(tokenMap.containsKey(buffer))
				{
					month=tokenMap.get(buffer);
				}
				else 
				{
						if(buffer.matches("\\d"))
						{
						day="0"+buffer;
						}
						else if(buffer.matches("\\d,"))
						{
							String dummy=buffer.replaceAll("\\W+","");
							stream.previous();
							stream.set(dummy);
							buffer=stream.next();
							day="0"+buffer;
							commacount++;
						}
					else
					{
						if(buffer.matches("\\d+"))
						{
						day=buffer;
						}
						else
						{
						
						String dummy=buffer.replaceAll("\\W+","");
						stream.previous();
						stream.set(dummy);
						buffer=stream.next();
						day=buffer;	
						}
					}	
				}	
			}
				
				else
				{
					stream.previous();
					stream.set(buffer);
					stream.next();
				}
			}
			    if(year!=null && bcadflag==false && adbcflag==false)
				strbuf.append(year);
			    if(month!=null && bcadflag==false && adbcflag==false)
			    {
				strbuf.append(month);
			    }
			    if(day!=null && bcadflag==false && adbcflag==false)
			    {
				strbuf.append(day);
			    }
				if(commacount>0)
				{
					strbuf.append(",");
				}
		}
		stream.reset();
		if(stream!=null)
		{
			
			int tokenPos=0;
			if(month==null)
			{
				strbuf.append("01");
				monthflag=true;
			}
			if(day==null)
			{
				strbuf.append("01");
			}
			while(stream.hasNext())
			{
				
				buffer=stream.next();
				int noofdates=0;
				if(year!=null)
				noofdates=stream.query(year);
				if(noofdates>1)
				{
					String tok=buffer.replaceAll(year,strbuf.toString());
					stream.previous();
					stream.set(tok);
					stream.next();
				}
				else if(buffer.matches("\\d+")||tokenMap.containsKey(buffer))
				{ 
					if(tokenPos==0)
					{
						stream.previous();
						stream.set(strbuf.toString());
						stream.next();
						tokenPos=1;
					}
					else
					{
					stream.previous();
					stream.remove();
					}
					}
			}	

		stream.reset();
		while(stream.hasNext())
		{
		buffer=stream.next();
		if(buffer.equals("BC"))
		{
			stream.previous();
			stream.remove();
		
		}
		else if(buffer.equals("UTC"))
		{
			
			stream.previous();
			stream.remove();
			buffer=stream.previous();
			strbuf.append(" "+buffer);
			stream.next();
			stream.remove();
			stream.remove();
			stream.remove();
			stream.previous();
			stream.set(strbuf.toString());
			stream.next();
		}
		else if(buffer.equals("AD.")||buffer.equals("AD"))
		{
			stream.previous();
			stream.remove();
		}
		else if (buffer.equals("am.")||buffer.equals("am")||buffer.equals("AM"))
		{
			pmtag=true;
			stream.previous();
			stream.remove();
		}
		else if(buffer.equals("pm.")||buffer.equals("pm")||buffer.equals("PM")||buffer.matches("\\d+\\:+\\d+\\w+.*"))
		{
		
		String pmt=buffer.replaceAll("[a-zA-Z].+","");
		stream.previous();
		stream.set(pmt);
		stream.next();
		}
		}
		stream.reset();
		while(stream.hasNext())
		{
		buffer=stream.next();
		StringBuffer timebuf=new StringBuffer();
		if(buffer.matches("\\d+\\:+\\d+.*"))
		{
		int amtag=0;
		boolean tag=false;
		amtag=stream.query("am");
		if(amtag>0)
		{
			 tag=true;
		}
		if(buffer.matches("\\d+\\:+\\d+"))
		{
			
			String[] loctok=buffer.split(":");
			if(pmtag==true)
			{
			hours=loctok[0];
			mins=loctok[1];
			seconds="00.";
			}
			else
			{
				int hour=Integer.parseInt(loctok[0])+12;
				hours=""+hour;
				mins=loctok[1];
				seconds="00.";
			}
			timebuf.append(hours+":");
			timebuf.append(mins+":");
			timebuf.append(seconds);
			stream.previous();
			stream.set(timebuf.toString());
			stream.next();
		}
		}
		else
		{
			stream.previous();
			stream.set(buffer);
			stream.next();
		}
	}
		if(noofyears>1 && monthflag==false)
		{
			stream.reset();
			while(stream.hasNext() && noofyears>1 )
			{
				StringBuffer strbuff=new StringBuffer();
				buffer=stream.next();
				if(buffer.matches("\\d{4,}"))
				{
					noofyears--;
					String yrtok=buffer.substring(0,4);
					if(yrtok!=null && !yrtok.equals(""))
					{
					strbuff.append(yrtok);
					strbuff.append("01");
					strbuff.append("01");
					stream.previous();
					stream.set(strbuff.toString());
					stream.next();
					}
				}
			}
		}
		stream.reset();
		
		while(stream.hasNext())
		{
			StringBuffer splbuf=new StringBuffer();
			buffer=stream.next();
			if(buffer.matches("\\d+\\W\\d+\\.*"))
			{
				String[] split=buffer.split("\\W+");
				String[] split2=split[1].split("\\.");
				if(split[0].matches("\\d{3,}"))
				{
				year=split[0];
				month="01";
				day="01";
				splbuf.append(year);
				splbuf.append(month);
				splbuf.append(day+"Ð");
				}
				if(split2[0].matches("\\d+"))
				{
				
					year="20"+split2[0];
					month="01";
					day="01";
					splbuf.append(year);
					splbuf.append(month);
					splbuf.append(day+".");
				}
				stream.previous();
				stream.set(splbuf.toString());
				stream.next();
			}
		}
		}
	stream.reset();
	}	
}
