package org.tellervo.desktop.gui.widgets;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.tridasv2.ui.ComboBoxFilterable;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.labels.ui.TridasListCellRenderer;
import org.tellervo.schema.WSIBox;
import org.tridas.interfaces.ITridas;

import com.dmurph.mvc.model.MVCArrayList;

import edu.emory.mathcs.backport.java.util.Collections;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;

public class WSIBoxPicker extends JDialog implements ActionListener{

	private final JPanel contentPanel = new JPanel();

	private WSIBox boxChosen = null;
    private ComboBoxFilterable cboBox;
	private MVCArrayList<WSIBox> boxList;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WSIBoxPicker dialog = new WSIBoxPicker();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static WSIBox pickBox(Window parent, String title)
	{
		
		WSIBoxPicker dialog = new WSIBoxPicker();
		dialog.setTitle(title);
        dialog.setLocationRelativeTo(parent);
        dialog.pack();
        
        
        dialog.setVisible(true); // blocks until user brings dialog down...
       	

        return dialog.getSelectedBox();

	}
	

	/**
	 * Create the dialog.
	 */
	public WSIBoxPicker() {
		setBounds(100, 100, 450, 300);
		setIconImage(Builder.getApplicationIcon());
		setTitle("Pick Box");
		setModal(true);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[]"));
		{
			JLabel lblBox = new JLabel("Box:");
			contentPanel.add(lblBox, "cell 0 0,alignx trailing");
		}
		{
			// Set up box list model etc
			boxList = (MVCArrayList<WSIBox>) Dictionary.getMutableDictionary("boxDictionary");
			TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
					TridasComparator.NullBehavior.NULLS_LAST, 
					TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
			Collections.sort(boxList, numSorter);
		
			WSIBox[] arr = new WSIBox[boxList.size()];
			
			for (int i=0; i<boxList.size(); i++)
			{
				arr[i] = boxList.get(i);
			}
			cboBox = new ComboBoxFilterable(arr);
			TridasListCellRenderer tlcr = new TridasListCellRenderer();
			cboBox.setRenderer(tlcr);			
			contentPanel.add(cboBox, "cell 1 0,growx");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("Cancel"))
		{
			this.boxChosen = null;
			this.setVisible(false);
		}
		
		if(evt.getActionCommand().equals("OK"))
		{
			this.boxChosen = (WSIBox) cboBox.getSelectedItem();
			this.setVisible(false);
		}
	}

	
	public WSIBox getSelectedBox()
	{
		return this.boxChosen;
	}
}
