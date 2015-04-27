package org.tellervo.desktop.gis2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gui.dbbrowse.SearchPanel;
import org.tellervo.desktop.gui.dbbrowse.SearchResultManager;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.schema.SearchReturnObject;
import org.tridas.interfaces.ITridas;

import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTextField;

public class DBMapSearchDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private FullEditor editor;
	private JTextField txtLayerName;
	
	
	public DBMapSearchDialog()
	{
		App.init();
		this.editor = null;
		init();
	}
	
	/**
	 * Create the dialog.
	 */
	public DBMapSearchDialog(FullEditor editor) {
		this.editor = editor;
		init();
	}
	
	private void init()
	{
		this.setTitle("Add Database Layer");
		this.setIconImage(Builder.getApplicationIcon());
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			SearchPanel panel = new SearchPanel(new SearchSupport(this), this);
			ArrayList<SearchReturnObject> returnitems = new ArrayList<SearchReturnObject>();
			returnitems.add(SearchReturnObject.OBJECT);
			returnitems.add(SearchReturnObject.ELEMENT);
			panel.setSearchForItems(returnitems);
			//JPanel panel = new JPanel();
			contentPanel.add(panel);
		}
		{
			JPanel panelName = new JPanel();
			contentPanel.add(panelName, BorderLayout.NORTH);
			panelName.setLayout(new MigLayout("", "[118px][grow]", "[15px]"));
			{
				JLabel lblMapLayerName = new JLabel("Map layer name:");
				panelName.add(lblMapLayerName, "cell 0 0,alignx trailing,aligny center");
			}
			{
				txtLayerName = new JTextField();
				panelName.add(txtLayerName, "cell 1 0,growx");
				txtLayerName.setText("New layer");
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Close");
				cancelButton.addActionListener(this);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		this.pack();
		this.setSize(new Dimension(500, 580));
		this.setLocationRelativeTo(editor);
	}
	
	/**
	 * Class implementing search support for DB browser
	 * 
	 * @author Lucas Madar
	 */
	private class SearchSupport implements SearchResultManager {

		private JDialog dialog;
		
		public SearchSupport(JDialog dialog) {
			this.dialog = dialog;
		}


		public void notifySeriesSearchFinished(ElementList elements) {
			// Not supported
		}




		public void notifySearchStarting() {
			System.out.println("SEARCH STARTING");

		}

		@Override
		public void notifyEntitySearchFinished(Collection<ITridas> entities) {

			if (entities != null && !entities.isEmpty()) {
				
				// Confirm save
				Object[] options = {"Yes",
	                    "No",
	                    "Cancel"};
					int n = JOptionPane.showOptionDialog(dialog,
						"Found "+entities.size()+" records.  Would you like to add them to the map?",
					    "Add to map",
					    JOptionPane.YES_NO_CANCEL_OPTION,
					    JOptionPane.QUESTION_MESSAGE,
					    null,
					    options,
					    options[2]);
					
				if(n==JOptionPane.YES_OPTION)
				{
					addEntitiesToMap(entities);
					dispose();
				}
				return;
			} else {

				Alert.message("No matches", "There are no matches");
			}
			
		}
	}

	
	private void addEntitiesToMap(Collection<ITridas> entities) {
		
		TridasEntityLayer layer = new TridasEntityLayer(txtLayerName.getText());
		
		for(ITridas entity : entities)
		{
		
			layer.addMarker(entity);
		}
		
		editor.wwMapPanel.getLayersList().add(layer);
		editor.wwMapPanel.layerPanel.update();
		
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cancel"))
		{
			this.dispose();
		}
		else if (e.getActionCommand().equals("OK"))
		{
			
		}
		
	}


}
