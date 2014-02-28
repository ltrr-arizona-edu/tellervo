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
package org.tellervo.desktop.tridasv2.ui;

import java.awt.Component;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.tellervo.desktop.sample.BaseSample;
import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleType;
import org.tellervo.desktop.ui.Builder;
import org.tridas.interfaces.ITridasDerivedSeries;
import org.tridas.schema.SeriesLink;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasMeasurementSeries;


public class ComponentTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;
	
	private EnumMap<SampleType, Icon> icons;
	private Icon badIcon = Builder.getMissingIcon(16);
	
	public ComponentTreeCellRenderer() {
		icons = new EnumMap<SampleType, Icon>(SampleType.class);
		
		icons.put(SampleType.DIRECT, Builder.getIcon("measurementseries.png", 16));
		icons.put(SampleType.REDATE, Builder.getIcon("redate.png", 16));
		icons.put(SampleType.SUM, Builder.getIcon("sum.png", 16));
		icons.put(SampleType.TRUNCATE, Builder.getIcon("truncate.png", 16));
		icons.put(SampleType.INDEX, Builder.getIcon("index.png", 16));
		icons.put(SampleType.CROSSDATE, Builder.getIcon("crossdate.png", 16));
	}
	
	private Icon getIcon(SampleType type) {
		return (icons.containsKey(type) ? icons.get(type) : badIcon);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		
		if(node.getUserObject() instanceof CachedElement) {
			CachedElement ce = (CachedElement) node.getUserObject();
			
			// err... no thanks!
			if(!ce.hasBasic())
				return this;
			
			try {
				BaseSample s = ce.loadBasic();
				setIcon(getIcon(s.getSampleType()));
			} catch (IOException e) {
			}
			
			if(!ce.hasFull())
				return this;
			
			try {
				Sample s = ce.load();
				
				setText(getFullTitle(s));
						
			
				setToolTipText(getTooltipText(s));
				
			} catch (IOException e) {
			}			
		}
				
		return this;
	}
	
	private String getTooltipText(Sample s)
	{
		StringBuilder builder = new StringBuilder();
		TridasDerivedSeries dSeries = null;
		
		builder.append("<html><b>"+getFullTitle(s, false)+"</b><br>");
		
		if(s.getSeries() instanceof TridasMeasurementSeries)
		{
			builder.append("raw measurement series");
			return builder.toString();
		}
		else if (s.getSeries() instanceof TridasDerivedSeries)
		{
			dSeries = (TridasDerivedSeries) s.getSeries();
				
			//@TODO replace with customised text for each derivedSeries type
			if(dSeries.getType().getValue().toLowerCase().equals("index"))
			{
				builder.append("is a "+ dSeries.getStandardizingMethod().toString());
				builder.append(" "+ dSeries.getType().getValue().toLowerCase()+" of ");
			}
			else
			{
				builder.append("is a "+ dSeries.getType().getValue().toLowerCase()+" of ");
			}
			
		}
				
		List<SeriesLink> ls = dSeries.getLinkSeries().getSeries();
		
		if(ls.size()>1)
		{
			builder.append(ls.size()+" series:");
		}
		else
		{
			//@TODO replace with title of parent series
			builder.append("its parent series");
		}
		
		return builder.toString();
		
	}
	
	private String getFullTitle(Sample s)
	{
		return getFullTitle(s, true);
	}
	
	public static String getFullTitle(Sample s, Boolean includeRange)
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append(s.getDisplayTitle());
		if(s.getSampleType().isDerived()) {
			ITridasDerivedSeries series = (ITridasDerivedSeries) s.getSeries();
			
			if(series.isSetVersion()) {
				builder.append(" ver. ");
				builder.append(series.getVersion());
			}
		}
	
		if(includeRange)
		{
			builder.append(" ");
			builder.append(s.getRange().toStringWithSpan());
		}
		
		return builder.toString();
	}
}
