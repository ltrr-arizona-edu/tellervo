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
			c.setBackground(index % 2 == 0 ? ElementListCellRenderer.BROWSER_ODD_ROW_COLOR
					: Color.white);	

		if(value instanceof TridasObjectEx) {
			TridasObjectEx site = (TridasObjectEx) value;
			
			String name = site.getTitle();
			
			if(maximumTitleLength > 0 && name.length() > maximumTitleLength)
				name = name.substring(0, maximumTitleLength) + "..."; 
			
			lblCode.setText(site.getLabCode());
			lblName.setText(name);
			
			Font font = list.getFont();
			lblCode.setFont(font);
			lblName.setFont(font.deriveFont(font.getSize() - 2.0f));			
		
			StringBuffer sb = new StringBuffer();
			sb.append("<html>");
			if(site.hasLabCode()) {
				sb.append("<b>Code:</b> ");
				sb.append(site.getLabCode());
				sb.append("<br>");
			}
			sb.append("<b>Title:</b> ");
			sb.append(site.getTitle());
			sb.append("<br>");
			
			// add parent info, if we can...
			TridasObjectEx parent;
			if((parent = site.getParent()) != null) {
				if(parent.hasLabCode()) {
					sb.append("<b>Parent Code:</b> ");
					sb.append(parent.getLabCode());
					sb.append("<br>");
				}
				sb.append("<b>Parent Title:</b> ");
				sb.append(parent.getTitle());
				sb.append("<br>");				
			}

			// add series count if we can...
			Integer seriesCount = site.getSeriesCount();
			if(seriesCount != null) {
				// it's populated, so make it bold
				lblCode.setFont(font.deriveFont(Font.BOLD));
				
				// and then add this info...
				sb.append("<b>Number of series:</b> ");
				sb.append(seriesCount);
				if(site.getChildSeriesCount() != seriesCount) {
					sb.append(" (of ");
					sb.append(site.getChildSeriesCount());
					sb.append(")");
				}
				sb.append("<br>");
			}
			
			panel.setToolTipText(sb.toString());
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
