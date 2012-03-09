package org.tellervo.desktop.gis;

import javax.swing.JPanel;

/*
 Copyright (C) 2001, 2011 United States Government as represented by
 the Administrator of the National Aeronautics and Space Administration.
 All Rights Reserved.
 */

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.*;

import javax.swing.*;
import javax.swing.border.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.graph.GraphToolbar;
import org.tellervo.desktop.gui.menus.actions.AddLayersAction;
import org.tellervo.desktop.gui.menus.actions.ExportMapAction;
import org.tellervo.desktop.gui.widgets.TitlelessButton;
import org.tellervo.desktop.ui.Builder;

import java.awt.*;
import java.awt.event.*;

import net.miginfocom.swing.MigLayout;

/**
 * Panel to display a list of layers. A layer can be turned on or off by
 * clicking a check box next to the layer name.
 * 
 * @version $Id: LayerPanel.java 1 2011-07-16 23:22:47Z dcollins $
 * 
 * @see LayerTreeUsage
 * @see OnScreenLayerManager
 */
public class TellervoLayerPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected JPanel layersPanel;
	protected JPanel topPanel;
	protected JScrollPane scrollPane;
	protected Font defaultFont;
	private JPanel controlsPanel_2;
	private JScrollPane scrollPane_1;
	private JPanel panel_1;
	private JPanel controlsPanel;
	private JPanel backgroundPanel_2;
	private JScrollPane scrollPane_2;
	private JPanel panel_2;
	private JPanel backgroundPanel;
	private JSplitPane splitPane;
	private JSplitPane splitPane2;
	private JButton btnAddData;
	private final static Logger log = LoggerFactory
			.getLogger(TellervoLayerPanel.class);
	private JButton btnAddLayers;
	private JPanel panel;
	private JToolBar toolBar;
	private final GISFrame parent;

	/**
	 * Create a panel with the default size.
	 * 
	 * @param wwd
	 *            WorldWindow to supply the layer list.
	 * @wbp.parser.constructor
	 */
	public TellervoLayerPanel(WorldWindow wwd, GISFrame parent) {
		this.parent = parent;
		this.makePanel(wwd, null);
	}

	/**
	 * Create a panel with a size.
	 * 
	 * @param wwd
	 *            WorldWindow to supply the layer list.
	 * @param size
	 *            Size of the panel.
	 */
	public TellervoLayerPanel(WorldWindow wwd, Dimension size, GISFrame parent) {
		// Make a panel at a specified size.
		super(new BorderLayout());
		this.parent = parent;
		this.makePanel(wwd, size);
	}

	protected void makePanel(WorldWindow wwd, Dimension size) {
		if (size != null)
			this.scrollPane.setPreferredSize(size);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.33);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane);

		// Add the scroll bar and name panel to a titled panel that will resize
		// with the main window.
		topPanel = new JPanel();
		splitPane.setLeftComponent(topPanel);
		topPanel.setBorder(null);
		topPanel.setToolTipText("Layers to Show");
		topPanel.setLayout(new MigLayout("novisualpadding, ins 0",
				"[420px,grow]", "[][27px,grow]"));

		toolBar = new JToolBar();
		topPanel.add(toolBar, "flowx,cell 0 0");

		Action exportAction = new ExportMapAction(parent.wwMapPanel);
		AbstractButton btnExport = new TitlelessButton(exportAction);
		toolBar.add(btnExport);
		toolBar.addSeparator();
		
		Action addLayersAction = new AddLayersAction(parent);
		btnAddLayers = new TitlelessButton(addLayersAction);
		toolBar.add(btnAddLayers);
		toolBar.addSeparator();

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Data layers",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		topPanel.add(panel, "cell 0 1,grow");
		panel.setLayout(new MigLayout("", "[420px,grow]", "[27px,grow]"));
		// Make and fill the panel holding the layer titles.
		this.layersPanel = new JPanel(new GridLayout(0, 1, 0, 4));
		this.layersPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Must put the layer grid in a container to prevent scroll panel from
		// stretching their vertical spacing.
		JPanel dummyPanel = new JPanel(new BorderLayout());
		dummyPanel.add(this.layersPanel, BorderLayout.NORTH);

		// Put the name panel in a scroll bar.
		this.scrollPane = new JScrollPane(dummyPanel);
		panel.add(scrollPane, "cell 0 0,grow");
		this.scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		splitPane2 = new JSplitPane();
		splitPane2.setResizeWeight(0.5);
		splitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane2);

		controlsPanel_2 = new JPanel();
		splitPane2.setRightComponent(controlsPanel_2);
		controlsPanel_2.setBorder(new TitledBorder(null,
				"Controls and decorations", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		controlsPanel_2.setLayout(new GridLayout(0, 1, 0, 0));

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		controlsPanel_2.add(scrollPane_1);

		panel_1 = new JPanel();
		scrollPane_1.setViewportView(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		controlsPanel = new JPanel();
		controlsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel_1.add(controlsPanel, BorderLayout.NORTH);
		controlsPanel.setLayout(new GridLayout(0, 1, 0, 4));

		backgroundPanel_2 = new JPanel();
		splitPane2.setLeftComponent(backgroundPanel_2);
		backgroundPanel_2.setBorder(new TitledBorder(null, "Background layers",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		backgroundPanel_2.setLayout(new GridLayout(0, 1, 0, 0));

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		backgroundPanel_2.add(scrollPane_2);

		panel_2 = new JPanel();
		scrollPane_2.setViewportView(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		backgroundPanel = new JPanel();
		backgroundPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel_2.add(backgroundPanel, BorderLayout.NORTH);
		backgroundPanel.setLayout(new GridLayout(0, 1, 0, 4));

		this.fill(wwd);
	}

	protected void fill(WorldWindow wwd) {

		LayerList layers = null;
		try {
			layers = wwd.getModel().getLayers();
		} catch (NullPointerException e) {
			log.debug("No layers in current WWJ map model");
		}

		// Fill the layers panel with the titles of all layers in the world
		// window's current model.

		for (Layer layer : layers) {
			LayerAction action = new LayerAction(layer, wwd, layer.isEnabled());
			JCheckBox jcb = new JCheckBox(action);
			jcb.setSelected(action.selected);

			// These layers go into the Control Panel
			if (layer instanceof CompassLayer || layer instanceof WorldMapLayer
					|| layer instanceof ScalebarLayer
					|| layer instanceof ViewControlsLayer
					|| layer.getName().equals("Stars")
					|| layer.getName().equals("Atmosphere")) {
				this.controlsPanel.add(jcb);
			}

			// This is ignored
			else if (layer.getName().equals("Popup information")) {
				continue;
			}

			// These are all data marker layers
			else if (layer instanceof MarkerLayer) {
				this.layersPanel.add(jcb);
			}

			// These are other background layers
			else {
				this.backgroundPanel.add(jcb);
			}

			if (defaultFont == null) {
				this.defaultFont = jcb.getFont();
			}
		}
	}

	/**
	 * Update the panel to match the layer list active in a WorldWindow.
	 * 
	 * @param wwd
	 *            WorldWindow that will supply the new layer list.
	 */
	public void update(WorldWindow wwd) {
		// Replace all the layer names in the layers panel with the names of the
		// current layers.
		this.layersPanel.removeAll();
		this.controlsPanel.removeAll();
		this.backgroundPanel.removeAll();

		this.fill(wwd);

		this.topPanel.revalidate();
		this.topPanel.repaint();

		this.controlsPanel_2.revalidate();
		this.controlsPanel_2.repaint();

		this.backgroundPanel_2.revalidate();
		this.backgroundPanel_2.repaint();

	}

	@Override
	public void setToolTipText(String string) {
		this.scrollPane.setToolTipText(string);
	}

	protected static class LayerAction extends AbstractAction {
		WorldWindow wwd;
		private Layer layer;
		private boolean selected;

		public LayerAction(Layer layer, WorldWindow wwd, boolean selected) {
			super(layer.getName());
			this.wwd = wwd;
			this.layer = layer;
			this.selected = selected;
			this.layer.setEnabled(this.selected);
		}

		public void actionPerformed(ActionEvent actionEvent) {
			// Simply enable or disable the layer based on its toggle button.
			if (((JCheckBox) actionEvent.getSource()).isSelected())
				this.layer.setEnabled(true);
			else
				this.layer.setEnabled(false);

			wwd.redraw();
		}
	}
}
