package corina.browser;

import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import java.text.DateFormat;

// (why doesn't this just extend Date?)
public class RelativeDate implements Comparable {
    private Date date;
    private static ResourceBundle msg = ResourceBundle.getBundle("TextBundle");
    
    public RelativeDate(Date date) {
        this.date = date;
    }
    public int compareTo(Object o2) {
        return date.compareTo(((RelativeDate) o2).date);
    }
    public String toString() {
        Calendar today = Calendar.getInstance();
        Calendar _date = Calendar.getInstance();
        _date.setTime(date);

        // today?
        if (sameDay(today, _date))
            return msg.getString("todayAt") + " " + timeFmt.format(date);

        // yesterday?
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        if (sameDay(yesterday, _date))
            return msg.getString("yesterdayAt") + " " + timeFmt.format(date);

        // some random day
        return dateFmt.format(date) + ", " + timeFmt.format(date);
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
