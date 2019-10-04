package org.tellervo.desktop.labelgen;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.tellervo.desktop.components.table.WSIBoxRenderer;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.util.ArrayListModel;
import org.tellervo.desktop.util.labels.ui.TridasListCellRenderer;
import org.tellervo.schema.WSIBox;

import net.miginfocom.swing.MigLayout;


public class LGWizardBoxPicker2 extends AbstractWizardPanel implements ActionListener{


	private static final long serialVersionUID = 1L;
	private JList lstSelected;
	private JTable tblAvailable;
	protected ArrayListModel<WSIBox> selModel = new ArrayListModel<WSIBox>();
	protected BoxTableModel availModel = new BoxTableModel();
	private JButton btnAddAll;
	private JButton btnAddOne;
	private JButton btnRemoveOne;
	private JButton btnRemoveAll;
	private JPanel panel_1;
	private JLabel lblSortLabelsBy;
	private JComboBox cboSort;
	private JLabel lblFilter;
	private JLabel lblAvailable;
	private JTextField txtFilter;
	private TableRowSorter<BoxTableModel> sorter;
	
	public enum BoxSortType{
		
		NO_SORT("No sort (print as listed)"),
		TITLE("Title"),
		LOCATION("Location"),
		CREATED_TIMESTAMP("Created timestamp"),
		LAST_MODIFIED_TIMESTAMP("Last modified timestamp");
		
		private String fullname;
		
		BoxSortType(String fullname){
			this.fullname = fullname;
		}
		
		public String getDescription()
		{
			return fullname;
		}
		
		public String toString()
		{
			return fullname;
		}
		
	}
	
	
	/**
	 * Create the panel.
	 */
	public LGWizardBoxPicker2() {
		super("Step 2 - Which boxes?", 
				"The next step is to define which boxes you would like labels for.");
		setLayout(new MigLayout("", "[186.00,grow][45.00,center][grow]", "[][grow][53.00,top][grow][][]"));
		
		lblAvailable = new JLabel("Available:");
		add(lblAvailable, "cell 0 0");
		
		JLabel lblSelected = new JLabel("Selected:");
		add(lblSelected, "cell 2 0");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1 1 3,grow");
		
		tblAvailable = new JTable();
		availModel = new BoxTableModel();
		tblAvailable.setDefaultRenderer(WSIBox.class, new WSIBoxRenderer());
		
		
		tblAvailable.setModel(availModel);
		tblAvailable.setFillsViewportHeight(true);
		tblAvailable.setShowVerticalLines(false);
		tblAvailable.setShowHorizontalLines(false);
		tblAvailable.setShowGrid(false);
		tblAvailable.setTableHeader(null);
		tblAvailable.setAutoCreateRowSorter(true);
		tblAvailable.setBackground(Color.WHITE);

		
		scrollPane.setViewportView(tblAvailable);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, "cell 1 2,grow");
		panel.setLayout(new MigLayout("", "[fill][]", "[][][][]"));
		
		btnAddAll = new JButton(">>");
		btnAddAll.addActionListener(this);
		panel.add(btnAddAll, "cell 0 0");
		
		btnAddOne = new JButton(">");
		btnAddOne.addActionListener(this);
		panel.add(btnAddOne, "cell 0 1");
		
		btnRemoveOne = new JButton("<");
		btnRemoveOne.addActionListener(this);
		panel.add(btnRemoveOne, "cell 0 2");
		
		btnRemoveAll = new JButton("<<");
		btnRemoveAll.addActionListener(this);
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel.add(btnRemoveAll, "cell 0 3");
		
		
		btnRemoveAll.setVisible(false);
		btnAddAll.setVisible(false);
		JScrollPane scrollPane_1 = new JScrollPane();
		add(scrollPane_1, "cell 2 1 1 3,grow");
		
		lstSelected = new JList();
		scrollPane_1.setViewportView(lstSelected);
		
		lblFilter = new JLabel("Filter:");
		add(lblFilter, "flowx,cell 0 4");
		
		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		add(panel_1, "cell 0 5 3 1,grow");
		panel_1.setLayout(new MigLayout("", "[][grow]", "[]"));
		
		lblSortLabelsBy = new JLabel("Sort labels by:");
		panel_1.add(lblSortLabelsBy, "cell 0 0,alignx trailing");
		
		cboSort = new JComboBox<BoxSortType>();
		cboSort.setModel(new DefaultComboBoxModel(BoxSortType.values()));
		cboSort.setBackground(Color.WHITE);
		panel_1.add(cboSort, "cell 1 0,growx");
		
		txtFilter = new JTextField("");
		
		txtFilter.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        newFilter();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        newFilter();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        newFilter();
                    }
                });
		
		add(txtFilter, "cell 0 4,growx");
		
		populateBoxList();
		
		
		
			
	}
	
	private void newFilter()
	{
	
		RowFilter<BoxTableModel,Object> boxFilter = new RowFilter<BoxTableModel,Object>() {
		   public boolean include(Entry<? extends BoxTableModel, ? extends Object> entry) {
		     for (int i = entry.getValueCount() - 1; i >= 0; i--) {
		       WSIBox box = (WSIBox) entry.getValue(i);
		       if (box.getTitle().contains(txtFilter.getText())) {
		         // The value starts with "a", include it
		         return true;
		       }
		       else
		       {
				     return false;		       }
		     }
		     // None of the columns start with "a"; return false so that this
		     // entry is not shown
		     return false;
		   }
		 };
		 
		 sorter.setRowFilter(boxFilter);
	}
	
	private void updateGUI()
	{
		
	}

	public ArrayList<WSIBox> getBoxes()
	{
		ArrayList<WSIBox> items = new ArrayList<WSIBox>();
		
		for(int i=0; i<selModel.size(); i++)
		{
			items.add(selModel.get(i));
		}
		
		return items;
	}

	public void actionPerformed(ActionEvent evt) {

		
		if(evt.getSource() == btnAddOne){

			ArrayList<WSIBox> boxesToMove = new ArrayList<WSIBox>();
			for (int i : tblAvailable.getSelectedRows())
			{
				int theind = i;
				if (txtFilter.getText().length()>0) theind = tblAvailable.convertRowIndexToModel(i);
				WSIBox myobj = (WSIBox) tblAvailable.getModel().getValueAt(theind,0);
				selModel.add(myobj);
				boxesToMove.add(myobj);				
			}
			tblAvailable.clearSelection();
			availModel.removeAllBoxes(boxesToMove);
			newFilter();
			tblAvailable.repaint();
		}
			
		if(evt.getSource() == btnRemoveOne)
		{
			for (Object obj : lstSelected.getSelectedValues())
			{
				WSIBox myobj = (WSIBox) obj;
				availModel.addBox(myobj);
				selModel.remove(myobj);
				//sortAvailableBoxList();
				tblAvailable.repaint();
			}
			
			newFilter();
		}
		
		if(evt.getSource() == btnAddAll)
		{
			ArrayList<WSIBox> listadd = new ArrayList<WSIBox>();
			
			for(int i=0; i<availModel.getRowCount(); i++)
			{
				listadd.add(availModel.getBox(i));
			}
					
			selModel.addAll(listadd);
			availModel.removeAllBoxes(listadd);
			newFilter();
			tblAvailable.repaint();
			lstSelected.repaint();
		}
		
		if(evt.getSource() == btnRemoveAll)
		{
			ArrayList<WSIBox> listremove = new ArrayList<WSIBox>();
			
			for(int i=0; i<selModel.getSize(); i++)
			{
				listremove.add(selModel.get(i));
			}
					
			availModel.addAllBoxes(listremove);
			selModel.removeAll(listremove);
			newFilter();
			tblAvailable.repaint();
			lstSelected.repaint();
		}
		
	}

	private void populateBoxList(){
		
	    // Grab box dictionary
	    ArrayList<WSIBox> boxlist = (ArrayList<WSIBox>) Dictionary.getMutableDictionary("boxDictionary");
			    
	    // Set up available list
	    availModel.clear();
	    availModel.addAllBoxes(boxlist);


	    
		
		tblAvailable.setModel(availModel);
		tblAvailable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		//lstAvailable.setCellRenderer(new TridasListCellRenderer());
		
	    
		sorter = new TableRowSorter<BoxTableModel>();
		sorter.setModel(availModel);

		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.SITE_CODES_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		
		sorter.setComparator(0, numSorter);
		tblAvailable.setRowSorter(sorter);
		
		sorter.sort();
		
		
		// Set up selected list
		lstSelected.setModel(selModel);
		lstSelected.setCellRenderer(new TridasListCellRenderer());
	    }

    /*private void sortAvailableBoxList(){
		// Sort list intelligently
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.SITE_CODES_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		
		Collections.sort(availModel, numSorter);
		

    }*/
    
    public BoxSortType getSortType()
    {
    	return (BoxSortType) this.cboSort.getSelectedItem();
    }
	
    @Override
	public boolean doPageValidation(){
		
    	if(this.selModel.getSize()>0)
    	{
    		return true;
    	}
    	
    	

		Alert.error("Error", "You must select one or more boxes to continue.");
		return false;
		
	}

    
    
    class BoxTableModel extends AbstractTableModel{
    	
    	private String[] columnNames = {"Box"};

    	ArrayList<WSIBox> boxes = new ArrayList<WSIBox>();
    	
    	public BoxTableModel() {
    	
    	}
    	
    	public BoxTableModel(ArrayList<WSIBox> boxes) {
    		this.boxes = boxes;
    	}
    	
        public String getColumnName(int col) {
            return "Box";
        }
    	
    	 public Class getColumnClass(int c) {
             return WSIBox.class;
         }
    	
		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public int getRowCount() {
			return boxes.size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			
			if(row<boxes.size())
			{
				return boxes.get(row);
			}
			return null;
		}
		
		public WSIBox getBox(int row)
		{
			return boxes.get(row);
		}
		
		public void addBox(WSIBox box)
		{
			boxes.add(box);
			sort();
		}
		
		public void removeBox(WSIBox box)
		{
			boxes.remove(box);
			sort();
		}
		
		public void addAllBoxes(Collection<WSIBox> boxes)
		{
			this.boxes.addAll(boxes);
			sort();
		}
		
		public void removeAllBoxes(Collection<WSIBox> boxes)
		{
			this.boxes.removeAll(boxes);
			sort();
		}
    	
		public void clear()
		{
			this.boxes.clear();
		}
		
		public void sort()
		{
			// Sort list intelligently
			TridasComparator numSorter = new TridasComparator(TridasComparator.Type.SITE_CODES_THEN_TITLES, 
					TridasComparator.NullBehavior.NULLS_LAST, 
					TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
			
			Collections.sort(boxes, numSorter);			
		}
    	
    }
    
   

    

}
