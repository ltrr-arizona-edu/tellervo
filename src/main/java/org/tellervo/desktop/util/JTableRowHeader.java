package org.tellervo.desktop.util;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.UIManager;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;
import javax.swing.table.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 *	Use a JTable as a renderer for row numbers of a given main table.
 *  This table must be added to the row header of the scrollpane that
 *  contains the main table.
 */
public class JTableRowHeader extends JTable
	implements ChangeListener, PropertyChangeListener, MouseListener
{
	private final static Logger log = LoggerFactory.getLogger(JTableRowHeader.class);

	private JTable mainTable;
	private Boolean isNumberLabelVisible = false;
	private Integer rowIndexSelected = null;
	private ArrayList<Integer> rowsSelected = new ArrayList<Integer>();
	protected JPopupMenu tablePopupMenu;
	
	public JTableRowHeader(JTable table, JPopupMenu tablePopupMenu)
	{
		this.tablePopupMenu = tablePopupMenu;
		
		mainTable = table;
		mainTable.addPropertyChangeListener( this );

		setFocusable( false );
		setAutoCreateColumnsFromModel( false );
		setModel( mainTable.getModel() );
		setSelectionModel( mainTable.getSelectionModel() );

		TableColumn column = new TableColumn();
		column.setHeaderValue(" ");
		addColumn( column );
		column.setCellRenderer(new RowHeaderRenderer());

		getColumnModel().getColumn(0).setPreferredWidth(10);
		setPreferredScrollableViewportSize(getPreferredSize());
		
		final JTableRowHeader glue = this;
		this.addMouseListener(this);
		
	}

	@Override
	public void addNotify()
	{
		super.addNotify();

		Component c = getParent();

		//  Keep scrolling of the row table in sync with the main table.

		if (c instanceof JViewport)
		{
			JViewport viewport = (JViewport)c;
			viewport.addChangeListener( this );
		}
	}

	/*
	 *  Delegate method to main table
	 */
	@Override
	public int getRowCount()
	{
		return mainTable.getRowCount();
	}

	@Override
	public int getRowHeight(int row)
	{
		return mainTable.getRowHeight(row);
	}

	/*
	 *  This table does not use any data from the main TableModel,
	 *  so just return a value based on the row parameter.
	 */
	@Override
	public Object getValueAt(int row, int column)
	{
		if(isNumberLabelVisible)
		{
			return Integer.toString(row + 1);
		}
		else
		{
			return " ";
		}
	}

	/*
	 *  Don't edit data in the main TableModel by mistake
	 */
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
//
//  Implement the ChangeListener
//
	public void stateChanged(ChangeEvent e)
	{
		//  Keep the scrolling of the row table in sync with main table

		JViewport viewport = (JViewport) e.getSource();
		JScrollPane scrollPane = (JScrollPane)viewport.getParent();
		scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
	}
//
//  Implement the PropertyChangeListener
//
	public void propertyChange(PropertyChangeEvent e)
	{
		//  Keep the row table in sync with the main table

		if ("selectionModel".equals(e.getPropertyName()))
		{
			setSelectionModel( mainTable.getSelectionModel() );
		}

		if ("model".equals(e.getPropertyName()))
		{
			setModel( mainTable.getModel() );
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent evt) {
		
		mouseHandler(evt);
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
	public void mousePressed(MouseEvent evt) {
		mouseHandler(evt);
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void mouseHandler(MouseEvent evt)
	{
		log.debug("Mouse clicked");
		int index = this.rowAtPoint(evt.getPoint());
		log.debug("Row index: "+index);
		
		
		
		if(evt.isPopupTrigger())
		{
			if(mainTable.getSelectedRowCount()==0)
			{
				mainTable.setRowSelectionInterval(index, index);
				mainTable.setColumnSelectionInterval(0, mainTable.getColumnCount()-1);
				rowIndexSelected = index;
			}
			
			tablePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY()); 
			
		}
			

		else if(!evt.isShiftDown() && !evt.isControlDown())
		{
			log.debug("Basic mouse click");
			
			
			mainTable.setRowSelectionInterval(index, index);
			mainTable.setColumnSelectionInterval(0, mainTable.getColumnCount()-1);
			rowIndexSelected = index;
		}
		else if (evt.isShiftDown())
		{
			log.debug("Shift mouse click");
			if(rowIndexSelected==null)
			{
				mainTable.setRowSelectionInterval(index, index);
				mainTable.setColumnSelectionInterval(0, mainTable.getColumnCount()-1);
				rowIndexSelected = index;
			}
			else
			{
				mainTable.setRowSelectionInterval(rowIndexSelected, index);
				mainTable.setColumnSelectionInterval(0, mainTable.getColumnCount()-1);
			}
		}
		
		mainTable.repaint();
		
		log.debug("Row "+mainTable.getSelectedRow()+" is selected");

		
	}
	
	

	
	
	/*
	 *  Borrow the renderer from JDK1.4.2 table header
	 */
	private static class RowHeaderRenderer extends DefaultTableCellRenderer
	{
		public RowHeaderRenderer()
		{
			setHorizontalAlignment(JLabel.CENTER);
		}

		public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{

			setText((value == null) ? "" : value.toString());
			//setBorder(UIManager.getBorder("TableHeader.cellBorder"));\
						
			if(isSelected)
			{
				setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			}
			else
			{
				setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));	
			}
			
			Color c = UIManager.getColor("ToolBar.background");
			setBackground(c);

			return this;
		}
	}
}
