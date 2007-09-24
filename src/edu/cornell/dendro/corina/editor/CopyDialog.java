/**
 * 
 */
package corina.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import corina.Range;
import corina.Year;
import corina.ui.Alert;
import corina.util.Center;


/**
 * @author Lucas Madar
 *
 */
public class CopyDialog extends JDialog {
	boolean cancelled = true;
	private JTextField start, end;
	private Range range;
	private Range completedRange;
	
	public Range getChosenRange() {
		return completedRange;
	}
	
	public boolean isOk() {
		return !cancelled;
	}
	
	public CopyDialog(JFrame parent, Range sampleRange) {
		super(parent, "Copy range...", true);
		range = sampleRange;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		setLayout(new BorderLayout());
		
		start = new JTextField(range.getStart().toString());
		end = new JTextField(range.getEnd().toString());
		JLabel label;
		
		JPanel center = new JPanel(new GridLayout(2, 2));
		label = new JLabel("Start year");
		center.add(label);
		center.add(start);
		label = new JLabel("End year");
		center.add(label);
		center.add(end);
		
		center.setBorder(BorderFactory.createTitledBorder("Copy range"));
		
		add(center, BorderLayout.CENTER);
				
		JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton copy = new JButton("Copy");
		copy.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				Year ybeg, yend;
				
				try {
					ybeg = new Year(Integer.parseInt(start.getText()));
				} catch (NumberFormatException nfe) {
					Alert.error("Can't copy", "Invalid start range is not a number!");
					return;
				}
				
				if(ybeg.compareTo(range.getStart()) < 0) {
					Alert.error("Can't copy", "Start year is less than sample start year (" + range.getStart().toString() + ")");
					return;
				}
				
				try {
					yend = new Year(Integer.parseInt(end.getText()));
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
		
		add(south, BorderLayout.SOUTH);
		
		pack();
		Center.center(this, parent);
		setVisible(true);
	}
}
