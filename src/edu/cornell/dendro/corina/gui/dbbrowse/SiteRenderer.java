package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import edu.cornell.dendro.corina.tridasv2.TridasObjectEx;

/**
 * A quick and dirty class to render stars in a combo box
 */
public class SiteRenderer implements ListCellRenderer {
	public SiteRenderer() {
		panel = new JPanel();
		lblCode = new JLabel("code");
		lblName = new JLabel("name");

		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		
		panel.add(lblCode);
		panel.add(lblName);	
	}
	
	private JPanel panel;
	private JLabel lblCode;
	private JLabel lblName;
	private int maximumTitleLength = -1;
	
	public void setMaximumTitleLength(int maximumTitleLength) {
		this.maximumTitleLength = maximumTitleLength;
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		JPanel c = panel;
		
		if(isSelected)
			c.setBackground(list.getSelectionBackground());
		else
			c.setBackground(index % 2 == 0 ? DBBrowserCellRenderer.BROWSER_ODD_ROW_COLOR
					: Color.white);	

		if(value instanceof TridasObjectEx) {
			TridasObjectEx site = (TridasObjectEx) value;
			
			String name = site.getTitle();
			
			if(maximumTitleLength > 0 && name.length() > maximumTitleLength)
				name = name.substring(0, maximumTitleLength) + "..."; 
			
			lblCode.setText(site.getLabCode());
			lblName.setText(name);
			
			Font font = list.getFont();
			lblCode.setFont(font.deriveFont(Font.BOLD));
			lblName.setFont(font.deriveFont(font.getSize() - 2.0f));			
		
			Integer seriesCount = site.getSeriesCount();
			if(seriesCount != null) {
				String countStr = seriesCount + "/" + site.getChildSeriesCount();
				//Font countFont = font.deriveFont(font.getSize() - 1.0f);

				lblCode.setText(lblCode.getText() + "   " + countStr);
			}
		} else if(value instanceof String) {
			lblCode.setText((String)value);
			lblName.setText((String)value);
			
			// yellow background
			if(!isSelected)
				c.setBackground(new Color(255, 255, 200));
			
			Font font = list.getFont();
			lblCode.setFont(font.deriveFont(Font.BOLD));
			lblName.setFont(font.deriveFont(font.getSize() - 2.0f));			
		}
		
		return c;
	}
}
