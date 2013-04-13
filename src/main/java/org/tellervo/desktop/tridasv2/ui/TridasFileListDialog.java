package org.tellervo.desktop.tridasv2.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.tridas.schema.TridasFile;

public class TridasFileListDialog extends JDialog implements ActionListener{

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNewFile;
	private ArrayList<TridasFile> fileList;

	/**
	 * Create the dialog.
	 */
	public TridasFileListDialog(Component parent, ArrayList<TridasFile> fileList) {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[285.00px,grow,left][][]", "[][][grow,top]"));
		{
			txtNewFile = new JTextField();
			contentPanel.add(txtNewFile, "cell 0 0,growx");
			txtNewFile.setColumns(10);
		}
		{
			JButton btnBrowse = new JButton("...");
			contentPanel.add(btnBrowse, "flowx,cell 1 0");
		}
		{
			JButton btnAdd = new JButton("+");
			contentPanel.add(btnAdd, "cell 2 0,alignx left");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "cell 0 2,grow");
			{
				JList lstFileList = new JList();
				scrollPane.setViewportView(lstFileList);
				lstFileList.setFont(new Font("Dialog", Font.PLAIN, 12));
				lstFileList.setModel(new AbstractListModel() {
					String[] values = new String[] {"http://blah.com/myfile.pdf", "//localserver/anotherfile.jpg", "http://blah.com/myfile.pdf", "http://blah.com/myfile.pdf", "http://blah.com/myfile.pdf", "http://blah.com/myfile.pdf", "http://blah.com/myfile.pdf", "http://blah.com/myfile.pdf", "http://blah.com/myfile.pdf", "http://blah.com/myfile.pdf", "http://blah.com/myfile.pdf", "http://blah.com/myfile.pdf", "http://blah.com/myfile.pdf"};
					public int getSize() {
						return values.length;
					}
					public Object getElementAt(int index) {
						return values[index];
					}
				});
				lstFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
		}
		{
			JButton btnRemove = new JButton("-");
			contentPanel.add(btnRemove, "flowx,cell 1 2,aligny top");
		}
		{
			JButton btnOpen = new JButton("Open");
			contentPanel.add(btnOpen, "cell 2 2");
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
				cancelButton.addActionListener(this);
			}
		}
	}

	public ArrayList<TridasFile> getFileList()
	{
		return this.fileList;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		
		if(ev.getActionCommand()=="Cancel")
		{
			this.dispose();
		}
		
	}

}
