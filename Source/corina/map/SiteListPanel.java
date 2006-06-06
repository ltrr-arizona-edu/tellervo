package corina.map;
// MOVEME: this should be in corina.site -- but there's already a SiteList there (eep, what's it do?)

import corina.site.Site;
import corina.site.SiteDB;
import corina.site.Country;
import corina.site.SiteInfoDialog;
import corina.util.PopupListener;
import corina.ui.Builder;
import corina.gui.Layout;
import corina.browser.Browser; // (for ODD_ROW_COLOR)
import corina.util.Sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import java.awt.Color;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.*; // !
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
   A panel which shows a table of sites.

   <p>By right-clicking on the table header, the user can decide what columns to display.
   Available columns are: Show?, Name, Code, ID, Location, Altitude, Country, Species, Type,
   Comments, and Distance.  "Show?" is a checkbox which toggles whether this site is visible
   on the map.  "Distance" is the distance from a target site; the user can pick any site to
   mark as the target.  ("Show?" and "Name" must always be visible.)</p>

   <p>If you have just 2 sites, and 3 of the nonessential columns showing, the table might
   look like this:</p>

 <table align="center" cellspacing="0" border="1">
   <form> <!-- for checkboxes -->
   <tr>
     <th>Show?</th>
     <th>Name</th>
     <th>Species</th>
     <th>Country</th>
     <th>Distance</th>
   </tr>
   <tr>
     <td align="center"><input type="checkbox" checked></td>
     <td>Zonguldak, Karab&uuml;k</td>
     <td>Quercus sp.</td>
     <td>Turkey</td>
     <td>Target</td>
   </tr>
   <tr>
     <td align="center"><input type="checkbox" checked></td>
     <td>Zonguldak, Yenice</td>
     <td>Quercus sp.</td>
     <td>Turkey</td>
     <td>10 km</td>
   </tr>
   </form>
 </table>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/

/*
  11-may-2003:

  this is a good start.  it just needs some refactoring.  ok, a lot.

  TODO:
  -- right-clicking header allows toggling columns
  -- left-clicking header changes sort
  -- use case-insensitive natural-order sort: use my nifty new
     corina.util.StringComparator
  -- sort is indicated by little arrow in header

  -- make "+" button add a new site
  -- make "-" button remove a site
  -- after changes made, save to disk
  -- in bottom left, add label: "%d site[s], %d shown on map[, %d selected]"

 -- javadoc: how table data gets saved in the prefs, etc.

 -- clean up .* import
 -- optimize!
  -- type-to-select, from start of "name" field
  -- dim "show?" checkbox for rows with no location
  -- i18n the titles ("altitude" -> "Altitude"); change "show?" title
     to "map"
  -- right-click "show on map": adjust map so all selected sites are
     visible ("find on map"?)
  -- "mark as target" dimmed if no location for this site
  -- double-right-click shouldn't show info
  -- store visibility, location, size of columns in prefs
  -- make zeroth column "number" -- no title, not movable, not sizeable
  -- add undo (for add, delete, edit)
  -- in distance column, show target as target icon?
  -- use even/odd white/blue coloring for checkboxes-column, too
  -- add itunes-like search field?  yes please!
  -- manual: making a new site
  -- manual: deleting a site
  -- manual: printing the site list
  -- manual: exporting the site list
  -- manual: finding sites near a site (the target)
  -- manual: changing which columns are visible
  -- manual: sorting the list of sites
  -- manual: the sites tab
  -- make menubar; put all things like "mark as target" under menus
     (site -> mark as target) for completeness
  -- add modify-multiple-sites-at-once, like itunes?
  -- profile; it's a bit sluggish
  -- if multiple rows selected, make "get info" cycle through them,
     with "previous"/"next" buttons on the left, like itunes?  or add
     checkboxes?
*/

public class SiteListPanel extends JPanel {

    private List allSites; // a list of all the sites -- my own copy, which i can sort as i like

    private LabelSet labels; // a LabelSet

    // "distance" = distance from this site;
    // the user can pick any site for this.
    private Site target = (Site) SiteDB.getSiteDB().sites.get(0); // pick one?
    private void setTarget(Site s) {
	target = s;
	model.fireTableDataChanged();
    }

    private AbstractTableModel model;

    // which fields are displayed, e.g., ("show?" "id" "code")
    private List columns = new ArrayList();
    {
	columns.add("show?");
	columns.add("name");
	columns.add("code");
	columns.add("id");
	columns.add("location");
	columns.add("altitude");
	columns.add("country");
	columns.add("species");
	columns.add("type");
	columns.add("comments");
	columns.add("distance");
    }

    // model for table to display all the sites
    private class SiteTableModel extends AbstractTableModel {
	// columns: id, code, name, country, type, species,
	//          altitude, latitude, longitude, comments,
	//          distance(!)
	public int getRowCount() {
	    return allSites.size();
	}
	public int getColumnCount() {
	    return columns.size();
	}
	public Object getValueAt(int row, int column) {
            long t1 = System.currentTimeMillis();
            try {
            
	    Site s = (Site) allSites.get(row);

            // PERF: why can't i store the field/column(?) by an int, instead of a string?
	    String field = (String) columns.get(column);

            // (i'd like to return null here if s.location=null, but
            // it treats that as FALSE.)
            if (field.equals("show?"))
                return (labels.isVisible(s) ? Boolean.TRUE : Boolean.FALSE);

	    if (field.equals("id"))       return s.getID();
	    if (field.equals("code"))     return s.getCode();
	    if (field.equals("name"))     return s.getName();
	    if (field.equals("type")) 	  return s.getTypesAsString(); // PERF
	    if (field.equals("species"))  return s.getSpecies();
	    if (field.equals("altitude")) return s.altitude; // !!!
	    if (field.equals("location")) return s.getLocation();
	    if (field.equals("comments")) return s.getComments();
	    if (field.equals("country")) {
	    	try {
	    		String cname = Country.getName(s.getCountry());
	    		return cname;
	    	} catch (IllegalArgumentException iee) {
	    		return "<unknown country code " + s.getCountry() + ">";
	    	}
	    }
	    
	    // --FINISH ME--
	    if (field.equals("distance")) {
		if (s == target)
		    return "Target"; // need I18n

		if (s.getLocation()==null || target.getLocation()==null)
                    return null; // was: "--"

		return s.distanceTo(target) + " km"; // PERF
	    }

	    // can't happen
	    return null;
            } finally {
                long t2 = System.currentTimeMillis();
                total += t2 - t1;
                number++;
                if (number == 500) {
                    System.out.println("average time spent in getValueAt()=" + (total/number) + " ms");
                    total = number = 0;
                }
            }
	}

        private long total = 0, number = 0;
        
	public void setValueAt(Object object, int row, int column) {
	    Site site = (Site) allSites.get(row);

	    // column must be 0 here (=visible)
	    boolean show = ((Boolean) object).booleanValue();
            labels.setVisible(site, show);

            // update the label
            updateLabel();

            // tell the map panel to update itself -- but just the sites layer.
            // (PERF: i should just update that part of the layer.)
            // BETTER: just ask it to invalidate this layer, so it'll get drawn
            // lazily.  could that be made to work?
            panel.updateBufferLabelsOnly();
	}

	public Class getColumnClass(int column) {
	    String field = (String) columns.get(column);
	    if (field.equals("show?"))
		return Boolean.class;
	    else
		return Object.class;

	    /*
	    switch (column) {
	    case 0: return Boolean.class;
	    case 1: return Object.class; // make this default?
	    case 2: return String.class; // ?
	    default: throw new IllegalArgumentException();
	    }
	    */
	}
	public String getColumnName(int column) {
	    String field = (String) columns.get(column);

	    return field;
	    // return FIELD_NAMES[i];

	    /*
	    switch (column) {
	    case 0: return "Show?";
	    case 1: return "Code";
	    case 2: return "Distance";
	    default: throw new IllegalArgumentException();
	    }
	    */
	}

	// FIXME: should i make the "show?" column not-editable if there's no location for it?
	public boolean isCellEditable(int row, int column) {
	    String field = (String) columns.get(column);
            Site site = (Site) allSites.get(row);
	    return field.equals("show?") && (site.getLocation() != null);
	}
    }

    // a checkbox menuitem which associates a field name ("id"), ???

    // HACK: PUBLIC for MapFrame (menuitems)
    public static final String FIELD_NAMES[] = new String[] {
	"Show?", // TODO: i18n?
	"Name",
	"Code",
	"ID",
	"Location",
	"Altitude",
	"Country",
	"Species",
	"Type",
	"Comments",
	"Distance",
    };

    // FIXME: separate latitude/longitude columns?  Locations aren't comparable!

    /*
      how do i say "give me all forests within 300km of ZKB"?
      it's not that hard, i think, but it's maybe not obvious.
      i need to document that.

      (how?)

      if "distance" isn't visible, right-click the header, and choose
      "distance" to show it.  right-click on ZKB, choose "mark as
      target".  then click on the "distance" header to sort by
      distance.
    */

    // WRITEME: everything

    private MapPanel panel;

    private JTable table;

    /**
       Make a new site list panel.

       <p>When sites are toggled on this panel, it will call show(Site) or hide(Site)
       on the MapPanel, and add or remove elements from the list provided.</p>

       @param panel the MapPanel to show sites on
       @param labels the LabelSet to mediate visibility, selection, etc.
    */
    public SiteListPanel(MapPanel panel, LabelSet labels) {

	// data
	this.panel = panel;
        this.labels = labels;
	this.allSites = SiteDB.getSiteDB().sites;

        init();
    }

    private void init() {
	// table
	table = new JTable();
	model = new SiteTableModel();
	table.setModel(model);

	// even/odd white/blue, with vertical gray lines
	table.setDefaultRenderer(Object.class, new EvenOddRenderer());
	table.setShowVerticalLines(true);
	table.setShowHorizontalLines(false);
	table.setGridColor(Color.lightGray);

	JScrollPane scroll = new JScrollPane(table);
	scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // click-to-sort
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // which column was clicked?
                int column = table.getTableHeader().columnAtPoint(e.getPoint());

                // sort by that column
                String field = (String) columns.get(column);
                if (field.equals("location")) {
                    // ignore
                } else if (field.equals("distance")) {
                    if (sortField.equals("distance")) {
                        Collections.reverse(allSites);
                        sortBackwards = !sortBackwards;
                        return;
                    }
                    sortField = field;
                    sortBackwards = false;

                    Collections.sort(allSites, new Comparator() {
                        public int compare(Object o1, Object o2) {

                            // EXTRACT: put all "targeT" stuff in one place -- its own class

                            Site s1 = (Site) o1;
                            Site s2 = (Site) o2;

                            // target goes at the beginning
                            if (s1 == target)
                                return -1;
                            if (s2 == target)
                                return +1;

                            // nulls go at the end
                            if (s1.getLocation() == null && s2.getLocation() == null)
                                return 0;
                            if (s1.getLocation() == null)
                                return +1;
                            if (s2.getLocation() == null)
                                return -1;

                            // everything else, let distanceTo() decide it
                            int d1 = s1.distanceTo(target);
                            int d2 = s2.distanceTo(target);
                            // PERF: inefficient!
                            return new Integer(d1).compareTo(new Integer(d2));
                        }
                    });
                    // WRITEME: other special cases?
                } else if (field.equals("country")) {
                    // default
                    if (sortField.equals(field)) {
                        Collections.reverse(allSites);
                        sortBackwards = !sortBackwards;
                    } else {
                        sortField = field;
                        sortBackwards = false;
                        
                        Collections.sort(allSites, new Comparator() {
                            public int compare(Object o1, Object o2) {
                                Site s1 = (Site) o1;
                                Site s2 = (Site) o2;
                                
                                // nulls at end
                                if (s1.getCountry() == null && s2.getCountry() == null)
                                    return 0;
                                if (s1.getCountry() == null)
                                    return +1;
                                if (s2.getCountry() == null)
                                    return -1;
                                
                                // everything else, use the name (not the code)
                                try {
                                	String n1 = Country.getName(s1.getCountry());
                                	String n2 = Country.getName(s2.getCountry());
                                	return n1.compareTo(n2);
                                } catch (IllegalArgumentException iee) {
                                	// Invalid country code somewhere. Compare just the codes, then?
                                	return s1.getCountry().compareTo(s2.getCountry());
                                }
                            }
                        });
                    }
                } else {
                    // default
                    if (sortField.equals(field)) {
                        Collections.reverse(allSites);
                        sortBackwards = !sortBackwards;
                    } else {
                        sortField = field;
                        sortBackwards = false;
                        Sort.sort(allSites, field);
                    }
                }

                // TODO: preserve selection for sort
                // FIXME: use better sort (case-insens, natural order, accent-removal, etc.)
                // FIXME: better sorts: species, nulls should go at end; show, visible first (throws ex now), etc.
                // FIXME: sort by output, not code (i.e., Turkey by "Turkey", not "TU")
                // TODO: store the sort in the prefs (corina.sites.sort.field = distance, corina.sites.sort.reverse = true)
                // TODO: draw arrow on table with current sort
                // (EXTRACT: click-to-sort, arrow-on-sort, save-to-prefs, preserve selection.)
                // TODO: if some data gets changed, or a site gets moved on the map, or the target site changes,
                // the site should automatically re-sort itself.

                // refresh display
                // BUG: this doesn't always work!
                table.repaint();
            }
        });

        // table popup
	JPopupMenu sitePopup = new SitePopup();
        table.addMouseListener(new PopupListener(sitePopup));
	table.addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
		    if (e.getClickCount() == 2) {
			int i = table.getSelectedRow();
			Site site = (Site) allSites.get(i);
			new SiteInfoDialog(site, (JFrame) table.getTopLevelAncestor());
		    }
		}
	    });
	// BUT: on double-click, do something else.
	// make a PopupDoubleClickListener(popup, doubleClickRunnable)?
	// can i make a DoubleClickListener(doubleClickRunnable), and add
	// them separately?

	// label
        label = new JLabel();
        updateLabel();
	table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
                    updateLabel();

                    // update |labels| from table selection now
                    int start = e.getFirstIndex();
                    int last = e.getLastIndex();
                    for (int i=start; i<=last; i++) {
                        labels.setSelected((Site) allSites.get(i),
                                           table.getSelectionModel().isSelectedIndex(i));
                    }

                    // redraw map with new selection
                    panel.updateBufferLabelsOnly();
		}
	    });

	// buttons
	JButton minus = new JButton(Builder.getIcon("minus.png"));
	JButton plus = new JButton(Builder.getIcon("plus.png"));
	JPanel buttons = Layout.buttonLayout(minus, plus);

	// bottom = label + buttons
	JPanel bottom = Layout.borderLayout(null,
					    label, null, buttons,
					    null);

	// NEW: panel
	setLayout(new BorderLayout()); // default is flow, for some reason
	add(Layout.borderLayout(null,
				null, scroll, null,
				bottom));
    }

    private String sortField = "name";
    private boolean sortBackwards = false;

    // update the label at the bottom; to be called wehnever the number of sites,
    // number of visible sites, or number of selected sites changes.
    private void updateLabel() {
        StringBuffer text = new StringBuffer();

        if (allSites.size() == 1)
            text.append("1 site");
        else
            text.append(allSites.size() + " sites");

        // TODO: need to update this when visibility changes elsewhere (like from the map?)
        if (labels.countVisibleSites() == 0)
            text.append(", none shown on map");
        else
            text.append(", " + labels.countVisibleSites() + " shown on map");

        int n = table.getSelectedRows().length;
        if (n != 0)
            text.append(", " + n + " selected");

        label.setText(text.toString());
    }

    private JLabel label;

    // for right-clicking on a site:
    // -- Show on Map
    // -- Get Info (cmd-I)
    // -- Mark as Target (cmd-T)
    // WAS: show all, hide all.  do i still want those?
    private class SitePopup extends JPopupMenu {
	SitePopup() {
	    // iick...
            final JMenuItem info = Builder.makeMenuItem("get_info..."); // new JMenuItem("Get Info"); // TODO: cmd-I
	    info.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			// same as double-click: REFACTOR
			int i = table.getSelectedRow();
			Site site = (Site) allSites.get(i);
			new SiteInfoDialog(site, (JFrame) table.getTopLevelAncestor());
		    }
		});
	    add(info);

	    JMenuItem show = new JMenuItem("Show on Map");
	    // WRITEME
	    add(show);

            JMenuItem mark = Builder.makeMenuItem("mark_as_target");
	    mark.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
                        markSelectionAsTarget();
		    }
		});
	    add(mark);
	}
    }

    // whatever the selected site is, make that the new target.
    // ASSUMES: only one site is selected!
    public void markSelectionAsTarget() {
        // get selected site -- EXTRACT METHOD
        int i = table.getSelectedRow();
        Site site = (Site) allSites.get(i);

        // set target
        setTarget(site);
    }
    
    // even/odd row coloring.
    // BUG: doesn't color checkbox cells!
    // QUESTION: what's a jcheckbox with background=blue look like?
    // (i.e., do i need to make col 0 non-opaque, and paint blue first?)
    private static class EvenOddRenderer extends DefaultTableCellRenderer {
	public Component getTableCellRendererComponent(JTable table,
						       Object value,
						       boolean isSelected,
						       boolean hasFocus,
						       int row, int column) {
	    Component c = super.getTableCellRendererComponent(table,
							      value,
							      isSelected,
							      hasFocus,
							      row, column);

	    JComponent j = (JComponent) c;
	    j.setOpaque(true);

            Color fore = (isSelected ? table.getSelectionForeground()
			             : table.getForeground());

            Color back = (isSelected ? table.getSelectionBackground()
			             : table.getBackground());

	    if ((row % 2) == 0 && !isSelected)
		back = Browser.ODD_ROW_COLOR;

	    c.setForeground(fore);
	    c.setBackground(back);

	    return c;
	}
    }

    // TODO: write Bootstrap class which takes care of stuff like this,
    // so Startup (RENAME to Main) merely calls Bootstrap, then new
    // XCorina().  that'll make startup cleaner, and make it much
    // easier to write little spikes.
}
