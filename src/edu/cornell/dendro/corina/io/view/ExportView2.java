/**
 * Created at Jan 24, 2011, 3:11:52 PM
 */
package edu.cornell.dendro.corina.io.view;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSplitPane;
import java.awt.FlowLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;

/**
 * @author Daniel
 *
 */
public class ExportView2 extends JFrame {
	private JTextField textField;
	public ExportView2() {
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		
		JTree tree = new JTree();
		scrollPane.setViewportView(tree);
		
		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JButton btnHelp = new JButton("Help");
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel_1.add(btnHelp);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panel_1.add(horizontalGlue);
		
		JButton btnConvert = new JButton("Convert");
		panel_1.add(btnConvert);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new MigLayout("", "[97px,grow][103px,grow]", "[27px][27px][][][][][]"));
		
		JLabel lblWhatToExport = new JLabel("What to export:");
		panel_2.add(lblWhatToExport, "cell 0 0,alignx left,aligny center");
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Testing", "Hi"}));
		panel_2.add(comboBox, "cell 1 0,growx");
		
		JLabel lblGrouping = new JLabel("Grouping:");
		panel_2.add(lblGrouping, "cell 0 1,alignx left");
		
		JComboBox comboBox_1 = new JComboBox();
		panel_2.add(comboBox_1, "cell 1 1,growx");
		
		JLabel lblFormat = new JLabel("Format:");
		panel_2.add(lblFormat, "cell 0 3,alignx left");
		
		JComboBox comboBox_2 = new JComboBox();
		panel_2.add(comboBox_2, "cell 1 3,growx");
		
		JLabel lblEncoding = new JLabel("Encoding:");
		panel_2.add(lblEncoding, "cell 0 4,alignx left");
		
		JComboBox comboBox_3 = new JComboBox();
		panel_2.add(comboBox_3, "cell 1 4,growx");
		
		JLabel lblOutputFolder = new JLabel("Output folder:");
		panel_2.add(lblOutputFolder, "cell 0 5,alignx left");
		
		JButton btnBrowse = new JButton("Browse");
		panel_2.add(btnBrowse, "cell 1 5");
		
		textField = new JTextField();
		panel_2.add(textField, "cell 0 6 2 1,growx");
		textField.setColumns(10);
	}

}
