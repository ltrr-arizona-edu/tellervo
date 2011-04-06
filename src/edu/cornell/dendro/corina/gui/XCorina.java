//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import edu.cornell.dendro.corina.editor.EditorFactory;
import edu.cornell.dendro.corina.gui.menus.AdminMenu;
import edu.cornell.dendro.corina.gui.menus.EditMenu;
import edu.cornell.dendro.corina.gui.menus.FileMenu;
import edu.cornell.dendro.corina.gui.menus.HelpMenu;
import edu.cornell.dendro.corina.gui.menus.WindowMenu;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.ListUtil;

public class XCorina extends JFrame {

	private static final long serialVersionUID = 1L;

	// --- DropLoader ----------------------------------------
	// EXTRACT METHOD/CLASS!
	public class DropLoader implements DropTargetListener {
		public void dragEnter(DropTargetDragEvent event) {
			event.acceptDrag(DnDConstants.ACTION_COPY);
		}

		public void dragOver(DropTargetDragEvent event) {
		}

		public void dragExit(DropTargetEvent event) {
		}

		public void dropActionChanged(DropTargetDragEvent event) {
		}

		public void drop(DropTargetDropEvent event) {
			try {
				Transferable transferable = event.getTransferable();

				// we accept only filelists
				if (transferable
						.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					event.acceptDrop(DnDConstants.ACTION_COPY);
					Object o = transferable
							.getTransferData(DataFlavor.javaFileListFlavor);
					List<File> l = ListUtil.subListOfType((List<?>) o, File.class); // a List of Files

					// load each one in turn
					for (int i = 0; i < l.size(); i++) {
						String pathname = l.get(i).getPath();
						try {
							CanOpener.open(pathname);
						} catch (IOException ioe) {
							System.out.println("error on " + pathname + "!"); // NEED BETTER ERROR HANDLING!
						}
					}
					repaint();
					event.getDropTargetContext().dropComplete(true);
				} else {
					event.rejectDrop();
				}
			} catch (IOException ioe) {
				event.rejectDrop(); // handle error?
			} catch (UnsupportedFlavorException ufe) {
				event.rejectDrop(); // handle error?
			}
		}
	}

	// --- DropLoader ----------------------------------------

	// SINGLETON.
	// show the toplevel, or throw an exception if there already is one.
	public static void showCorinaWindow() {
		if (_self == null) {
			_self = new XCorina();
		} else {
			throw new RuntimeException("There can be only one!");
		}
	}

	private static XCorina _self = null;

	private XCorina() {
		// there can be only one
		if (_self != null)
			throw new RuntimeException("There can be only one!");
		_self = this;

		// boilerplate
		setTitle("Corina");

		// set application icon
		setIconImage(Builder.getApplicationIcon());

		// set background...
		ClassLoader cl = this.getClass().getClassLoader();		
		BufferedImage img = null;
		URL url = cl.getResource("edu/cornell/dendro/corina_resources/Images/background3.png");
		if (url != null) {
			try {
				img = ImageIO.read(url);
			} catch (IOException e1) {
			}
		}

		// menubar
		{
			JMenuBar menubar = new JMenuBar();
			menubar.add(new FileMenu(this));
			menubar.add(new EditMenu(this));
			menubar.add(new AdminMenu(this));
			//menubar.add(new OldCrossdateMenu());
			if (Platform.isMac())
				menubar.add(new WindowMenu(this));
			menubar.add(new HelpMenu());
			setJMenuBar(menubar);
		}

		ImagePanel panel = new ImagePanel(img, ImagePanel.ACTUAL);
		panel.setLayout(new GridLayout(4, 1, 60, 0));
		Dimension d = new Dimension(img.getWidth(), img.getHeight());	
		panel.setMinimumSize(d);
		panel.setMaximumSize(d);
		panel.setSize(d);
		
		addQuickLinkButtons(panel, d);
		
		setContentPane(panel);

		
		// exit when this window closes
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				quit();
			}
		});

		// pack
		pack();

		// enable drop-loading for panel
		/*
		 * These are never used?
		 * 
		DropTargetListener dropLoader = new DropLoader();
		DropTarget target = new DropTarget(this, dropLoader);
		if (!App.platform.isMac()) { // (note: braces are mandatory here!)
			DropTarget target2 = new DropTarget(getJMenuBar(), dropLoader);
		}
		DropTarget target3 = new DropTarget(panel, dropLoader);
		*/

		// size it?
		//setSize(480, 360); // was: 320, 240
		// height of the menu bar, height of the frame, and an extra two pels for good measure
		if (img == null) {
			setSize(480, getJMenuBar().getHeight() + getInsets().top + 2);
		}
		else {
			setResizable(false);
			setSize(img.getWidth(), getJMenuBar().getHeight()
					+ getInsets().top + 2 + img.getHeight());
		}

		/*
		 // set split locations (BETTER: keep these in the prefs)
		 lrSplit.setDividerLocation(0.25);
		 tbSplit.setDividerLocation(0.25);
		 */

		Center.center(this);
		// show
		setVisible(true);
	}
	
	// simple convenience method
	private void qlbutton(JButton button) {
		button.setFocusable(false);
		button.setContentAreaFilled(false);
		button.setRolloverEnabled(true);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
	}

	private JPanel addQuickLinkButtons(final JPanel btnPanel, Dimension d) {		
		// no layout
		btnPanel.setLayout(null);

		JButton newSeries = new JButton();
		JButton openSeries = new JButton();
		JButton importSeries = new JButton();
				
		qlbutton(newSeries);
		qlbutton(openSeries);
		qlbutton(importSeries);
		
		newSeries.setToolTipText(I18n.getText("workspace.createNewSeries"));
		openSeries.setToolTipText(I18n.getText("workspace.openExistingSeries"));
		importSeries.setToolTipText(I18n.getText("workspace.importExistingSeries"));
		
		newSeries.setIcon(Builder.getIcon("filenew.png", 64));
		openSeries.setIcon(Builder.getIcon("fileopen.png", 64));
		importSeries.setIcon(Builder.getIcon("fileimport.png", 64));
		
		btnPanel.add(newSeries);
		btnPanel.add(openSeries);
		btnPanel.add(importSeries);		
		
		Dimension size = newSeries.getPreferredSize();
		newSeries.setBounds(300, 125, size.width, size.height);
		openSeries.setBounds(370, 125, size.width, size.height);
		importSeries.setBounds(440, 125, size.width, size.height);
		
		newSeries.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditorFactory.newSeries(btnPanel.getParent());
			}
		});
		
		openSeries.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileMenu.opendb();
			}
		});
		
		importSeries.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileMenu.importdbwithbarcode();
			}
		});

		return btnPanel;
	}
		
	// shutdown
	// REFACTOR: wouldn't startup.java (renamed, perhaps) be a better place for this?)
	public static void quit() {
		// close all windows, asking the user
		Frame f[] = Frame.getFrames();
		for (int i = 0; i < f.length; i++) {
			// skip invisible frames
			if (!f[i].isVisible())
				continue;

			// skip me
			if (f[i] instanceof XCorina)
				continue;

			if (f[i] instanceof XFrame)
				((XFrame) f[i]).close(); // checks for unsaved
			else
				f[i].dispose();
		}

		// no frames left but me?  bye.
		int n = 0;
		f = Frame.getFrames();
		for (int i = 0; i < f.length; i++)
			if (f[i].isVisible() && !(f[i] instanceof XCorina))
				n++;
		if (n == 0)
			System.exit(0);

		// for Mac OS: by
		// http://developer.apple.com/techpubs/macosx/Java/Reference/Java/com/apple/mrj/MRJQuitHandler.html#handleQuit()
		// throw IllegalStateException to prevent the quit
		if (Platform.isMac())
			throw new IllegalStateException();
	}

}
