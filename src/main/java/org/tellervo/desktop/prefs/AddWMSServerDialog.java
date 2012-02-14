package org.tellervo.desktop.prefs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.tellervo.desktop.schema.WSIWmsServer;
import org.tellervo.desktop.ui.Builder;

import net.miginfocom.swing.MigLayout;

public class AddWMSServerDialog extends JDialog {


	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtName;
	private JFormattedTextField txtURL;
	private JButton btnOK;
	private URL u = null;
	

	public static WSIWmsServer showAddWMSServerDialog(JDialog parent)
	{
		AddWMSServerDialog dialog = new AddWMSServerDialog(parent);
		
		
		dialog.setModal(true);
		dialog.setVisible(true);
		
			
		
		return dialog.getWSIWmsServer();
		
		
	}
	
	
	public WSIWmsServer getWSIWmsServer()
	{
		if((txtName.getText()==null) || (txtURL.getText()==null))
		{
			return null;
		}
		
		WSIWmsServer server = new WSIWmsServer();
		server.setName(txtName.getText());
		server.setUrl(txtURL.getText());
		
		return server;
	}
	
	public String getURL()
	{
		return txtURL.getText();
	}
	
	
	/**
	 * Create the dialog.
	 */
	public AddWMSServerDialog(JDialog parent) {
	
		super(parent);

	    try {
	      u = new java.net.URL("http://www.example.com/");
	    } catch (java.net.MalformedURLException ignored) {
	    }
		
		setTitle("Add Web Mapping Server");
		setIconImage(Builder.getApplicationIcon());
		setSize(new Dimension(462, 168));
		setLocationRelativeTo(parent);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][][grow]", "[][]"));
		{
			JLabel lblName = new JLabel("Name:");
			contentPanel.add(lblName, "cell 1 0,alignx trailing");
		}
		{
			txtName = new JTextField();
			
			txtName.addKeyListener(new KeyListener(){
				@Override
				public void keyPressed(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyTyped(KeyEvent arg0) {
					checkEntries();
					
				}
				
			});
			
			contentPanel.add(txtName, "cell 2 0,growx");
			txtName.setColumns(10);
		}
		{
			JLabel lblIcon = new JLabel("");
			contentPanel.add(lblIcon, "cell 0 0 1 2");
			lblIcon.setIcon(Builder.getIcon("map.png", 64));
		}
		{
			JLabel lblUrl = new JLabel("URL:");
			contentPanel.add(lblUrl, "cell 1 1,alignx trailing");
		}
		{
			txtURL = new JFormattedTextField(u);
			txtURL.addKeyListener(new KeyListener(){
				@Override
				public void keyPressed(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyTyped(KeyEvent arg0) {
					checkEntries();
					
				}
				
			});
			
			txtURL.setInputVerifier(new InputVerifier() {
			      public boolean verify(JComponent input) {
			        if (!(input instanceof JFormattedTextField))
			          return true; // give up focus
			        return ((JFormattedTextField) input).isEditValid();
			      }
			    });
			
			
			contentPanel.add(txtURL, "cell 2 1,growx");
			txtURL.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnOK = new JButton("OK");
				btnOK.setEnabled(false);
				btnOK.setActionCommand("OK");
				buttonPane.add(btnOK);
				getRootPane().setDefaultButton(btnOK);
				
				btnOK.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
						
					}
					
	
					
				});
				
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				
				cancelButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						txtURL.setText(null);
						txtName.setText(null);
						
						setVisible(false);						
					}
					
				});
			}
		}
	}


	public void checkEntries() {

		if((!txtURL.getText().isEmpty()) && (!txtName.getText().isEmpty()))
		{
			btnOK.setEnabled(true);
		}
		else
		{
			btnOK.setEnabled(false);
		}
				
	}

}
