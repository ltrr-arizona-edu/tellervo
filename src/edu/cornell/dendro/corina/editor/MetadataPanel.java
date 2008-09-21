//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//


package edu.cornell.dendro.corina.editor;

import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;
import edu.cornell.dendro.corina.sample.SampleSummary;
import edu.cornell.dendro.corina.site.GenericIntermediateObject;
import edu.cornell.dendro.corina.site.Radius;
import edu.cornell.dendro.corina.site.Specimen;
import edu.cornell.dendro.corina.site.Tree;
import edu.cornell.dendro.corina.util.Center;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.*;

/**
   A view of the metadata of a Sample, which lets the user edit it.

    <h2>Left to do:</h2>
    <ul>

	<li>use DialogLayout, instead of weird gridbaglayout that
	nobody understands.

        <li>Bug: comments field is sometimes zero-width (solved by
        dialog layout?) (still reproducible?)

	<li>don't bug the user to fix a bad value; simply leave the
	bad value there, until he tries to change it, and then only
	allow it to be changed to a valid value.

    </ul>

<pre>
fixme[dialoglayout]: change dialog-layout into generic "two-column layout".
       i'll have to make my own labels, but the increased flexibility should
       be worth it.
fixme[dialog]layout): shouldn't be any extra spacing to left of labels
fixme[dialog-layout?]: comments field should be as wide as possible

     -- Scroll when the user tabs out of view; scroll if the user
     types into a text component that's not in view

     -- Possible to use only one DocumentListener that works for all
     fields?  Probably not worth the effort (they share code, now,
     anyway).

     -- Make any changes here Undoable.  This will probably involve
     creating an inner class for "FieldEdit" ("MetadataEdit"?), which
     implements Undoable.  The fun part will be to coalesce multiple
     FieldEdits together -- if I type "Quercus", and then select Undo,
     it should un-type "Quercus", not just the "s".

 -- Figure out how to use a popup for the species?  (!)

FIXME: extend JPanel, and have a scroll pane -- don't BE the scroll pane (why?).

note: only the invoker of an edit should post the undo.  for example,
indexing itself should post the undo, and the user changing the format
should post an undo, but when indexing changes the format, that
shouldn't post an undo.  (OR: maybe i should make it so calling a
method to change something doesn't post an undo here -- but how would
that work?)

 </pre>

<pre>
fields are:
-- one-of (a,b,c)
   -- one-of, plus bad value (a,b,c,x) (WRITEME)
-- simple field ([___])
   -- number field? ([123])
-- read-only field ("cubic spline")
-- multi-line field (comments)
</pre>

    </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id: MetadataPanel.java 1073 2008-04-27 08:34:27Z lucasm $
*/
public class MetadataPanel extends JScrollPane implements SampleListener {

	// the sample to view
	private Sample s;

	// maps of our components
	private HashMap<String, JComponent> gioComponentMap;
	private HashMap<String, JComponent> metaComponentMap;
	private HashMap<String, JComponent> summaryComponentMap;

	// our panel
	private JPanel panel;
	/**
	 Construct a new view of the metadata for a sample.

	 @param sample the Sample
	 */
	public MetadataPanel(Sample sample) {
		// copy data
		this.s = sample;
		
		// listen for changes
		sample.addSampleListener(this);

		// no border!
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		panel = new JPanel(new GridBagLayout());
		
		setViewportView(panel);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = c.gridy = 0;
		
		addSummary(c);
		finishPart(c);

		addMetadata(c);
		finishPart(c);
	
		// now, for the "fun" parts!
		gioComponentMap = new HashMap<String, JComponent>();
		addRadius(c);
		addSpecimen(c);
		addTree(c);
		updateGioComponents();
		
		setScrollyGoodness();
	}
	
	private void addRadius(GridBagConstraints c) {
		Radius r = (Radius) s.getMeta("::radius");
		
		if(r == null)
			return;

		JButton edit = new JButton("edit");
		bindEditButton(edit, "::radius");
		addTitle("Radius", c, edit);
		
		JLabel a, b;
		
		a = new JLabel("Name:");
		b = new JLabel();
		gioComponentMap.put("radius.toString", b);
		addPair(a, b, c);
		
		a = new JLabel("db id:");
		b = new JLabel();
		gioComponentMap.put("radius.getID", b);
		addPair(a, b, c);
	}
	
	private void addSpecimen(GridBagConstraints c) {
		Specimen sp = (Specimen) s.getMeta("::specimen");
		
		if(sp == null)
			return;

		JButton edit = new JButton("edit");
		bindEditButton(edit, "::specimen");
		addTitle("Specimen", c, edit);
		
		JLabel a, b;
		
		a = new JLabel("Name:");
		b = new JLabel();
		gioComponentMap.put("specimen.toString", b);
		addPair(a, b, c);
		
		a = new JLabel("DB ID:");
		b = new JLabel();
		gioComponentMap.put("specimen.getID", b);
		addPair(a, b, c);
		
		a = new JLabel("Type:");
		b = new JLabel();
		gioComponentMap.put("specimen.getSpecimenType", b);
		addPair(a, b, c);

		a = new JLabel("Date Collected:");
		b = new JLabel();
		gioComponentMap.put("specimen.getDateCollected", b);
		addPair(a, b, c);
		
		a = new JLabel("Continuity:");
		b = new JLabel();
		gioComponentMap.put("specimen.getSpecimenContinuity", b);
		addPair(a, b, c);

		a = new JLabel("Cont. verified:");
		b = new JLabel();
		gioComponentMap.put("specimen.getIsSpecimenContinuityVerified", b);
		addPair(a, b, c);

		a = new JLabel("Terminal Ring:");
		b = new JLabel();
		gioComponentMap.put("specimen.getTerminalRing", b);
		addPair(a, b, c);
		
		a = new JLabel("T.R. verified:");
		b = new JLabel();
		gioComponentMap.put("specimen.getIsTerminalRingVerified", b);
		addPair(a, b, c);
		
		a = new JLabel("Pith:");
		b = new JLabel();
		gioComponentMap.put("specimen.getPith", b);
		addPair(a, b, c);
		
		a = new JLabel("Pith verified:");
		b = new JLabel();
		gioComponentMap.put("specimen.getIsPithVerified", b);
		addPair(a, b, c);
		
		a = new JLabel("Sapwood Count:");
		b = new JLabel();
		gioComponentMap.put("specimen.getSapwoodCount", b);
		addPair(a, b, c);
		
		a = new JLabel("Sapwood verified:");
		b = new JLabel();
		gioComponentMap.put("specimen.getIsSapwoodCountVerified", b);
		addPair(a, b, c);
		
		a = new JLabel("Unmeasured pre:");
		b = new JLabel();
		gioComponentMap.put("specimen.getUnmeasuredPre", b);
		addPair(a, b, c);
		a = new JLabel("U. Pre verified:");
		b = new JLabel();
		gioComponentMap.put("specimen.getIsUnmeasuredPreVerified", b);
		addPair(a, b, c);

		a = new JLabel("Unmeasured post:");
		b = new JLabel();
		gioComponentMap.put("specimen.getUnmeasuredPost", b);
		addPair(a, b, c);
		a = new JLabel("U. Post verified:");
		b = new JLabel();
		gioComponentMap.put("specimen.getIsUnmeasuredPostVerified", b);
		addPair(a, b, c);
	}

	private void addTree(GridBagConstraints c) {
		Tree t = (Tree) s.getMeta("::tree");
		
		if(t == null)
			return;		

		JButton edit = new JButton("edit");
		bindEditButton(edit, "::tree");
		addTitle("Tree", c, edit);

		JLabel a, b;
		
		a = new JLabel("Latitude:");
		b = new JLabel();
		gioComponentMap.put("tree.getLatitude", b);
		addPair(a, b, c);

		a = new JLabel("Longitude:");
		b = new JLabel();
		gioComponentMap.put("tree.getLongitude", b);
		addPair(a, b, c);

		a = new JLabel("Precision:");
		b = new JLabel();
		gioComponentMap.put("tree.getPrecision", b);
		addPair(a, b, c);

		a = new JLabel("Is alive:");
		b = new JLabel();
		gioComponentMap.put("tree.getIsLiveTree", b);
		addPair(a, b, c);

		a = new JLabel("Validated taxon:");
		b = new JLabel();
		gioComponentMap.put("tree.getValidatedTaxon", b);
		addPair(a, b, c);

		a = new JLabel("Orig. taxon:");
		b = new JLabel();
		gioComponentMap.put("tree.getOriginalTaxonName", b);
		addPair(a, b, c);
	}

	private void addSummary(GridBagConstraints c) {
		SampleSummary ss = (SampleSummary) s.getMeta("::summary");

		summaryComponentMap = new HashMap<String, JComponent>();
		
		if(ss == null)
			return;
		
		addTitle("Measurement Summary", c, null);
		
		JLabel a, b;
		
		a = new JLabel("Lab code:");
		b = new JLabel();
		summaryComponentMap.put("labcode", b);
		addPair(a, b, c);

		a = new JLabel("Site code:");
		b = new JLabel();
		summaryComponentMap.put("sitecode", b);
		addPair(a, b, c);

		a = new JLabel("Common taxon:");
		b = new JLabel();
		summaryComponentMap.put("commontaxon", b);
		addPair(a, b, c);
		
		a = new JLabel("Site description:");
		b = new JLabel();
		summaryComponentMap.put("sitedescription", b);
		addPair(a, b, c);

		a = new JLabel("Taxon description:");
		b = new JLabel();
		summaryComponentMap.put("taxondescription", b);
		addPair(a, b, c);

		a = new JLabel("Type:");
		b = new JLabel();
		summaryComponentMap.put("sampletype", b);
		addPair(a, b, c);
		
		updateSummaryComponents();
	}
	
	private void addMetadata(GridBagConstraints c) {
		JButton edit = new JButton("edit");
		bindEditButton(edit, null);
		addTitle("Measurement Metadata", c, edit);

		metaComponentMap = new HashMap<String, JComponent>();
		
		Map<String, Object> meta = s.getMetadata();
		Set<String> keys = meta.keySet();
		
		// pass 1: things that are nice
		for(String key : keys) {
			if(key.startsWith("::"))
				continue;
		
			// special case
			if(key.equalsIgnoreCase("comments"))
				continue;
			
			// create
			JLabel a = new JLabel(getMetaName(key) + ":");
			JComponent b = createComponentForObject(meta.get(key));
			
			// add a key to the metamap
			metaComponentMap.put(key, b);
		
			// add the thing to the table
			addPair(a, b, c);
		}

		// pass 2: things that are unknown!
		for(String key : keys) {
			if(!key.startsWith("::-::"))
				continue;
			
			JLabel a = new JLabel(key.substring(5) + "(?):");
			JComponent b = createComponentForObject(meta.get(key));

			// add a key to the metamap
			metaComponentMap.put(key, b);

			addPair(a, b, c);
		}

		// keep this for good measure
		if(meta.containsKey("::dbid"))
			addPair(new JLabel("db id:"), new JLabel(meta.get("::dbid").toString()), c);
		
		String comments = (String) meta.get("comments");
		if(comments != null) {
			// on its own line
			finishPart(c);
			
			JLabel a = new JLabel("Comments:");
			
			// create it and add it to the map!
			metaComponentMap.put("comments", addComments(a, comments, c));
		}
		
		// redundant, but helpful
		updateMetadataComponents();
	}
	
	private JComponent createComponentForObject(Object o) {
		// special handling for booleans!
		/* no!
		if(o instanceof Boolean) {
			JCheckBox box = new JCheckBox();
			
			box.setSelected(((Boolean)o).booleanValue());
			box.setEnabled(false);
			
			return box;
		}*/
		
		JLabel label = new JLabel(o.toString());
		
		return label;
	}

	private void addPair(JLabel label, JComponent comp, GridBagConstraints c) {
		label.setLabelFor(comp);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(0, 8, 4, 8);

		panel.add(label, c);
		c.gridx++;
		c.anchor = GridBagConstraints.WEST;
		panel.add(comp, c);
		c.gridx++;
		
		// end of the line!
		if(c.gridx >= 3) {
			c.gridx = 0;
			c.gridy++;
		}
	}

	private JComponent addComments(JLabel label, String comments, GridBagConstraints c) {
		JEditorPane comp = new JEditorPane();
		
		comp.setFocusable(false);
		comp.setEditable(false);
		comp.setOpaque(false);
		comp.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.gray));
		comp.setText(comments + comments + comments + comments);
		
		label.setLabelFor(comp);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setAlignmentY(JLabel.TOP_ALIGNMENT);
		
		c.gridwidth = 1;
		c.weightx = 1.0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(0, 8, 0, 8);
		
		panel.add(label, c);
		c.gridx++;
		
		c.gridwidth = 3;
		c.weightx = c.weighty = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		panel.add(comp, c);
		c.gridx++;
		
		c.gridx = 0;
		c.gridy++;
		
		return comp;
	}

	
	private void finishPart(GridBagConstraints c) {
		if(c.gridx != 0) {
			c.gridx = 0;
			c.gridy++;
		}
	}
	
	// number of sections added so far
	private int nsections = 0;
	private void addTitle(String title, GridBagConstraints c, JButton editButton) {
		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBorder(BorderFactory.createMatteBorder(2, 3, 1, 0, Color.gray));

		JLabel l = new JLabel(title);
		l.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		l.setHorizontalAlignment(SwingConstants.LEFT);
		
		titlePanel.add(l, BorderLayout.WEST);
		titlePanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		titlePanel.setAlignmentY(JPanel.TOP_ALIGNMENT);
		
		// position the button
		if(editButton != null) {
			editButton.setHorizontalAlignment(SwingConstants.RIGHT);
			editButton.setMargin(new Insets(0, 2, 0, 2)); // small button
			editButton.setDefaultCapable(false);
			JPanel dummy = new JPanel();
			dummy.add(editButton);
			titlePanel.add(dummy, BorderLayout.EAST);
		}
		
		c.gridwidth = 4;
		c.gridx = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets((nsections == 0) ? 0 : 10, 0, 10, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 2.0;
		c.weighty = 0.0;
		
		panel.add(titlePanel, c);
		
		c.gridwidth = 1;
		c.gridx = 0;
		c.weightx = 0;
		c.gridy++;
		nsections++;
	}

	private void bindEditButton(JButton button, final String metaKey) {
		final Container parent = this;
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// find our JFrame
				Container c = parent;
				while(!(c instanceof Frame) && !(c == null))
					c = c.getParent();				

				IntermediateEditorDialog ied = new IntermediateEditorDialog((Frame) c, s, metaKey);
				ied.setTitle("Modify Existing Metadata");
				
				Center.center(ied, (Frame) c);
				
				ied.setVisible(true);
			}
		});
	}
	
	private void updateSummaryComponents() {
		SampleSummary ss = (SampleSummary) s.getMeta("::summary");
		
		// error?
		if(ss == null) {
			for(JComponent comp : summaryComponentMap.values()) {
				comp.setBackground(Color.red);
				comp.setOpaque(true);
			}
			return;
		}
		
		JLabel l;
		
		if((l = (JLabel) summaryComponentMap.get("labcode")) != null)
			l.setText(ss.getLabCode());
		if((l = (JLabel) summaryComponentMap.get("sitecode")) != null)
			l.setText(ss.getSiteCode());
		if((l = (JLabel) summaryComponentMap.get("commontaxon")) != null)
			l.setText(ss.getCommonTaxon());
		if((l = (JLabel) summaryComponentMap.get("sitedescription")) != null)
			l.setText(ss.siteDescription());
		if((l = (JLabel) summaryComponentMap.get("taxondescription")) != null)
			l.setText(ss.taxonDescription());
		if((l = (JLabel) summaryComponentMap.get("sampletype")) != null)
			l.setText(s.getSampleType().toString());
	}
	
	// things that actually set values...
	private void updateMetadataComponents() {
		for(String key : metaComponentMap.keySet()) {
			JComponent comp = metaComponentMap.get(key);
			
			if(!s.hasMeta(key)) {
				// this shouldn't happen, but mark it if it does
				comp.setBackground(Color.red);
				comp.setOpaque(true);
				continue;
			}
			
			if(comp instanceof JLabel) {
				((JLabel) comp).setText(s.getMeta(key).toString());
			}
			else if(comp instanceof JEditorPane) {
				((JEditorPane) comp).setText(s.getMeta(key).toString());
			}
			else {
				// mark errors
				comp.setBackground(Color.orange);
				comp.setOpaque(true);
			}
		}
	}
	
	private void updateGioComponents() {
		for(String key : gioComponentMap.keySet()) {
			// split the key up
			// e.g., radius.getID -> radius, getID
			String[] x = key.split("\\.");
			JComponent comp = gioComponentMap.get(key);
			
			// sanity
			if(x.length != 2) {
				comp.setBackground(Color.orange);
				comp.setOpaque(true);
				continue;
			}
			
			// find the object in the sample
			GenericIntermediateObject src = (GenericIntermediateObject) s.getMeta("::" + x[0]);
			if(src == null) {
				comp.setBackground(Color.pink);				
				comp.setOpaque(true);
				continue;
			}
			
			// now, invoke the method specified in the key
			try {
				Class<?> types[] = new Class[] {};
				Method m = src.getClass().getMethod(x[1], types);
				Object args[] = new Object[] { };

				Object value = m.invoke(src, args);
				if(value != null) {
					if(comp instanceof JLabel) {
						((JLabel) comp).setText(value.toString());
						comp.setForeground(UIManager.getDefaults().getColor("Label.foreground"));
					}
				}
				else {
					if(comp instanceof JLabel) {
						((JLabel) comp).setText("not specified");
						comp.setForeground(UIManager.getDefaults().getColor("Label.disabledForeground"));
					}
				}
				
			} catch (Exception ex) {
				comp.setBackground(Color.cyan);
				ex.printStackTrace();
			}
		}
	}

	private void setScrollyGoodness()
	{
		// set some sane unit increments (one line per click), but this can only be done after it's packed
		// FIXME: shouldn't this be an addNotify(), then?
		final JScrollPane glue = this;
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				GridBagLayout gbl = (GridBagLayout) panel.getLayout();
				int h = gbl.getLayoutDimensions()[1][0]; // (1=height, 0=first row)  "Most applications do not call this method directly."  (I'm special.)
				glue.getVerticalScrollBar().setUnitIncrement(h);
				glue.getHorizontalScrollBar().setUnitIncrement(h);
				// todo: i can remove myself now, right?  [--wtf does this comment mean?] [ it means this componentlistener is only used once.]
			}
		});
	}

	public void sampleRedated(SampleEvent e) {
	}

	public void sampleDataChanged(SampleEvent e) {
	}

	public void sampleMetadataChanged(SampleEvent e) {
		// update all the fun stuff in our metadata
		updateMetadataComponents();
		updateSummaryComponents();
		updateGioComponents();
	}
	
	public void sampleElementsChanged(SampleEvent e) {
	}

	/**
	 * Get a nice name for a metadata key
	 * 
	 * @param key
	 * @return
	 */
	private static String getMetaName(String key) {
		String ret = niceMetaName.get(key);
		
		return (ret == null) ? key : ret;
	}
	
	private static HashMap<String, String> niceMetaName;
	static {
		niceMetaName = new HashMap<String, String>();
		
		// stick any key -> etc niceness here
		niceMetaName.put("isreconciled", "Is reconciled");
	}
	
}
