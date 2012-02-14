/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
/**
 * 
 */
package org.tellervo.desktop.tridasv2.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.l2fprod.common.propertysheet.PropertySheetTable;
import com.l2fprod.common.propertysheet.PropertySheetTableModel.Item;

public class CorinaPropertySheetTable extends PropertySheetTable {
	private static final long serialVersionUID = 1L;

	private boolean isEditable;
	
	/** If true, we put some text over the top that says 'PREVIEW' or similar */
	private boolean hasWatermark;
	
	/** The text we want to show in the watermark */
	private String watermarkText;
	
	public CorinaPropertySheetTable() {
		isEditable = true;
		hasWatermark = false;
		setColumnWidths();
	}
	
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
		
		if(!isEditable)
			cancelEditing();
		
		repaint();
	}
	
	public boolean hasWatermark() {
		return hasWatermark;
	}
	
	public void setHasWatermark(boolean b) {
		if(this.hasWatermark == b)
			return;
		
		this.hasWatermark = b;
		repaint();
	}
	
	public void setWatermarkText(String text) {		
		this.watermarkText = text;
		
		if(hasWatermark)
			repaint();
	}
	
	public boolean isEditable() {
		return isEditable;
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		if(!isEditable)
			return false;
		
		return super.isCellEditable(row, column);
	}

	private final Rectangle2D getTextRect(Graphics2D g2, Point2D center, String text) {
		FontRenderContext renderCtx = g2.getFontRenderContext();
		LineMetrics metrics = g2.getFont().getLineMetrics(text, renderCtx);
		double width = g2.getFont().getStringBounds(text, renderCtx).getWidth();
		
		return new Rectangle2D.Double(center.getX() - (width/2f), center.getY() - metrics.getAscent(), 
				width, metrics.getAscent() + metrics.getDescent());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// mark it as a preview...
		if(hasWatermark) {
			Graphics2D g2 = (Graphics2D) g;
			Composite oldComposite = g2.getComposite();
			int h = getHeight();
			int w = getWidth();
						
			// overlay the top of the whole thing with gray
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
			g2.setColor(Color.DARK_GRAY);
			g2.fillRect(0, 0, w, h);
			
			// now, draw the word "PREVIEW" over it all
			if(watermarkText != null && watermarkText.length() > 0) {
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
				String preview = watermarkText;
			
				Font font = new Font("Arial", Font.BOLD, 72);
				g2.setFont(font);
			
				Rectangle2D rect = getTextRect(g2, new Point(w/2, h/2), preview);
				float x = (float) rect.getX();
				float y = h/2f;

				// rotate it by 45 degrees
				g2.rotate(Math.PI/4, w/2d, h/2d);
				g2.drawString(preview, x, y);
			}
			
			// clean up and be nice
			g2.setComposite(oldComposite);
		}
	}
	
	private void setColumnWidths()
	{
		TableColumn column = null;
		for (int i = 0; i < this.getColumnCount(); i++) {
		    column = this.getColumnModel().getColumn(i);
		    if (i == 0) {
		        column.setMinWidth(170);
		        column.setMaxWidth(270);
		        column.setPreferredWidth(170);
		    } 
		    else
		    {
		    	column.setMinWidth(200);
		    	column.setMaxWidth(99999);
		    }
		}

	}
	
	public void expandAllBranches(Boolean b)
	{
		TableModel model = this.getSheetModel();
		
		for (int i = 0; i < model.getRowCount(); i++)
		{
			Item item = this.getSheetModel().getPropertySheetElement(i);
			if(item.isVisible()!=b)
			{
				item.toggle();
			}
		}
	}
}
