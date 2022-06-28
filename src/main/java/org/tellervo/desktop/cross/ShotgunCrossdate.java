package org.tellervo.desktop.cross;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.graph.Graph;
import org.tellervo.desktop.manip.Redate;
import org.tellervo.desktop.sample.Sample;
import org.tridas.schema.NormalTridasDatingType;
import org.tridas.schema.TridasDating;

public class ShotgunCrossdate extends JPanel {
	private final static Logger log = LoggerFactory.getLogger(ShotgunCrossdate.class);

	private static final long serialVersionUID = 1L;
	private JXTable table;
	private ArrayList<Sample> samples = null;
	private ShotgunTableModel model;
	private CrossdateDialog parent;
	private JSpinner spnMin;
	private JSpinner spnMax;
	private JButton btnReset;
	private TableHeatMapRenderer renderer;
	
	/**
	 * Create the panel.
	 */
	public ShotgunCrossdate(CrossdateDialog parent) {
		this.parent = parent;
		
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		
		
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		table = new JXTable();
		table.setSortable(false);
		table.setColumnControlVisible(false);
		scrollPane.setViewportView(table);
		
		JTable rowTable = new CrossdataMatrixRowHeaderTable(table);
		scrollPane.setRowHeaderView(rowTable);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
		    rowTable.getTableHeader());
		
		
		table.setCellSelectionEnabled(true);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new MigLayout("", "[][][][][]", "[]"));
		
		JLabel lblSetColorRamp = new JLabel("Set color ramp range:");
		panel.add(lblSetColorRamp, "cell 0 0");
		
		spnMin = new JSpinner();
		spnMin.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				setRendererBounds();
				
			}
			
		});
		spnMin.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		panel.add(spnMin, "cell 1 0");
		
		JLabel lblTo = new JLabel("to");
		panel.add(lblTo, "cell 2 0");
		
		spnMax = new JSpinner();
		spnMax.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				setRendererBounds();
				
			}
			
		});
		spnMax.setModel(new SpinnerNumberModel(10, 1, 100, 1));
		panel.add(spnMax, "cell 3 0");
		
		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				spnMin.setValue(0);
				spnMax.setValue(model.getMaxValueInTable());
				
			}
			
		});
		panel.add(btnReset, "cell 4 0");
		ListSelectionModel selectionModel = table.getSelectionModel();

		selectionModel.addListSelectionListener(new ListSelectionListener() {


			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				graphSelectedCross();
				
			}


		});
		
		
		renderer = new TableHeatMapRenderer();
		setRendererBoundsToFullExtent();
		table.setDefaultRenderer(Cross.class, renderer);
		
	}
	
	
	public void setRendererBoundsToFullExtent()
	{
		if(model!=null)
		{
			renderer.setMaxValue(model.getMaxValueInTable());
			spnMax.setValue(model.getMaxValueInTable());

		}
		
		renderer.setMinValue(0.0);
		
		spnMin.setValue(0.0);
		
	}
	
	protected void setRendererBounds() {
		renderer.setMinValue(Double.valueOf(spnMin.getValue().toString()));
		Double doubleval = Double.valueOf(spnMax.getValue().toString());
		renderer.setMaxValue(doubleval.floatValue());
		table.repaint();
		
	}


	private void graphSelectedCross() {
		Cross cross = (Cross) model.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
		
		
		if(cross==null){
			log.debug("Cross is null");
			parent.updateGraph(null);
			return;
		}
		
		
		ArrayList<Graph> newGraphs = new ArrayList<Graph>();
		
		newGraphs.add(new Graph(cross.getFixed()));
		
		
		Sample clonedSample = new Sample();

		Sample.copy(cross.getMoving(), clonedSample);
		
		TridasDating newDating = new TridasDating();
		newDating.setType(NormalTridasDatingType.ABSOLUTE);
		Redate.redate(clonedSample, cross.getHighScores().getScores().get(0).movingRange, newDating);	
		newGraphs.add(new Graph(clonedSample));
			
		
		parent.updateGraph(newGraphs);
		
		
		
	}
	
	public void calculate() {

		model = new ShotgunTableModel(samples);
		table.setModel(model);

		
		for(int row=0; row<samples.size(); row++)
		{
			Sample primary = samples.get(row);
			
			for(int col=0; col<samples.size(); col++)
			{
				if(col==row) 
				{
					model.setValueAt(row, col, null);
					continue;
				}
				
				try{
					Sample secondary = samples.get(col);
			
					Cross cross = Cross.makeCross(Cross.ALL_CROSSDATES[0], primary, secondary);
			
					// do the crossdating
					cross.run();
							
					model.setValueAt(row, col, cross );
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		

		
		
		
	}
	
	public void setSamples(Collection<Sample> samples)
	{
		this.samples = (ArrayList<Sample>) samples;
	}

	public ArrayList<Sample> getSamples() {
		return samples;
	}


}
