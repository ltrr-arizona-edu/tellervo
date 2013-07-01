/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Ken Harris, Peter Brewer
 ******************************************************************************/


package org.tellervo.desktop.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.support.ModifiableTableCellRenderer;
import org.tellervo.desktop.editor.support.TableCellModifier;
import org.tellervo.desktop.editor.support.TableCellModifierListener;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.manip.RedateDialog;
import org.tellervo.desktop.prefs.PrefsEvent;
import org.tellervo.desktop.prefs.PrefsListener;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.remarks.AbstractRemark;
import org.tellervo.desktop.remarks.RemarkPanel;
import org.tellervo.desktop.remarks.Remarks;
import org.tellervo.desktop.remarks.TridasReadingRemark;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleEvent;
import org.tellervo.desktop.sample.SampleListener;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.PopupListener;
import org.tridas.schema.NormalTridasRemark;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.TridasValue;
import javax.swing.JSplitPane;


/**
   A view of the raw data in a series.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/

    // left to add:
    // - setSelectedYear(Year) -- (why?)
    // - (the popup)

public class SeriesDataMatrix extends JPanel implements SampleListener,
		PrefsListener {

	private final static Logger log = LoggerFactory.getLogger(SeriesDataMatrix.class);

	private static final long serialVersionUID = 1L;

	private Sample mySample;

	private EditorStatusBar statusBar;
	public JTable myTable;

	protected TableModel myModel;
	
	protected ModifiableTableCellRenderer myCellRenderer;
	private JSplitPane splitPane;
	private JPanel panelLeft;
	private JPanel panelRight;
	private RemarkPanel remarkPanel;
	private Editor e;
	
	// pass this along to the table
	@Override
	public void requestFocus() {
		myTable.requestFocus();
	}
	
	public void setStatusBarVisible(Boolean b)
	{
		if(statusBar!=null)	statusBar.setVisible(b);
	}
	
	public Sample getSample()
	{
		return mySample;
	}
	
	// (for Editor)
	public void stopEditing(boolean disableFutureEdits) {
		
		int row = myTable.getEditingRow();
		int col = myTable.getEditingColumn();
		if (row != -1 && col != -1) {
		  myTable.getCellEditor(row,col).stopCellEditing(); 

		  // disable editing
		  if(disableFutureEdits)
			  ((UnitAwareDecadalModel)myModel).enableEditing(false);
		  
		  // reselect whatever we just deselected
		  myTable.setRowSelectionInterval(row, row);
		  myTable.setColumnSelectionInterval(col, col);
		}
		
	}
	
	public void startEditing() {
		((UnitAwareDecadalModel)myModel).enableEditing(true);
	}


	public void enableEditing(boolean enable) {
		if(enable)
			startEditing();
		else
			stopEditing(true);
	}
	
	
	public SeriesDataMatrix(Sample s, Editor e) {
		// copy data reference, add self as observer
		mySample = s;
		mySample.addSampleListener(this);
		this.e = e;
		
		// create table
		myModel = new UnitAwareDecadalModel(mySample);
		/*
		 final Color DARK = new Color(0.7333f, 0.7765f, 0.8431f); // EXTRACT CONSTs!
		 final Color LIGHT = new Color(0.8196f, 0.8510f, 0.9216f);
		 final int THIN = 2;
		 final int THICK = 5;
		 */
		myTable = new JTable(myModel); /* {
		 public void paint(Graphics g) {
		 setOpaque(true);

		 int w = getWidth(), h = getHeight();

		 // fill light
		 g.setColor(LIGHT);
		 g.fillRect(0, 0, w, h);

		 // dark stripes
		 g.setColor(DARK);
		 ((Graphics2D) g).setStroke(new BasicStroke(THIN-1f));
		 for (int x=0; x<w+h; x+=THIN+THICK)
		 g.drawLine(x, 0, x-h, h);

		 super.paint(g);
		 }
		 }; */
		myTable.setGridColor(new Color(240, 240, 240)); 
		
		myTable.setDefaultEditor(EWLWValue.class, new EWLWCellEditor());

		// mouse listener for table
		
		final Editor glue = e;
		myTable.addMouseListener(new MouseListener() {


			@Override
			public void mouseClicked(MouseEvent ev) {
				
				if(ev.getButton() == MouseEvent.BUTTON3)
				{
					// Right click popup
					int row = myTable.rowAtPoint(ev.getPoint());
					int col = myTable.columnAtPoint(ev.getPoint());

					JPopupMenu menu = createPopupMenu(row, col);
					
					if(menu != null)
						menu.show(myTable, ev.getX(), ev.getY());
					
					ev.consume();
					return;
				}
				
				if ((ev.getClickCount()==2) && (!ev.isConsumed()))
				{
					if(glue!=null)
					{
						if(myTable.columnAtPoint(ev.getPoint())==0)
						{
							showRedateDialog();
						}
					}
					ev.consume();
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		// key listener for table
		myTable.addKeyListener(new DecadalKeyListener(myTable, mySample));

		myTable.setCellSelectionEnabled(true);
		myTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		// select the first year
		myTable.setRowSelectionInterval(0, 0);
		myTable.setColumnSelectionInterval(
				mySample.getRange().getStart().column() + 1, mySample.getRange()
						.getStart().column() + 1);
		

		// don't let the columns be rearranged or resized
		myTable.getTableHeader().setReorderingAllowed(false);
		myTable.getColumnModel().getColumn(0).setMinWidth(100);
		myTable.getTableHeader().setResizingAllowed(false);
		myTable.setRowSelectionAllowed(true);
		
		// make the last column a jprogressbar, % of max
		int max = 0;
		if (mySample.hasCount())
			max = (Collections.max(mySample.getCount())).intValue();
		// DISABLED: use column-header renderer for first column (pseudo-row-headers)
		// -- it doesn't look that great, since there are still gridlines between
		// rows; what i should really do is make a real table-row-header, which isn't too hard.
		// myTable.getColumnModel().getColumn(0).setCellRenderer(new
		//         javax.swing.table.JTableHeader().getDefaultRenderer());
		myTable.getColumnModel().getColumn(11).setCellRenderer(
				new CountRenderer(max));
		
		myCellRenderer = new ModifiableTableCellRenderer(new IconBackgroundCellRenderer(mySample));
		for(int i = 1; i < 11; i++)
			myTable.getColumnModel().getColumn(i).setCellRenderer(myCellRenderer);
		
		// make nulls elsewhere shaded, to indicate "can't use"
		// DISABLED, because it doesn't hit the area below the table yet (how?).
		// (but it looks really cool.)
		//SlashedIfNullRenderer slasher = new SlashedIfNullRenderer(mySample, myModel);
		//for (int i=1; i<=10; i++)
		//myTable.getColumnModel().getColumn(i).setCellRenderer(slasher);
		//myTable.setIntercellSpacing(new Dimension(0, 0));

		// set font, gridlines, colors ==> handled by refreshFromPreferences()
		
		// add to panel
		setLayout(new BorderLayout(0, 0)); // huh?
		JScrollPane sp = new JScrollPane(myTable,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		
		
		sp.setPreferredSize(new Dimension(700, 520));
		
		
		splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		
		add(splitPane, BorderLayout.CENTER);
		
		panelLeft = new JPanel();
		splitPane.setLeftComponent(panelLeft);
		
		panelRight = new JPanel();
		splitPane.setRightComponent(panelRight);
		panelRight.setLayout(new BorderLayout(0, 0));
		panelLeft.setLayout(new BorderLayout(0, 0));
		
		remarkPanel = new RemarkPanel(myTable, mySample);
		remarkPanel.setMinimumSize(new Dimension(280,280));
		
		panelRight.add(remarkPanel);
		
		panelLeft.add(sp);
		statusBar = new EditorStatusBar(myTable, mySample);
		add(statusBar, BorderLayout.SOUTH);
		
		initPrefs();
		App.prefs.addPrefsListener(this);
		splitPane.setDividerLocation(2000);
		splitPane.setResizeWeight(1.0);
		
	}
	
	private void showRedateDialog()
	{
		if(e!=null)	new RedateDialog(mySample, e).setVisible(true);
	}
	
	protected JPopupMenu createPopupMenu(int row, int col) {
		
		JPopupMenu popup = new JPopupMenu();
		
		JMenuItem redate = new JMenuItem("Redate series");
		redate.setIcon(Builder.getIcon("redate.png", 22));
		redate.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showRedateDialog();
			}
			
		});
		popup.add(redate);
		popup.addSeparator();
		
		// Clicked on year column
		if (col == 0)
		{
			// That's all we need.
			return popup;
		}
			
		// select the cell at e.getPoint()
		myTable.setRowSelectionInterval(row, row);
		myTable.setColumnSelectionInterval(col, col);

		addAddDeleteMenu(popup);
		popup.addSeparator();
		addNotesMenu(popup, row, col);
		
		return popup;
	}
	
	protected final void addAddDeleteMenu(JPopupMenu menu){
		
		JMenuItem insert = Builder.makeMenuItem("menus.edit.insert_year", true, "insertyear.png");
		insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				insertYear();
			}
		});
		

		JMenuItem insertBackwards = Builder.makeMenuItem("menus.edit.insert_year.back", true, "insertyear.png");
		insertBackwards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				insertYear(0, true, null, false);
			}
		});
				

		JMenuItem insertMR = Builder.makeMenuItem("menus.edit.insert_mr", true, "insertmissingyear.png");
		insertMR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				insertMissingRing();
			}
		});
		
		JMenuItem insertMRBackwards = Builder.makeMenuItem("menus.edit.insert_mr.back", true, "insertmissingyear.png");
		insertMRBackwards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				insertMissingRingBackwards();
			}
		});
		insertMRBackwards.setEnabled(false);

		JMenuItem delete = Builder.makeMenuItem("menus.edit.delete_year", true, "deleteyear.png");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				deleteYear();
			}
		});

		menu.add(insert);
		menu.add(insertBackwards);
		menu.add(insertMR);
		menu.add(insertMRBackwards);
		menu.add(delete);

		// (dim insert/insertMR/delete, if it's not an editable sample.)
		if (!mySample.isEditable()) {
			insertBackwards.setEnabled(false);
			insert.setEnabled(false);
			insertMR.setEnabled(false);
			delete.setEnabled(false);
		}
		
	}
	
	protected final void addNotesMenu(JPopupMenu menu, final int row, final int col) {
		// get the year
		final Year y = ((UnitAwareDecadalModel) myModel).getYear(row, col);
		
		if(!mySample.getRange().contains(y))
			return;

		TridasValue value = mySample.getRingWidthValueForYear(y);
		
		Runnable onRemarkChange = new Runnable() {
			public void run() {
				// notify the table that its data has changed
				((UnitAwareDecadalModel) myModel).fireTableCellUpdated(row, col);
				
				// set the sample as modified
				mySample.setModified();
				mySample.fireSampleDataChanged();
			}
		};
		
		Remarks.appendRemarksToMenu(menu, value, onRemarkChange, mySample.isEditable());
	}

	/** Return the Year of the currently selected cell.
	 @return the selected Year */
	public Year getSelectedYear() {
		return ((UnitAwareDecadalModel) myModel).getYear(myTable.getSelectedRow(),
				myTable.getSelectedColumn());
	}

	/**
	 * Insert a zero value ring
	 */
	public void insertYear() {
		insertYear(0, true);
	}

	/**
	 * Helper function to simultaneously insert a year with a value of zero,
	 * and also to add a 'missing ring' remark.
	 */
	public void insertMissingRing() {
		TridasReadingRemark remark = new TridasReadingRemark(NormalTridasRemark.MISSING_RING);
		insertYear(new Integer(Sample.missingRingValue), true, remark, true);
	}
	
	public void insertMissingRingBackwards() {
		TridasReadingRemark remark = new TridasReadingRemark(NormalTridasRemark.MISSING_RING);
		insertYear(new Integer(Sample.missingRingValue), true, remark, false);
	}

	/**
	 * Insert a year into the series
	 * 
	 * @param val
	 * @param selectAndEdit
	 */
	public void insertYear(Integer val, boolean selectAndEdit) {
		insertYear(val, selectAndEdit, null, true);
	}
	
	/**
	 * Insert a year into a series and set in to include a TridasRemark
	 * 
	 * @param val
	 * @param selectAndEdit
	 * @param remark
	 */
	public void insertYear(Integer val, boolean selectAndEdit, AbstractRemark remark, boolean pushForward) {

		// make sure it's not indexed or summed
		if (!mySample.isEditable()) {
			Alert.error("Can't Modify Data",
					"You cannot modify indexed or summed data files.");
			return;
		}
		
		Boolean addReverseRow = false;
		
		if(mySample.getRange().getStart().column()==0)
		{
			addReverseRow = true;
		}
		
		
		// get row, col
		int selectedRow = myTable.getSelectedRow();
		int selectedCol = myTable.getSelectedColumn();

		// get year => get data index
		Year yearToInsert = ((UnitAwareDecadalModel) myModel).getYear(selectedRow, selectedCol);
		if(!pushForward)
		{
			yearToInsert = yearToInsert.add(1);
		}
		int i = yearToInsert.diff(mySample.getRange().getStart());

		// make sure it's a valid place to insert a year
		if (!mySample.getRange().contains(yearToInsert)
				&& !mySample.getRange().getEnd().add(+1).equals(yearToInsert)) {
			// Alert.error("Can't insert here",
			//    "This isn't a valid place to insert a year.");
			return;
		}

		// insert value		
		if(mySample.containsSubAnnualData())
		{
			mySample.getEarlywoodWidthData().add(i, val);
			mySample.getLatewoodWidthData().add(i, val);
			mySample.getRingWidthData().add(i, val); 
			mySample.recalculateRingWidths();
		}
		else
		{
			mySample.getRingWidthData().add(i, val); 
		}
		
		mySample.setRange(new Range(mySample.getRange().getStart(), mySample.getRange()
				.getEnd().add(+1)));

		
		if(!pushForward)
		{	
			// Users wants to insert backwards
			mySample.setRange(new Range(mySample.getRange().getStart().add(-1), mySample.getRange()
					.getEnd().add(-1)));
		}	
				
		myTable.setColumnSelectionInterval(selectedCol, selectedCol);
		myTable.setRowSelectionInterval(selectedRow, selectedRow);
		myTable.editCellAt(selectedRow, selectedCol);
		
		// fire event -- obsolete?
		((UnitAwareDecadalModel) myModel).fireTableDataChanged();		

		// set modified
		mySample.fireSampleDataChanged();
		mySample.fireSampleRedated();
		mySample.setModified();
		
		// Add remark if provided
		if(remark!=null)
		{
			TridasValue value = mySample.getRingWidthValueForYear(yearToInsert);
			remark.applyRemark(value);
		}	
		
		// select this cell again?  edit it
		if(selectAndEdit) {
			if(!pushForward && addReverseRow)
			{
				log.debug("skipping forward a row");
				// Handle user inserting years and shifting backwards a row
				selectedRow++;
			}	
			
			log.debug("selecting cell "+selectedRow+","+selectedCol);
			
		}
		else
		{
			// what's the next year?
			yearToInsert = yearToInsert.add(1);

			// where's it located?
			selectedRow = yearToInsert.row() - mySample.getRange().getStart().row();
			selectedCol = yearToInsert.column() + 1;

			myTable.setRowSelectionInterval(selectedRow, selectedRow);
			myTable.setColumnSelectionInterval(selectedCol, selectedCol);
		}
		
		
	}

	public void insertYears(Integer val, int nYears) {
		int row = myTable.getSelectedRow();
		int col = myTable.getSelectedColumn();
		
		insertYears(val, nYears, row, col);
	}
	
	public void insertYears(Integer val, int nYears, int insertpointrow, int insertpointcol) {
		// make sure it's not indexed or summed
		if (!mySample.isEditable()) {
			Alert.error("Can't Modify Data",
					"You cannot modify indexed or summed data files.");
			return;
		}

		// get row, col
		int row = insertpointrow;
		int col = insertpointcol;

		// get year => get data index
		Year y = ((UnitAwareDecadalModel) myModel).getYear(row, col);
		int i = y.diff(mySample.getRange().getStart());

		// make sure it's a valid place to insert a year
		if ((!mySample.getRange().contains(y)
				&& !mySample.getRange().getEnd().add(nYears).equals(y)) && mySample.getRange().getSpan()>1) {
			 Alert.error("Can't insert here",
			    "This isn't a valid place to insert a year.");
			return;
		}

		// insert 0, nyears times...
		for(int j = 0; j < nYears; j++)
			mySample.getRingWidthData().add(i, val); // new Integer(0));
		mySample.setRange(new Range(mySample.getRange().getStart(), mySample.getRange()
				.getEnd().add(nYears)));
		// REFACTOR: by LoD, should be range.extend()

		// fire event -- obsolete?
		((UnitAwareDecadalModel) myModel).fireTableDataChanged();

		// select this cell again?  edit it
		myTable.setRowSelectionInterval(row, row);
		myTable.setColumnSelectionInterval(col, col);

		// set modified
		mySample.fireSampleDataChanged();
		mySample.fireSampleRedated();
		mySample.setModified();
		
		myTable.setRowSelectionInterval(row, row);
		myTable.setColumnSelectionInterval(col, col);
	}
	

	public void deleteYear() {
		// make sure it's not indexed or summed
		if (!mySample.isEditable()) {
			new Bug(new IllegalArgumentException(
					"deleteYear() called on non-editable sample"));
			return;
		}

		// get row, col
		int row = myTable.getSelectedRow();
		int col = myTable.getSelectedColumn();

		// get year => get data index
		Year y = ((UnitAwareDecadalModel) myModel).getYear(row, col);
		int i = y.diff(mySample.getRange().getStart());

		// make sure there's data to delete
		if (!mySample.getRange().contains(y)) {
			// Alert.error("Can't delete here",
			//    "This isn't a value that can be deleted.");
			return;
		}

		if(mySample.containsSubAnnualData())
		{
			mySample.getEarlywoodWidthData().remove(i);
			mySample.getLatewoodWidthData().remove(i);
			mySample.getRingWidthData().remove(i);
			mySample.setRange(new Range(mySample.getRange().getStart(), mySample.getRange()
					.getEnd().add(-1)));
			mySample.recalculateRingWidths();
		}
		else
		{
			// delete value
			mySample.getRingWidthData().remove(i);
			mySample.setRange(new Range(mySample.getRange().getStart(), mySample.getRange()
					.getEnd().add(-1)));
		}
		
		// fire event
		((UnitAwareDecadalModel) myModel).fireTableDataChanged();

		// select this cell again
		myTable.setRowSelectionInterval(row, row);
		myTable.setColumnSelectionInterval(col, col);

		// note: if the last datum was just deleted, should back up
		// the cursor to the new-last-cell.  better yet: BACKSPACE
		// does this always, DELETE never does.

		// set modified
		mySample.setModified();		
		mySample.fireSampleDataChanged();
		mySample.fireSampleRedated();
	}

	public void sampleRedated(SampleEvent e) {
		// update data view
		((UnitAwareDecadalModel) myModel).fireTableDataChanged();
	}

	public void sampleDataChanged(SampleEvent e) {
		// update data view
		((UnitAwareDecadalModel) myModel).fireTableDataChanged();
		// FIXME: make myModel an AbstractTableModel, so i don't have to cast
	}

	public void sampleMetadataChanged(SampleEvent e) {
	}

	public void sampleElementsChanged(SampleEvent e) {
	}

	private void initPrefs() {
		// reset fonts
		Font font = App.prefs.getFontPref(PrefKey.EDIT_FONT, null);
		if (font != null)
			myTable.setFont(font);

		// from font size, set table row height
		myTable.setRowHeight((font == null ? 12 : font.getSize()) + 4);
		// BUG: this seems to not work sometimes (?) -- try zapfino

		myTable.setRowHeight(16);
		
		// disable gridlines, if requested
		boolean gridlines = App.prefs.getBooleanPref(PrefKey.EDIT_GRIDLINES, true);
		myTable.setShowGrid(gridlines);

		// set colors
		myTable.setBackground(App.prefs.getColorPref(PrefKey.EDIT_BACKGROUND,
				Color.white));
		myTable.setForeground(App.prefs.getColorPref(PrefKey.EDIT_FOREGROUND,
				Color.black));
	}

	// should this be part of update()?  well, the constructor will
	// need it, too, so it might as well be a separate method, anyway.
	public void prefChanged(PrefsEvent e) {
		initPrefs();
	}

	/**
	 * Set whole ring width value programatically
	 * 
	 * @param x
	 * @return
	 */
	public Year measured(int x)
	{
		return measured(x, null);
	}
	
	/**
	 * Set value programatically.  The second value is optional.  If one
	 * value is provided uses it as a whole ring width value, if two are 
	 * provided it uses them as early/late wood values
	 * 
	 * @param firstval
	 * @param secondval
	 * @return
	 */
	public Year measured(int firstval, Integer secondval)
	{
		Year y = ((UnitAwareDecadalModel) myTable.getModel()).getYear(myTable
				.getSelectedRow(), myTable.getSelectedColumn());
	
		int i = y.diff(mySample.getRange().getStart());
		
		if(secondval==null)
		{		
			// Just one value provided, so assuming its whole ring width value
			
			if (!mySample.getRange().contains(y)) {
				mySample.setRange(new Range(mySample.getRange().getStart(),
						mySample.getRange().getEnd().add(1)));
				mySample.getRingWidthData().add(new Integer(0));
			}
			
			mySample.getRingWidthData().set(i, new Integer(firstval));
		}
		else
		{
			// Two values provided, so assuming they are early and late wood
			
			if (!mySample.getRange().contains(y)) {
				mySample.setRange(new Range(mySample.getRange().getStart(),
						mySample.getRange().getEnd().add(1)));
				mySample.getEarlywoodWidthData().add(new Integer(0));
				mySample.getLatewoodWidthData().add(new Integer(0));
			}
			
			mySample.getEarlywoodWidthData().set(i, firstval);
			mySample.getLatewoodWidthData().set(i, secondval);
			mySample.recalculateRingWidths();			
		}
		
		// this is the year we return...
		Year retYear = y;

		// what's the next year?
		if(App.prefs.getBooleanPref(PrefKey.MEASURE_BARK_TO_PITH, false))
		{
			y= y.add(-1);
		}
		else
		{
			y = y.add(1);
		}

		// where's it located?
		int row = y.row() - mySample.getRange().getStart().row();
		int col = y.column() + 1;

		// fire sample events first, so the table update below gets the good data
		mySample.fireSampleRedated();
		mySample.fireSampleDataChanged();
		mySample.setModified();

		// update table (overkill?)
		((AbstractTableModel) myTable.getModel()).fireTableDataChanged();

		// select it
		myTable.setRowSelectionInterval(row, row);
		myTable.setColumnSelectionInterval(col, col);
		
		return retYear;
	}
	

	/**
	 * Add a cell modifier to the table and repaint
	 * @param modifier
	 */
	public void addCellModifier(TableCellModifier modifier) {
		myCellRenderer.addModifier(modifier);
		
		// repaint the table on notification
		modifier.setListener(new TableCellModifierListener() {
			public void cellModifierChanged(TableCellModifier modifier) {
				myTable.repaint();
			}
		});
		
		// repaint now, to be safe
		myTable.repaint();
	}
	
	/**
	 * Remove a cell modifier from the table and repaint
	 * @param modifier
	 */
	public void removeCellModifier(TableCellModifier modifier) {
		myCellRenderer.removeModifier(modifier);
		modifier.setListener(null);
		myTable.repaint();
	}

	public void sampleDisplayUnitsChanged(SampleEvent e) {
		
		if (myModel instanceof UnitAwareDecadalModel)
		{
			String pref = App.prefs.getPref(PrefKey.DISPLAY_UNITS, NormalTridasUnit.MICROMETRES.name().toString());

			
			((UnitAwareDecadalModel) myModel).setDisplayUnits(NormalTridasUnit.valueOf(pref));
		}
		
		
		
	}


	public void toggleRemarks()
	{
		log.debug("toggling Remarks panel");
		
		int currLoc = splitPane.getDividerLocation();
		int totalWidth = splitPane.getWidth();
		
		log.debug("Currloc     = "+currLoc);
		log.debug("Total width = " + totalWidth);
		
		
		if(currLoc+20 >= totalWidth)
		{
			log.debug("Panel appears to be shut so open");
			BasicSplitPaneUI ui = (BasicSplitPaneUI)splitPane.getUI();
			JButton oneClick = (JButton)ui.getDivider().getComponent(0);
			oneClick.doClick();
		}
		else
		{
			log.debug("Panel appears to be open so shut");
			splitPane.setDividerLocation(1.0);
		}
		

		
		
	}
	
	@Override
	public void measurementVariableChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sampleDisplayCalendarChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}
}
