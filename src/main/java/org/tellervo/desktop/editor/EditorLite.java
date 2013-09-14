package org.tellervo.desktop.editor;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.tellervo.desktop.gui.menus.AdminMenu;
import org.tellervo.desktop.gui.menus.HelpMenu;
import org.tellervo.desktop.gui.menus.WindowMenu;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;

public class EditorLite extends Editor {

	private BasicMetadataPanel basicMeta ;
	
	public EditorLite(Sample sample) {
		super(sample);
	
		this.metaView.setVisible(false);
		this.editorViewMenu.setVisible(false);
		this.metadbAction.setEnabled(false);
		init();
	}
	
	
	private void init()
	{
		
		if (tabbedPanel.indexOfComponent(metaView) > -1)
		{
			tabbedPanel.remove(metaView);
		}
		
		basicMeta = new BasicMetadataPanel();
		
		tabbedPanel.addTab("Basic metadata", Builder.getIcon("database.png", 16), basicMeta);
		
		
	}
	
	public BasicMetadataPanel getMetadataPanel()
	{
		return basicMeta;
	}

	
	@Override
	protected void setupMenuBar()
	{
		// menubar
		// This must happen *after* initRolodex(), as dataview and elempanel come from it.
		JMenuBar menubar = new JMenuBar();

		menubar.add(new EditorLiteFileMenu(this, sample));
		editorEditMenu = new EditorEditMenu(sample, dataView, this);
		menubar.add(editorEditMenu);
		menubar.add(this.editorViewMenu);
		menubar.add(new EditorToolsMenu(sample, this));
		menubar.add(new EditorGraphMenu(sample));
		//menubar.add(new EditorSiteMenu(sample));
		if (Platform.isMac())
			menubar.add(new WindowMenu(this));
		menubar.add(new HelpMenu());
		setJMenuBar(menubar);
	}
}
