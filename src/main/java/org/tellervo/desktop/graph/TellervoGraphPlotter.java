/**
 * 
 */
package org.tellervo.desktop.graph;

import java.awt.Graphics2D;
import java.awt.Point;

/**
 * @author Lucas Madar
 *
 */
public interface TellervoGraphPlotter {
	public void draw(GraphInfo gInfo, Graphics2D g2, int bottom, Graph g, int thickness, int xscroll);
	public boolean contact(GraphInfo gInfo, Graph g, Point p, int bottom);
	public int getYRange(GraphInfo gInfo, Graph g);
	public int getFirstValue(Graph g);
}
