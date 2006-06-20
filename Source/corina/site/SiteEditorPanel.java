package corina.site;

import corina.map.MapFrame;
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
import java.awt.Event;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.*; // !
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

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

public class SiteEditorPanel extends JPanel {

	private List allSites; // a list of all the sites -- my own copy, which i can sort as i like
	private Boolean dataModified = false; // was any of this data modified?

	// "distance" = distance from this site;
	// the user can pick any site for this.
	private Site target;
	
	private SiteEditor parent;
	
	private void setTarget(Site s) {
		target = s;
		model.fireTableDataChanged();
	}

	private AbstractTableModel model;

	// which fields are displayed, e.g., ("show?" "id" "code")
	private List columns = new ArrayList();
	{
		columns.add("name");
		columns.add("code");
		columns.add("id");
		columns.add("latitude");
		columns.add("longitude");
		columns.add("altitude");
		columns.add("country");
		columns.add("species");
		columns.add("type");
		columns.add("folder");
		columns.add("comments");
		columns.add("distance");
	}

	private final class SiteEditorSorter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			// which column was clicked?
			int column = table.getTableHeader().columnAtPoint(e.getPoint());
		
			// sort by that column
			String field = (String) columns.get(column);
			if (field.equals("distance")) {
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
						if (s1.getLocation() == null
								&& s2.getLocation() == null)
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
			} else if (field.equals("latitude")) {
				if (sortField.equals("latitude")) {
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
		
						// nulls go at the end
						if (s1.getLocation() == null
								&& s2.getLocation() == null)
							return 0;
						if (s1.getLocation() == null)
							return +1;
						if (s2.getLocation() == null)
							return -1;
		
						// everything else, use the name (not the code)
						String n1 = s1.getLocation().getLatitudeAsString();
						String n2 = s2.getLocation().getLatitudeAsString();
						return n1.compareTo(n2);
					}
				});
			} else if (field.equals("longitude")) {
				if (sortField.equals("longitude")) {
					Collections.reverse(allSites);
					sortBackwards = !sortBackwards;
					return;
				}
				sortField = field;
				sortBackwards = false;
		
				Collections.sort(allSites, new Comparator() {
					public int compare(Object o1, Object o2) {
		
						Site s1 = (Site) o1;
						Site s2 = (Site) o2;
		
						// nulls go at the end
						if (s1.getLocation() == null
								&& s2.getLocation() == null)
							return 0;
						if (s1.getLocation() == null)
							return +1;
						if (s2.getLocation() == null)
							return -1;
		
						// everything else, use the name (not the code)
						String n1 = s1.getLocation().getLongitudeAsString();
						String n2 = s2.getLocation().getLongitudeAsString();
						return n1.compareTo(n2);
					}
				});
			} else if (field.equals("country")) {
				// default
				if (sortField.equals(field)) {
					Collections.reverse(allSites);
					sortBackwards = !sortBackwards;
					return;
				} else {
					sortField = field;
					sortBackwards = false;
		
					Collections.sort(allSites, new Comparator() {
						public int compare(Object o1, Object o2) {
							Site s1 = (Site) o1;
							Site s2 = (Site) o2;
		
							// nulls at end
							if (s1.getCountry() == null
									&& s2.getCountry() == null)
								return 0;
							if (s1.getCountry() == null)
								return +1;
							if (s2.getCountry() == null)
								return -1;
		
							// everything else, use the name (not the code)
							try {
								String n1 = Country
										.getName(s1.getCountry());
								String n2 = Country
										.getName(s2.getCountry());
								return n1.compareTo(n2);
							} catch (IllegalArgumentException iee) {
								// Invalid country code somewhere. Compare just the codes, then?
								return s1.getCountry().compareTo(
										s2.getCountry());
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
			model.fireTableDataChanged();
		}
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

				if (field.equals("id"))
					return s.getId();
				if (field.equals("code"))
					return s.getCode();
				if (field.equals("name"))
					return s.getName();
				if (field.equals("folder"))
					return s.getFolder();
				if (field.equals("type"))
					return s.getTypesAsString(); // PERF
				if (field.equals("species"))
					return s.getSpecies();
				if (field.equals("altitude"))
					return (s.getAltitude() != null) ? s.getAltitude().toString() : null;
				
				Location loc;
				if (field.equals("latitude"))
					return ((loc = s.getLocation()) != null) ? loc.getLatitudeAsString() : null;
				if (field.equals("longitude"))
					return ((loc = s.getLocation()) != null) ? loc.getLongitudeAsString() : null;
					
				if (field.equals("comments"))
					return s.getComments();
				if (field.equals("country")) 
					return s.getCountry();

				if (field.equals("distance")) {
					if (s == target)
						return "Target"; // need I18n

					if (s.getLocation() == null || target.getLocation() == null)
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
					System.out.println("average time spent in getValueAt()="
							+ (total / number) + " ms");
					total = number = 0;
				}
			}
		}

		private long total = 0, number = 0;

		public void setValueAt(Object object, int row, int column) {
			Site s = (Site) allSites.get(row);
			String field = (String) columns.get(column);
			Object oldval = getValueAt(row, column);
						
			// no changes? so what.
			if((oldval != null && oldval.equals(object)) ||
			   (oldval == null && object != null && object.equals("")))
				return;
			
			if (field.equals("id"))
				s.setId((String)object);
			else if (field.equals("code"))
				s.setCode((String)object);
			else if (field.equals("name"))
				s.setName((String)object);
			else if (field.equals("folder"))
				s.setFolder((String)object);
			else if (field.equals("species"))
				s.setSpecies((String)object);
			else if (field.equals("comments"))
				s.setComments((String)object);
			else if (field.equals("altitude")) {
				try {
					Integer val = Integer.parseInt(((String) object));
					s.setAltitude(val);
				} catch (NumberFormatException nfe) {
					// ignore this if it's an invalid altitude
					return;
				}
			}
			else if(field.equals("country"))
				s.setCountry((String) object);
			
			setDataModified(true);
			
			// update the label
			updateLabel();
		}
		
		public Class getColumnClass(int column) {
			String field = (String) columns.get(column);
			
			if(field.equals("latitude") || field.equals("longitude"))
				return Location.class;
			
			if(field.equals("country"))
				return Country.class;

			return String.class;
		}

		public String getColumnName(int column) {
			String field = (String) columns.get(column);

			return field;
		}

		public boolean isCellEditable(int row, int column) {
			String field = (String) columns.get(column);
			
			if(field.equals("distance") || field.equals("type"))
				return false;
			
			return true;
		}
	}

	private JTable table;
	private JButton plus, minus, editit;

	public SiteEditorPanel(SiteEditor parent) {
		this.parent = parent;
		
		// get a COPY of the site list.
		allSites = parent.getImmutableSitelist();
		
		init();
		
		// and we start with our first target.
		target = (Site) allSites.get(0);		
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
		table.setGridColor(Color.lightGray.brighter());
		table.setRowSelectionAllowed(true);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// click-to-sort
		table.getTableHeader().addMouseListener(new SiteEditorSorter());

		// table popup
		JPopupMenu sitePopup = new SitePopup();
		table.addMouseListener(new PopupListener(sitePopup));
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int i = table.getSelectedRow();
					Site site = (Site) allSites.get(i);
					SiteInfoDialog sid = new SiteInfoDialog(site, (JFrame) table.getTopLevelAncestor());
					
					if(sid.shouldSave())
						setDataModified(true);
				}
			}
		});
		// BUT: on double-click, do something else.
		// make a PopupDoubleClickListener(popup, doubleClickRunnable)?
		// can i make a DoubleClickListener(doubleClickRunnable), and add
		// them separately?

		// buttons
		minus = new JButton(Builder.getIcon("minus.png"));
		plus = new JButton(Builder.getIcon("plus.png"));
		editit = new JButton("Edit...");
		
		editit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// same as double-click: REFACTOR
				int i = table.getSelectedRow();
				Site site = (Site) allSites.get(i);
				SiteInfoDialog sid = new SiteInfoDialog(site, (JFrame) table.getTopLevelAncestor());
				
				if(sid.shouldSave())
					setDataModified(true);
			}			
		});
		
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				
				if(i < 0)
					return;
					
				Site site = (Site) allSites.get(i);
				
				int ret = JOptionPane.showConfirmDialog(table.getTopLevelAncestor(), 
						"Are you sure you wish to delete information for the site:\n" +
						site.getName() + "?\n" + 
						"This operation is not undoable.", 
						"Remove site?",
						JOptionPane.YES_NO_OPTION);
				
				// delete it, and don't forget to save.
				if(ret == JOptionPane.YES_OPTION) {
					allSites.remove(site);
					setDataModified(true);
				}
			}
		});
		
		plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Site site = new Site();
				SiteInfoDialog sid = new SiteInfoDialog(site, (JFrame) table.getTopLevelAncestor());
				
				if(sid.shouldSave()) {
					allSites.add(site);
					Sort.sort(allSites, sortField);
					if(sortBackwards)
						Collections.reverse(allSites);
					
					setDataModified(true);
				}
			}
		});

		// label
		label = new JLabel();
		label.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 2));
		updateLabel();
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						updateLabel();
					}
				});
		
		// more table init
		Sort.sort(allSites, sortField);
		table.getColumnModel().getColumn(0).setPreferredWidth(300);
		
		table.setDefaultRenderer(Country.class, new CountryRenderer());
		
		table.setDefaultEditor(Location.class, new LocationEditor());
		table.setDefaultEditor(Country.class, new CountryEditor());
		
		JPanel buttons = Layout.buttonLayout(editit, minus, plus);
		buttons.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));

		// bottom = label + buttons
		JPanel bottom = Layout.borderLayout(null, label, null, buttons, null);

		// NEW: panel
		setLayout(new BorderLayout()); // default is flow, for some reason
		add(Layout.borderLayout(null, null, scroll, null, bottom));
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
			text.append(allSites.size() + " sites.");
		
		int i = table.getSelectedRow();
		if(i < 0) {
			minus.setEnabled(false);
			editit.setEnabled(false);
		}
		else {
			minus.setEnabled(true);
			editit.setEnabled(true);
			
			Site site = (Site) allSites.get(i);
			text.append(" " + site.getName() + " selected.");
		}
		
		label.setText(text.toString());
	}

	private JLabel label;

	// for right-clicking on a site:
	// -- Show on Map
	// -- Get Info (cmd-I)
	// -- Mark as Target (cmd-T)
	private class SitePopup extends JPopupMenu {
		SitePopup() {
			// iick...
			final JMenuItem info = new JMenuItem("Edit..."); 
			info.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
			info.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					// same as double-click: REFACTOR
					int i = table.getSelectedRow();
					Site site = (Site) allSites.get(i);
					SiteInfoDialog sid = new SiteInfoDialog(site, (JFrame) table.getTopLevelAncestor());
					
					if(sid.shouldSave())
						setDataModified(true);
				}
			});
			add(info);
			addSeparator();

			JMenuItem show = new JMenuItem("Show on Map");
			show.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					int i = table.getSelectedRow();
					Site site = (Site) allSites.get(i);
					// ahhh! horrible kludge, but it works ;)
					new MapFrame(site, site);
				}
			});
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
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component c = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);

			JComponent j = (JComponent) c;
			j.setOpaque(true);

			Color fore = (isSelected ? table.getSelectionForeground() : table
					.getForeground());

			Color back = (isSelected ? table.getSelectionBackground() : table
					.getBackground());

			if ((row % 2) == 0 && !isSelected)
				back = Browser.ODD_ROW_COLOR;

			c.setForeground(fore);
			c.setBackground(back);

			return c;
		}
	}
	
	class LocationEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	    private JButton button;
	    private Site site;
	    protected static final String EDIT = "edit";

	    public LocationEditor() {
	        button = new JButton();
	        button.setActionCommand(EDIT);
	        button.addActionListener(this);
	        button.setBorderPainted(false);
	    }
	    
		public Object getCellEditorValue() {
			return site.getLocation();
		}
		
		public void actionPerformed(ActionEvent e) {
			if (EDIT.equals(e.getActionCommand())) {
				new LocationEditorDialog(site);
				
				fireEditingStopped();
			}
		}		

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {

			site = (Site) allSites.get(row);
			return button;
		}
	}	
	
	private class CountryRenderer extends DefaultTableCellRenderer {
	    public CountryRenderer() { super(); }

	    public void setValue(Object value) {
	    	if(value instanceof String) {
	    		String country = (String) value;
				try {
					setText(Country.getName(country));
				} catch (IllegalArgumentException iee) {
					setText(Country.badCountry(country));
				}	    		
	    	}
	    }
	}
	
	private class CountryEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	    private JButton button;
	    private String country;
	    protected static final String EDIT = "edit";

	    public CountryEditor() {
	        button = new JButton();
	        button.setActionCommand(EDIT);
	        button.addActionListener(this);
	        button.setBorderPainted(false);
	    }
	    
		public Object getCellEditorValue() {
			return country;
		}
		
		public void actionPerformed(ActionEvent e) {
			if (EDIT.equals(e.getActionCommand())) {
				country = CountryDialog.showDialog(null, country);
				
				fireEditingStopped();
			}
		}		

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {

			country = ((Site) allSites.get(row)).getCountry();
			return button;
		}
	}	
	
	public void setDataModified(boolean isModified) {
		model.fireTableDataChanged();
		parent.setTitle(isModified ? "[Modified]" : null);
		dataModified = isModified;
	}
	
	public boolean isDataModified() {
		return dataModified;
	}
}
