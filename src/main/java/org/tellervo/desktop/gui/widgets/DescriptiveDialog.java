package org.tellervo.desktop.gui.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.tellervo.desktop.ui.Builder;

/**
 * This is an extension of the standard JDialog with a pretty banner bar at the top containing title, description and icon
 * 
 * @author pwb48
 *
 */
public abstract class DescriptiveDialog extends JDialog implements ActionListener{
	private final static Logger log = LoggerFactory.getLogger(DescriptiveDialog.class);

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	String[] fieldOptions = {"Series name", "File name", "Full path", "Final folder"};
	String[] methodOptions = {"None", "Fixed width", "Regex"};
	private JPanel panelTitle;
	private JLabel lblTitle;
	private JPanel panelIcon;
	private JLabel lblIcon;
	private JTextArea txtDescription;
	private JPanel mainPanel;
	private JPanel buttonPane;
	protected JButton btnOK;
	protected JButton btnCancel;
		

	public DescriptiveDialog(Window parent, String title, String description, Icon icon) 
	{
		init();
		setDescriptiveText(description);
		setTitleText(title);
		setBannerIcon(icon);
		
		try{
			this.setIconImage(parent.getIconImages().get(0));
		} catch (Exception e)
		{
			
		}
		
		this.setLocationRelativeTo(parent);
		
	}
	
	/**
	 * Set the text for the description in the banner
	 * 
	 * @param text
	 */
	protected void setDescriptiveText(String text)
	{
		txtDescription.setText(text);
	}
	
	/**
	 * Set the text for the title in banner
	 * 
	 * @param text
	 */
	protected void setTitleText(String text)
	{
		lblTitle.setText(text);
	}
	
	/**
	 * Set the icon for the banner bar.  Should be 64x64px
	 * 
	 * @param icon
	 */
	protected void setBannerIcon(Icon icon)
	{
		lblIcon.setIcon(icon);
	}
	
	public void init(){
		setTitle("Define Patterns");
		setModal(true);
		setBounds(100, 100, 676, 379);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			panelTitle = new JPanel();
	        panelTitle.setBackground(Color.WHITE);
	        panelTitle.setLayout(new MigLayout("", "[378.00,grow][]", "[20.00][34.00,grow]"));
	        
	        lblTitle = new JLabel("Title goes here");
	        lblTitle.setFont(new Font("Dialog", Font.BOLD, 14));
	        panelTitle.add(lblTitle, "cell 0 0");
	        
	        panelIcon = new JPanel();
	        panelIcon.setBackground(Color.WHITE);
	        panelTitle.add(panelIcon, "cell 1 0 1 2,grow");
	        panelIcon.setLayout(new BorderLayout(0, 0));
	        
	        lblIcon = new JLabel();
	        lblIcon.setIcon(Builder.getIcon("missing.png", 64));
	        panelIcon.add(lblIcon);
	        
	        txtDescription = new JTextArea();
	        txtDescription.setWrapStyleWord(true);
	        txtDescription.setBorder(null);
	        txtDescription.setText("Description text goes here");
	        txtDescription.setLineWrap(true);
	        txtDescription.setFont(new Font("Dialog", Font.PLAIN, 10));
	        txtDescription.setFocusable(false);
	        txtDescription.setEditable(false);
	        panelTitle.add(txtDescription, "cell 0 1,grow, wmin 10");
		}
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			contentPanel.add(panelTitle, BorderLayout.NORTH);
		}
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnOK = new JButton("OK");
				btnOK.setActionCommand("OK");
				btnOK.addActionListener(this);
				buttonPane.add(btnOK);
				getRootPane().setDefaultButton(btnOK);
			}
			{
				btnCancel = new JButton("Cancel");
				btnCancel.setActionCommand("Cancel");
				btnCancel.addActionListener(this);
				buttonPane.add(btnCancel);
			}
		}
		
		mainPanel = new JPanel();
		contentPanel.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
	}
	
	/**
	 * Get the panel into which the actual dialog items go
	 * 
	 * @return
	 */
	protected JPanel getMainPanel()
	{
		return this.mainPanel;
	}

	
	/**
	 * Get the panel that holds the ok and cancel buttons. 
	 * Action commands for buttons are "OK" and "Cancel"
	 * 
	 * @return
	 */
	protected JPanel getButtonPanel()
	{
		return this.buttonPane;
	}

	

}
