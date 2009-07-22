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

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;

import edu.cornell.dendro.corina.Build;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.gui.menus.EditMenu;
import edu.cornell.dendro.corina.gui.menus.FileMenu;
import edu.cornell.dendro.corina.gui.menus.HelpMenu;
import edu.cornell.dendro.corina.gui.menus.WindowMenu;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.OKCancel;

public class XCorina extends JFrame {

	// menubar, and omnipresent menus
	private JMenuBar menubar;

	private List leftMenus, rightMenus;

	private ImageIcon backgroundImage;

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
					List l = (List) o; // a List of Files

					// load each one in turn
					for (int i = 0; i < l.size(); i++) {
						String pathname = ((File) l.get(i)).getPath();
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

	private JSplitPane lrSplit, tbSplit;

	private DropTarget drop;

	private XCorina() {
		// there can be only one
		if (_self != null)
			throw new RuntimeException("There can be only one!");
		_self = this;

		// boilerplate
		setTitle("Corina - " + Build.VERSION + " " + Build.TIMESTAMP);

		// set tree icon (also in XFrame).
		// EEP!  i can't use builder.geticon() if it doesn't guarantee an imageicon, no?
		ClassLoader cl = this.getClass().getClassLoader();
		java.net.URL url = cl.getResource("edu/cornell/dendro/corina_resources/Images/Tree.png");
		if (url != null) {
			ImageIcon treeIcon = new ImageIcon(url);
			setIconImage(treeIcon.getImage());
		}

		url = cl.getResource("edu/cornell/dendro/corina_resources/Images/background.png");
		if (url != null) {
			backgroundImage = new ImageIcon(url);
		}

		// menubar
		{
			JMenuBar menubar = new JMenuBar();
			menubar.add(new FileMenu(this));
			menubar.add(new EditMenu(this));
			//menubar.add(new OldCrossdateMenu());
			if (App.platform.isMac())
				menubar.add(new WindowMenu(this));
			menubar.add(new HelpMenu());
			setJMenuBar(menubar);
		}

		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				if (backgroundImage != null) {
					g.drawImage(backgroundImage.getImage(), 0, 0, null);
				}
			}
		};
		setContentPane(panel);

		/*
		 // content: a browser
		 // final JSplitPane lrSplit, tbSplit; // "final" so i can reference them from inner classes.
		 {
		 // sources list
		 final SourcesTable s = new SourcesTable(); // "final" so i can reference it in inner classes.

		 // left-right split
		 lrSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		 lrSplit.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		 // top-bottom split
		 tbSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		 tbSplit.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		 // add components
		 JScrollPane sp = new JScrollPane(s);
		 JPanel left = new JPanel(new BorderLayout());
		 sp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		 left.add(sp, BorderLayout.CENTER);
		 JPanel addRemovePanel = makeAddRemovePanel(s);
		 left.add(addRemovePanel, BorderLayout.SOUTH);

		 // QUESTION: how to add a drop-target-listener to watch for drops below the last row?

		 lrSplit.setLeftComponent(left);
		 lrSplit.setRightComponent(tbSplit);
		 tbSplit.setTopComponent(new JLabel("browser stuff here"));
		 tbSplit.setBottomComponent(new JLabel("file list here"));

		 setContentPane(lrSplit); // BUG: kills drop-loading (no, that's not a bug, drop-loading is dead, right?)

		 // TODO: add listener: on new selection, query Source for tbSplit top and bottom components
		 s.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		 public void valueChanged(ListSelectionEvent e) {
		 // wait until it stops
		 if (e.getValueIsAdjusting())
		 return;
		 // (makeBrowser(), etc., get called twice, if you drag, with this disabled.)

		 // use that source
		 SourcesTable.Source source = s.getSource();
		 if (source == null) {
		 tbSplit.setTopComponent(null);
		 tbSplit.setBottomComponent(new JPanel());
		 } else
		 setSource(source);
		 }
		 });

		 // set initial source
		 setSource(s.getSource());
		 }
		 */

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
		DropTargetListener dropLoader = new DropLoader();
		DropTarget target = new DropTarget(this, dropLoader);
		if (!App.platform.isMac()) { // (note: braces are mandatory here!)
			DropTarget target2 = new DropTarget(getJMenuBar(), dropLoader);
		}
		DropTarget target3 = new DropTarget(panel, dropLoader);

		// size it?
		//setSize(480, 360); // was: 320, 240
		// height of the menu bar, height of the frame, and an extra two pels for good measure
		if (backgroundImage == null) {
			setSize(480, getJMenuBar().getHeight() + getInsets().top + 2);
		}
		else {
			setResizable(false);
			setSize(backgroundImage.getIconWidth(), getJMenuBar().getHeight()
					+ getInsets().top + 2 + backgroundImage.getIconHeight());
		}

		/*
		 // set split locations (BETTER: keep these in the prefs)
		 lrSplit.setDividerLocation(0.25);
		 tbSplit.setDividerLocation(0.25);
		 */

		Center.center(this);
		// show
		show();
	}

	private JPanel makeAddRemovePanel(SourcesTable ss) {
		final SourcesTable s = ss;

		JButton plus = new JButton(Builder.getIcon("edit_add.png", 22));
		JButton minus = new JButton(Builder.getIcon("edit_remove.png", 22));

		plus.setToolTipText("Add Data Source");
		plus.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				showAddNewSourceDialog();
			}
		});

		minus.setToolTipText("Remove Data Source");
		minus.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				s.remove();
			}
		});

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.add(plus);
		panel.add(Box.createHorizontalStrut(4));
		panel.add(minus);
		return panel;
	}

	// show a window asking what type of source to add,
	// then a specific window for adding that type of source.
	private void showAddNewSourceDialog() {
		// TODO: add icons?
		/*
		 [ ======= Add New Source ======= ]
		 | Type: (*) Folder               |
		 |       ( ) Database             |
		 |       ( ) Network (FTP)        |
		 |       ( ) Favorites list       |
		 |       ( ) Smart List           |
		 |                                |
		 |              (Cancel) (( OK )) |
		 */
		final JDialog d = new JDialog();
		d.setTitle("Add New Source");

		JPanel l = Layout.flowLayoutL("Choose data source:");
		l.setBorder(BorderFactory.createEmptyBorder(14, 20, 6, 20));

		JPanel r = new JPanel();
		r.setLayout(new GridLayout(0, 1, 4, 4));

		ButtonGroup group = new ButtonGroup();

		final JRadioButton buttons[] = new JRadioButton[] {
				new JRadioButton("Folder"), new JRadioButton("Database"),
				new JRadioButton("Network (FTP)"),
				new JRadioButton("Favorites List"),
				new JRadioButton("Smart List"), };

		for (int i = 0; i < buttons.length; i++) {
			group.add(buttons[i]);
			r.add(buttons[i]);
		}
		buttons[0].setSelected(true);

		// IN PROGRESS: disable some choices
		for (int i = 2; i < buttons.length; i++)
			buttons[i].setEnabled(false);

		JButton cancel = Builder.makeButton("cancel");
		JButton ok = Builder.makeButton("ok");

		cancel.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				d.dispose();
			}
		});
		ok.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int x = 0;
				for (int i = 0; i < buttons.length; i++)
					if (buttons[i].isSelected())
						x = i;

				switch (x) {
				case 0:
					SourcesTable.Source f = new SourcesTable.FolderSource(
							"Untitled Source", ""); // use wd?
					f.showInfo();
					// WRITEME: if cancel, drop, else add to |sources|
					break;
				case 1:
					System.out.println("WRITEME: show database dialog now");
					break;
				default:
					Bug.bug(new IllegalArgumentException("bad selection"));
				}
				d.dispose();
			}
		});

		JPanel b = Layout.buttonLayout(cancel, ok);
		b.setBorder(BorderFactory.createEmptyBorder(18, 20, 20, 20));

		JPanel p = Layout.borderLayout(l, Box.createHorizontalStrut(40), r,
				null, b);
		d.setContentPane(p);

		OKCancel.addKeyboardDefaults(ok);

		d.pack();
		d.setResizable(false);
		d.show();
	}

	private void setSource(SourcesTable.Source source) {
		// set the top component when it changes. (bottom component will get set automatically.)
		if (source.hasBrowser()) {
			int old;

			old = tbSplit.getDividerLocation(); // (don't let divider location change)
			// tbSplit.setTopComponent(new JLabel("source: " + source.getName()));
			tbSplit.setTopComponent(source.makeBrowser());
			tbSplit.setDividerLocation(old);

			old = lrSplit.getDividerLocation();
			lrSplit.setRightComponent(tbSplit);
			lrSplit.setDividerLocation(old);
		} else {
			int old = lrSplit.getDividerLocation();
			lrSplit.setRightComponent(new JLabel("file list here"));
			lrSplit.setDividerLocation(old);
		}
		// WRITEME
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
		if (App.platform.isMac())
			throw new IllegalStateException();
	}

}
