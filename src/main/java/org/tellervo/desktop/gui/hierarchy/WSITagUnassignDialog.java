package org.tellervo.desktop.gui.hierarchy;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.AbstractListModel;

import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.schema.WSITag;
import org.tridas.interfaces.ITridasSeries;

import net.miginfocom.swing.MigLayout;

public class WSITagUnassignDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JList<WSITag> lstTags;
	private DefaultListModel<WSITag> model;


	/**
	 * Create the dialog.
	 */
	public WSITagUnassignDialog(ArrayList<WSITag> tags) {
		
		setupGUI();
		populateList(tags);
	}

	private void populateList(ArrayList<WSITag> tags)
	{
		model = new DefaultListModel<WSITag>();
		
		for(WSITag tag : tags)
		{
			model.addElement(tag);
		}
		
		lstTags.setModel(model);
	}
	
	private void setupGUI()
	{
		setTitle("Unassign Tags");
		this.setIconImage(Builder.getApplicationIcon());
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[436px][]", "[228px,top]"));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "cell 0 0,grow");
			{
				lstTags = new JList<WSITag>();
				scrollPane.setViewportView(lstTags);
			}
		}
		{
			JButton btnRemove = new JButton("");
			btnRemove.setIcon(Builder.getIcon("cancel.png", 16));
			btnRemove.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent evt) {
					if(lstTags.getSelectedIndex()>=0)
					{
						model.remove(lstTags.getSelectedIndex());
					}
				}
				
			});
			contentPanel.add(btnRemove, "cell 1 0");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public static void showDialog(Component parent, ITridasSeries series)
	{
		
		WSITagUnassignDialog dialog = new WSITagUnassignDialog(null);
		
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
	}
}
