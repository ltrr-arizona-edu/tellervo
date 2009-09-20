package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

import com.lowagie.text.Font;

import edu.cornell.dendro.corina.gui.dbbrowse.SiteRenderer;
import edu.cornell.dendro.corina.tridasv2.ui.support.NotPresent;
import edu.cornell.dendro.corina.ui.FilterableComboBoxModel;
import edu.cornell.dendro.corina.util.ArrayListModel;

public class EntityListComboBox extends ComboBoxFilterable {
	private static final long serialVersionUID = 1L;

	/**
	 * Silly inner class - assists in debugging and the filtering engine
	 */
	private static class IrrelevantEntity implements NotPresent {
		private final String value;
		
		public IrrelevantEntity(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	public final static Object NEW_ITEM = new IrrelevantEntity("New...");
	public final static Object SEPARATOR_ITEM = new IrrelevantEntity("------");
	
	public EntityListComboBox(List<? extends ITridas> entities) {
		super(new FilterableComboBoxModel());
		
		FilterableComboBoxModel model = (FilterableComboBoxModel) getModel();
		
		model.addElement(NEW_ITEM);
		model.addElement(SEPARATOR_ITEM);
		model.addElements(entities);
		
		finishInit();
	}

	public EntityListComboBox() {
		super(new FilterableComboBoxModel());
		
		FilterableComboBoxModel model = (FilterableComboBoxModel) getModel();
		
		model.addElement(NEW_ITEM);
		model.addElement(SEPARATOR_ITEM);
		
		finishInit();
	}

	public void setList(List<? extends ITridas> entities) {
		FilterableComboBoxModel model = (FilterableComboBoxModel) getModel();

		model.clearElements();
		model.addElement(NEW_ITEM);
		model.addElement(SEPARATOR_ITEM);
		model.addElements(entities);
		
		finishInit();
	}
	
	private void finishInit() {
		EntityListComboBoxRenderer renderer = new EntityListComboBoxRenderer();
		renderer.setMaxWidth(45);		
		setRenderer(renderer);
		addActionListener(new NoSeparatorSelectionComboListener(this));		
	}
	
	// don't let users select the separator
	private class NoSeparatorSelectionComboListener implements ActionListener {
		private final JComboBox combo;
		private Object lastSelection = null;
		
		public NoSeparatorSelectionComboListener(JComboBox combo) {
			this.combo = combo;
		}
		
		public void actionPerformed(ActionEvent e) {
			Object item = combo.getSelectedItem();
			
			if(SEPARATOR_ITEM == item)
				combo.setSelectedItem(lastSelection);
			else
				lastSelection = item;
		}		
	}
	
	private class EntityListComboBoxRenderer extends JLabel implements
			ListCellRenderer {
		private static final long serialVersionUID = 1L;

		private JSeparator separator;
		private SiteRenderer siteRenderer;
		
		public EntityListComboBoxRenderer() {
			setOpaque(true);
			setBorder(new EmptyBorder(1, 1, 1, 1));

			separator = new JSeparator(JSeparator.HORIZONTAL);
			siteRenderer = new SiteRenderer();
		}		
		
		public void setMaxWidth(int maxwidth) {
			siteRenderer.setMaximumTitleLength(maxwidth);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			if (SEPARATOR_ITEM == value)
				return separator;
			
			// special handing for Objects
			if(value instanceof TridasObject)
				return siteRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			
			if (NEW_ITEM == value) {
				setFont(list.getFont().deriveFont(Font.BOLD));
				setText("New ...");
				return this;
			}
			
			String str;
			if(value != null) {
				if(value instanceof ITridas)
					str = ((ITridas)value).getTitle();
				else
					str = value.toString();
			}
			else str = "";
			
			setFont(list.getFont());
			setText(str);
			
			return this;
		}
	}
}
