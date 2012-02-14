/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.util;

import java.text.ParseException;
import java.util.Date;

/**
 * Code to represent an ISO8601 Date (dateTime format in XML)
 * 
 * All credit goes to the authors of JibX
 * http://jibx.sourceforge.net
 * 
 * @author lucasm
 *
 */

public class ISO8601Date extends Date {
	/**
	 * Construct an ISO8601 date from this date object
	 * 
	 * @param value
	 * @throws ParseException
	 */
	public ISO8601Date(String value) throws ParseException {
		super(parseDateTime(value));
	}
	
	/**
	 * @return an ISO8601-formatted representation of this date
	 */
	@Override
	public String toString() {
		return serializeDateTime(this.getTime(), true);
	}
	
	
	// all code below from Jibx Utility.java
	
    /**
     * Parse general dateTime value from text. Date values are expected to be in
     * W3C XML Schema standard format as CCYY-MM-DDThh:mm:ss.fff, with optional
     * leading sign and trailing time zone.
     *
     * @param text text to be parsed
     * @return converted date as millisecond value
     * @throws ParseException on parse error
     */
    public static long parseDateTime(String text) throws ParseException {
        // split text to convert portions separately
        int split = text.indexOf('T');
        if (split < 0) {
            throw new ParseException("Missing 'T' separator in dateTime", 0);
        }
        return parseDate(text.substring(0, split)) +
            parseTime(text, split+1, text.length());
    }


	/**
	 * Parse general time value from text. Time values are expected to be in W3C
	 * XML Schema standard format as hh:mm:ss.fff, with optional leading sign
	 * and trailing time zone.
	 * 
	 * @param text
	 *            text to be parsed
	 * @param start
	 *            offset of first character of time value
	 * @param length
	 *            number of characters in time value
	 * @return converted time as millisecond value
	 * @throws ParseException
	 *             on parse error
	 */
	public static long parseTime(String text, int start, int length)
			throws ParseException {

		// validate time value following date
		long milli = 0;
		boolean valid = length > (start + 7) && (text.charAt(start + 2) == ':')
				&& (text.charAt(start + 5) == ':');
		if (valid) {
			int hour = parseDigits(text, start, 2);
			int minute = parseDigits(text, start + 3, 2);
			int second = parseDigits(text, start + 6, 2);
			if (hour > 23 || minute > 59 || second > 60) {
				valid = false;
			} else {

				// convert to base millisecond in day
				milli = (((hour * 60) + minute) * 60 + second) * 1000;
				start += 8;
				if (length > start) {

					// adjust for time zone
					if (text.charAt(length - 1) == 'Z') {
						length--;
					} else {
						char chr = text.charAt(length - 6);
						if (chr == '-' || chr == '+') {
							hour = parseDigits(text, length - 5, 2);
							minute = parseDigits(text, length - 2, 2);
							if (hour > 23 || minute > 59) {
								valid = false;
							} else {
								int offset = ((hour * 60) + minute) * 60 * 1000;
								if (chr == '-') {
									milli += offset;
								} else {
									milli -= offset;
								}
							}
							length -= 6;
						}
					}

					// check for trailing fractional second
					if (text.charAt(start) == '.') {
						double fraction = Double.parseDouble(text.substring(
								start, length));
						milli += fraction * 1000.0;
					} else if (length > start) {
						valid = false;
					}
				}
			}
		}

		// check for valid result
		if (valid) {
			return milli;
		} else {
			throw new ParseException("Invalid dateTime format", length);
		}
	}

	/**
	 * Convert date text to Java date. Date values are expected to be in W3C XML
	 * Schema standard format as CCYY-MM-DD, with optional leading sign and
	 * trailing time zone (though the time zone is ignored in this case).
	 * 
	 * Note that the returned value is based on UTC, which matches the
	 * definition of <code>java.util.Date</code> but will typically not be the
	 * expected value if you're using a <code>java.util.Calendar</code> on the
	 * result. In this case you probably want to instead use
	 * {@link #deserializeSqlDate(String)}, which <i>does</i> adjust the value
	 * to match the local time zone.
	 * 
	 * @param text
	 *            text to be parsed
	 * @return start of day in month and year date as millisecond value
	 * @throws ParseException
	 *             on parse error
	 */
	public static long parseDate(String text) throws ParseException {
		// start by validating the length and basic format
		if (!ifDate(text)) {
			throw new ParseException("Invalid date format", 0);
		}

		// handle year, month, and day conversion
		int split = text.indexOf('-', 1);
		int year = parseInt(text.substring(0, split));
		if (year == 0) {
			throw new ParseException("Year value 0 is not allowed", 0);
		}
		int month = parseDigits(text, split + 1, 2) - 1;
		if (month < 0 || month > 11) {
			throw new ParseException("Month value out of range", split + 1);
		}
		long day = parseDigits(text, split + 4, 2) - 1;
		boolean leap = (year % 4 == 0)
				&& !((year % 100 == 0) && (year % 400 != 0));
		int[] starts = leap ? MONTHS_LEAP : MONTHS_NONLEAP;
		if (day < 0 || day >= (starts[month + 1] - starts[month])) {
			throw new ParseException("Day value out of range", split + 4);
		}
		if (year > 0) {
			year--;
		}
		day += ((long) year) * 365 + year / 4 - year / 100 + year / 400
				+ starts[month];
		return day * MSPERDAY - TIME_BASE;
	}

	/**
	 * Check if a text string follows the schema date format. This does not
	 * assure that the text string is actually a valid date representation,
	 * since it doesn't fully check the value ranges (such as the day number
	 * range for a particular month).
	 * 
	 * @param text
	 *            (non-<code>null</code>)
	 * @return <code>true</code> if date format, <code>false</code> if not
	 */
	public static boolean ifDate(String text) {
		if (text.length() < 10) {
			return false;
		} else {
			int base = 0;
			int split = text.indexOf('-');
			if (split == 0) {
				base = 1;
				split = text.indexOf('-', 1);
			}
			if (split > 0 && text.length() - split >= 5
					&& ifDigits(text, base, split)
					&& text.charAt(split + 3) == '-'
					&& ifFixedDigits(text, split + 1, "12")
					&& ifFixedDigits(text, split + 4, "31")) {
				return ifZoneSuffix(text, split + 6);
			} else {
				return false;
			}
		}
	}

	/**
	 * Check if a portion of a text string is a bounded string of decimal
	 * digits. This is used for checking fixed-length decimal fields with a
	 * maximum value.
	 * 
	 * @param text
	 * @param offset
	 * @param bound
	 * @return <code>true</code> if bounded decimal, <code>false</code> if not
	 */
	public static boolean ifFixedDigits(String text, int offset, String bound) {
		int length = bound.length();
		boolean lessthan = false;
		for (int i = 0; i < length; i++) {
			char chr = text.charAt(offset + i);
			if (chr < '0' || chr > '9') {
				return false;
			} else if (!lessthan) {
				int diff = bound.charAt(i) - chr;
				if (diff > 0) {
					lessthan = true;
				} else if (diff < 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Check if a text string ends with a valid zone suffix. This accepts an
	 * empty suffix, the single letter 'Z', or an offset of +/-HH:MM.
	 * 
	 * @param text
	 * @param offset
	 * @return <code>true</code> if valid suffix, <code>false</code> if not
	 */
	public static boolean ifZoneSuffix(String text, int offset) {
		int length = text.length();
		if (length <= offset) {
			return true;
		} else {
			char chr = text.charAt(offset);
			if (length == offset + 1 && chr == 'Z') {
				return true;
			} else if (length == offset + 6 && (chr == '+' || chr == '-')
					&& text.charAt(offset + 3) == ':') {
				return ifFixedDigits(text, offset + 1, "24")
						&& ifFixedDigits(text, offset + 4, "59");
			} else {
				return false;
			}
		}
	}

	/**
	 * Check if a portion of a text string consists of decimal digits.
	 * 
	 * @param text
	 * @param offset
	 *            starting offset
	 * @param limit
	 *            ending offset plus one (<code>false</code> return if the text
	 *            length is less than this value)
	 * @return <code>true</code> if digits, <code>false</code> if not
	 */
	public static boolean ifDigits(String text, int offset, int limit) {
		if (text.length() < limit) {
			return false;
		} else {
			for (int i = offset; i < limit; i++) {
				char chr = text.charAt(i);
				if (chr < '0' || chr > '9') {
					return false;
				}
			}
			return true;
		}
	}

	private static int parseDigits(String text, int offset, int length)
			throws NumberFormatException {

		// check if overflow a potential problem
		int value = 0;
		if (length > 9) {

			// use library parse code for potential overflow
			value = Integer.parseInt(text.substring(offset, offset + length));

		} else {

			// parse with no overflow worries
			int limit = offset + length;
			while (offset < limit) {
				char chr = text.charAt(offset++);
				if (chr >= '0' && chr <= '9') {
					value = value * 10 + (chr - '0');
				} else {
					throw new NumberFormatException("Non-digit in number value");
				}
			}

		}
		return value;
	}

	/**
	 * Parse integer value from text. Integer values are parsed with optional
	 * leading sign flag, followed by any number of digits.
	 * 
	 * @param text
	 *            text to be parsed
	 * @return converted integer value
	 * @throws ParseException
	 *             on parse error
	 */
	public static int parseInt(String text) throws ParseException {

		// make sure there's text to be processed
		text = text.trim();
		int offset = 0;
		int limit = text.length();
		if (limit == 0) {
			throw new ParseException("Empty number value", 0);
		}

		// check leading sign present in text
		boolean negate = false;
		char chr = text.charAt(0);
		if (chr == '-') {
			if (limit > 9) {
				// special case to make sure maximum negative value handled
				return Integer.parseInt(text);
			} else {
				negate = true;
				offset++;
			}
		} else if (chr == '+') {
			offset++;
		}
		if (offset >= limit) {
			throw new ParseException("Invalid number format", offset);
		}

		// handle actual value conversion
		int value = parseDigits(text, offset, limit - offset);
		if (negate) {
			return -value;
		} else {
			return value;
		}
	}
	
    /**
     * Serialize time to general dateTime text. Date values are formatted in
     * W3C XML Schema standard format as CCYY-MM-DDThh:mm:ss, with optional
     * leading sign and trailing seconds decimal, as necessary.
     *
     * @param time time to be converted, as milliseconds from January 1, 1970
     * @param zone flag for trailing 'Z' to be appended to indicate UTC
     * @return converted dateTime text
     */
    public static String serializeDateTime(long time, boolean zone) {
        
        // start with the year, month, and day
        StringBuffer buff = new StringBuffer(25);
        int extra = formatYearMonthDay(time + TIME_BASE, buff);
         
        // append the time for full form
        buff.append('T');
        serializeTime(extra, buff);
       
        // return full text with optional trailing zone indicator
        if (zone) {
            buff.append('Z');
        }
        return buff.toString();
    } 

    /**
     * Serialize time to general time text in buffer. Time values are formatted
     * in W3C XML Schema standard format as hh:mm:ss, with optional trailing
     * seconds decimal, as necessary. This form uses a supplied buffer to
     * support flexible use, including with dateTime combination values.
     *
     * @param time time to be converted, as milliseconds in day
     * @param buff buffer for appending time text
     */
    public static void serializeTime(int time, StringBuffer buff) {
        
        // append the hour, minute, and second
        formatTwoDigits(time/MSPERHOUR, buff);
        time = time % MSPERHOUR;
        buff.append(':');
        formatTwoDigits(time/MSPERMINUTE, buff);
        time = time % MSPERMINUTE;
        buff.append(':');
        formatTwoDigits(time/1000, buff);
        time = time % 1000;
                
        // check if decimals needed on second
        if (time > 0) {
            buff.append('.');
            buff.append(time / 100);
            time = time % 100;
            if (time > 0) {
                buff.append(time / 10);
                time = time % 10;
                if (time > 0) {
                    buff.append(time);
                }
            }
        }
    }

    /**
     * Format time in milliseconds to year number, month number, and day
     * number. The resulting year number format is consistent with W3C XML
     * Schema definitions, using a minimum of four digits for the year and
     * exactly two digits each for the month and day.
     *
     * @param value time in milliseconds to be converted (from 1 C.E.)
     * @param buff text formatting buffer
     * @return number of milliseconds into day
     */
    protected static int formatYearMonthDay(long value, StringBuffer buff) {
         
        // convert year and month
        long extra = formatYearMonth(value, buff);
        
        // append the day of month
        int day = (int)(extra / MSPERDAY) + 1;
        buff.append('-');
        formatTwoDigits(day, buff);
        
        // return excess of milliseconds into day
        return (int)(extra % MSPERDAY);
    }

    /**
     * Format time in milliseconds to year number and month number. The
     * resulting year number format is consistent with W3C XML Schema
     * definitions, using a minimum of four digits for the year and exactly
     * two digits for the month.
     *
     * @param value time in milliseconds to be converted (from 1 C.E.)
     * @param buff text formatting buffer
     * @return number of milliseconds into month
     */
    protected static long formatYearMonth(long value, StringBuffer buff) {
     
        // find the actual year and month number; this uses a integer arithmetic
        //  conversion based on Baum, first making the millisecond count
        //  relative to March 1 of the year 0 C.E., then using simple arithmetic
        //  operations to compute century, year, and month; it's slightly
        //  different for pre-C.E. values because of Java's handling of divisions.
        long time = value + 306*LMSPERDAY + LMSPERDAY*3/4;
        long century = time / MSPERCENTURY;             // count of centuries
        long adjusted = time + (century - (century/4)) * MSPERDAY;
        int year = (int)(adjusted / MSPERAVGYEAR);      // year in March 1 terms
        if (adjusted < 0) {
            year--;
        }
        long yms = adjusted + LMSPERDAY/4 - (year * 365 + year/4) * LMSPERDAY;
        int yday = (int)(yms / LMSPERDAY);              // day number in year
        if (yday == 0) {                                // special for negative
            boolean bce = year < 0;
            if (bce) {
                year--;
            }
            int dcnt = year % 4 == 0 ? 366 : 365;
            if (!bce) {
                year--;
            }
            yms += dcnt * LMSPERDAY;
            yday += dcnt;
        }
        int month = (5*yday + 456) / 153;               // (biased) month number
        long rem = yms - BIAS_MONTHMS[month] - LMSPERDAY;   // ms into month
        if (month > 12) {                               // convert start of year
            year++;
            month -= 12;
        }

        // format year and month as text
        formatYearNumber(year, buff);
        buff.append('-');
        formatTwoDigits(month, buff);
       
        // return extra milliseconds into month
        return rem;
    }
    
    /**
     * Format year number consistent with W3C XML Schema definitions, using a  
     * minimum of four digits padded with zeros if necessary. A leading minus
     * sign is included for years prior to 1 C.E.
     *
     * @param year number to be formatted
     * @param buff text formatting buffer
     */
    protected static void formatYearNumber(long year, StringBuffer buff) {

        // start with minus sign for dates prior to 1 C.E.
        if (year <= 0) {
            buff.append('-');
            year = -(year-1);
        }

        // add padding if needed to bring to length of four
        if (year < 1000) {
            buff.append('0');
            if (year < 100) {
                buff.append('0');
                if (year < 10) {
                    buff.append('0');
                }
            }
        }
   
        // finish by converting the actual year number
        buff.append(year);
    }
    
    /**
     * Format a positive number as two digits. This uses an optional leading
     * zero digit for values less than ten.
     * 
     * @param value number to be formatted (<code>0</code> to <code>99</code>)
     * @param buff text formatting buffer
     */
    protected static void formatTwoDigits(int value, StringBuffer buff) {
        if (value < 10) {
            buff.append('0');
        }
        buff.append(value);
    }


	/** Day number for start of month in non-leap year. */
	private static final int[] MONTHS_NONLEAP = { 0, 31, 59, 90, 120, 151, 181,
			212, 243, 273, 304, 334, 365 };

	/** Day number for start of month in non-leap year. */
	private static final int[] MONTHS_LEAP = { 0, 31, 60, 91, 121, 152, 182,
			213, 244, 274, 305, 335, 366 };

	/** Number of milliseconds in a minute. */
	private static final int MSPERMINUTE = 60000;

	/** Number of milliseconds in an hour. */
	private static final int MSPERHOUR = MSPERMINUTE * 60;

	/** Number of milliseconds in a day. */
	private static final int MSPERDAY = MSPERHOUR * 24;

	/** Number of milliseconds in a day as a long. */
	private static final long LMSPERDAY = (long) MSPERDAY;

	/** Number of milliseconds in a (non-leap) year. */
	private static final long MSPERYEAR = LMSPERDAY * 365;

    /** Average number of milliseconds in a year within century. */
    private static final long MSPERAVGYEAR = (long)(MSPERDAY*365.25);
       
    /** Number of milliseconds in a normal century. */
    private static final long MSPERCENTURY = (long)(MSPERDAY*36524.25);

	/**
	 * Millisecond value of base time for internal representation. This gives
	 * the bias relative to January 1 of the year 1 C.E.
	 */
	private static final long TIME_BASE = 1969 * MSPERYEAR
			+ (1969 / 4 - 19 + 4) * LMSPERDAY;
	
    /** Millisecond count prior to start of month in March 1-biased year. */
    private static final long[] BIAS_MONTHMS =
    {
        0*LMSPERDAY, 0*LMSPERDAY, 0*LMSPERDAY, 0*LMSPERDAY,
        31*LMSPERDAY, 61*LMSPERDAY, 92*LMSPERDAY, 122*LMSPERDAY, 
        153*LMSPERDAY, 184*LMSPERDAY, 214*LMSPERDAY, 245*LMSPERDAY,
        275*LMSPERDAY, 306*LMSPERDAY, 337*LMSPERDAY
    };

	private static final long serialVersionUID = -2649918375915058425L;

}
