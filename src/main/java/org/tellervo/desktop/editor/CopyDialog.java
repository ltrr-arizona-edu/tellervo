/**
 * 
 */
package org.tellervo.desktop.editor;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.util.Center;

import net.miginfocom.swing.MigLayout;



/**
 * @author Lucas Madar
 *
 */
public class CopyDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	boolean cancelled = true;
	private JTextField txtStartRange, txtEndRange;
	private Range range;
	private Range completedRange;
	private JLabel lblCopyFrom;
	private JLabel lblTo;
	private JCheckBox chkAllYears;
	
	public Range getChosenRange() {
		return completedRange;
	}
	
	public boolean isOk() {
		return !cancelled;
	}
	
	@SuppressWarnings("serial")
	public CopyDialog(JFrame parent, Range sampleRange) {
		super(parent, "Copy range...", true);
		range = sampleRange;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());
		
		txtStartRange = new JTextField(range.getStart().toString());
		txtStartRange.setEnabled(false);
		JLabel label;
		
		JPanel center = new JPanel();
		center.setLayout(new MigLayout("", "[77px,right][grow][][grow]", "[][19px]"));
		
		chkAllYears = new JCheckBox("All years");
		chkAllYears.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setGui();
				
			}

			
		});
		chkAllYears.setSelected(true);
		center.add(chkAllYears, "cell 1 0 3 1");
		lblCopyFrom = new JLabel("From:");
		lblCopyFrom.setEnabled(false);
		center.add(lblCopyFrom, "cell 0 1,alignx right,growy");
		center.add(txtStartRange, "cell 1 1,grow");
		lblTo = new JLabel("to");
		lblTo.setEnabled(false);
		center.add(lblTo, "cell 2 1,grow");
		txtEndRange = new JTextField(range.getEnd().toString());
		txtEndRange.setEnabled(false);
		center.add(txtEndRange, "cell 3 1,grow");
		
		center.setBorder(BorderFactory.createTitledBorder("Copy measurements"));
		
		getContentPane().add(center, BorderLayout.CENTER);
				
		JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton copy = new JButton("Copy");
		copy.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				Year ybeg, yend;
				
				try {
					ybeg = new Year(Integer.parseInt(txtStartRange.getText()));
				} catch (NumberFormatException nfe) {
					Alert.error("Can't copy", "Invalid start range is not a number!");
					return;
				}
				
				if(ybeg.compareTo(range.getStart()) < 0) {
					Alert.error("Can't copy", "Start year is less than sample start year (" + range.getStart().toString() + ")");
					return;
				}
				
				try {
					yend = new Year(Integer.parseInt(txtEndRange.getText()));
				} catch (NumberFormatException nfe) {
					Alert.error("Can't copy", "Invalid end of range is not a number!");
					return;
				}
				
				if(yend.compareTo(range.getEnd()) > 0) {
					Alert.error("Can't copy", "End year is greater than sample end year (" + range.getEnd().toString() + ")");
					return;
				}
				
				if(ybeg.compareTo(yend) >= 0) {
					Alert.error("Can't copy", "Invalid range (must be at least one year, must be from low to high!)");
				}
				
				completedRange = new Range(ybeg, yend);
				cancelled = false;
				dispose();
			}
		});
		south.add(copy);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
		south.add(cancel);		
		
		getContentPane().add(south, BorderLayout.SOUTH);
		
		pack();
		Center.center(this, parent);
		setVisible(true);
	}
	
	private void setGui()
	{
		lblCopyFrom.setEnabled(!chkAllYears.isSelected());
		lblTo.setEnabled(!chkAllYears.isSelected());
		txtStartRange.setEnabled(!chkAllYears.isSelected());
		txtEndRange.setEnabled(!chkAllYears.isSelected());
	}

}
