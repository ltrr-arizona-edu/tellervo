package org.tellervo.server;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.util.StringUtils;

public class TimeKeeper {
	private static final Logger log = LoggerFactory.getLogger(TimeKeeper.class);

	long start;
	long previous;
	
	public TimeKeeper()
	{
		start = new Date().getTime();
		previous = start;
	}
	
	public long getTimeSinceStart()
	{
		long now = new Date().getTime();
		return now - start;
	}
	
	
	public String getTimeSinceStartAsString()
	{
		String val = String.valueOf(getTimeSinceStart());
		return val;
	}
	
	public String getTimeSincePrevious()
	{
		long now = new Date().getTime();
		
		String val = String.valueOf(now - previous);
		previous = now;
		
		return val;
		
	}
	
	public void log(String label)
	{
		log.debug(StringUtils.leftPad(getTimeSinceStartAsString(), 8) +" ms -   "+StringUtils.leftPad(getTimeSincePrevious(), 8)+" ms  --  ("+label + ")");
	}
}
