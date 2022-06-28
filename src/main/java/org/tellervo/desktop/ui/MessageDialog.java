package org.tellervo.desktop.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.setupwizard.SetupWizard;

public class MessageDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private final String title;
	private final String message;
	private final DialogType type;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	
	private Window parent;
	private final static Logger log = LoggerFactory.getLogger(MessageDialog.class);

	enum DialogType {
		INFO,
		WARNING,
		ERROR
	}

	/**
	 * Create the dialog.
	 * @wbp.parser.constructor
	 */
	public MessageDialog(String title, String message) {
		this.title = title;
		this.message = message;
		this.type = DialogType.INFO;
		setupGui();
	}
	
	/**
	 * Create the dialog.
	 */
	public MessageDialog(String title, String message, DialogType type) {
		this.title = title;
		this.message = message;
		this.type = type;
		setupGui();
	}
	
	/**
	 * Create the dialog.
	 * @wbp.parser.constructor
	 */
	public MessageDialog(Window parent, String title, String message) {
		this.title = title;
		this.message = message;
		this.type = DialogType.INFO;
		this.parent = parent;
		setupGui();
	}
	
	/**
	 * Create the dialog.
	 */
	public MessageDialog(Window parent, String title, String message, DialogType type) {
		this.title = title;
		this.message = message;
		this.type = type;
		this.parent = parent;
		setupGui();
	}
	
	private void setupGui()
	{
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setMinimumSize(new Dimension(460,150));
		this.setMaximumSize(new Dimension(800,600));
		//setBounds(100, 100, 386, 310);
		this.setIconImage(Builder.getApplicationIcon());
		
		this.setTitle(title);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[70px:70px:70px,center][grow]", "[64:64:64][grow,fill]"));
		{
			JLabel lblIcon = new JLabel("");
			
			if(type.equals(DialogType.INFO))
			{
				lblIcon.setIcon(Builder.getIcon("info.png", 64));
			}
			if(type.equals(DialogType.ERROR))
			{
				lblIcon.setIcon(Builder.getIcon("error.png", 64));
			}
			if(type.equals(DialogType.WARNING))
			{
				lblIcon.setIcon(Builder.getIcon("warn.png", 64));
			}
			contentPanel.add(lblIcon, "cell 0 0, hidemode 2");
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new EmptyBorder(0,0,0,0));
			contentPanel.add(panel, "cell 1 0 1 2,grow");
			panel.setLayout(new MigLayout("", "[110px,grow,fill]", "[25px,grow,fill]"));
			{
				scrollPane = new JScrollPane();
				scrollPane.setBorder(new EmptyBorder(5,5,5,5));
				scrollPane.setViewportBorder(null);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				panel.add(scrollPane, "cell 0 0,grow");
				{
					textArea = new JTextArea();
					scrollPane.setViewportView(textArea);
					textArea.setText(message);
					textArea.setEditable(false);
					textArea.setLineWrap(true);
					textArea.setWrapStyleWord(true);
					textArea.setEditable(false);
					textArea.setOpaque(false);
					textArea.setBackground(new Color(0,0,0,0));
					textArea.setFont(new Font("Dialog", Font.PLAIN, 12));
					textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
					
					textArea.doLayout();
					textArea.getHeight();

					
					
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						dispose();
						
					}
					
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}

		pack();


		int currWidth = this.getWidth();
		int currHeight = 150;
		//log.debug("textArea height = "+textArea.getHeight());
		
		if(textArea.getHeight()>450)
		{
			currHeight = 600;

		}
		else if(textArea.getHeight()>20)
		{
			//log.debug("Increasing size of window to remove scrollbars if possible");
			currHeight = 150+ (textArea.getHeight()-20);
		}
		
		//log.debug("Setting dialog size to "+currWidth+", "+currHeight);
		this.setPreferredSize(new Dimension(currWidth, currHeight));
		pack();

		
		if(parent!=null)
		{
			this.setLocationRelativeTo(parent);
		}
		else 
		{
			this.setLocationRelativeTo(App.mainWindow);
		}

	}


}
