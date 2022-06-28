package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwindx.examples.util.SectorSelector;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gis2.WWJPanel;
import org.tellervo.desktop.gui.dbbrowse.DBBrowser;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;

public class MapSpatialSearchAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	protected WWJPanel wwjPanel;
	private SectorSelector selector;
	
	public MapSpatialSearchAction(FullEditor editor) {
        super("Spatial search", Builder.getIcon("spatialsearch.png", 22));
		putValue(SHORT_DESCRIPTION, "Search for series from a specific region");
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
	
		editor.switchToMapTab();
		
		 selector = new SectorSelector(editor.wwMapPanel.getWwd());
         this.selector.setInteriorColor(new Color(1f, 1f, 1f, 0.1f));
         this.selector.setBorderColor(new Color(1f, 0f, 0f, 0.5f));
         this.selector.setBorderWidth(3);
         selector.enable();
         
         editor.wwMapPanel.getWwd().getInputHandler().addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				doSearch();
				
			}
        	 
         });
		
	}


	
	private void doSearch()
	{
		if(selector==null) return;
		
		Sector sector = selector.getSector();
		
		if(sector==null || sector.getMinLatitude()==null) return;
		
		selector.disable();
		
		
		sector.getMinLatitude().toDecimalDegreesString(10);
		
		DBBrowser browser = new DBBrowser(editor, true, true);
		SearchParameters params = new SearchParameters(SearchReturnObject.MEASUREMENT_SERIES);
		params.addSearchConstraint(SearchParameterName.OBJECTLATITUDE, SearchOperator.GREATER_THAN, removeLastCharacter(sector.getMinLatitude().toDecimalDegreesString(10)));
		params.addSearchConstraint(SearchParameterName.OBJECTLATITUDE, SearchOperator.LESS_THAN, removeLastCharacter(sector.getMaxLatitude().toDecimalDegreesString(10)));
		params.addSearchConstraint(SearchParameterName.OBJECTLONGITUDE, SearchOperator.GREATER_THAN, removeLastCharacter(sector.getMinLongitude().toDecimalDegreesString(10)));
		params.addSearchConstraint(SearchParameterName.OBJECTLONGITUDE, SearchOperator.LESS_THAN, removeLastCharacter(sector.getMaxLongitude().toDecimalDegreesString(10)));
		browser.doSearch(params);
		browser.setVisible(true);
	}
	
	private String removeLastCharacter(String string)
	{
		return string.substring(0, string.length()-1);
	}
}



