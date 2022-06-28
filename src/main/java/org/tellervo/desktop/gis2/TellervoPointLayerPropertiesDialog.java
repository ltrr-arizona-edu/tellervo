package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.ui.Builder;

public class TellervoPointLayerPropertiesDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private boolean success = false;
	protected final static Logger log = LoggerFactory.getLogger(TellervoPointLayerPropertiesDialog.class);
	private JComboBox<String> cboShape;
	private JButton btnColor; 
	private JSlider sldTransparency;
	private JSpinner spnSize;
	private Component parent = null;
	
	/**
	 * Create the dialog.
	 */
	public TellervoPointLayerPropertiesDialog(Component parent, String title, TellervoPointDataLayer layer) {
		this.parent = parent;
		init();

		if(layer instanceof MarkerLayer)
		{
			this.setTitle("Marker properties - "+((MarkerLayer)layer).getName());
		}
		else
		{
			this.setTitle("Properties");
		}

		setMarkerAttributes(layer.getMarkerStyle());
		this.setVisible(true);

	}
	
	public TellervoPointLayerPropertiesDialog()
	{
		this.setTitle("Properties");
		init();
	}
	
	public void setColor(Color object){
		btnColor.setBackground(object);
	}
	

	public void setShapeName(String s){
		
		
		log.debug("Searching for shape : "+s);
		for(int i=0; i<cboShape.getModel().getSize(); i++)
		{
			String item = cboShape.getModel().getElementAt(i);
			log.debug("Shape option : "+item);
			
			
		}
		
		
		cboShape.setSelectedItem(s);
	}
	
	public String getShapeName(){

		return (String) cboShape.getSelectedItem();
	}
	
	public void setMarkerOpacity(Double o){

		o = o*100;
		sldTransparency.setValue((int) (100-o));
	}
	
	public Double getMarkerOpacity(){

		log.debug("Transparency slider value = "+sldTransparency.getValue());
		
		Double d=  (double) (sldTransparency.getValue());
		return 1-(d/100);
	}
	@Override
	public void actionPerformed(ActionEvent evt) {
		
		if(evt.getActionCommand().equals("OK"))
		{
			success = true;
			setVisible(false);

		}
		else if(evt.getActionCommand().equals("Cancel"))
		{
			success = false;
			setVisible(false);

		}
		else if (evt.getActionCommand().equals("Color"))
		{
			Color color = JColorChooser.showDialog(parent, "Pick color", btnColor.getBackground());
			Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
			
			btnColor.setBackground(color2);
		}

		
	}
	
	private void init()
	{
		this.setModal(true);
		this.setIconImage(Builder.getApplicationIcon());
		
		getContentPane().setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[76.00px,right][grow]", "[][][][]"));
		{
			JLabel lblNewLabel = new JLabel("Color:");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			panel.add(lblNewLabel, "cell 0 0,alignx right");
		}
		{
			/*cboColor = new JComboBox();
			int[] values = new int[] { 0, 128, 192, 255 };
		    for (int r = 0; r < values.length; r++)
		      for (int g = 0; g < values.length; g++)
		        for (int b = 0; b < values.length; b++) {
		          Color c = new Color(values[r], values[g], values[b]);
		          cboColor.addItem(c);
		        }
		    cboColor.setRenderer(new ColorComboRenderer());*/
			
			btnColor = new JButton("Choose");
			
			btnColor.addActionListener(this);
			btnColor.setActionCommand("Color");
			
		    panel.add(btnColor, "cell 1 0,alignx left");
		    
		    
		    
		}
		{
			JLabel lblShape = new JLabel("Shape:");
			lblShape.setHorizontalAlignment(SwingConstants.CENTER);
			panel.add(lblShape, "cell 0 1,alignx trailing");
		}
		{
			cboShape = new JComboBox<String>();

			String[] shapes = new String[] {BasicMarkerShape.CYLINDER, BasicMarkerShape.CONE, BasicMarkerShape.CUBE, BasicMarkerShape.SPHERE};
			for(int i=0; i<shapes.length;i++){
				String shape1 = shapes[i].toString();
				cboShape.addItem(shape1);
			}
						
		    cboShape.setRenderer(new ShapeComboRenderer());
			panel.add(cboShape, "cell 1 1,growx");

		}
		{
			SpinnerNumberModel model = new SpinnerNumberModel(0.6, 0 ,1.0,0.1);
		}
		{
			JLabel lblSize = new JLabel("Size:");
			panel.add(lblSize, "cell 0 2");
		}
		{
			spnSize = new JSpinner();
			spnSize.setModel(new SpinnerNumberModel(3, 1, 30, 1));
			panel.add(spnSize, "cell 1 2");
		}
		{
			JLabel lblTransparency = new JLabel("Transparency:");
			panel.add(lblTransparency, "cell 0 3");
		}
		{
			sldTransparency = new JSlider();
			sldTransparency.setPaintLabels(true);
			sldTransparency.setMajorTickSpacing(10);
			sldTransparency.setMinorTickSpacing(5);
			sldTransparency.setValue(0);
			sldTransparency.setPaintTicks(true);
			panel.add(sldTransparency, "cell 1 3,growx");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(this);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
		
		this.pack();
		this.setMinimumSize(new Dimension(420,240));
		this.setLocationRelativeTo(parent);
	
		
	}
	
	public Material getMaterial()
	{
		return new Material((Color) btnColor.getBackground());
	}
	
	public void setMarkerSize(Integer size)
	{
		spnSize.setValue(size);
	}
	
	public BasicMarkerAttributes getMarkerAttributes()
	{
		
		BasicMarkerAttributes attrib = new BasicMarkerAttributes(getMaterial(), getShapeName(), getMarkerOpacity());
		int value = (Integer) spnSize.getValue();
		attrib.setMarkerPixels(Double.valueOf(value));
		
		return attrib;
	}

	public boolean isSuccessful()
	{
		return success;
	}
	
	public void setMarkerAttributes(BasicMarkerAttributes attrib)
	{
		Color color = attrib.getMaterial().getDiffuse();
		String shape = attrib.getShapeType();
		double opacity = attrib.getOpacity();
		int size = (int) attrib.getMarkerPixels();
		
		setColor(color);
		setShapeName(shape);
		setMarkerOpacity(opacity);
		setMarkerSize(size);
		
		this.repaint();
		
	}
}
