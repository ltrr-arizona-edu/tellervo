package org.tellervo.desktop.gui.widgets;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.gui.dbbrowse.TridasProjectRenderer;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.tridasv2.ui.ComboBoxFilterable;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.labels.ui.TridasListCellRenderer;
import org.tellervo.schema.WSIBox;
import org.tridas.schema.TridasProject;

import com.dmurph.mvc.model.MVCArrayList;

import edu.emory.mathcs.backport.java.util.Collections;
import net.miginfocom.swing.MigLayout;

public class TridasProjectPicker extends JDialog implements ActionListener{

	private final JPanel contentPanel = new JPanel();

	private TridasProject projectChosen = null;
    private ComboBoxFilterable cboBox;
	private MVCArrayList<TridasProject> projectList;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TridasProjectPicker dialog = new TridasProjectPicker();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static TridasProject pickProject(Window parent, String title)
	{
		
		TridasProjectPicker dialog = new TridasProjectPicker();
		dialog.setTitle(title);
        dialog.setLocationRelativeTo(parent);
        dialog.pack();
        
        
        dialog.setVisible(true); // blocks until user brings dialog down...
       	

        return dialog.getSelectedProject();

	}
	

	/**
	 * Create the dialog.
	 */
	public TridasProjectPicker() {
		setBounds(100, 100, 450, 300);
		setIconImage(Builder.getApplicationIcon());
		setTitle("Pick Project");
		setModal(true);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[]"));
		{
			JLabel lblBox = new JLabel("Project:");
			contentPanel.add(lblBox, "cell 0 0,alignx trailing");
		}
		{
			// Set up box list model etc
			projectList = App.tridasProjects.getMutableProjectList();
					
			TridasProject[] arr = new TridasProject[projectList.size()];
			
			for (int i=0; i<projectList.size(); i++)
			{
				arr[i] = projectList.get(i);
			}
			cboBox = new ComboBoxFilterable(arr);
			TridasProjectRenderer tlcr = new TridasProjectRenderer();
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
			this.projectChosen = null;
			this.setVisible(false);
		}
		
		if(evt.getActionCommand().equals("OK"))
		{
			this.projectChosen = (TridasProject) cboBox.getSelectedItem();
			this.setVisible(false);
		}
	}

	
	public TridasProject getSelectedProject()
	{
		return this.projectChosen;
	}
}
