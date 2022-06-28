/*
 * ReconcileMeasureDialog.java
 *
 * Created on September 16, 2008, 9:17 PM
 */

package org.tellervo.desktop.manip;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.tellervo.desktop.Year;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice;
import org.tellervo.desktop.hardware.MeasuringDeviceSelector;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author  Lucas Madar
 */
public class ReconcileMeasureDialog extends javax.swing.JDialog {   
	private static final long serialVersionUID = 1L;
	private Sample src, ref;
	private int yearIndex;
	private ArrayList<AMeasurement> measurements;
	private Integer finalValue;
	private AbstractMeasuringDevice dev;
	
	/** Creates new form ReconcileMeasureDialog */
	public ReconcileMeasureDialog(java.awt.Frame parent, boolean modal, Sample src, Sample ref, Year year) {
		super(parent, modal);

		initComponents();
		initMeasuringDevice();
		initMeasuringPanel();

		this.src = src;
		this.ref = ref;

		//this.year = year;
		this.yearIndex = year.diff(src.getStart());

		setTitle("Remeasuring for " + year + " (index " + yearIndex + ")");
		initialize();
		this.setPreferredSize(new Dimension(600,220));
		pack();
		panelMeasure.setDefaultFocus();
		
	}

	private void initMeasuringPanel()
	{
		panelMeasure = new ReconcileMeasurePanel(this, dev);
		panelMeasureHolder.add(panelMeasure, BorderLayout.CENTER);

		pack();
	}

	private void initMeasuringDevice()
	{
		try {
			dev = MeasuringDeviceSelector.getSelectedDevice(true);
			dev.setMeasurementReceiver(this.panelMeasure);
			devStatus.setText("Initialized");
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



	private void initialize() { 


		// now, stuff that really matters
		measurements = new ArrayList<AMeasurement>();

		// add our first two measurements
		measurements.add(new AMeasurement(true, 
				((Number) src.getRingWidthData().get(yearIndex)).intValue(), 
				"source"));
		measurements.add(new AMeasurement(true, 
				((Number) ref.getRingWidthData().get(yearIndex)).intValue(), 
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
					glue.dispose();
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
				glue.dispose();
			}
		});


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
		panelMeasure.cleanup();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings({ "unchecked", "serial" })
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		jScrollPane1 = new javax.swing.JScrollPane();
		measurementsTable = new javax.swing.JTable();
		jLabel1 = new javax.swing.JLabel();
		newValue = new javax.swing.JTextField();
		newValue.setHorizontalAlignment(SwingConstants.RIGHT);
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
		measurementsTable.setMinimumSize(new Dimension(100,300));

		jLabel1.setLabelFor(newValue);
		jLabel1.setText("Final value:");

		okBtn.setText("Apply");

		cancelBtn.setText("Cancel");

		jLabel2.setText("Device status:");

		devStatus.setText("unknown");
		
		panelMeasureHolder = new JPanel();
		panelMeasureHolder.setBorder(null);
		panelMeasureHolder.setLayout(new BorderLayout());

		
		
		getContentPane().setLayout(new MigLayout("", "[137px:137px,fill][59px][80px:80px:80px,right][][][28.00,grow][][66px][6px][80px]", "[101px][][6px][140.00px,grow][6.00px][14px][29px]"));
		getContentPane().add(jScrollPane1, "cell 0 0 1 7,grow");
		getContentPane().add(jLabel2, "cell 1 5,alignx left,aligny top");
		getContentPane().add(devStatus, "cell 2 5 7 1,alignx left,aligny top");
		getContentPane().add(jLabel1, "cell 1 6,alignx left,aligny center");
		getContentPane().add(newValue, "cell 2 6 2 1,growx,aligny top");
		getContentPane().add(okBtn, "cell 7 6,growx,aligny top");
		getContentPane().add(cancelBtn, "cell 9 6,alignx left,aligny top");
		getContentPane().add(panelMeasureHolder, "cell 1 0 9 4,grow");

	}// </editor-fold>

	// Variables declaration - do not modify
	private javax.swing.JButton cancelBtn;
	private javax.swing.JLabel devStatus;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTable measurementsTable;
	private javax.swing.JTextField newValue;
	private javax.swing.JButton okBtn;
	private JPanel panelMeasureHolder;
	private ReconcileMeasurePanel panelMeasure;
	// End of variables declaration
	
	
	/**
	 * Add a measurement to the table
	 */
	public void addMeasurementValue(AMeasurement value)
	{
		measurements.add(value);
		
		// notify our table
		((MeasurementsTableModel)measurementsTable.getModel()).fireTableDataChanged();
	}
}
