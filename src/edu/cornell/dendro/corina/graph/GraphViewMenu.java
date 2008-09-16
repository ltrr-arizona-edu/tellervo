/**
 * 
 */
package edu.cornell.dendro.corina.graph;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;

class GraphViewMenu extends JMenu {
	// custom menus for graph windows
	private JMenuItem _axisMenu, _gridlinesMenu, _baselinesMenu, _compnamesMenu, _hundredpercentlinesMenu;

	private GraphWindow window;

	GraphViewMenu(GraphWindow win) {			
		super(I18n.getText("view"));		
		this.window = win;

		// start adding the menu items!
		
		final JCheckBoxMenuItem showElemPanel = Builder.makeCheckBoxMenuItem("view_elements");
		showElemPanel.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.elemPanel.setVisible(!window.elemPanel.isVisible());
				showElemPanel.setSelected(window.elemPanel.isVisible());
				revalidate();
			}
		});
		this.add(showElemPanel);			
		this.addSeparator();

		// Show/hide axis
		_axisMenu = Builder.makeMenuItem(Boolean.valueOf(
				App.prefs.getPref("corina.graph.vertical-axis"))
				.booleanValue() ? "vert_hide" : "vert_show", true, "axisshow.png");
		_axisMenu.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				boolean vis = Boolean.valueOf(
						App.prefs.getPref("corina.graph.vertical-axis")).booleanValue();
				
				window.plot.setAxisVisible(!vis);
				
				_axisMenu.setText(I18n.getText(vis ? "vert_show"
						: "vert_hide"));
			}
		});
		this.add(_axisMenu);

		// Show/hide gridlines
		_gridlinesMenu = Builder.makeMenuItem(Boolean.valueOf(
				App.prefs.getPref("corina.graph.graphpaper"))
				.booleanValue() ? "grid_hide" : "grid_show", true, "showgrid.png");
		_gridlinesMenu.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				boolean vis = Boolean.valueOf(
						App.prefs.getPref("corina.graph.graphpaper")).booleanValue();
				
				window.plot.setGraphPaperVisible(!vis);

				_gridlinesMenu.setText(I18n.getText(vis ? "grid_show"
						: "grid_hide"));
				window.repaint();
			}
		});
		this.add(_gridlinesMenu);

		// Show/hide baselines
		_baselinesMenu = Builder
				.makeMenuItem(Boolean.valueOf(
						App.prefs.getPref("corina.graph.baselines"))
						.booleanValue() ? "base_hide" : "base_show");
		_baselinesMenu.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {					
				boolean vis = Boolean.valueOf(App.prefs.getPref("corina.graph.baselines")).booleanValue();

				window.plot.setBaselinesVisible(!vis);
				
				_baselinesMenu.setText(I18n.getText(vis ? "base_show"
						: "base_hide"));
				window.repaint();
			}
		});
		this.add(_baselinesMenu);

		// Show/hide baselines
		_hundredpercentlinesMenu = Builder
				.makeMenuItem(Boolean.valueOf(
						App.prefs.getPref("corina.graph.hundredpercentlines"))
						.booleanValue() ? "hperc_hide" : "hperc_show");
		_hundredpercentlinesMenu.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {					
				boolean vis = Boolean.valueOf(App.prefs.getPref("corina.graph.hundredpercentlines")).booleanValue();

				window.plot.setHundredpercentlinesVisible(!vis);
				
				_hundredpercentlinesMenu.setText(I18n.getText(vis ? "hperc_show"
						: "hperc_hide"));
				window.repaint();
			}
		});
		this.add(_hundredpercentlinesMenu);
		
		// Show/hide graph component names
		_compnamesMenu = Builder
				.makeMenuItem(Boolean.valueOf(
						App.prefs.getPref("corina.graph.componentnames"))
						.booleanValue() ? "compn_hide" : "compn_show", true, "label.png");
		_compnamesMenu.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {					
				boolean vis = Boolean.valueOf(App.prefs.getPref("corina.graph.componentnames")).booleanValue();

				window.plot.setComponentNamesVisible(!vis);
				
				_compnamesMenu.setText(I18n.getText(vis ? "compn_show"
						: "compn_hide"));
				window.repaint();
			}
		});
		this.add(_compnamesMenu);
		
		// ---
		this.addSeparator();

		// TODO: put the baseline menuitems under an "Align" menu (and
		// reword them)

		// Squeeze together
		JMenuItem squeeze = Builder.makeMenuItem("baselines_align", true, "squeezevertically.png");
		squeeze.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.squeezeTogether();
			}
		});
		this.add(squeeze);

		// Spread apart
		JMenu spread = Builder.makeMenu("baselines_spread");
		JMenuItem spread_25 = new JMenuItem("25 units");
		spread_25.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.spreadOut(25);
			}
		});
		spread.add(spread_25);
		
		JMenuItem spread_50 = new JMenuItem("50 units");
		spread_50.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.spreadOut(50);
			}
		});
		spread.add(spread_50);
		
		JMenuItem spread_100 = new JMenuItem("100 units (half scale index plot)");
		spread_100.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.spreadOut(100);
			}
		});
		spread.add(spread_100);

		JMenuItem spread_200 = new JMenuItem("200 units (full scale index plot)");
		spread_200.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.spreadOut(200);
			}
		});
		spread.add(spread_200);
		
		this.add(spread);

		// Squish
		JMenuItem squish = Builder.makeMenuItem("baselines_squish");
		squish.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
		// -- i don't know how i'd put VK_SPACE in a properties file
		// ("space" doesn't seem to work),
		// but i don't think anybody should ever change it, either.
		squish.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				try {
					window.squishTogether();
				} catch (Exception ex) {
					// see squishTogether() method for at least 1 remaining
					// bug
					new Bug(ex);
				}
			}
		});
		this.add(squish);

		// Squish
		JMenuItem fitwidth = Builder.makeMenuItem("fit_horiz", true, "fitcharthoriz.png");
		fitwidth.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				try {
					// first, squish them together.
					window.squishTogether();
					// then, squish it horizontally.
					window.scaleToFitWidth();
				} catch (Exception ex) {
					// see squishTogether() method for at least 1 remaining
					// bug
					new Bug(ex);
				}
			}
		});
		this.add(fitwidth);

		// Squish
		JMenuItem fitboth = Builder.makeMenuItem("fit_both");
		fitboth.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				try {
					// first, squish them together.
					window.squishTogether();
					// then, squish it horizontally.
					window.scaleToFitWidth();
					window.scaleToFitHeight();
				} catch (Exception ex) {
					// see squishTogether() method for at least 1 remaining
					// bug
					new Bug(ex);
				}
			}
		});
		this.add(fitboth);
		
		this.addSeparator();
		
		// scaling... half scale
		JMenuItem halvescale = Builder.makeMenuItem("escale_halve", true, "axiszoomout.png");
		halvescale.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.halveScale();
			}
		});
		this.add(halvescale);			
		
		// scaling... half scale
		JMenuItem doublescale = Builder.makeMenuItem("escale_double", true, "axiszoomin.png");
		doublescale.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.doubleScale();
			}
		});
		this.add(doublescale);			

		// scaling... reset scale
		JMenuItem resetscale = Builder.makeMenuItem("escale_reset");
		resetscale.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.resetScaling();
			}
		});
		this.add(resetscale);			

		this.addSeparator();

		JMenu plottype = Builder.makeMenu("plot_type");			
		////////
		String[] agentnames = window.agents.getAgents();
		ButtonGroup agentgroup = new ButtonGroup();
		for(int i = 0; i < agentnames.length; i++) {
			JRadioButtonMenuItem sa = new JRadioButtonMenuItem(agentnames[i], window.agents.isDefault(i));
			agentgroup.add(sa);
			
			// glue
			final int ii = i;
			sa.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					window.agents.setAgent(ii);
					window.plot.update();
				}
			});
			plottype.add(sa);
		}
		this.add(plottype);
	}
}