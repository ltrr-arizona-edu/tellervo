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
package org.tellervo.desktop.prefs.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.components.UIDefaultsComponent;
import org.tellervo.desktop.prefs.wrappers.CheckBoxWrapper;
import org.tellervo.desktop.prefs.wrappers.ColorComboBoxWrapper;
import org.tellervo.desktop.prefs.wrappers.FontButtonWrapper;
import org.tellervo.desktop.prefs.wrappers.FormatWrapper;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.ui.I18n.TellervoLocale;
import org.tridas.schema.NormalTridasUnit;


public class AppearancePrefsPanel extends AbstractPreferencesPanel {


	private static final long serialVersionUID = 1L;
	private JComboBox cboLocale;
	private TellervoLocale originalLocale;
	
	
	
	
	/**
	 * Create the panel.
	 */
	public AppearancePrefsPanel(final JDialog parent) {
		super(I18n.getText("preferences.appearance"), 
				"appearance.png", 
				"Change how your Corina interface should appear",
				parent);
		setLayout(new MigLayout("", "[111.00px,fill][grow]", "[101.00px,fill][grow,fill]"));
		
		originalLocale = I18n.getPreferredTellervoLocale(false);		
		
		JPanel panelEditor = new JPanel();
		panelEditor.setBorder(new TitledBorder(null, "Editor panel", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panelEditor, "cell 0 0,alignx center,aligny center");
		panelEditor.setLayout(new MigLayout("", "[][][grow]", "[][][][]"));
		
		JLabel lblUnits = new JLabel("Units:");
		panelEditor.add(lblUnits, "cell 0 0,alignx trailing");
		
		JComboBox cboDisplayUnits = new JComboBox();
		panelEditor.add(cboDisplayUnits, "cell 1 0 2 1,growx");
		
		JLabel lblBackgroundColor = new JLabel("Background:");
		panelEditor.add(lblBackgroundColor, "cell 0 2,alignx trailing");
		
		JComboBox cboEditorBGColor = new JComboBox();
		panelEditor.add(cboEditorBGColor, "flowx,cell 1 2,alignx left");
		new ColorComboBoxWrapper(cboEditorBGColor, PrefKey.EDIT_BACKGROUND, Color.white);
		
		JCheckBox chkShowEditorGrid = new JCheckBox("Gridlines");
		panelEditor.add(chkShowEditorGrid, "cell 2 2");
		new CheckBoxWrapper(chkShowEditorGrid, PrefKey.EDIT_GRIDLINES, true);
		
		JLabel lblFont = new JLabel("Font:");
		panelEditor.add(lblFont, "cell 0 3,alignx right,aligny center");
		
		JButton btnFont = new JButton("Font");
		panelEditor.add(btnFont, "flowy,cell 1 3 2 1,alignx left");
		new FontButtonWrapper(btnFont, PrefKey.EDIT_FONT, getFont());
		
		JPanel panelCharts = new JPanel();
		panelCharts.setBorder(new TitledBorder(null, "Charts", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panelCharts, "cell 1 0,grow");
		panelCharts.setLayout(new MigLayout("", "[][grow]", "[][][]"));
		
		JLabel lblAxiscursorColor = new JLabel("Axis/cursor color:");
		panelCharts.add(lblAxiscursorColor, "flowy,cell 0 0,alignx trailing");
		
		JComboBox cboAxisCursorColor = new JComboBox();
		panelCharts.add(cboAxisCursorColor, "cell 1 0,growx");
		
		JLabel lblBackgroundColor_1 = new JLabel("Background color:");
		panelCharts.add(lblBackgroundColor_1, "cell 0 1,alignx trailing");
		
		JComboBox cboChartBGColor = new JComboBox();
		panelCharts.add(cboChartBGColor, "cell 1 1,growx");
		
		JLabel lblGridColor = new JLabel("Gridlines:");
		panelCharts.add(lblGridColor, "cell 0 2,alignx trailing");
		
		final JCheckBox chkShowChartGrid = new JCheckBox("");
		panelCharts.add(chkShowChartGrid, "flowx,cell 1 2");
		new CheckBoxWrapper(chkShowChartGrid, PrefKey.GRAPH_GRIDLINES, true);
		
				
				
				final JComboBox cboGridColor = new JComboBox();
				panelCharts.add(cboGridColor, "cell 1 2,growx");
				chkShowChartGrid.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						cboGridColor.setEnabled(chkShowChartGrid.isSelected());
						
					}
					
				});
				new ColorComboBoxWrapper(cboAxisCursorColor, PrefKey.GRAPH_AXISCURSORCOLOR, Color.white);
				new ColorComboBoxWrapper(cboChartBGColor, PrefKey.GRAPH_BACKGROUND, Color.black);
				new ColorComboBoxWrapper(cboGridColor, PrefKey.GRAPH_GRIDLINES_COLOR, Color.darkGray);
		
		JPanel panelGeneral = new JPanel();
		panelGeneral.setBorder(new TitledBorder(null, "General", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panelGeneral, "cell 0 1 2 1,grow");
		panelGeneral.setLayout(new MigLayout("", "[149.00px][grow]", "[][191px,grow]"));
		
		JLabel lblLanguage = new JLabel("Language:");
		panelGeneral.add(lblLanguage, "cell 0 0");
		
		cboLocale = new JComboBox();
		panelGeneral.add(cboLocale, "cell 1 0");
		cboLocale.setModel(new DefaultComboBoxModel(I18n.TellervoLocale.values()));

		String country = App.prefs.getPref(PrefKey.LOCALE_COUNTRY_CODE, "xxx");
		String language = App.prefs.getPref(PrefKey.LOCALE_LANGUAGE_CODE, "xxx");
		
		TellervoLocale loc = I18n.getTellervoLocale(country, language);
		cboLocale.setSelectedItem(loc);
		cboLocale.setRenderer(new LocaleComboRenderer());
		
		cboLocale.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				TellervoLocale selloc = (TellervoLocale) cboLocale.getSelectedItem();
				App.prefs.setPref(PrefKey.LOCALE_COUNTRY_CODE, selloc.getCountryCode());
				App.prefs.setPref(PrefKey.LOCALE_LANGUAGE_CODE, selloc.getLanguageCode());
			}
			
		});
		
		panelGeneral.add(new UIDefaultsComponent(), "cell 0 1 2 1,grow");
		
		
		
	    String unit_keys[] = new String[] {
	    		NormalTridasUnit.HUNDREDTH_MM.name().toString(),
	    		NormalTridasUnit.MICROMETRES.name().toString()
	    };
		
		new FormatWrapper(cboDisplayUnits, 
				PrefKey.DISPLAY_UNITS, 
				NormalTridasUnit.MICROMETRES.name().toString(), 
				unit_keys);
		
		JComboBox cboTextColor = new JComboBox();
		panelEditor.add(cboTextColor, "cell 1 3,alignx left");
		new ColorComboBoxWrapper(cboTextColor, PrefKey.EDIT_FOREGROUND, Color.black);
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	public boolean hasLocaleChanged() {

		if(originalLocale.equals(cboLocale.getSelectedItem()))
		{
			return false;
		}
		
		return true;
	}

}
