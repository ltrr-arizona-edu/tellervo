package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import javax.swing.JComboBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.editor.FullEditor;

public class Properties extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	Color c;
	String shape;
	double opacity;
	BasicMarkerAttributes Material;
	protected final static Logger log = LoggerFactory.getLogger(Properties.class);
	FullEditor editor= FullEditor.getInstance();


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			FullEditor editor= FullEditor.getInstance();
			Properties dialog = new Properties(editor, "Properties", true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Properties(FullEditor editor,String title,boolean modal) {
		//setTitle("Properties");
		super(editor,title,modal);
		setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[76.00px,right][grow]", "[][][]"));
		{
			JLabel lblNewLabel = new JLabel("Color:");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel, "cell 0 0,alignx right,aligny top");
		}
		{
			final JComboBox comboBox = new JComboBox();
			int[] values = new int[] { 0, 128, 192, 255 };
		    for (int r = 0; r < values.length; r++)
		      for (int g = 0; g < values.length; g++)
		        for (int b = 0; b < values.length; b++) {
		           c = new Color(values[r], values[g], values[b]);
		          comboBox.addItem(c);
		        }
		    comboBox.setRenderer(new ColorComboRenderer());

		    comboBox.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		setColor((Color) comboBox.getSelectedItem());
		    		
		    	}
		    });
		    
		    contentPanel.add(comboBox, "cell 1 0,growx");
		}
		{
			JLabel lblShape = new JLabel("Shape:");
			lblShape.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblShape, "cell 0 1,alignx trailing");
		}
		{
			final JComboBox comboBox = new JComboBox();

			String[] shapes = new String[] {"CYLINDER", "CONE", "RECTANGLE", "SPHERE", "CUBE","HEADING_ARROW", "HEADING_LINE", "ORIENTED_SPHERE", "ORIENTED_CUBE", "ORIENTED_CONE", "ORIENTED_CYLINDER", "ORIENTED_SPHERE_LINE", "ORIENTED_CONE_LINE", "ORIENTED_CYLINDER_LINE"  };
			for(int i=0; i<shapes.length;i++){
				String shape1 = shapes[i].toString();
				comboBox.addItem(shape1);
			}
						
		    comboBox.setRenderer(new ShapeComboRenderer());	
		    comboBox.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		setShapeName((String) comboBox.getSelectedItem());
		    		
		    	}
		    });
			contentPanel.add(comboBox, "cell 1 1,growx");

		}
		{
			JLabel lblOpacity = new JLabel("Opacity:");
			contentPanel.add(lblOpacity, "cell 0 2,alignx trailing");
		}
		{
			double[] opacityValues = new double[] { 0.6d, 1.0d};
			JSpinner spinner = new JSpinner();
		    JTable table = new JTable();
		    DefaultTableModel model = (DefaultTableModel) table.getModel();

		    model.addColumn("");
		    
		    TableColumn col = table.getColumnModel().getColumn(0);
			
			for(int i=0;i<opacityValues.length;i++){
				double opacity1 = opacityValues[i];
			SpinnerModel model1 = new SpinnerNumberModel(0.6d, opacity1-opacity1, 1d, 0.1d);
			spinner = new JSpinner(model1);

			}
			
		    col.setCellEditor(new OpacitySpinnerRenderer());
			contentPanel.add(spinner, "cell 1 2,growx");
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
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public void setColor(Color object){
		this.c=object;
	}
	
	public gov.nasa.worldwind.render.Material getColor(){

		gov.nasa.worldwind.render.Material color = new gov.nasa.worldwind.render.Material(c);
		return color;
	}

	public void setShapeName(String s){
		this.shape=s;
	}
	
	public String getShapeName(){

		return shape;
	}
	
	public void setOpacity(double o){
		this.opacity=o;
	}
	
	public float getOpacity(){

		return (float)opacity;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//System.out.println(c.toString());
		log.debug(c.toString());
		getColor();
		getShapeName();
		getOpacity();
		editor.getMapPanel().getLayerPanel().setMarkerColor(c);
		editor.getMapPanel().getLayerPanel().setMarkerShape(shape);

		//setVisible(false);
	}

}
