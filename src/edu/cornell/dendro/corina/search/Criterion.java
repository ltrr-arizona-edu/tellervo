package edu.cornell.dendro.corina.search;

import edu.cornell.dendro.corina.Element;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.Range;
import java.io.File;

import java.util.Date;
import java.util.Calendar;

/*
  NOTE: this class basically has 2 interfaces.

  first, there are almost-RDBMS-style class methods, like getComparisonsForType().
  when the user is playing around with the dialog, these are used.  they let you know
  what sort of searches are available, given certain parameters.

  second, there are object methods.  when you're done playing around and are ready
  to start a search, you make new Criterion object, which only knows how to map
  Elements to boolean values.

  try not to get confused as i am now.

  (nope, they're not well-separated yet.  they should be, and there should be a huge
  "*** end of class methods, start of object methods ***" line between them.)
*/

public abstract class Criterion {

    // criteria:
    // -- strings -- contains, starts with, ends with, is
    // -- numbers -- =, <, <=, >, >=, != (use real chars)
    // -- dates -- is today, is yesterday, is within, is before, is after, is exactly
    // -- sizes (kb) -- is less than, is greater than
    // -- one-of (indexed,raw) -- is, is not?

    /*
      need:
      -- list of criteria
      -- list of (type => criterion_1 criterion_2 ...) -- which criteria are available for which type
      (working here)
    */

    /*
      it would be easiest if i had just one Line class that presented the interface for all Criteria.
      how hard would that be?

      Line will need to know a bunch of stuff...
    */

    private static Criterion criteria[];
    private static void initCriteria() {
	criteria = new Criterion[] {
	    new StringContains(),
	    new StringStartsWith(),
	    new StringEndsWith(),
	    new StringIs(),

	    new YearIsBefore(),
	    new YearIsAfter(),
	    new YearIsExactly(),
	    // WRITEME: "is in the range" [yyyy] to [yyyy]

	    new LengthIsGreaterThan(),
	    new LengthIsLessThan(),

	    new NumberIsGreaterThan(),
	    new NumberIsLessThan(),
	    new NumberIsEqualTo(),
	    new NumberIsNotEqualTo(),

	    new DateIsToday(),
	    new DateIsWithin(),
	    new DateIsBefore(),
	    new DateIsAfter(),
	    new DateIsExactly(),

	    new OneOfIs(),
	    new OneOfIsNot(),
	    // new OneOfNotSet(), -- DISABLED: doesn't work yet
	};
    }

    // given a type (LHS), list all the criteria that apply to it
    public static String[] getComparisonsForType(int type) {
	if (criteria == null)
	    initCriteria();

	int n=0;
	for (int i=0; i<criteria.length; i++)
	    if (criteria[i].getLHSType() == type)
		n++;

	String result[] = new String[n];
	int j=0;
	for (int i=0; i<criteria.length; i++)
	    if (criteria[i].getLHSType() == type)
		result[j++] = criteria[i].getComparison();

	return result;
    }

    // given a type (LHS), list all the criteria that apply to it
    public static Criterion getCriterion(int type, String comparator) {
	if (criteria == null)
	    initCriteria();

	int n=0;
	for (int i=0; i<criteria.length; i++)
	    if (criteria[i].getLHSType()==type && criteria[i].getComparison().equals(comparator))
		return criteria[i];

	return null; // not found -- can't happen!
    }

    // given a type, comparator, and object, build a comparator for it
    public static Criterion makeCriterion(int type, String field, String comparator, Object rhs) {
	if (criteria == null)
	    initCriteria();

	int n=0;
	for (int i=0; i<criteria.length; i++)
	    if (criteria[i].getLHSType()==type && criteria[i].getComparison().equals(comparator)) {
		// make a new criterion, of proper type -- call constructor for given class?
		// Criterion c = (Criterion) criteria[i].clone(); -- but it's not cloneable
		try {
		    // basically, clone and set fields -- ick!
		    Criterion c = criteria[i].getClass().newInstance();
		    c.lhsField = field;
		    c.rhs = rhs;
		    return c;
		} catch (InstantiationException ie) { // can't happen
		    System.out.println("can't happen error #1");
		    return null;
		} catch (IllegalAccessException iae) { // can't happen
		    System.out.println("can't happen error #2");
		    return null;
		}
	    }

	return null; // not found -- can't happen!
    }
    protected Object rhs;
    protected String lhsField;

    // given a type (LHS), list all the criteria that apply to it
    public static int getRHSType(int lhsType, String comparator) {
	if (criteria == null)
	    initCriteria();

	int n=0;
	for (int i=0; i<criteria.length; i++)
	    if (criteria[i].getLHSType()==lhsType && criteria[i].getComparison().equals(comparator)) {
		return criteria[i].getRHSType();
	    }

	return SearchDialog.NONE; // not found -- can't happen!
    }

    // given a type (LHS), list all the criteria that apply to it
    public static Object[] getRHSValues(int lhsType, String comparator) {
	if (criteria == null)
	    initCriteria();

	// find the Criterion with matching lhsType and comparator, and return its rhsValues
	int n=0;
	for (int i=0; i<criteria.length; i++)
	    if (criteria[i].getLHSType()==lhsType && criteria[i].getComparison().equals(comparator))
		return criteria[i].getRHSValues();

	// not present?  must be a metadata field.

	return null; // not found -- can't happen!
    }

    // for the above, it'll need the LHS type -- NONE isn't allowed here (obviously)
    abstract int getLHSType();

    // it'll need to know a bunch of stuff about the Criteria:
    // -- what the possible values for the second field are
    abstract String getComparison(); // e.g., => "is"

    // !!!
    abstract String toSQL();

    // -- given a choice in the second field, what the third field is.
    // (it's usually the same as the LHS field, so we'll return that by default.)
    // must be at least protected: if it's private, it can't be overridden,
    // probably because the inheritance rules were written before inner classes,
    // so there's a bit of weirdness.  (it still lets you write it as "private",
    // with subclasses having same-named private methods, with no warnings, but
    // it never calls the subclass method.)
    protected int getRHSType() {
	return getLHSType();
    }

    // if it's POPUP, call this.
    // -- can't be private: see getRHSType(), above.
    protected String[] getRHSValues() {
	return null; // not always needed, so don't force subclasses to override it
	// BETTER: throw illegalargument, or method-not-defined(?), or some appropriate exception. (or Bug?)
    }

    // BUG: if you have a text field, and select a new comparator, it should NOT
    // clear the text field -- that is, if the RHS type doesn't change, don't change the component.

    // get the LHS, based on the field.
    private Object getLHS(Element e) {
	// REFACTOR: this seems awkward.  by LOD, this mess belongs in Element -- ok?
	Object lhs=null;
	if (lhsField.equals("start") || lhsField.equals("end") || lhsField.equals("length")) {
	    Range r = e.getRange();
	    if (r == null) // getRange() can return null now, if it's not a loadable sample (bad interface?)
		lhs = null;
	    else if (lhsField.equals("start"))
		lhs = r.getStart();
	    else if (lhsField.equals("end"))
		lhs = r.getEnd();
	    else if (lhsField.equals("length"))
		lhs = new Integer(r.span());
	} else if (lhsField.equals("filename"))
	    lhs = new File(e.filename).getName(); // operate on name only, not full path;
	else if (lhsField.equals("moddate")) // !!!
	    lhs = new Date(new File(e.filename).lastModified());
	else
	    lhs = e.getMeta(lhsField); // loads element, if needed

	return lhs;
    }

    // finally, it'll need to be able to say whether a given sample/file (which??) passes, given the third field
    public boolean test(Element e) {
	// get the LHS
	Object lhs = getLHS(e);

	// if anything is null, don't bother checking -- DESIGN: is this correct?
	if (lhs==null) //  || rhs==null)
	    return false;
	// rhs can be null (e.g., moddate is-today), and that's ok.  it's never
	// null when it shouldn't be.  so just check lhs.

	// run subclass test
	return test(lhs, rhs);
    }

    // subclasses of criterion have to implement this.
    abstract boolean test(Object lhs, Object rhs);

    //
    // criteria classes
    //

    // REFACTOR: make a static list of (lhsType, comparison, rhsType, Criterion), and make Criterion just the closure.

    static class StringContains extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.TEXT; }
	@Override
	String getComparison() {		return "contains"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    // String s1 = (String) lhs;
	    String s1 = lhs.toString(); // is this indicitave of a BUG?
	    String s2 = (String) rhs;
	    s1 = s1.toUpperCase();
	    s2 = s2.toUpperCase();

	    return (s1.indexOf(s2) != -1);

	    // (DESIGN: is what i really want "contains all these words"?)
	    // (DESIGN: should it be case-sensitive if there are any capital letters?)
	}
	@Override
	public String toSQL() {
   	    return "m." + lhsField + " like '~" + rhs + "~'";
	    // BUG: lhsField is on "m." sometimes, but sometimes on "r."
	    // (DESIGN: is this a schema problem?  should start/end/span be in meta, too?)
	}
	@Override
	public String toString() {
	    return lhsField + "=*" + rhs + "*";
	}
    }
    static class StringStartsWith extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.TEXT; }
	@Override
	String getComparison() {		return "starts with"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    String s1 = (String) lhs;
	    String s2 = (String) rhs;
	    s1 = s1.toUpperCase();
	    s2 = s2.toUpperCase();

	    return s1.startsWith(s2);
	}
	@Override
	public String toSQL() {
   	    return "m." + lhsField + " like '" + rhs + "~'";
	}
	@Override
	public String toString() {
	    return lhsField + "=" + rhs + "*";
	}
    }
    static class StringEndsWith extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.TEXT; }
	@Override
	String getComparison() {		return "ends with"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    String s1 = (String) lhs;
	    String s2 = (String) rhs;
	    s1 = s1.toUpperCase();
	    s2 = s2.toUpperCase();

	    return s1.endsWith(s2);
	}
	@Override
	public String toSQL() {
   	    return "m." + lhsField + " like '~" + rhs + "'";
	}
	@Override
	public String toString() {
	    return lhsField + "=*" + rhs;
	}
    }
    static class StringIs extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.TEXT; }
	@Override
	String getComparison() {		return "is"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    String s1 = (String) lhs;
	    String s2 = (String) rhs;
	    s1 = s1.toUpperCase();
	    s2 = s2.toUpperCase();

	    return s1.equals(s2);
	}
	@Override
	public String toSQL() {
   	    return "m." + lhsField + " = '" + rhs + "'";
	}
	@Override
	public String toString() {
	    return lhsField + "=" + rhs;
	}
    }

    // ----

    static class YearIsBefore extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.YEAR; }
	@Override
	String getComparison() {		return "is before"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    Year y1 = (Year) lhs;
	    Year y2 = (Year) rhs;

	    return (y1.compareTo(y2) < 0);
	}
	@Override
	public String toSQL() {
   	    return "r." + lhsField + " < " + rhs;
	}
	@Override
	public String toString() {
	    return lhsField + "<" + rhs;
	}
    }
    static class YearIsAfter extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.YEAR; }
	@Override
	String getComparison() {		return "is after"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    Year y1 = (Year) lhs;
	    Year y2 = (Year) rhs;

	    return (y1.compareTo(y2) > 0);
	}
	@Override
	public String toSQL() {
   	    return "r." + lhsField + " > " + rhs;
	}
	@Override
	public String toString() {
	    return lhsField + ">" + rhs;
	}
    }
    static class YearIsExactly extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.YEAR; }
	@Override
	String getComparison() {		return "is exactly"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    Year y1 = (Year) lhs;
	    Year y2 = (Year) rhs;

	    return y1.equals(y2);
	}
	@Override
	public String toSQL() {
   	    return "r." + lhsField + " = '" + rhs + "'";
	}
	@Override
	public String toString() {
	    return lhsField + "=" + rhs;
	}
    }

    // ----

    static class LengthIsGreaterThan extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.LENGTH; }
	@Override
	String getComparison() {		return "is greater than"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    Integer l1 = (Integer) lhs;
	    Integer l2 = (Integer) rhs;

	    return (l1.compareTo(l2) > 0);
	}
	@Override
	public String toSQL() {
   	    return "r." + lhsField + " > " + rhs;
	}
	@Override
	public String toString() {
	    return lhsField + ">" + rhs;
	}
    }
    static class LengthIsLessThan extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.LENGTH; }
	@Override
	String getComparison() {		return "is less than"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    Integer l1 = (Integer) lhs;
	    Integer l2 = (Integer) rhs;

	    return (l1.compareTo(l2) < 0);
	}
	@Override
	public String toSQL() {
   	    return "r." + lhsField + " < " + rhs;
	}
	@Override
	public String toString() {
	    return lhsField + "<" + rhs;
	}
    }

    // ----

    static class NumberIsGreaterThan extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.NUMBER; }
	@Override
	String getComparison() {		return "is greater than"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    Integer l1 = (Integer) lhs;
	    Integer l2 = (Integer) rhs;

	    return (l1.compareTo(l2) > 0);
	}
	@Override
	public String toSQL() {
   	    return "m." + lhsField + " > " + rhs; // m or r?
	}
	@Override
	public String toString() {
	    return lhsField + ">" + rhs;
	}
    }
    static class NumberIsLessThan extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.NUMBER; }
	@Override
	String getComparison() {		return "is less than"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    Integer l1 = (Integer) lhs;
	    Integer l2 = (Integer) rhs;

	    return (l1.compareTo(l2) < 0);
	}
	@Override
	public String toSQL() {
   	    return "m." + lhsField + " < " + rhs; // m or r?
	}
	@Override
	public String toString() {
	    return lhsField + "<" + rhs;
	}
    }
    static class NumberIsEqualTo extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.NUMBER; }
	@Override
	String getComparison() {		return "is equal to"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    Integer l1 = (Integer) lhs;
	    Integer l2 = (Integer) rhs;

	    return l1.equals(l2);
	}
	@Override
	public String toSQL() {
   	    return "m." + lhsField + " = '" + rhs + "'"; // m or r?
	}
	@Override
	public String toString() {
	    return lhsField + "=" + rhs;
	}
    }
    static class NumberIsNotEqualTo extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.NUMBER; }
	@Override
	String getComparison() {		return "is not equal to"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    Integer l1 = (Integer) lhs;
	    Integer l2 = (Integer) rhs;

	    return !l1.equals(l2);
	}
	@Override
	public String toSQL() {
   	    return "NOT m." + lhsField + " = '" + rhs + "'"; // m or r?
	}
	@Override
	public String toString() {
	    return lhsField + "\u2260" + rhs; // "NOT EQUAL TO"
	}
    }

    // ----

    private static void resetToMidnight(Calendar c) {
	c.set(Calendar.HOUR_OF_DAY, 0);
	c.set(Calendar.MINUTE, 0);
	c.set(Calendar.SECOND, 0);
	c.set(Calendar.MILLISECOND, 0);
    }

    static class DateIsToday extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.DATE; }
	@Override
	String getComparison() {		return "is today"; }
	@Override
	protected int getRHSType() {		return SearchDialog.NONE; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    Date d1 = (Date) lhs;
	    // rhs is null

	    // stuff lhs into a Calendar object -- java's date classes are weird
	    Calendar target = Calendar.getInstance();
	    target.setTime(d1);

	    // today
	    Calendar now = Calendar.getInstance();

	    resetToMidnight(target);
	    resetToMidnight(now);
	    return target.equals(now);
	}
	@Override
	public String toSQL() {
	    return "???";
	}
	@Override
	public String toString() {
	    return lhsField + " modified today";
	}
    }
    static class DateIsWithin extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.DATE; }
	@Override
	String getComparison() {		return "is within"; }
	@Override
	protected int getRHSType() {		return SearchDialog.POPUP; }
	private static String VALUES[] = new String[] {
	    "1 day", "2 days", "3 days",
	    "1 week", "2 weeks", "3 weeks",
	    "1 month", "2 month", "3 month", "6 months",
	};
	@Override
	public String[] getRHSValues() {
	    return VALUES.clone();
	}
	@Override
	public boolean test(Object lhs, Object rhs) {
	    Date d1 = (Date) lhs;

	    Calendar target = Calendar.getInstance();
	    target.setTime(d1);

	    // today
	    Calendar now = Calendar.getInstance();
	    int x=0; // index into VALUES
	    while (!VALUES[x].equals(rhs))
		x++;

	    // normalize -- correct?
	    resetToMidnight(now);
	    resetToMidnight(target);

	    // subtract rhs from today
	    switch (x) {
	    case 0: now.add(Calendar.DATE, -1); break;
	    case 1: now.add(Calendar.DATE, -2); break;
	    case 2: now.add(Calendar.DATE, -3); break;
	    case 3: now.add(Calendar.WEEK_OF_YEAR, -1); break;
	    case 4: now.add(Calendar.WEEK_OF_YEAR, -2); break;
	    case 5: now.add(Calendar.WEEK_OF_YEAR, -3); break;
	    case 6: now.add(Calendar.MONTH, -1); break;
	    case 7: now.add(Calendar.MONTH, -2); break;
	    case 8: now.add(Calendar.MONTH, -3); break;
	    case 9: now.add(Calendar.MONTH, -6); break;
	    }

	    // e.g., if it was changed 2 days ago,
	    //    (now-2).before(now-2) => false!
	    // so what i really need is less-than-or-equals,
	    // but that doesn't exist, so make one by hand.
	    return now.before(target) || now.equals(target);
	}
	@Override
	public String toSQL() {
	    return "???";
	}
	@Override
	public String toString() {
	    return lhsField + " modified within " + rhs;
	}
    }
    static class DateIsBefore extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.DATE; }
	@Override
	String getComparison() {		return "is before"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    Date d1 = (Date) lhs;
	    Date d2 = (Date) rhs;

	    // stuff lhs, rhs into Calendar objects -- java's date classes are weird
	    Calendar c1 = Calendar.getInstance();
	    c1.setTime(d1);
	    Calendar c2 = Calendar.getInstance();
	    c2.setTime(d2);

	    // normalize!  (only want to compare dates, not times)
	    resetToMidnight(c1);
	    resetToMidnight(c2);

	    // compare and return
	    return c1.before(c2);
	}
	@Override
	public String toSQL() {
	    return "???";
	}
	@Override
	public String toString() {
	    return lhsField + " modified before " + rhs;
	}
    }
    static class DateIsAfter extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.DATE; }
	@Override
	String getComparison() {		return "is after"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    Date d1 = (Date) lhs;
	    Date d2 = (Date) rhs;

	    // stuff lhs, rhs into Calendar objects -- java's date classes are weird
	    Calendar c1 = Calendar.getInstance();
	    c1.setTime(d1);
	    Calendar c2 = Calendar.getInstance();
	    c2.setTime(d2);

	    // normalize!  (only want to compare dates, not times)
	    resetToMidnight(c1);
	    resetToMidnight(c2);

	    // compare and return
	    return c1.after(c2);
	}
	@Override
	public String toSQL() {
	    return "???";
	}
	@Override
	public String toString() {
	    return lhsField + " modified after " + rhs;
	}
    }
    static class DateIsExactly extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.DATE; }
	@Override
	String getComparison() {		return "is exactly"; }
	@Override
	public boolean test(Object lhs, Object rhs) {
	    Date d1 = (Date) lhs;
	    Date d2 = (Date) rhs;

	    // stuff lhs, rhs into Calendar objects -- java's date classes are weird
	    Calendar c1 = Calendar.getInstance();
	    c1.setTime(d1);
	    Calendar c2 = Calendar.getInstance();
	    c2.setTime(d2);

	    resetToMidnight(c1);
	    resetToMidnight(c2);

	    return c1.equals(c2);
	}
	@Override
	public String toSQL() {
	    return "???";
	}
	@Override
	public String toString() {
	    return lhsField + " modified on " + rhs;
	}
    }

    // ----

    static class OneOfIs extends Criterion {
	@Override
	int getLHSType() {		return SearchDialog.POPUP; }
	@Override
	String getComparison() {		return "is"; }
	@Override
	public String[] getRHSValues() {
	    return null; // WRITEME
	}
	public boolean test(Object lhs, Object rhs) {
	    // they're both strings, so i'll just compare them
	    return (lhs.equals(rhs));
	}
	public String toSQL() {
   	    return "m." + lhsField + " = '" + rhs + "'";
	}
	public String toString() {
	    return lhsField + "=" + rhs; // FIXME: look up rhs in table?
	}
    }
    static class OneOfIsNot extends Criterion {
	int getLHSType() {		return SearchDialog.POPUP; }
	String getComparison() {		return "is not"; }
	public String[] getRHSValues() {
	    return null; // WRITEME
	}
	public boolean test(Object lhs, Object rhs) {
	    // they're both strings, so i'll just compare them
	    return (!lhs.equals(rhs));
	}
	public String toSQL() {
   	    return "NOT m." + lhsField + " = '" + rhs + "'";
	}
	public String toString() {
	    return lhsField + "\u2260" + rhs; // FIXME: look up rhs in table?
	}
    }
    static class OneOfNotSet extends Criterion {
	int getLHSType() {		return SearchDialog.POPUP; }
	String getComparison() {		return "not set"; }
	protected int getRHSType() {		return SearchDialog.NONE; }
	public String[] getRHSValues() {
	    return null; // WRITEME
	}
	public boolean test(Object lhs, Object rhs) {
	    // lhs is a string, so i'll check for ""
	    // BUT: can't it be null?  but isn't lhs==null caught earlier?  oops.
	    return lhs.equals("");
	}
	public String toSQL() {
   	    return "IS NULL m." + lhsField;
	}
	public String toString() {
	    return lhsField + " not set";
	}
    }

    /*
      this is all too klutzy.  simplify!

      need mappings:

      (1) metadata field => description (already exists)
      -- so i can display the LHS

      (2) metadata field => type (i have this already, right?  i should.  nope, not yet.)
      -- see next...

      (3) type => list of comparators (e.g., string => (contains, starts-with, ends-with, is))
      -- so when the user selects a LHS, i can show the proper comparators

      (4) comparator => rhs type (e.g., contains => string)
      -- so when the user selects a comparator, i can show the proper RHS
      -- (rhs type may be another popup!)
      -- so "rhs type" is really "rhs type, and if that's POPUP, then also the possible values"

      and when we're done, i need to be able to take metadata field (a
      popup), comparator (a popup), rhs field (whatever it is) and
      make or already have a Criterion, that i can use to test(Elements).

      (5) (type, comparator, rhs field value) => Criterion

      (6) and Criterion's purpose: (criterion, element) => bool.
    */
}
