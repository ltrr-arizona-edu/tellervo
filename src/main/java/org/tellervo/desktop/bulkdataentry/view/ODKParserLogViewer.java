package org.tellervo.desktop.bulkdataentry.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import org.tellervo.desktop.ui.Builder;

import net.miginfocom.swing.MigLayout;

public class ODKParserLogViewer extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextPane txtLog;
	private JLabel lblFileCount;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ODKParserLogViewer dialog = new ODKParserLogViewer(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ODKParserLogViewer(Component parent) {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[310.00px][grow,fill][]", "[15px][grow]"));
		{
			lblFileCount = new JLabel("<html>ds dasf fdsa fdsaf sdf asdfsad fsda Files parsed successfully.");
			contentPanel.add(lblFileCount, "cell 0 0,alignx left,aligny top");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "cell 0 1 3 1,grow");
			{
				txtLog = new JTextPane();
				txtLog.setText("<html><b>hello</b> world");
				txtLog.setContentType("text/html");
				scrollPane.setViewportView(txtLog);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnOK = new JButton("OK");
				btnOK.setActionCommand("OK");
				buttonPane.add(btnOK);
				btnOK.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						dispose();
						
					}
					
				});
				getRootPane().setDefaultButton(btnOK);
			}
		}
		
		this.setIconImage(Builder.getApplicationIcon());
		this.setTitle("ODK Parser Logs");
		this.setLocationRelativeTo(parent);
	}
	
	public void setFileCount(Integer filesProcessed, Integer filesSuccessful)
	{
		lblFileCount.setText("<html>Files found: "+filesProcessed+"<br/> Files loaded successfully: "+filesSuccessful+"</html>");
	}
	
	public void setLog(String string)
	{
		txtLog.setText(string);
	}


}
