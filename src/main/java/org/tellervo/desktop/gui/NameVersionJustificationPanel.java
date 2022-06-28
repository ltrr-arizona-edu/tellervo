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
package org.tellervo.desktop.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.MessageFormat;
import java.util.EnumSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.tridasv2.LabCode;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;
import org.tellervo.desktop.tridasv2.support.VersionUtil;
import org.tellervo.desktop.ui.I18n;
import org.tridas.interfaces.ITridasDerivedSeries;


public class NameVersionJustificationPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/** The name of the series */
	private JTextComponent seriesName;
	/** The version to be applied to the new series */
	private JTextComponent versionName;
	/** The justification */
	private JTextComponent justification;

	/**
	 * A the set of fields we have
	 */
	public enum Fields {
		NAME, VERSION, JUSTIFICATION;
	}
	
	/**
	 * Create a new panel for the given sample, without a justification box
	 * @param sample
	 * @wbp.parser.constructor
	 */
	public NameVersionJustificationPanel(Sample sample) {
		this(sample, false, false);
	}
	
	/**
	 * Create a new panel for the given sample
	 * 
	 * @param sample
	 * @paran nameIsEditable true if we can change the name (sums), false otherwise
	 * @param showJustification true if we have a justification box
	 */
	public NameVersionJustificationPanel(Sample sample, boolean nameIsEditable,
			boolean showJustification) {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2, 2, 5, 5));

		// Create name components
		JLabel l = new JLabel(I18n.getText("general.seriesCode")+":");

		JLabel prefix = new JLabel(App.getLabCodePrefix()+"XXX-X-X-X-");

		// make the prefix more relevant if we have a labcode
		if (sample.hasMeta(Metadata.LABCODE)) {
			prefix.setText(LabCodeFormatter.getSeriesPrefixFormatter().format(
					sample.getMeta(Metadata.LABCODE, LabCode.class))
					+ "- ");
		}

		JTextField name = new JTextField(sample.getSeries().getTitle());
		name.setColumns(10);
		seriesName = name;
		prefix.setLabelFor(seriesName);

		// set the name to not editable if we can't change it
		seriesName.setEditable(nameIsEditable);
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		titlePanel.add(prefix, BorderLayout.WEST);
		titlePanel.add(seriesName, BorderLayout.CENTER);

		// Create version components
		JLabel l2 = new JLabel(I18n.getText("general.version")+":");
		JTextField version = new JTextField("");
		version.setColumns(20);
		versionName = version;
		l.setLabelFor(versionName);

		// set default version
		if(sample.getSeries() instanceof ITridasDerivedSeries) {
			String parentVersion = ((ITridasDerivedSeries) sample.getSeries()).getVersion();
			
			version.setText(VersionUtil.nextVersion(parentVersion));
		}
		else {
			// default to v. 2
			version.setText("2");
		}
		
		// Add items to panel
		p.add(l);
		p.add(titlePanel);
		p.add(l2);
		p.add(versionName);

		// kludge to make this panel resize nicely
		setLayout(new BorderLayout());
		add(p, BorderLayout.NORTH);
		
		if(showJustification)
			add(getJustificationPanel(), BorderLayout.CENTER);
	}
	
	private JPanel getJustificationPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		
		JLabel l = new JLabel(I18n.getText("general.justification")+":");
		JTextArea text = new JTextArea("", 2, 0);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);

		justification = text;
		
		JPanel topLabel = new JPanel();
		topLabel.setLayout(new BoxLayout(topLabel, BoxLayout.Y_AXIS));
		topLabel.add(l);
		topLabel.add(Box.createVerticalGlue());
		
		p.add(topLabel);
		p.add(Box.createHorizontalStrut(8));
		p.add(new JScrollPane(text));

		p.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
		return p;
	}
	
	/**
	 * @return true if the series name box has characters in it
	 */
	public boolean hasSeriesName() {
		return seriesName.getText().length() > 0;
	}

	/**
	 * @return true if the series version box has characters in it
	 */
	public boolean hasVersion() {
		return versionName.getText().length() > 0;
	}
	
	/**
	 * @return true if a justificaiton exists and has characters in it
	 */
	public boolean hasJustification() {
		return justification != null && justification.getText() != null && justification.getText().length() > 0;
	}

	/**
	 * Get the name of the series
	 * @return The name of the series
	 */
	public String getSeriesName() {
		return seriesName.getText();
	}
	
	/**
	 * @return The version of the series
	 */
	public String getVersion() {
		return versionName.getText();
	}
	
	/**
	 * @return the justification
	 */
	public String getJustification() {
		return justification.getText();
	}

	/**
	 * Check if the fields exist; show a dialog and focus if they don't.
	 * 
	 * @param valuesRequired
	 * @return true if the values are satisfied, false otherwise
	 */
	public boolean testAndComplainRequired(EnumSet<Fields> valuesRequired) {
		if(valuesRequired.contains(Fields.NAME) && !hasSeriesName()) {
			complain(I18n.getText("general.seriesCode"));
			seriesName.requestFocusInWindow();
			return false;
		}

		if(valuesRequired.contains(Fields.VERSION) && !hasVersion()) {
			complain(I18n.getText("general.version"));
			versionName.requestFocusInWindow();
			return false;
		}

		if(valuesRequired.contains(Fields.JUSTIFICATION) && !hasJustification()) {
			complain(I18n.getText("general.justification"));
			justification.requestFocusInWindow();
			return false;
		}

		return true;
	}
	
	private void complain(String name) {
		
        String error = MessageFormat.format(I18n.getText("error.fieldMissing"), new Object[] { name.toLowerCase() });
		
		JOptionPane.showMessageDialog(this, error, 
				I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
	}
}
