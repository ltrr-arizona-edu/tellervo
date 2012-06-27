package org.tellervo.desktop.prefs.wrappers;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.AutoCompletion;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tridas.io.formats.corina.CorinaReader;

public class URLComponentWrapper extends PrefWrapper<String> implements ItemListener{
	
	private static final Logger log = LoggerFactory.getLogger(URLComponentWrapper.class);
	private JComboBox cbo;
	private PrefKey optionsPrefName;
		
	public URLComponentWrapper(final JComboBox cbo, final PrefKey prefName,
			String defaultValue, final PrefKey optionsPrefName) {
		super(prefName, defaultValue, String.class);
		
		this.optionsPrefName = optionsPrefName;
		this.cbo = cbo;
		init();		
		cbo.addItemListener(this);
		cbo.getEditor().getEditorComponent().addKeyListener(new KeyListener(){

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
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER)
				{
					init();
				}
				
			}
			
		});
		//AutoCompletion ac = new AutoCompletion(cbo);
		
	}
	
	public void clearHistory()
	{
		String current = (String) cbo.getSelectedItem();
		cbo.removeAllItems();
		App.prefs.setArrayListPref(optionsPrefName, null);
		App.prefs.addToArrayListPref(prefName, optionsPrefName, current);
		init();

	}
	
	private void init()
	{
		ArrayList<String> options = App.prefs.getArrayListPref(optionsPrefName, null);
				
		if(options !=null)
		{
			// Add all the options
			for(String option : options)
			{
				cbo.addItem(option);
			}
			
			if(options.contains(getValue()))
			{
				// Select the right value
				cbo.setSelectedItem(getValue());
			}
			else
			{
				// Item not in options, so just add
				cbo.addItem(new String(this.getValue()));
				cbo.setSelectedIndex(0);
			}
		}
		else if (getValue()!=null)
		{
			// Item not in options, so just add
			cbo.addItem(new String(this.getValue()));
			cbo.setSelectedIndex(0);
		}
		

	}
	
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		
		if(arg0.getStateChange()!=ItemEvent.SELECTED) return;
		
		log.debug("URL selected in combo box");

		String selected = (String) cbo.getSelectedItem();
		
		if(selected==null || selected.length()==0) return;
		
		if(selected.startsWith(" ") || selected.endsWith(" "))
		{
			log.debug("Trimming spaces from URL");
			selected.trim();
		}
		
		if(selected.endsWith("/tellervo"))
		{
			log.debug("Auto adding trailing slash to Tellervo URL");
			selected = selected+"/";
		}
		
		//cbo.setSelectedItem(selected);
		App.prefs.addToArrayListPref(prefName, optionsPrefName, selected);
		
		ArrayList<String> options = App.prefs.getArrayListPref(prefName, null);
		if(options==null)
		{
			log.debug("Adding URL '"+selected+" to combo box");
			cbo.addItem(selected);
		}
		else if (!options.contains(selected))
		{
			log.debug("Adding URL '"+selected+" to combo box");
			cbo.addItem(selected);
		}
		
	}


}
