/**
 * 
 */
package org.tellervo.desktop.graph;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;


@SuppressWarnings("serial")
class GraphViewMenu extends JMenu {
	// custom menus for graph windows
	private JMenuItem _axisMenu, _gridlinesMenu, _baselinesMenu, _compnamesMenu, _remarksMenu, _hundredpercentlinesMenu;

	GraphViewMenu(GraphWindow win, GraphActions actions) {			
		super(I18n.getText("menus.view"));		

		// start adding the menu items!
		
		final JMenuItem showElemPanel = new JCheckBoxMenuItem(actions.showElementsPanel);
		actions.showElementsPanel.connectToggleableButton(showElemPanel);
		
		this.add(showElemPanel);			
		this.addSeparator();

		// Show/hide axis
		_axisMenu = new JCheckBoxMenuItem(actions.showVerticalAxis);
		actions.showVerticalAxis.connectToggleableButton(_axisMenu);
		this.add(_axisMenu);

		// Show/hide gridlines
		_gridlinesMenu = new JCheckBoxMenuItem(actions.showGridlines);
		actions.showGridlines.connectToggleableButton(_gridlinesMenu);
		this.add(_gridlinesMenu);

		// Show/hide baselines
		_baselinesMenu = new JCheckBoxMenuItem(actions.showBaselines);
		actions.showBaselines.connectToggleableButton(_baselinesMenu);
		this.add(_baselinesMenu);

		// Show/hide hundred percent lines
		_hundredpercentlinesMenu = new JCheckBoxMenuItem(actions.showHundredPercentLines);
		actions.showHundredPercentLines.connectToggleableButton(_hundredpercentlinesMenu);
		this.add(_hundredpercentlinesMenu);
		
		// Show/hide graph component names
		_compnamesMenu = new JCheckBoxMenuItem(actions.showComponentNames);
		actions.showComponentNames.connectToggleableButton(_compnamesMenu);
		this.add(_compnamesMenu);
		
		// Show/hide graph remarks
		_remarksMenu = new JCheckBoxMenuItem(actions.showRemarks);
		actions.showRemarks.connectToggleableButton(_remarksMenu);
		this.add(_remarksMenu);
		
		// ---
		this.addSeparator();

		// TODO: put the baseline menuitems under an "Align" menu (and
		// reword them)

		// Squeeze together
		JMenuItem squeeze = new JMenuItem(actions.squeezeVertically);
		this.add(squeeze);

		// Spread apart
		JMenu spread = Builder.makeMenu("graph.baselines_spread");
		JMenuItem spread_25 = new JMenuItem(actions.spreadB250);		
		JMenuItem spread_50 = new JMenuItem(actions.spreadB500);
		JMenuItem spread_100 = new JMenuItem(actions.spreadB1000);		
		JMenuItem spread_200 = new JMenuItem(actions.spreadB2000);
		
		spread.add(spread_25);
		spread.add(spread_50);
		spread.add(spread_100);
		spread.add(spread_200);
		
		this.add(spread);

		// Squish
		JMenuItem squish = new JMenuItem(actions.squishBaselines);
		this.add(squish);

		// Squish
		JMenuItem fitwidth = new JMenuItem(actions.fitHorizontally);
		this.add(fitwidth);

		// Squish
		JMenuItem fitboth = new JMenuItem(actions.fitBoth);
		this.add(fitboth);
		
		this.addSeparator();
		
		// scaling... half scale
		JMenuItem halvescale = new JMenuItem(actions.scaleDown);
		this.add(halvescale);			
		
		// scaling... double scale
		JMenuItem doublescale = new JMenuItem(actions.scaleUp);
		this.add(doublescale);			

		// scaling... reset scale
		JMenuItem resetscale = new JMenuItem(actions.scaleReset);
		this.add(resetscale);			

		this.addSeparator();

		JMenu plottype = Builder.makeMenu("graph.plot_type");			
		////////
		for(int i = 0; i < actions.plotTypes.length; i++) {
			JRadioButtonMenuItem sa = new JRadioButtonMenuItem(actions.plotTypes[i]);
			actions.plotTypes[i].connectToggleableButton(sa);
			
			plottype.add(sa);
		}
		this.add(plottype);
	}
}