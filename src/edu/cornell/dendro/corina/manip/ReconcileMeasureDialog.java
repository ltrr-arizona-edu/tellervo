/*
 * ReconcileMeasureDialog.java
 *
 * Created on September 16, 2008, 9:17 PM
 */

package edu.cornell.dendro.corina.manip;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.tridas.schema.NormalTridasUnit;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.LegacyCorinaMeasuringDevice;
import edu.cornell.dendro.corina.hardware.LegacySerialSampleIO;
import edu.cornell.dendro.corina.hardware.MeasurementReceiver;
import edu.cornell.dendro.corina.hardware.SerialDeviceSelector;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;

/**
 *
 * @author  Lucas Madar
 */
public class ReconcileMeasureDialog extends javax.swing.JDialog implements MeasurementReceiver {   
	private static final long serialVersionUID = 1L;
	private NormalTridasUnit displayUnits;
	private Sample src, ref;
	//private Year year;
	private int yearIndex;

	private Integer finalValue;

	private AbstractSerialMeasuringDevice dev;
	private ArrayList<AMeasurement> measurements;

	/* audioclips to play... */
	private AudioClip measure_one;
	private AudioClip measure_error;

	/** Creates new form ReconcileMeasureDialog */
	public ReconcileMeasureDialog(java.awt.Frame parent, boolean modal, Sample src, Sample ref, Year year) {
		super(parent, modal);
		initComponents();

		// Set preferred display units
		displayUnits = NormalTridasUnit.valueOf(
				App.prefs.getPref("corina.displayunits", NormalTridasUnit.HUNDREDTH_MM.value().toString()));
		
		this.src = src;
		this.ref = ref;

		//this.year = year;
		this.yearIndex = year.diff(src.getStart());

		setTitle("Remeasuring for " + year + " (index " + yearIndex + ")");

		initialize();
	}

	private void initMeasuringDevice()
	{
		try {
			dev = new SerialDeviceSelector ().getDevice();
			dev.initialize();
		}
		catch (Exception ioe) {
			Alert.error(I18n.getText("error"), 
					I18n.getText("error.initExtComms") + ": " +
					ioe.toString());
			devStatus.setText("Error communicating with platform");
			return;
		} 
	}
	public Integer getFinalValue() {
		return finalValue;
	}

	public void receiverUpdateStatus(String status) {
		devStatus.setText(status);
	}

	public void receiverNewMeasurement(Integer value) {
		if(value.intValue() == 0 || value.intValue() > 9900) {
			if(measure_error != null)
				measure_error.play();

			devStatus.setText("active, bad previous measurement.");

			return;
		}

		// nice! play a sound.
		if(measure_one != null)
			measure_one.play();

		// make a new measurement!
		AMeasurement nm = new AMeasurement(true, value.intValue(), "meas");
		measurements.add(nm);

		// notify our table
		((MeasurementsTableModel) measurementsTable.getModel()).fireTableDataChanged();
	}

	private void initialize() { 
		// audio clips :)
		try {
			measure_one = Applet.newAudioClip(getClass().getClassLoader().getResource("edu/cornell/dendro/corina_resources/Sounds/meas1.wav"));
			measure_error = Applet.newAudioClip(getClass().getClassLoader().getResource("edu/cornell/dendro/corina_resources/Sounds/measerr.wav"));
		} catch (Exception ae) { /* ignore this... */ }

		// now, stuff that really matters
		measurements = new ArrayList<AMeasurement>();

		// add our first two measurements
		measurements.add(new AMeasurement(true, 
				((Number) src.getData().get(yearIndex)).intValue(), 
				"source"));
		measurements.add(new AMeasurement(true, 
				((Number) ref.getData().get(yearIndex)).intValue(), 
				"ref"));

		// calculate our initial value
		calculateFinal();

		measurementsTable.setModel(new MeasurementsTableModel());
		measurementsTable.setRowSelectionAllowed(false);
		measurementsTable.setColumnSelectionAllowed(false);

		// right align the "source" text
		measurementsTable.getColumnModel().getColumn(2).setCellRenderer(new RightTextRenderer());

		// notify whenever the data changes
		measurementsTable.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				// recalculate the average
				calculateFinal();
			}
		});

		// set up any buttons
		final JDialog glue = this;
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					finalValue = Integer.valueOf(newValue.getText());
					if(dev != null) {
						dev.close();
						dev = null;
					}
					dispose();
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(glue, 
							"Invalid integer value: " + newValue.getText(), "Error", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				finalValue = null;
				if(dev != null) {
					dev.close();
					dev = null;
				}
				dispose();
			}
		});

		// initialize our measuring device
		initMeasuringDevice();
	}

	private void calculateFinal() {
		float sum = 0;
		int count = 0;

		// do an average
		for(AMeasurement m : measurements) {
			if(m.enabled) {
				sum += m.value;
				count++;
			}
		}

		// nothing selected? boo
		if(count == 0) {
			newValue.setText("");
			return;
		}

		// calc average
		float avg = sum / (float) count;

		// and set it!
		newValue.setText("" + (int) Math.round(avg));
	}

	// lame holder for a measurement to be listed in our table...
	private class AMeasurement {	
		public AMeasurement(boolean enabled, Integer value, String source) {
			this.enabled = enabled;
			this.value = value;
			this.source = source;
		}

		public boolean enabled;
		public Integer value;
		public String source;
	}

	public class RightTextRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		public RightTextRenderer() {
			super();

			setHorizontalAlignment(SwingConstants.RIGHT);
		}
	}

	public class MeasurementsTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private String[] columnNames = { "Use?", "Value", "From" };

		public int getColumnCount() {
			return 3;
		}

		public int getRowCount() {
			return measurements.size() + 1;
		}

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

		@Override
		public Class<?> getColumnClass(int col) {
			switch(col) {
			case 0:
				return Boolean.class;
			case 1:
				return Integer.class;
			case 2:
				return String.class;
			}

			return String.class;
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			// can't edit source!
			return (col == 2) ? false : true;
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			// inserting a new value!
			if(row == measurements.size()) {
				if(col == 1 && value.toString().length() > 0) {
					int newval = Integer.valueOf(value.toString()).intValue();

					if(newval > 0) {
						AMeasurement m = new AMeasurement(true, newval, "manual");
						measurements.add(m);
						fireTableRowsUpdated(row, row);
					}
				}
				return;
			}

			AMeasurement m = measurements.get(row);

			switch(col) {
			case 0:
				m.enabled = ((Boolean) value).booleanValue();
				fireTableCellUpdated(row, col);
				break;

			case 1:
				int newValue = Integer.valueOf(value.toString()).intValue();
				if(newValue != m.value) {
					m.value = newValue; 
					m.source = "manual";
					fireTableRowsUpdated(row, row);
				}
				break;
			}

		}

		public Object getValueAt(int row, int col) {
			if(row == measurements.size())
				return null;

			AMeasurement m = measurements.get(row);

			switch(col) {
			case 0:
				return m.enabled;
			case 1:
				return m.value;
			case 2:
				return m.source;
			}

			return null;
		}
	}

	private void formWindowClosed(java.awt.event.WindowEvent evt) {
		// make sure we close the device.
		if(dev != null) {
			dev.close();
			dev = null;
		}
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings({ "unchecked", "serial" })
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jTextArea1 = new javax.swing.JTextArea();
		jScrollPane1 = new javax.swing.JScrollPane();
		measurementsTable = new javax.swing.JTable();
		jLabel1 = new javax.swing.JLabel();
		newValue = new javax.swing.JTextField();
		okBtn = new javax.swing.JButton();
		cancelBtn = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		devStatus = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent evt) {
				formWindowClosed(evt);
			}
		});

		jTextArea1.setColumns(20);
		jTextArea1.setEditable(false);
		jTextArea1.setFont(new java.awt.Font("Dialog", 0, 13));
		jTextArea1.setLineWrap(true);
		jTextArea1.setRows(5);
		jTextArea1.setText("The average of the checked values is presented below. New values may be created by manually entering them into the table or by using the measuring device, if enabled. Alternatively, you can enter a final value manually. Pressing \"apply\" will change the value in both measurements.");
		jTextArea1.setWrapStyleWord(true);
		jTextArea1.setAutoscrolls(false);
		jTextArea1.setBorder(null);
		jTextArea1.setFocusable(false);
		jTextArea1.setOpaque(false);

		measurementsTable.setModel(new javax.swing.table.DefaultTableModel(
				new Object [][] {
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null}
				},
				new String [] {
						"Use?", "Value", "Source"
				}
		) {
			Class[] types = new Class [] {
					java.lang.Object.class, java.lang.Integer.class, java.lang.String.class
			};
			boolean[] canEdit = new boolean [] {
					true, true, false
			};

			public Class getColumnClass(int columnIndex) {
				return types [columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit [columnIndex];
			}
		});
		measurementsTable.getTableHeader().setReorderingAllowed(false);
		jScrollPane1.setViewportView(measurementsTable);
		measurementsTable.getColumnModel().getColumn(0).setResizable(false);
		measurementsTable.getColumnModel().getColumn(1).setResizable(false);
		measurementsTable.getColumnModel().getColumn(2).setResizable(false);

		jLabel1.setLabelFor(newValue);
		jLabel1.setText("Final value:");

		okBtn.setText("Apply");

		cancelBtn.setText("Cancel");

		jLabel2.setText("Device status:");

		devStatus.setText("unknown");

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout.createSequentialGroup()
						.addContainerGap()
						.add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 137, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.add(18, 18, 18)
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
								.add(jTextArea1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
								.add(layout.createSequentialGroup()
										.add(jLabel1)
										.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
										.add(newValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 116, Short.MAX_VALUE)
										.add(okBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 66, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
										.add(cancelBtn))
										.add(layout.createSequentialGroup()
												.add(jLabel2)
												.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
												.add(devStatus)))
												.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout.createSequentialGroup()
						.addContainerGap()
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
								.add(layout.createSequentialGroup()
										.add(jTextArea1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 121, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
										.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
												.add(jLabel2)
												.add(devStatus))
												.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 27, Short.MAX_VALUE)
												.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
														.add(jLabel1)
														.add(newValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
														.add(cancelBtn)
														.add(okBtn)))
														.add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
														.addContainerGap())
		);

		pack();
	}// </editor-fold>

	// Variables declaration - do not modify
	private javax.swing.JButton cancelBtn;
	private javax.swing.JLabel devStatus;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextArea jTextArea1;
	private javax.swing.JTable measurementsTable;
	private javax.swing.JTextField newValue;
	private javax.swing.JButton okBtn;
	// End of variables declaration

}
