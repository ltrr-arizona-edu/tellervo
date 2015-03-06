package org.tellervo.desktop.editor.view;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.AbstractMetadataPanel;
import org.tellervo.desktop.editor.BasicMetadataPanel;
import org.tellervo.desktop.editor.SeriesDataMatrix;
import org.tellervo.desktop.prefs.PrefsEvent;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleEvent;
import org.tellervo.desktop.tridasv2.ui.TridasMetadataPanel;
import org.tellervo.desktop.ui.Builder;

public class LiteEditor extends AbstractEditor {
	
	JPanel metadataHolder;
	

	public LiteEditor()
	{
		super();
		this.setVisible(true);
		initLiteEditor();
	}
	
	public LiteEditor(Sample sample)
	{
		super(sample);
		this.setVisible(true);
		initLiteEditor();

	}
	
	public LiteEditor(ArrayList<Sample> samples)
	{
		super(samples);
		this.setVisible(true);
		initLiteEditor();

	}
	
	public void initLiteEditor()
	{
		metadataHolder = new JPanel();
		metadataHolder.setLayout(new BorderLayout());
		tabbedPane.addTab("Metadata", Builder.getIcon("database.png", 16), metadataHolder, null);

		
		itemSelected();
		
	}
	
	public LiteEditor(File file)
	{
		
	}
	
	
	@Override
	public boolean isSaved() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNameChangeable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFilename(String fn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getSavedDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prefChanged(PrefsEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sampleRedated(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sampleDataChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sampleMetadataChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sampleElementsChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sampleDisplayUnitsChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sampleDisplayCalendarChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void measurementVariableChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void itemSelected() {
	
			log.debug("Item selected");
			Sample sample = getSample();
			if (sample != null) {
				
				if(dataView!=null) 
				{
					dataView.saveRemarksDividerLocation();			
				}
				
				dataView = new SeriesDataMatrix(sample, this);
				dataPanel.removeAll();
				dataPanel.add(dataView, BorderLayout.CENTER);
				dataPanel.repaint();
				this.repaint();
				dataView.restoreRemarksDividerLocation();
				
				try{
					this.metadataHolder.removeAll();
					BasicMetadataPanel metadata = new BasicMetadataPanel();
					((BasicMetadataPanel) metadata).populateFromSample(sample);
					this.metadataHolder.add(metadata, BorderLayout.CENTER);
				} catch (Exception e)
				{
					
				}

				
				
			}
			
			setTitle();
	
	}
	
	
}
