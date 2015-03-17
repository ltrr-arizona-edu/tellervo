/**
 * 
 */
package org.tellervo.desktop.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.tellervo.desktop.editor.EditorFactory.BarcodeDialogResult;
import org.tellervo.desktop.editor.view.FullEditor;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleAdapter;
import org.tellervo.desktop.sample.SampleEvent;
import org.tellervo.desktop.sample.TellervoWsiTridasElement;
import org.tellervo.desktop.tridasv2.ui.TridasImportPanel;
import org.tellervo.desktop.util.Center;
import org.tellervo.desktop.util.LegacySampleExtractor;


/**
 * @author Lucas Madar
 *
 */
public class ImportFrame extends XFrame {
	private static final long serialVersionUID = 1L;

	private LegacySampleExtractor extractor;
	private TridasImportPanel importer;

	private JButton okButton;
	
	private Sample s;
	
	/**
	 * @param owner
	 */
	public ImportFrame(Sample samp) {
		super();
		
		extractor = new LegacySampleExtractor(samp);
		this.s = samp;
		importer = new TridasImportPanel(samp, extractor, new FullEditor(samp));
		
		setupGUI();
	}
	
	/**
	 * Use when you also have barcode information to set the object, element and sample
	 * @param samp
	 * @param result
	 */
	
	public ImportFrame(Sample samp, BarcodeDialogResult result){
		super();
		
		extractor = new LegacySampleExtractor(samp, result);
		importer = new TridasImportPanel(samp, extractor, new FullEditor(samp));
		this.s = samp;
		
		setupGUI();
		
	}   
	
	private void setupGUI()
	{
		
		
		setTitle("Importing " + s.getMetaString("title"));


				
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setLayout(new BorderLayout());
		add(importer, BorderLayout.CENTER);
		add(makeButtonPanel(), BorderLayout.SOUTH);
		add(makeInfoPanel(), BorderLayout.EAST);
		
		pack();
		Center.center(this);
		
		okButton.setEnabled(false);
		
		s.addSampleListener(new SampleAdapter() {
			@Override
			public void sampleMetadataChanged(SampleEvent e) {				
				if(importer.importCompleted())
					okButton.setEnabled(true);
				else
					okButton.setEnabled(false);
			}			
		});
	}
	
	private JPanel makeInfoPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.setPreferredSize(new Dimension(220, 400));
		
		JEditorPane editor = new JEditorPane();
		editor.setContentType("text/html");
		editor.setEditable(false);
		editor.setText(extractor.asHTML());
		
		panel.add(editor, BorderLayout.CENTER);
		
		return panel;
	}
	
	private JPanel makeButtonPanel() {
		JButton cancelButton = new JButton("Cancel Import");
		okButton = new JButton("Import");
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				
				// attach a new loader (well, saver!)
				TellervoWsiTridasElement.attachNewSample(s);
				
				// and open!
				new FullEditor(s).setVisible(true);
			}
		});
		
		JPanel panel = Layout.buttonLayout(null, cancelButton, okButton);
		panel.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		
		return panel;
	}
	
	
}
