/*
 * Created by JFormDesigner on Thu Feb 21 17:07:59 GMT 2008
 */

package edu.cornell.dendro.corina.gui.newui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author aps03pwb
 */
public class PrototypeMain extends JDialog {
	public PrototypeMain(Frame owner) {
		super(owner);
		initComponents();
		setVisible(true);
	}

	public PrototypeMain(Dialog owner) {
		super(owner);
		initComponents();
		setVisible(true);
	}
	
	public PrototypeMain(String string) {
		// TODO Auto-generated constructor stub
		this((Frame) null);
	}

	public static void showMe(){
		
		PrototypeMain myDialog = new PrototypeMain("hello");

		myDialog.show();
		
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Charlotte Pearson
		panel2 = new JPanel();
		this2 = new JPanel();
		siteDetailsPanel = new JPanel();
		label5 = new JLabel();
		cboSiteCode = new JComboBox();
		label6 = new JLabel();
		cboSiteName = new JComboBox();
		label7 = new JLabel();
		cboSubSite = new JComboBox();
		locationMapPanel = new JPanel();
		treeDetailsPanel = new JPanel();
		label8 = new JLabel();
		txtTreeCode = new JTextField();
		label9 = new JLabel();
		cboTaxon = new JComboBox();
		label10 = new JLabel();
		locationPanel = new JPanel();
		label11 = new JLabel();
		label12 = new JLabel();
		label13 = new JLabel();
		textField1 = new JTextField();
		textField2 = new JTextField();
		spinner1 = new JSpinner();
		label14 = new JLabel();
		specimenDetailsPanel = new JPanel();
		label15 = new JLabel();
		textField3 = new JTextField();
		label19 = new JLabel();
		comboBox6 = new JComboBox();
		label25 = new JLabel();
		checkBox1 = new JCheckBox();
		label16 = new JLabel();
		textField4 = new JTextField();
		label20 = new JLabel();
		comboBox7 = new JComboBox();
		label26 = new JLabel();
		checkBox2 = new JCheckBox();
		label17 = new JLabel();
		comboBox4 = new JComboBox();
		label21 = new JLabel();
		comboBox8 = new JComboBox();
		label27 = new JLabel();
		checkBox3 = new JCheckBox();
		label18 = new JLabel();
		comboBox5 = new JComboBox();
		label22 = new JLabel();
		spinner2 = new JSpinner();
		label28 = new JLabel();
		checkBox4 = new JCheckBox();
		label23 = new JLabel();
		spinner3 = new JSpinner();
		label29 = new JLabel();
		checkBox5 = new JCheckBox();
		label24 = new JLabel();
		spinner4 = new JSpinner();
		label30 = new JLabel();
		checkBox6 = new JCheckBox();
		measurementDetailsPanel = new JPanel();
		label31 = new JLabel();
		textField5 = new JTextField();
		label32 = new JLabel();
		comboBox9 = new JComboBox();
		checkBox7 = new JCheckBox();
		label33 = new JLabel();
		comboBox10 = new JComboBox();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridLayout());

		//======== panel2 ========
		{

			// JFormDesigner evaluation mark
			panel2.setBorder(new javax.swing.border.CompoundBorder(
				new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
					"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
					javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
					java.awt.Color.red), panel2.getBorder())); panel2.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

			panel2.setLayout(new FormLayout(
				"default:grow",
				"default:grow"));

			//======== this2 ========
			{
				this2.setLayout(new FormLayout(
					new ColumnSpec[] {
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(182), FormSpec.DEFAULT_GROW)
					},
					new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC
					}));

				//======== siteDetailsPanel ========
				{
					siteDetailsPanel.setBorder(new TitledBorder("Site Details"));
					siteDetailsPanel.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(Sizes.dluX(50)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						new RowSpec[] {
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC
						}));

					//---- label5 ----
					label5.setText("Code:");
					siteDetailsPanel.add(label5, cc.xy(1, 1));

					//---- cboSiteCode ----
					cboSiteCode.setModel(new DefaultComboBoxModel(new String[] {
						"YMK",
						"ABC"
					}));
					siteDetailsPanel.add(cboSiteCode, cc.xy(3, 1));

					//---- label6 ----
					label6.setText("Name:");
					siteDetailsPanel.add(label6, cc.xy(1, 3));

					//---- cboSiteName ----
					cboSiteName.setModel(new DefaultComboBoxModel(new String[] {
						"Yenikapi"
					}));
					siteDetailsPanel.add(cboSiteName, cc.xy(3, 3));

					//---- label7 ----
					label7.setText("Sub site:");
					siteDetailsPanel.add(label7, cc.xy(1, 5));

					//---- cboSubSite ----
					cboSubSite.setModel(new DefaultComboBoxModel(new String[] {
						"Main"
					}));
					siteDetailsPanel.add(cboSubSite, cc.xy(3, 5));
				}
				this2.add(siteDetailsPanel, cc.xy(1, 1));

				//======== locationMapPanel ========
				{
					locationMapPanel.setBorder(new TitledBorder("Location Map"));
					locationMapPanel.setLayout(new FormLayout(
						"default:grow",
						"default:grow"));
				}
				this2.add(locationMapPanel, cc.xywh(3, 1, 1, 3));

				//======== treeDetailsPanel ========
				{
					treeDetailsPanel.setBorder(new TitledBorder("Tree Details"));
					treeDetailsPanel.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(Sizes.dluX(50)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						new RowSpec[] {
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC
						}));

					//---- label8 ----
					label8.setText("Code:");
					treeDetailsPanel.add(label8, cc.xy(1, 1));
					treeDetailsPanel.add(txtTreeCode, cc.xy(3, 1));

					//---- label9 ----
					label9.setText("Taxon:");
					treeDetailsPanel.add(label9, cc.xy(1, 3));

					//---- cboTaxon ----
					cboTaxon.setModel(new DefaultComboBoxModel(new String[] {
						"Pinus nigra",
						"Quercus robur"
					}));
					treeDetailsPanel.add(cboTaxon, cc.xy(3, 3));

					//---- label10 ----
					label10.setText("Location:");
					treeDetailsPanel.add(label10, cc.xy(1, 5));

					//======== locationPanel ========
					{
						locationPanel.setLayout(new FormLayout(
							new ColumnSpec[] {
								FormFactory.DEFAULT_COLSPEC,
								FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
								FormFactory.DEFAULT_COLSPEC,
								FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
								FormFactory.DEFAULT_COLSPEC,
								FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
								FormFactory.DEFAULT_COLSPEC
							},
							new RowSpec[] {
								FormFactory.DEFAULT_ROWSPEC,
								FormFactory.LINE_GAP_ROWSPEC,
								FormFactory.DEFAULT_ROWSPEC
							}));

						//---- label11 ----
						label11.setText("Latitude");
						locationPanel.add(label11, cc.xy(1, 1));

						//---- label12 ----
						label12.setText("Longitude");
						locationPanel.add(label12, cc.xy(3, 1));

						//---- label13 ----
						label13.setText("Precision");
						locationPanel.add(label13, cc.xy(5, 1));
						locationPanel.add(textField1, cc.xy(1, 3));
						locationPanel.add(textField2, cc.xy(3, 3));

						//---- spinner1 ----
						spinner1.setModel(new SpinnerNumberModel(1000, null, null, 1));
						locationPanel.add(spinner1, cc.xy(5, 3));

						//---- label14 ----
						label14.setText("m");
						locationPanel.add(label14, cc.xy(7, 3));
					}
					treeDetailsPanel.add(locationPanel, cc.xy(3, 5));
				}
				this2.add(treeDetailsPanel, cc.xy(1, 3));

				//======== specimenDetailsPanel ========
				{
					specimenDetailsPanel.setBorder(new TitledBorder("Specimen Details"));
					specimenDetailsPanel.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(Sizes.dluX(50)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(60)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						new RowSpec[] {
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC
						}));

					//---- label15 ----
					label15.setText("Code:");
					specimenDetailsPanel.add(label15, cc.xy(1, 1));
					specimenDetailsPanel.add(textField3, cc.xy(3, 1));

					//---- label19 ----
					label19.setText("Terminal ring:");
					specimenDetailsPanel.add(label19, cc.xy(5, 1));
					specimenDetailsPanel.add(comboBox6, cc.xy(7, 1));

					//---- label25 ----
					label25.setText("Verified:");
					specimenDetailsPanel.add(label25, cc.xy(9, 1));
					specimenDetailsPanel.add(checkBox1, cc.xy(11, 1));

					//---- label16 ----
					label16.setText("Collection date:");
					specimenDetailsPanel.add(label16, cc.xy(1, 3));
					specimenDetailsPanel.add(textField4, cc.xy(3, 3));

					//---- label20 ----
					label20.setText("Quality:");
					specimenDetailsPanel.add(label20, cc.xy(5, 3));
					specimenDetailsPanel.add(comboBox7, cc.xy(7, 3));

					//---- label26 ----
					label26.setText("Verified:");
					specimenDetailsPanel.add(label26, cc.xy(9, 3));
					specimenDetailsPanel.add(checkBox2, cc.xy(11, 3));

					//---- label17 ----
					label17.setText("Type:");
					specimenDetailsPanel.add(label17, cc.xy(1, 5));

					//---- comboBox4 ----
					comboBox4.setModel(new DefaultComboBoxModel(new String[] {
						"- Not specified -"
					}));
					specimenDetailsPanel.add(comboBox4, cc.xy(3, 5));

					//---- label21 ----
					label21.setText("Pith:");
					specimenDetailsPanel.add(label21, cc.xy(5, 5));
					specimenDetailsPanel.add(comboBox8, cc.xy(7, 5));

					//---- label27 ----
					label27.setText("Verified:");
					specimenDetailsPanel.add(label27, cc.xy(9, 5));
					specimenDetailsPanel.add(checkBox3, cc.xy(11, 5));

					//---- label18 ----
					label18.setText("Continuity:");
					specimenDetailsPanel.add(label18, cc.xy(1, 7));
					specimenDetailsPanel.add(comboBox5, cc.xy(3, 7));

					//---- label22 ----
					label22.setText("Sapwood count:");
					specimenDetailsPanel.add(label22, cc.xy(5, 7));
					specimenDetailsPanel.add(spinner2, cc.xy(7, 7));

					//---- label28 ----
					label28.setText("Verified:");
					specimenDetailsPanel.add(label28, cc.xy(9, 7));
					specimenDetailsPanel.add(checkBox4, cc.xy(11, 7));

					//---- label23 ----
					label23.setText("Unmeasured rings at beginning:");
					specimenDetailsPanel.add(label23, cc.xy(5, 9));
					specimenDetailsPanel.add(spinner3, cc.xy(7, 9));

					//---- label29 ----
					label29.setText("Verified:");
					specimenDetailsPanel.add(label29, cc.xy(9, 9));
					specimenDetailsPanel.add(checkBox5, cc.xy(11, 9));

					//---- label24 ----
					label24.setText("Unmeasured rings at end:");
					specimenDetailsPanel.add(label24, cc.xy(5, 11));
					specimenDetailsPanel.add(spinner4, cc.xy(7, 11));

					//---- label30 ----
					label30.setText("Verified:");
					specimenDetailsPanel.add(label30, cc.xy(9, 11));
					specimenDetailsPanel.add(checkBox6, cc.xy(11, 11));
				}
				this2.add(specimenDetailsPanel, cc.xywh(1, 5, 3, 1));

				//======== measurementDetailsPanel ========
				{
					measurementDetailsPanel.setBorder(new TitledBorder("Measurement Details"));
					measurementDetailsPanel.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(Sizes.dluX(50)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						new RowSpec[] {
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC
						}));

					//---- label31 ----
					label31.setText("Code:");
					measurementDetailsPanel.add(label31, cc.xy(1, 1));
					measurementDetailsPanel.add(textField5, cc.xy(3, 1));

					//---- label32 ----
					label32.setText("Radius:");
					measurementDetailsPanel.add(label32, cc.xy(1, 3));
					measurementDetailsPanel.add(comboBox9, cc.xy(3, 3));

					//---- checkBox7 ----
					checkBox7.setText("Reconciled?");
					measurementDetailsPanel.add(checkBox7, cc.xy(3, 5));

					//---- label33 ----
					label33.setText("Measured by:");
					measurementDetailsPanel.add(label33, cc.xy(1, 7));
					measurementDetailsPanel.add(comboBox10, cc.xy(3, 7));
				}
				this2.add(measurementDetailsPanel, cc.xywh(1, 7, 2, 1));
			}
			panel2.add(this2, cc.xy(1, 1));
		}
		contentPane.add(panel2);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Charlotte Pearson
	private JPanel panel2;
	private JPanel this2;
	private JPanel siteDetailsPanel;
	private JLabel label5;
	private JComboBox cboSiteCode;
	private JLabel label6;
	private JComboBox cboSiteName;
	private JLabel label7;
	private JComboBox cboSubSite;
	private JPanel locationMapPanel;
	private JPanel treeDetailsPanel;
	private JLabel label8;
	private JTextField txtTreeCode;
	private JLabel label9;
	private JComboBox cboTaxon;
	private JLabel label10;
	private JPanel locationPanel;
	private JLabel label11;
	private JLabel label12;
	private JLabel label13;
	private JTextField textField1;
	private JTextField textField2;
	private JSpinner spinner1;
	private JLabel label14;
	private JPanel specimenDetailsPanel;
	private JLabel label15;
	private JTextField textField3;
	private JLabel label19;
	private JComboBox comboBox6;
	private JLabel label25;
	private JCheckBox checkBox1;
	private JLabel label16;
	private JTextField textField4;
	private JLabel label20;
	private JComboBox comboBox7;
	private JLabel label26;
	private JCheckBox checkBox2;
	private JLabel label17;
	private JComboBox comboBox4;
	private JLabel label21;
	private JComboBox comboBox8;
	private JLabel label27;
	private JCheckBox checkBox3;
	private JLabel label18;
	private JComboBox comboBox5;
	private JLabel label22;
	private JSpinner spinner2;
	private JLabel label28;
	private JCheckBox checkBox4;
	private JLabel label23;
	private JSpinner spinner3;
	private JLabel label29;
	private JCheckBox checkBox5;
	private JLabel label24;
	private JSpinner spinner4;
	private JLabel label30;
	private JCheckBox checkBox6;
	private JPanel measurementDetailsPanel;
	private JLabel label31;
	private JTextField textField5;
	private JLabel label32;
	private JComboBox comboBox9;
	private JCheckBox checkBox7;
	private JLabel label33;
	private JComboBox comboBox10;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
