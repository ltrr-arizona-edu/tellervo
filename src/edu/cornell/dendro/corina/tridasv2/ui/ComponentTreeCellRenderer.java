package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Component;
import java.io.IOException;
import java.util.EnumMap;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.tridas.interfaces.ITridasDerivedSeries;

import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.ui.Builder;

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
				
				StringBuilder builder = new StringBuilder();
				
				builder.append(s.getDisplayTitle());
				if(s.getSampleType().isDerived()) {
					ITridasDerivedSeries series = (ITridasDerivedSeries) s.getSeries();
					
					if(series.isSetVersion()) {
						builder.append(" ver. ");
						builder.append(series.getVersion());
					}
				}
				builder.append(" ");
				builder.append(s.getRange().toStringWithSpan());
				
				setText(builder.toString());
			} catch (IOException e) {
			}			
		}
		
		return this;
	}
}
