package org.tellervo.desktop.gui.dbbrowse;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class JTableStatusBar extends JPanel {

	private static final long serialVersionUID = 1L;
	private final JTable tbl;
	private JLabel label = new JLabel();
	
	public JTableStatusBar(JTable tbl)
	{
		this.tbl = tbl;
		
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(Box.createHorizontalGlue());
		add(Box.createHorizontalStrut(2));
		add(label);
		add(Box.createHorizontalStrut(2));    		
		
		tbl.getModel().addTableModelListener(new TableModelListener(){

			@Override
			public void tableChanged(TableModelEvent arg0) {
					update();
			}
			
		});
		
		update();
	}
	
	private void update()
	{
		try{
			label.setText("Number of series: "+tbl.getModel().getRowCount());
		} catch (Exception e)
		{
			label.setText("");
		}
	}
	
	
}
