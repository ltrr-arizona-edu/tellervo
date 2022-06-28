package org.tellervo.server;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.util.StringUtils;

/**
 * Class for recording processing times.  Keeps track of the processing time since the start, as well
 * as the time since the last marker 
 * 
 * @author pwb48
 *
 */
public class TimeKeeper {
	private static final Logger log = LoggerFactory.getLogger(TimeKeeper.class);

	long start;
	long previous;
	
	/**
	 * Construct a new time keeper and start the stop clock
	 */
	public TimeKeeper()
	{
		start = new Date().getTime();
		previous = start;
	}
	
	/**
	 * Get the number of milliseconds since this TimeKeeper stop clock was started
	 * 
	 * @return
	 */
	public long getTimeSinceStart()
	{
		long now = new Date().getTime();
		return now - start;
	}
	
	
	/**
	 * Get the time since this TimeKeeper stop clock was started as a string
	 * 
	 * @return
	 */
	public String getTimeSinceStartAsString()
	{
		String val = String.valueOf(getTimeSinceStart());
		return val;
	}
	
	/**
	 * Get the time in milliseconds since the most recent time marker
	 * 
	 * @return
	 */
	public String getTimeSincePrevious()
	{
		long now = new Date().getTime();
		
		String val = String.valueOf(now - previous);
		previous = now;
		
		return val;
		
	}
	
	/**
	 * Write the most recent time and time since start to the debug log.  Mark with the specified label.
	 * 
	 * @param label
	 */
	public void log(String label)
	{
		log.debug(StringUtils.leftPad(getTimeSinceStartAsString(), 8) +" ms -   "+StringUtils.leftPad(getTimeSincePrevious(), 8)+" ms  --  ("+label + ")");
	}
}
