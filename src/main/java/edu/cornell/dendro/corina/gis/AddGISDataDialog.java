package edu.cornell.dendro.corina.gis;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;
import edu.cornell.dendro.corina.ui.Builder;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.WWIO;


public class AddGISDataDialog extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GISFrame parent;
	private GISPanel wwMapPanel;
	private JComboBox cboAddType;
	
	/**
	 * Create the panel.
	 * @param worldWindowGLCanvas 
	 */
	public AddGISDataDialog(GISFrame parent) {
		
		this.setIconImage(Builder.getApplicationIcon());
		this.setModal(true);
		
		
		this.wwMapPanel = parent.wwMapPanel;
		this.parent = parent;
		getContentPane().setLayout(new MigLayout("", "[65.00px][grow]", "[][][grow][]"));
		
		JLabel lblAddLayer = new JLabel("Add:");
		getContentPane().add(lblAddLayer, "cell 0 0,alignx trailing");
		
		cboAddType = new JComboBox();
		cboAddType.setModel(new DefaultComboBoxModel(new String[] {"All ITRDB sites", "All Corina objects", "All Corina elements for an object", "Specific Corina object", "Specific Corina element", "ESRI Shapefile"}));
		getContentPane().add(cboAddType, "cell 1 0,growx");
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, "cell 0 3 2 1,alignx right,growy");
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setActionCommand("add");
		btnAdd.addActionListener(this);
		panel_1.add(btnAdd);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("cancel");
		btnCancel.addActionListener(this);
		panel_1.add(btnCancel);
		
		pack();
		this.setLocationRelativeTo(parent);
		
		

		
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("add"))
		{
			if(cboAddType.getSelectedItem().equals("All ITRDB sites"))
			{
				try{
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					wwMapPanel.addLayer(ITRDBMarkerLayerBuilder.createITRDBLayer());
					parent.layerPanel.update(parent.wwMapPanel.getWwd());
					
				} finally {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
				
				
			}
			
			else if(cboAddType.getSelectedItem().equals("ESRI Shapefile"))
			{
	            // Setup file chooser
				JFileChooser fc = new JFileChooser();
	            fc.addChoosableFileFilter(new FileNameExtensionFilter("ESRI Shapefile", "shp"));
	            
	            int retVal = fc.showOpenDialog(wwMapPanel);
	            if (retVal != JFileChooser.APPROVE_OPTION)
	                return;

	            Thread t = new WorkerThread(fc.getSelectedFile(), parent);
	            t.start();
	            wwMapPanel.getWwd().setCursor(new Cursor(Cursor.WAIT_CURSOR));
	            
			}
			
			dispose();
		}
		else if (event.getActionCommand().equals("cancel"))
		{
			dispose();
		}
		
	}

	
    public static class WorkerThread extends Thread
    {
        protected Object source;
        protected GISFrame frame;

        public WorkerThread(Object source, GISFrame frame)
        {
            this.source = source;
            this.frame = frame;

        }

        public void run()
        {
            try
            {
                final List<Layer> layers = this.makeShapefileLayers();
                for (int i = 0; i < layers.size(); i++)
                {
                    String name = this.makeDisplayName(this.source);
                    layers.get(i).setName(i == 0 ? name : name + "-" + Integer.toString(i));
                }

                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        for (Layer layer : layers)
                        {
                            //insertBeforePlacenames(appFrame.getWwd(), layer);
                            frame.wwMapPanel.addLayer(layer);
                        }

                        frame.layerPanel.update(frame.wwMapPanel.getWwd());
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        frame.wwMapPanel.getWwd().setCursor(Cursor.getDefaultCursor());
                    }
                });
            }
        }

        protected List<Layer> makeShapefileLayers()
        {
            if (OpenStreetMapShapefileLoader.isOSMPlacesSource(this.source))
            {
                Layer layer = OpenStreetMapShapefileLoader.makeLayerFromOSMPlacesSource(source);
                List<Layer> layers = new ArrayList<Layer>();
                layers.add(layer);
                return layers;
            }
            else
            {
                ShapefileLoader loader = new ShapefileLoader();
                return loader.createLayersFromSource(this.source);
            }
        }

        protected String makeDisplayName(Object source)
        {
            String name = WWIO.getSourcePath(source);
            if (name != null)
                name = WWIO.getFilename(name);
            if (name == null)
                name = "Shapefile";

            return name;
        }
    }

	
}
