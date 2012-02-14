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
package org.tellervo.desktop.editor;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.tellervo.desktop.Year;
import org.tellervo.desktop.remarks.Remarks;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.NormalTridasRemark;
import org.tridas.schema.TridasRemark;


public class IconBackgroundCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	private Sample mySample;

	/** The list of icons to draw */
	private List<Icon> icons;
	/** The height of our JTable row */
	private int rowHeight;
	/** A map for the lazy-loading of icons */
	private Map<String, Icon> lazyIconMap;
	
	/**
	 * Construct a new IconBackgroundCellRenderer 
	 * that matches this sample
	 * 
	 * @param s
	 */
	public IconBackgroundCellRenderer(Sample s) {
		super();

		this.mySample = s;
		
		// make an ArrayList that we reuse for a list of icons
		icons = new ArrayList<Icon>();

		// lazily load icons
	    lazyIconMap = new HashMap<String, Icon>();
		
		// right-align numbers
	    setHorizontalAlignment(SwingConstants.RIGHT);
	}

	/**
	 * Lazily-load this icon
	 * 
	 * @param iconName
	 * @return the icon, or null if iconName was null
	 */
	private Icon lazyLoadIcon(String iconName) {
		if(iconName == null)
			return null;
		
		Icon icon = lazyIconMap.get(iconName);
		if(icon == null) {
			// lazy-load the icon
			icon = Builder.getIcon(iconName, 16);
			lazyIconMap.put(iconName, icon);
		}
		
		return icon;		
	}
	
	/**
	 * Get an icon for this tridas remark
	 * @param remark
	 * @return the icon, lazily loaded, or null
	 */
	private Icon getTridasIcon(NormalTridasRemark remark) {
		return lazyLoadIcon(Remarks.getTridasRemarkIcons().get(remark));
	}
	
	/**
	 * Get an icon for this Corina remark (text)
	 * @param remark
	 * @return the icon, lazily loaded, or null
	 */
	private Icon getCorinaIcon(String remark) {
		return lazyLoadIcon(Remarks.getCorinaRemarkIcons().get(remark));
	}
		
	private final static String CORINA = "Corina";

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		// get the year and the remarks list for the year
		Year year = ((DecadalModel)table.getModel()).getYear(row, column);
		List<TridasRemark> remarks = mySample.getRemarksForYear(year);
		
		// populate a set of icons for these remarks
		populateIcons(remarks);

		// save this so we know where to draw the icons
		rowHeight = table.getRowHeight(row);
		
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#isOpaque()
	 */
	@Override
	public boolean isOpaque() {
		// if true, our background is painted for us
		// but we want to make our own background!
		return false;
	}

	/**
	 * Populate the list of icons, given these remarks
	 * @param remarks
	 */
	private void populateIcons(List<TridasRemark> remarks) {
		boolean usedMissing = false;
		icons.clear();
		
		// loop through each remark and get the appropriate icon
		for(TridasRemark remark : remarks) {
			// tridas icon
			if(remark.isSetNormalTridas()) {
				Icon icon = getTridasIcon(remark.getNormalTridas());
				
				if(icon != null)
					icons.add(icon);
				else if(!usedMissing) {
					icons.add(missingIcon);
					usedMissing = true;
				}
			}
			else if(CORINA.equals(remark.getNormalStd())) {
				Icon icon = getCorinaIcon(remark.getNormal());				
				
				if(icon != null)
					icons.add(icon);
				else if(!usedMissing) {
					icons.add(missingIcon);
					usedMissing = true;
				}
			}
			else if(!usedMissing) {
				// we don't know what this is...?
				icons.add(missingIcon);
				usedMissing = true;
			}
		}
	}
	
	/** For showing an icon we don't have */
	private final Icon missingIcon = Builder.getMissingIcon(16);
	
	/** For alpha blending our icons with the background */
	private final AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.66f);
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// paint our background as if we were opaque
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

		// only bother with this stuff if there are icons
		if (icons.size() > 0) {

			// icons.add(getTridasIcon(NormalTridasRemark.FIRE_DAMAGE));
			// icons.add(getTridasIcon(NormalTridasRemark.FROST_DAMAGE));

			// set up alpha blending
			Graphics2D g2 = (Graphics2D) g;
			Composite comp = g2.getComposite();
			g2.setComposite(alpha);

			// paint all icons
			int xoffset = 0;
			for (Icon icon : icons) {
				icon.paintIcon(this, g, xoffset, rowHeight - icon.getIconHeight());
				xoffset += icon.getIconWidth();
			}

			/* DEBUG 
			{
				Font oldFont = g.getFont();
				g.setFont(g.getFont().deriveFont(9.0f));

				g.setColor(Color.white);

				g.drawString("" + row + ":" + col, xoffset + 1, 13);
				g.drawString("" + row + ":" + col, xoffset + 1, 11);
				g.drawString("" + row + ":" + col, xoffset + 3, 13);
				g.drawString("" + row + ":" + col, xoffset + 3, 11);

				g.setColor(Color.red);
				g.drawString("" + row + ":" + col, xoffset + 2, 12);

				g.setFont(oldFont);
			}
			END DEBUG */

			// restore regular painting
			g2.setComposite(comp);
		}

		// now, draw our content on top
		super.paintComponent(g);
	}

}
