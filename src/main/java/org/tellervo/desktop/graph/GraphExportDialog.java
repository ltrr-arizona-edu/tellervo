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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.graph;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class GraphExportDialog extends JPanel {
	JDialog myframe;
	JFrame parent;
	@SuppressWarnings("unchecked")
	protected List graphs;
	protected GraphComponentInfo graphInfo[];

	public class colorPair {
		public String colorName;
		public Color colorVal;
		
		public colorPair(String colorName, Color colorVal) {
			this.colorName = colorName;
			this.colorVal = colorVal;
		}
	};
	
	public final colorPair colors[] = { 
			new colorPair("Black", Color.BLACK),
			new colorPair("Blue", Color.BLUE),
			new colorPair("Cyan", Color.CYAN),
			new colorPair("Light Gray", Color.lightGray),
			new colorPair("Green", Color.green),
			new colorPair("Magenta", Color.magenta),
			new colorPair("Orange", Color.orange),
			new colorPair("Red", Color.red),
			new colorPair("Dark Gray", Color.darkGray),
			new colorPair("Pink", Color.pink),
			new colorPair("Yellow", Color.yellow),
			new colorPair("Gray", Color.gray) 
			};
	
	public final Integer thicknesses[] = { new Integer(1), new Integer(2), new Integer(3), new Integer(4) };
	
	private class GraphComponentInfo {
		public String name;
		public colorPair color;
		public Integer thickness;
	}

	// renderers for color drop-down box
	private class colorComboBoxRenderer extends JLabel implements ListCellRenderer {
		public colorComboBoxRenderer() {
			setOpaque(true);
		}
		
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
			colorPair p = (colorPair) value;

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
			
			
			colorPair p = (colorPair) value;
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
		GraphExportDialog parent;
		
		public exportInfoTableModel(GraphExportDialog parent) {
			this.parent = parent;
		}
		
		public int getRowCount() {
			return parent.graphs.size();
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
				parent.graphInfo[row].name = (String) value;
				break;
			case 1:
				parent.graphInfo[row].color = (colorPair) value;
				break;
			case 2:
				parent.graphInfo[row].thickness = (Integer) value;
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
				return colorPair.class;
			case 2:
				return Integer.class;
			default:
				return Integer.class;
			}
		}
		
		public Object getValueAt(int row, int col) {
			
			
			switch(col) {
			case 0:
				return parent.graphInfo[row].name;
			case 1:
				return parent.graphInfo[row].color;
			case 2:
				return parent.graphInfo[row].thickness;
			default:
				return null;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public GraphExportDialog(JFrame parent, List graphs) {
		this.parent = parent;
		this.graphs = graphs;
		
		graphInfo = new GraphComponentInfo[graphs.size()];
		
		int i;
		for(i = 0; i < graphs.size(); i++) {
			graphInfo[i] = new GraphComponentInfo();
			graphInfo[i].name = ((Graph)graphs.get(i)).graph.toString();
			graphInfo[i].color = colors[i % colors.length];
			graphInfo[i].thickness = new Integer(1);
		}
		
		myframe = new JDialog(parent, "Graph export controls...", true);
		myframe.setContentPane(this);		
		myframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		setLayout(new BorderLayout());		
		
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
		JScrollPane scrollpane = new JScrollPane(table);

		add(scrollpane, BorderLayout.CENTER);
		
		JLabel info = new JLabel();
		info.setText(
				"<html><p>Please take a moment to customize your graph output.<p>" +
				"If labels are used in your graph, you may modify the text in the table below for each label. " +
				"This information will not be saved with or modify your graph.</html>");
				
		add(info, BorderLayout.NORTH);
		
		JPanel buttonpanel = new JPanel();
		buttonpanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton okbutton = new JButton("OK");
		okbutton.setPreferredSize(new Dimension(74, 28));
		okbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				success = true;
				myframe.dispose();
			}
		});		
		buttonpanel.add(okbutton);

		JButton cancelbutton = new JButton("Cancel");
		cancelbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				success = false;
				myframe.dispose();
			}
		});
		cancelbutton.setPreferredSize(new Dimension(74, 28));		
		buttonpanel.add(cancelbutton);
		
		add(buttonpanel, BorderLayout.SOUTH);
				
		myframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				success = false;
				myframe.dispose();
			}
		});
		
		myframe.setPreferredSize(new Dimension(500, 200));
		myframe.pack();
		myframe.setVisible(true);	
	}
	
	boolean success = false;
}
