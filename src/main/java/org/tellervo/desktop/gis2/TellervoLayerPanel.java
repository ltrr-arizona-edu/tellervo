package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ScalebarLayer;
import gov.nasa.worldwind.layers.SkyColorLayer;
import gov.nasa.worldwind.layers.SkyGradientLayer;
import gov.nasa.worldwind.layers.StarsLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.layers.Earth.BMNGWMSLayer;
import gov.nasa.worldwind.layers.Earth.CountryBoundariesLayer;
import gov.nasa.worldwind.layers.Earth.MGRSGraticuleLayer;
import gov.nasa.worldwind.layers.Earth.NASAWFSPlaceNameLayer;
import gov.nasa.worldwind.layers.Earth.UTMGraticuleLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gui.menus.FullEditorActions;
import org.tellervo.desktop.gui.menus.actions.ExportLayerToKML;
import org.tellervo.desktop.ui.Builder;

public class TellervoLayerPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	protected final static Logger log = LoggerFactory.getLogger(TellervoLayerPanel.class);
	
	protected FullEditorActions actions;
	protected FullEditor editor;
	protected JPanel layersPanel;
	protected JPanel panelMain;
	protected JScrollPane scrollPane;
	protected Font defaultFont;
	
	private WorldWindow wwd;
	private JPanel panelHeader;
	private JLabel lblMapLayers;
	private JPanel dummyPanel;

	/**
	 * Create a panel with the default size.
	 * 
	 * @param wwd
	 *            WorldWindow to supply the layer list.
	 * @wbp.parser.constructor
	 */
	public TellervoLayerPanel(WorldWindow wwd) {
		// Make a panel at a default size.
		super(new BorderLayout());
		actions = FullEditor.getInstance().getAction();
		this.wwd = wwd;
		this.makePanel(new Dimension(200, 400));

	}

	/**
	 * Create a panel with a size.
	 * 
	 * @param wwd
	 *            WorldWindow to supply the layer list.
	 * @param size
	 *            Size of the panel.
	 */
	public TellervoLayerPanel(WorldWindow wwd, Dimension size) {
		// Make a panel at a specified size.
		super(new BorderLayout());
		actions = FullEditor.getInstance().getAction();
		this.wwd = wwd;
		this.makePanel(size);

	}

	protected void makePanel(Dimension size) {
		// Make and fill the panel holding the layer titles.
		this.populateLayersGUI();
		
		this.scrollPane = new JScrollPane(dummyPanel);
		scrollPane.getViewport().setOpaque(true);
		scrollPane.getViewport().setBackground(Color.WHITE);
		this.scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		if (size != null)
			this.scrollPane.setPreferredSize(size);

		// Add the scroll bar and name panel to a titled panel that will resize
		// with the main window.
		panelMain = new JPanel();
		panelMain.setToolTipText("Layers to Show");
		panelMain.setLayout(new BorderLayout(0, 0));
		panelMain.add(scrollPane);
		this.add(panelMain, BorderLayout.CENTER);

		panelHeader = new JPanel();
		panelMain.add(panelHeader, BorderLayout.NORTH);
		panelHeader.setLayout(new MigLayout("", "[78px]", "[15px]"));

		lblMapLayers = new JLabel("Map layers:");
		panelHeader.add(lblMapLayers, "cell 0 0,alignx left,aligny top");
	}

	protected void populateLayersGUI() {
		layersPanel = new JPanel(new GridLayout(0, 1, 0, 4));
		layersPanel.setBackground(Color.WHITE);
		layersPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JLabel heading = new JLabel("Background layers");
		heading.setFont(new Font("Dialog", Font.BOLD, 13));
		this.layersPanel.add(heading);

		for (Layer layer : getBackgroundLayers()) {
			JCheckBox cbx = getCheckboxForLayer(layer);
			this.layersPanel.add(cbx);

			final JPopupMenu popupMenu = this.createPopupMenu(layer);

			cbx.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					showPopup(e);
				}

				public void mouseReleased(MouseEvent e) {
					showPopup(e);
				}

				private void showPopup(MouseEvent e) {

					if (e.isPopupTrigger()) {
						popupMenu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			});

		}

		heading = new JLabel("Data layers");
		heading.setFont(new Font("Dialog", Font.BOLD, 13));
		this.layersPanel.add(heading);
		for (Layer layer : getDataLayers()) {
			JCheckBox cbx = getCheckboxForLayer(layer);
			this.layersPanel.add(cbx);

			final JPopupMenu popupMenu = this.createPopupMenu(layer);

			cbx.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					showPopup(e);
				}

				public void mouseReleased(MouseEvent e) {
					showPopup(e);
				}

				private void showPopup(MouseEvent e) {

					if (e.isPopupTrigger()) {
						popupMenu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			});

		}
		
		// Must put the layer grid in a container to prevent scroll panel from
				// stretching their vertical spacing.
		if(dummyPanel==null) dummyPanel = new JPanel(new BorderLayout());
		
		dummyPanel.setBackground(Color.WHITE);
		dummyPanel.removeAll();
		dummyPanel.add(this.layersPanel, BorderLayout.NORTH);
	}

	protected JPopupMenu createPopupMenu(final Layer layer) {

		final JPopupMenu popupMenu = new JPopupMenu();

		final FullEditor ed = editor;

		JMenu addLayers = new JMenu(actions.mapAddLayersAction);

		JMenuItem shapeFileLayer = new JMenuItem(actions.mapShapefileLayerAction);
		addLayers.add(shapeFileLayer);

		JMenuItem KMLLayer = new JMenuItem(actions.mapKMLLayerAction);
		addLayers.add(KMLLayer);

		JMenuItem WMSLayer = new JMenuItem(actions.mapWMSLayerAction);
		addLayers.add(WMSLayer);

		JMenuItem GISLayer = new JMenuItem(actions.mapGISImageAction);
		addLayers.add(GISLayer);

		JMenuItem miDBLayer = new JMenuItem(actions.mapDatabaseLayerAction);
		addLayers.add(miDBLayer);

		popupMenu.add(addLayers);

		
		
		if (layer instanceof TellervoPointDataLayer) {
		
			popupMenu.addSeparator();
			
			JMenuItem miRemoveLayer = new JMenuItem("Remove");
			miRemoveLayer.setIcon(Builder.getIcon("button_cancel.png", 22));
			miRemoveLayer.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					FullEditor.getInstance().getMapPanel().getLayersList().remove(layer);
					update();

				}

			});
			popupMenu.add(miRemoveLayer);

			JMenuItem miRenameLayer = new JMenuItem("Rename");
			miRenameLayer.setIcon(Builder.getIcon("rename.png", 22));

			miRenameLayer.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String name = (String) JOptionPane.showInputDialog(FullEditor.getInstance(), "Name", "Rename layer",
							JOptionPane.QUESTION_MESSAGE);

					if (name == null) {
						return;
					}

					layer.setName(name);
					update();
				}

			});
			popupMenu.add(miRenameLayer);
			
			popupMenu.addSeparator();
			
			JMenuItem export = new JMenuItem(new ExportLayerToKML(App.mainWindow,
					((TellervoPointDataLayer) layer).getTridasMarkers()));
			popupMenu.add(export);

			popupMenu.addSeparator();

			
			JMenuItem properties = new JMenuItem("Layer properties");
			properties.setIcon(Builder.getIcon("advancedsettings.png", 22));

			properties.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					TellervoPointLayerPropertiesDialog propertiesDialog = new TellervoPointLayerPropertiesDialog(
							popupMenu, "Properties", (TellervoPointDataLayer) layer);

					if (!propertiesDialog.isSuccessful())
						return;

					BasicMarkerAttributes style = propertiesDialog.getMarkerAttributes();
					((TellervoPointDataLayer) layer).setMarkerStyle(style);

				}

			});

			popupMenu.add(properties);

			
		}
		
		

		return popupMenu;

	}

	/**
	 * Update the panel to match the layer list active in a WorldWindow.
	 * 
	 * @param wwd
	 *            WorldWindow that will supply the new layer list.
	 */
	public void update() {
		// Replace all the layer names in the layers panel with the names of the
		// current layers.
		//this.layersPanel.removeAll();
		this.populateLayersGUI();
		this.panelMain.revalidate();
		this.panelMain.repaint();
	}

	@Override
	public void setToolTipText(String string) {
		this.scrollPane.setToolTipText(string);
	}

	protected static class LayerAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		protected WorldWindow wwd;
		protected Layer layer;
		protected boolean selected;

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

	private boolean isHiddenLayer(Layer layer) {
		if (layer instanceof StarsLayer || layer instanceof ScalebarLayer || layer instanceof SkyColorLayer
				|| layer instanceof SkyGradientLayer || layer instanceof BMNGWMSLayer || layer instanceof CompassLayer
				|| layer instanceof UTMGraticuleLayer || layer instanceof MGRSGraticuleLayer
				|| layer instanceof NASAWFSPlaceNameLayer || layer instanceof CountryBoundariesLayer
				|| layer.getName().equals("Popup information") || layer instanceof ViewControlsLayer
				|| layer instanceof WorldMapLayer) {
			return true;
		}

		return false;
	}

	private ArrayList<Layer> getBackgroundLayers() {
		ArrayList<Layer> layers = new ArrayList<Layer>();
		LayerList wwdlayers = wwd.getModel().getLayers();
		for (Layer layer : wwdlayers) {
			if (isHiddenLayer(layer) || layer instanceof TellervoDataLayer || layer instanceof Renderable
					|| layer instanceof RenderableLayer)
				continue;

			layers.add(layer);

		}
		return layers;
	}

	private ArrayList<JCheckBox> getBackgroundLayersAsCheckboxes() {
		ArrayList<JCheckBox> items = new ArrayList<JCheckBox>();

		for (Layer layer : getBackgroundLayers()) {
			JCheckBox jcb = getCheckboxForLayer(layer);
			items.add(jcb);
		}

		return items;
	}

	private JCheckBox getCheckboxForLayer(Layer layer) {
		LayerAction action = new LayerAction(layer, wwd, layer.isEnabled());
		JCheckBox jcb = new JCheckBox(action);
		jcb.setSelected(action.selected);

		if (defaultFont == null) {
			this.defaultFont = jcb.getFont();
		}

		return jcb;
	}

	private ArrayList<Layer> getDataLayers() {
		ArrayList<Layer> layers = new ArrayList<Layer>();

		for (Layer layer : wwd.getModel().getLayers()) {
			if (isHiddenLayer(layer))
				continue;

			if (layer instanceof TellervoDataLayer || layer instanceof Renderable || layer instanceof RenderableLayer) {
				layers.add(layer);
			}
		}
		return layers;
	}

	private ArrayList<JCheckBox> getDataLayersAsCheckboxes(WorldWindow wwd) {
		ArrayList<JCheckBox> items = new ArrayList<JCheckBox>();

		for (Layer layer : getDataLayers()) {
			JCheckBox jcb = this.getCheckboxForLayer(layer);
			items.add(jcb);

		}

		return items;
	}

}
