/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.tridasv2.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.tellervo.desktop.tridasv2.ui.support.NotPresent;
import org.tellervo.desktop.ui.FilterableComboBoxModel;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.ControlledVoc;

import org.tridas.util.TridasObjectEx;

@SuppressWarnings("serial")
public class ComboBoxFilterable extends JComboBox {

	/** The JLayeredPane that contains us */
	private JLayeredPane windowPane;
	
	/** The search panel that holds the text field */
	private JPanel searchPanel;
	
	/** The text field for entering in search data */
	private ForcableJTextField searchField;

	private FilterableComboBoxModel model;
	private ContainsFilter filter;
	
	boolean isPopupShowing;
	boolean isComboPopupShowing;

	public ComboBoxFilterable(Object[] data) {
		super();
			
		model = new FilterableComboBoxModel(Arrays.asList(data));
		
		initialize();
	}

	public ComboBoxFilterable(FilterableComboBoxModel model) {
		super();
		
		this.model = model;
		
		initialize();
	}
	
	private void initialize() {
		setModel(model);
		
		filter = new ContainsFilter();

		isPopupShowing = isComboPopupShowing = false;

		// keep track of when the combo box popup dropdown menu exists
		// get rid of the search menu when it goes away
		addPopupMenuListener(new PopupMenuListener() {
		      public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		    	  isComboPopupShowing = false;
		    	  
		    	  if(isPopupShowing) {
		    		  cleanupPopup(true);
		    		  isPopupShowing = false;
		    	  }
		      }
		      public void popupMenuCanceled(PopupMenuEvent e) {
		    	  isComboPopupShowing = false;
		      }
		      public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		    	  isComboPopupShowing = true;
		      }
	    });
		
		// Bring up the search dialog when someone presses a key
		addKeyListener(new KeyAdapter() {
			
			public void keyTyped(KeyEvent e) {	
				
				// ignore it if the combo popup menu isn't showing
				if(!isComboPopupShowing)
					ComboBoxFilterable.this.showPopup();
								
				// is control or alt pressed? Ignore anything, then
				if((e.getModifiers() & (KeyEvent.CTRL_DOWN_MASK|KeyEvent.ALT_DOWN_MASK)) != 0)
					return;

				// close popup on escape
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if(isPopupShowing) {
						cleanupPopup(true);
						e.consume();
					}
					return;
				}
				
				char c = e.getKeyChar();
				
				// ignore non-alphanumeric keys
				if(!Character.isLetterOrDigit(c) && !(c == ' '))
					return;
				
				if(!isPopupShowing) {
					Point p;
				
					try {
						p = ComboBoxFilterable.this.getLocationOnScreen();
					} catch (IllegalComponentStateException icse) {
						// component isn't on screen, shouldn't happen??
						return;
					}
				
					initPopup(p);				

					searchField.setText("");
					searchPanel.setVisible(true);
					searchField.requestFocus(true);
					isPopupShowing = true;
				}				
				searchField.forceProcessKeyEvent(e);
			}
		});		
	}
	
	private void initPopup(Point p) {
		if(searchPanel == null) {
			initSearchPanel();

			Window containerWindow = SwingUtilities.windowForComponent(this);
			if(containerWindow instanceof JDialog)
				windowPane = ((JDialog)containerWindow).getLayeredPane();
			else if(containerWindow instanceof JFrame)
				windowPane = ((JFrame)containerWindow).getLayeredPane();
			else
				// not a JFrame or JDialog??
				return;
					
			windowPane.add(searchPanel, JLayeredPane.POPUP_LAYER);
		}
		
		Dimension panelSize = searchPanel.getPreferredSize();
		int x = windowPane.getWidth() - panelSize.width;
		int y = (this.getLocationOnScreen().y - windowPane.getLocationOnScreen().y) -
				(panelSize.height + 5);

		// too wide?
		if(x < 0) {
			panelSize.width += x;
			x = 0;
		}
		// too tall
		if(y < 0)
			y = 0;
		searchPanel.setBounds(x, y, panelSize.width, panelSize.height);
		searchPanel.validate();
	}
	
	/**
	 * Remove the popup from the screen
	 * @param resetFilter remove a filter, if true
	 */
	private void cleanupPopup(boolean resetFilter) {
		if(isPopupShowing) {
			searchPanel.setVisible(false);
			isPopupShowing = false;
		}
		
		if(resetFilter) {
			filter.setFilterText("");
			model.setFilter(filter);
		}
	}
	
	/**
	 * Create the JPanel that our search text box resides in
	 */
	private void initSearchPanel() {
		searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		searchPanel.setBorder(BorderFactory.createLineBorder(Color.RED.darker().darker(), 1));
		
		searchPanel.add(Box.createHorizontalStrut(4));
		searchPanel.add(new JLabel("Search: "));

		searchField = new ForcableJTextField(20);
		searchPanel.add(searchField);
		searchPanel.setVisible(false);
		
		// notify when search field contents change
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				searchFieldChanged();
			}

			public void insertUpdate(DocumentEvent e) {
				searchFieldChanged();
			}

			public void removeUpdate(DocumentEvent e) {
				searchFieldChanged();
			}
		});
		
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// remove the search box if someone presses escape
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cleanupPopup(true);
					e.consume();
					return;
				}
				
				// forward enter key onto combo box
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					
					// don't forward it on if nothing has been selected
					// instead, select the first item
					if(ComboBoxFilterable.this.getSelectedIndex() == -1) {
						ComboBoxFilterable.this.setSelectedIndex(0);
						e.consume();
						return;
					}
					
					ComboBoxFilterable.this.processKeyEvent(e);
					return;
				}
			}
		});
	}

	/**
	 * Called when the text in the search field changes
	 */
	private void searchFieldChanged() {
		filter.setFilterText(searchField.getText());
		model.setFilter(filter);
	}

	/**
	 * Subclass of JTextField that lets us force keypresses directly
	 */
	private static class ForcableJTextField extends JTextField {
		public ForcableJTextField(int size) {
			super(size);
		}

		public void forceProcessKeyEvent(final KeyEvent k) {
			ForcableJTextField.super.processKeyEvent(k);
		}
	}

	/**
	 * A filter that checks for string contents, case insensitive
	 */
	private static class ContainsFilter implements FilterableComboBoxModel.Filter {
		private String filterText = "";
		
		public void setFilterText(String filterText) {
			this.filterText = filterText.toLowerCase();
		}
		
		public boolean accept(Object obj) {
			// no filter? everything's good!
			if(filterText.length() == 0)
				return true;

			// skip not present things
			if(obj instanceof NotPresent)
				return false;
			
			String val = objectToString(obj).toLowerCase();
			return val.contains(filterText);
		}		
		
		private String objectToString(Object o) {
			// durr...
			if(o instanceof String) {
				return (String) o;
			}
			else if(o.getClass().isEnum()) {
				try {
					Method method = o.getClass().getMethod("value", (Class<?>[]) null);
					return method.invoke(o, new Object[] {}).toString();
				} catch (Exception e) {
					// fall through
				}
			}
			else if(o instanceof ControlledVoc) {
				return ((ControlledVoc)o).getNormal();
			}
			else if(o instanceof ITridas) {
				if(o instanceof TridasObjectEx) {
					TridasObjectEx objex = (TridasObjectEx)o;
					
					if(objex.hasLabCode())
						return objex.getLabCode() + "\n" + objex.getTitle();
					
					return objex.getTitle();
				}
				else
					return ((ITridas)o).getTitle();
			}
			
			return o.toString();
		}
	}
}
