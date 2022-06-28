/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class Axis extends JPanel {
	private final static Logger log = LoggerFactory.getLogger(Axis.class);

	private GraphSettings gInfo;
	private int axisType;
	private JPanel parent;
	
	public static final int AXIS_STANDARD = 1; // counts up from zero
	public static final int AXIS_LOG = 3; // has percentages...
	
	public static final int AXIS_WIDTH = 65;

	public Axis(GraphSettings gInfo, int type, JPanel parent) {
		// background -- default is black
		this.gInfo = gInfo;
		this.parent = parent;
		setBackground(gInfo.getBackgroundColor());

		axisType = type;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(AXIS_WIDTH, parent.getSize().height);
	}
	
	public void setAxisType(int axisType) {
		this.axisType = axisType;
		
		repaint();
	}

	public void drawVertAxis(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int hundredunitSize = gInfo.getHundredUnitHeight();
		//log.debug("Hundred unit height: "+ hundredunitSize);
		int w = getWidth();
		int lastLabelTop = Integer.MAX_VALUE;

		// draw vertical axis
		int bottom = getHeight() - GrapherPanel.AXIS_HEIGHT;
		g2.setColor(gInfo.getForeColor());
		g2.drawLine(w - 1, 0, w - 1, bottom);

		
		// draw ticks
		switch(axisType) {
			case AXIS_STANDARD: {	
				
				int i = 1;
				int y = bottom - i * hundredunitSize;
				while (y > 0) {
					
					// draw tick
					g2.drawLine(w - 1, y, w - 1 - (i % 5 == 0 ? 10 : 5), y);

					// draw number -- every 50
					if (i % 5 == 0) {
												
						int lsz = g2.getFontMetrics().getMaxAscent();
						
						if(lastLabelTop - (y+5) >= (lsz * 1.10)) {
						
							String value = String.valueOf(i * 100);				
							g2.drawString(value, 
									w - (g2.getFontMetrics().stringWidth(value) + 15), 
									y + 5);	
							
							lastLabelTop = (y+5) - lsz;
						}
					}
				
					// update coordinates
					i++;
					y = bottom - i * hundredunitSize;
				}
				break;
			}
			case AXIS_LOG: {
				int i = 1;
				int y = bottom - i * hundredunitSize;
				while (y > 0) {
					// draw tick
					g2.drawLine(w - 1, y, w - 1 - (i % 10 == 0 ? 10 : 5), y);

					// draw number -- every "100"
					if (i % 10 == 0) {
						String value = String.valueOf((int)Math.pow(10.0, i / 10.0));
						g2.drawString(value, 
								w - (g2.getFontMetrics().stringWidth(value) + 15), 
								y + 5);
					}
					// update coordinates
					i++;
					y = bottom - i * hundredunitSize;
				}
				break;
			}
		}
/*			
			case AXIS_LOG: {
				String value = String.valueOf(i * 10);				
				g2.drawString(value, 
						w - (g2.getFontMetrics().stringWidth(value) + 15), 
						y + 5);
				
				// update coordinates
				i++;
				y = bottom - i * tenunitSize;
				
				break;
			}
				
			case AXIS_PERCENT: {
				if(i % 3 == 2) {
					String value = "0%";				
					g2.drawString(value, 
							w - (g2.getFontMetrics().stringWidth(value) + 15), 
							y + 5);
				} /*else if(i % 3 == 0) {
					String value = "-100%";
					int yv = g2.getFontMetrics().getHeight();
					int tv = g2.getFontMetrics().stringWidth(value);
					g2.drawString(value, 
							w - (tv + 15), 
							y - yv);
					g2.drawLine(w - 12, y - yv, w - 4, y);
				} else if(i % 3 == 2) {
					String value = "+100%";
					int yv = g2.getFontMetrics().getHeight();
					int tv = g2.getFontMetrics().stringWidth(value);
					g2.drawString(value, 
							w - (tv + 15), 
							y + yv);
					g2.drawLine(w - 12, y + yv, w - 4, y);
				}*/
		/*
				break;
			}
			
			}
		}		
	*/
	}
	
	// this doesn't get drawn very often: maybe half a dozen times,
	// max, even with resizing and scrolling.  so don't worry about
	// efficiency here.
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		drawVertAxis(g);

	}

}
