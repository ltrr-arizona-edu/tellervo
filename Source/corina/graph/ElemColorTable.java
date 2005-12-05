/**
 * 
 */
package corina.graph;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import corina.util.ColorPair;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import javax.swing.ListCellRenderer;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

/**
 * @author Lucas
 *
 */
public class ElemColorTable extends JScrollPane {
	
	boolean isPrinting = true;
	
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
	
	private List getGraphs() {
		return graphs;
	}

	protected List graphs;
	protected ColorPair graphColors[];
	
	public final ColorPair colors[] = { 
			new ColorPair("Black", Color.BLACK),
			new ColorPair("Blue", Color.BLUE),
			new ColorPair("Cyan", Color.CYAN),
			new ColorPair("Light Gray", Color.lightGray),
			new ColorPair("Green", Color.green),
			new ColorPair("Magenta", Color.magenta),
			new ColorPair("Orange", Color.orange),
			new ColorPair("Red", Color.red),
			new ColorPair("Dark Gray", Color.darkGray),
			new ColorPair("Pink", Color.pink),
			new ColorPair("Yellow", Color.yellow),
			new ColorPair("Gray", Color.gray), 
			new ColorPair("White", Color.white) 
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
		
		public void paint(Graphics g1) {
			Graphics2D g = (Graphics2D) g1;

			g.clearRect(0, 0, getWidth(), getHeight());
			g.setStroke(new BasicStroke((float) myThickness));
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

		public void paint(Graphics g1) {
			Graphics2D g = (Graphics2D) g1;

			g.clearRect(0, 0, getWidth(), getHeight());
			g.setStroke(new BasicStroke((float) myThickness));
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
		
		public boolean isCellEditable(int row, int col) {
			return true;
		}
		
		public int getColumnCount() {
			return 3;
		}
		
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
			Graph g = (Graph) parent.graphs.get(row);
			
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
