/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.security.AccessControlException;
import java.security.AccessController;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.tellervo.desktop.CorinaPermission;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.core.AppModel;
import org.tellervo.desktop.cross.CrossdateDialog;
import org.tellervo.desktop.gui.dbbrowse.DBBrowser;
import org.tellervo.desktop.index.IndexDialog;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.manip.ReconcileWindow;
import org.tellervo.desktop.manip.RedateDialog;
import org.tellervo.desktop.manip.Reverse;
import org.tellervo.desktop.manip.SumCreationDialog;
import org.tellervo.desktop.manip.TruncateDialog;
import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleEvent;
import org.tellervo.desktop.sample.SampleListener;
import org.tellervo.desktop.sample.SampleType;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasDerivedSeries;



public class EditorToolsMenu extends JMenu implements SampleListener {
	private static final long serialVersionUID = 1L;
	
	private Sample sample;
	private Editor editor;
	
	private JMenuItem truncate = new JMenuItem();
	private JMenuItem reverseMenu = new JMenuItem();
	private JMenuItem reconcile = new JMenuItem();
	private JMenuItem indexMenu = new JMenuItem();
	private JMenuItem sumMenuItem = new JMenuItem();
	private JMenuItem modifySum = new JMenuItem();
	private JMenuItem redate = new JMenuItem();
	private JMenuItem crossAgainst = new JMenuItem();
	private JMenu crossMenu;
	
	public EditorToolsMenu(Sample s, Editor e) {
		super(I18n.getText("menus.tools")); 

		this.sample = s;
		this.editor = e;

		sample.addSampleListener(this);

		// truncate
		truncate = Builder.makeMenuItem("menus.tools.truncate", true, "truncate.png");
		truncate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new TruncateDialog(sample, editor);
			}
		});
		add(truncate);

		// reverse
		reverseMenu = Builder.makeMenuItem("menus.tools.reverse", true, "reverse.png");
		reverseMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// reverse, and add to the undo-stack
				editor.postEdit(Reverse.reverse(sample));
			}
		});
		add(reverseMenu);
		reverseMenu.setEnabled(sample.getSampleType() == SampleType.DIRECT
				&& (!sample.hasMeta(Metadata.CHILD_COUNT) || sample.getMeta(
						Metadata.CHILD_COUNT, Integer.class) == 0));

		// ---
		addSeparator();		
		
		// reconcile
		reconcile = Builder.makeMenuItem("menus.tools.reconcile", true, "reconcile.png");
		reconcile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DBBrowser browser = new DBBrowser(editor, true, false);

				// select the site we're in
				if(sample.meta().hasSiteCode()) 
					browser.selectSiteByCode(sample.meta().getSiteCode());
				
				browser.setTitle(I18n.getText("question.chooseReference"));
				browser.setVisible(true);
				
				if(browser.getReturnStatus() == DBBrowser.RET_OK) {
					ElementList toOpen = browser.getSelectedElements();

					if(toOpen.size() < 1)
						return;

					// load it
					Sample reference;
					try {
						reference = toOpen.get(0).load();
					} catch (IOException ioe) {
						Alert.error(I18n.getText("error.loadingSample"),
								I18n.getText("error.cantOpenFile") +": " + ioe.getMessage());
						return;
					}

					OpenRecent.sampleOpened(new SeriesDescriptor(reference), "reconcile");
					
					// open it for fun times
					new ReconcileWindow(sample, reference);
				}
			}
		});
		add(reconcile);		
	
		// index
		indexMenu = Builder.makeMenuItem("menus.tools.index", true, "index.png");
		indexMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// PERF: for big samples, it can take a couple
				// seconds for the dialog to appear.  not enough
				// for a progressbar, but enough that i should use
				// the "wait" cursor on the editor window.
				new IndexDialog(sample, editor);
			}
		});
		indexMenu.setEnabled(!sample.isIndexed());
		add(indexMenu);

		// sum
		if(!sample.isSummed())
		{
			// Sample is not a sum so add standard 'sum' button
			sumMenuItem = Builder.makeMenuItem("menus.tools.sum", true, "sum.png");
			sumMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					new SumCreationDialog(editor, ElementList.singletonList(new CachedElement(sample)));
				}
			});
			add(sumMenuItem);
		}
		else
		{
			// Sample is already a sum so offer to modify or create a version
			modifySum = Builder.makeMenuItem("menus.tools.modifysumandreplace");
			modifySum.setEnabled(false);
			
			JMenuItem modifySumAsVersion = Builder.makeMenuItem("menus.tools.modifysumasversion");
			modifySumAsVersion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					new SumCreationDialog(editor, sample);
				}
			});
			
			JMenu sumSubMenu = Builder.makeMenu("menus.tools.sum", "sum.png");
			sumSubMenu.add(modifySum);
			sumSubMenu.add(modifySumAsVersion);
			add(sumSubMenu);
		}
		
		

		// redate
		redate = Builder.makeMenuItem("menus.tools.redate", true, "redate.png");
		redate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new RedateDialog(sample, editor).setVisible(true);
			}
		});
		if (System.getSecurityManager() != null) {
			try {
				AccessController.checkPermission(new CorinaPermission("redate"));
			} catch (AccessControlException ace) {
				ace.printStackTrace();
				redate.setEnabled(false);
				redate.setBackground(Color.red.darker().darker());
			}
		}
		add(redate);
		redate.setEnabled(true);
		redate.setVisible(true);		
		
		
		// Crossdating	
		crossAgainst = Builder.makeMenuItem("menus.tools.new_crossdate", true, "crossdate.png");
		crossAgainst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Element secondary = new CachedElement(sample); 
				new CrossdateDialog(editor, ElementList.singletonList(secondary), secondary);
			}
		});
		
		// now, make this remember the last things we reconciled against!
		crossMenu = Builder.makeMenu("menus.tools.crossdateAgainst");
		crossMenu.putClientProperty("tellervo.crossdate_open_recent_action", new OpenRecent.SampleOpener("crossdate") {
			@Override
			public void performOpen(Sample s) {
				// new crossdate window
				//new ReconcileWindow(sample, s);
				// move to top of menu
				//OpenRecent.sampleOpened(new SeriesDescriptor(s), getTag());
				Element secondary = new CachedElement(sample); 
				new CrossdateDialog(editor, ElementList.singletonList(secondary), secondary);
				OpenRecent.sampleOpened(new SeriesDescriptor(s), getTag());
			}
		});
		
		ITridasSeries series = s.getSeries();
		if(series instanceof TridasDerivedSeries)
		{
			TridasDerivedSeries ds = (TridasDerivedSeries) series;
			if(ds.getType().getValue().equals("Crossdate"))
			{
				JMenuItem reviewCrossdate = Builder.makeMenuItem("crossdate.reviewCrossdate", true, "crossdate.png");
				final CachedElement element = new CachedElement(s);
				reviewCrossdate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						new CrossdateDialog(editor, element);
					}
				});
				add(reviewCrossdate);
			}
			else
			{
				crossMenu.add(crossAgainst);
				crossMenu.addSeparator();
				OpenRecent.makeOpenRecentMenu("menus.tools.crossdate", crossMenu, 10);
				add(crossMenu);	
			}
			
		}
		else
		{
			crossMenu.add(crossAgainst);
			crossMenu.addSeparator();
			OpenRecent.makeOpenRecentMenu("menus.tools.crossdate", crossMenu, 10);
			add(crossMenu);	
		}
		
		linkModel();
	}


	public void sampleRedated(SampleEvent e) { }
	public void sampleDataChanged(SampleEvent e) { }
	public void sampleElementsChanged(SampleEvent e) { }
	public void sampleDisplayUnitsChanged(SampleEvent e) {	}
	public void sampleMetadataChanged(SampleEvent e) {
		setMenusForNetworkStatus();
	}

	
	protected void linkModel()
	{
		  App.appmodel.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent argEvt) {
					if(argEvt.getPropertyName().equals(AppModel.NETWORK_STATUS)){
						setMenusForNetworkStatus();
					}	
				}
			});
		  
		  setMenusForNetworkStatus();
	}
	  
	protected void setMenusForNetworkStatus()
	{
		  reconcile.setEnabled(App.isLoggedIn());
		  indexMenu.setEnabled(App.isLoggedIn() && !sample.isIndexed());
		  sumMenuItem.setEnabled(App.isLoggedIn());
		  redate.setEnabled(App.isLoggedIn());
		  crossAgainst.setEnabled(App.isLoggedIn());
		  crossMenu.setEnabled(App.isLoggedIn());
	}


	@Override
	public void measurementVariableChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}
}
