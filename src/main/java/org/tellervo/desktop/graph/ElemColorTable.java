/**
 * 
 */
package org.tellervo.desktop.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.tellervo.desktop.util.ColorPair;


/**
 * @author Lucas
 *
 */
@SuppressWarnings("serial")
public class ElemColorTable extends JScrollPane {
	
	boolean isPrinting = true;
	
	@SuppressWarnings("unchecked")
	public ElemColorTable(List graphs, boolean isPrinting) {
		super();

		this.isPrinting = isPrinting;
		this.graphs = graphs;
		graphColors = new ColorPair[graphs.size()];		
		
		int i;
		for(i = 0; i < graphs.size(); i++) {
			int j;
			ColorPair gcolor = null;
			Color mcolor = ((Graph)graphs.get(i)).getColor(true);
			for(j = 0; j < colors.length; j++) {
				if(colors[j].colorVal.equals(mcolor)) {
					gcolor = colors[j];
					break;
				}
			}
			if(gcolor == null)
				gcolor = new ColorPair("Unknown", mcolor);
			
			graphColors[i] = gcolor;
		}
		
		TableModel dataModel = new exportInfoTableModel(this);
		JTable table = new JTable(dataModel);
	
		JComboBox colorCombo = new JComboBox(colors);
		colorCombo.setRenderer(new colorComboBoxRenderer());		
		table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(colorCombo));
		table.getColumnModel().getColumn(1).setCellRenderer(new colorTableCellRenderer());

		JComboBox thickCombo = new JComboBox(thicknesses);
		thickCombo.setRenderer(new thicknessComboBoxRenderer());		
		table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(thickCombo));
		table.getColumnModel().getColumn(2).setCellRenderer(new thicknessTableCellRenderer());
		
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		
		table.setPreferredScrollableViewportSize(table.getPreferredSize());

		setViewportView(table);
	}
	
	@SuppressWarnings("unchecked")
	private List getGraphs() {
		return graphs;
	}

	@SuppressWarnings("unchecked")
	protected List graphs;
	protected ColorPair graphColors[];
	
	public final ColorPair colors[] = { 
			new ColorPair("Blue", new Color(0.00f, 0.53f, 1.00f)),
			new ColorPair("Green", new Color(0.27f, 1.00f, 0.49f)),
			new ColorPair("Red", new Color(1.00f, 0.28f, 0.27f)),
			new ColorPair("Cyan", new Color(0.22f, 0.80f, 0.82f)),
			new ColorPair("Yellow", new Color(0.82f, 0.81f, 0.23f)),
			new ColorPair("Magenta", new Color(0.85f, 0.26f, 0.81f)),
			new ColorPair("Gray", Color.gray),			
			new ColorPair("Orange", Color.ORANGE),
			new ColorPair("Black", Color.BLACK),
			new ColorPair("Pink", Color.PINK),			
			new ColorPair("Dk Blue", Color.BLUE),
			new ColorPair("Lt Cyan", Color.CYAN),
			new ColorPair("Lt Gray", Color.lightGray),
			new ColorPair("Dk Green", Color.green),
			new ColorPair("Dk Magenta", Color.magenta),
			new ColorPair("Dk Red", Color.red),
			new ColorPair("Dk Gray", Color.darkGray)
			};
	
	public final Integer thicknesses[] = { new Integer(1), new Integer(2), 
			new Integer(3), new Integer(4), new Integer(5)};
	
	// renderers for color drop-down box
	private class colorComboBoxRenderer extends JLabel implements ListCellRenderer {
		public colorComboBoxRenderer() {
			setOpaque(true);
		}
		
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
			ColorPair p = (ColorPair) value;

			setBackground(p.colorVal);
			setText(p.colorName);
			
			if(p.colorVal.equals(Color.BLACK))
				setForeground(Color.WHITE);
			
			if(p.colorVal.equals(Color.YELLOW))
				setForeground(Color.BLACK);
			
			return this;
		}
	}
		
	private class colorTableCellRenderer extends JLabel implements TableCellRenderer {
		public colorTableCellRenderer() {
			setOpaque(true);
		}
		
		public Component getTableCellRendererComponent(JTable table, 
				Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
			
			
			ColorPair p = (ColorPair) value;
			setBackground(p.colorVal);
			
			if(p.colorVal.equals(Color.BLACK))
				setForeground(Color.WHITE);
			
			if(p.colorVal.equals(Color.YELLOW))
				setForeground(Color.BLACK);
			
			setText(p.colorName);

            return this;			
		}
	}
	
	// renderers for thickness drop-down box
	private class thicknessComboBoxRenderer extends JComponent implements ListCellRenderer {
		int myThickness;
		
		public thicknessComboBoxRenderer() {
			setOpaque(true);
			setPreferredSize(new Dimension(30, 20));
		}
		
		@Override
		public void paint(Graphics g1) {
			Graphics2D g = (Graphics2D) g1;

			g.clearRect(0, 0, getWidth(), getHeight());
			g.setStroke(new BasicStroke(myThickness));
			g.setColor(new Color(0, 0, 0));
			g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);			
		}
		
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
			Integer p = (Integer) value;

			myThickness = p.intValue();
			repaint();
			
			return this;
		}
	}

	private class thicknessTableCellRenderer extends JComponent implements TableCellRenderer {
		int myThickness;
		
		public thicknessTableCellRenderer() {
			setOpaque(true);
		}

		@Override
		public void paint(Graphics g1) {
			Graphics2D g = (Graphics2D) g1;

			g.clearRect(0, 0, getWidth(), getHeight());
			g.setStroke(new BasicStroke(myThickness));
			g.setColor(new Color(0, 0, 0));
			g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);			
		}
		
		public Component getTableCellRendererComponent(JTable table, 
				Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
			
			
			Integer p = (Integer) value;

			myThickness = p.intValue();
			repaint();
			
			return this;
		}
	}
	
	
	private class exportInfoTableModel extends AbstractTableModel {
		ElemColorTable parent;
		
		public exportInfoTableModel(ElemColorTable parent) {
			this.parent = parent;
		}
		
		public int getRowCount() {
			return parent.getGraphs().size();
		}
		
		@Override
		public String getColumnName(int col) {
			switch(col) {
			case 0:
				return "Element name";
			case 1:
				return "Line Color";
			case 2:
				return "Line Thickness";
			}
			return null;
		}
		
		@Override
		public boolean isCellEditable(int row, int col) {
			return true;
		}
		
		public int getColumnCount() {
			return 3;
		}
		
		@Override
		public void setValueAt(Object value, int row, int col) {
			switch(col) {
			case 0:
				((Graph)parent.getGraphs().get(row)).setGraphName((String) value);
				break;
			case 1:
				parent.graphColors[row] = (ColorPair) value;
				((Graph)parent.getGraphs().get(row)).setColor(((ColorPair)value).colorVal, parent.isPrinting);				
				break;
			case 2:
				((Graph)parent.getGraphs().get(row)).setThickness(((Integer)value).intValue(), parent.isPrinting);
				break;
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Class getColumnClass(int col) {
			switch(col) {
			case 0:
				return String.class;
			case 1:
				return ColorPair.class;
			case 2:
				return Integer.class;
			default:
				return Integer.class;
			}
		}
		
		public Object getValueAt(int row, int col) {
			
			switch(col) {
			case 0:
				return ((Graph)parent.getGraphs().get(row)).getGraphName();
			case 1:
				return parent.graphColors[row];
			case 2:
				return new Integer(((Graph)parent.getGraphs().get(row)).getThickness(parent.isPrinting));
			default:
				return null;
			}
		}
	}	
}
