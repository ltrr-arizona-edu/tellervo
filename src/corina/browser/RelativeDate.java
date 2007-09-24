//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.browser;

import corina.ui.I18n;

import java.util.Calendar;
import java.util.Date;

import java.text.DateFormat;

// (why doesn't this just extend Date?)
public class RelativeDate implements Comparable {
    private Date date;

    // there are only 2 things i need translated, so let's just grab them now
    private static String TODAY = I18n.getText("todayAt") + " ";
    private static String YESTERDAY = I18n.getText("yesterdayAt") + " ";

    public RelativeDate(Date date) {
        this.date = date;
    }
    public int compareTo(Object o2) {
        return date.compareTo(((RelativeDate) o2).date);
    }
    // PERF: if this date is immutable, then the only reason toString()
    // won't be a constant is if the current date changes; but that will
    // happen only rarely, so memoization is still a big win, right?
    // ALSO: would representing this date as ms since epoch help wolog?
    private static int called=0; // THIS METHOD IS CALLED FAR TOO MANY TIMES!

    private String memo=null;
    public String toString() {
	called++;

	// naive memoization
	// BUG: if this.date changes (not done but possible), this will be wrong
	// BUG: if current day changes (rare), this will be wrong
	if (memo != null) {
	    return memo;
	}

        Calendar today = Calendar.getInstance();
        Calendar _date = Calendar.getInstance();
        _date.setTime(date);

	String formattedTime = timeFmt.format(date);

        // today?
        if (sameDay(today, _date)) {
	    memo = TODAY + formattedTime;
            return memo;
	}

        // yesterday?
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        if (sameDay(yesterday, _date)) {
	    memo = YESTERDAY + formattedTime;
            return memo;
	}

        // some random day
	memo = dateFmt.format(date) + ", " + formattedTime;
        return memo;
    }

    // time&date formatters, short forms
    private static DateFormat dateFmt = DateFormat.getDateInstance(DateFormat.SHORT);
    private static DateFormat timeFmt = DateFormat.getTimeInstance(DateFormat.SHORT);

    // true iff date1 and date2 happen to be during the same day
    private boolean sameDay(Calendar date1, Calendar date2) {
        return (date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR) &&
                date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR));
    }
}
