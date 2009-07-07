/**
 * 
 */
package edu.cornell.dendro.corina.graph;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import edu.cornell.dendro.corina.ui.CorinaAction;
import edu.cornell.dendro.corina.ui.I18n;

/**
 * @author Lucas Madar
 *
 */
@SuppressWarnings("serial")
public class GraphToolbar extends JToolBar {	
	public GraphToolbar(GraphActions actions) {
		super(SwingConstants.HORIZONTAL);
		

		if(actions.hasElements()) {
			addToggle(actions.showElementsPanel, "view_elements");
			addSeparator();
		}
		
		addToggle(actions.showVerticalAxis, "vert_show");
		addToggle(actions.showGridlines, "grid_show");
		addToggle(actions.showComponentNames, "compn_show");
		
		addSeparator();
		
		if(actions.hasController()) {
			addButton(actions.squeezeVertically, "baselines_align");
			addButton(actions.fitHorizontally, "fit_horiz");
		
			addSeparator();
		
			addButton(actions.zoomIn, "escale_double");
			addButton(actions.zoomOut, "escale_halve");
		}
	}
	
	private void addToggle(CorinaAction action, String key) {
		AbstractButton button = new TitlelessToggleButton(action);
		action.connectToggleableButton(button);

		button.setToolTipText(getToolTip(key));

		add(button);
	}
	
	private void addButton(CorinaAction action, String key) {
		AbstractButton button = new TitlelessButton(action);

		button.setToolTipText(getToolTip(key));

		add(button);
	}
	
	private String getToolTip(String key) {
		try {
			return (key == null) ? null : I18n.getText(key);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * A JButton with no text content
	 */
	private static class TitlelessButton extends JButton {
		public TitlelessButton(Action action) {
			super(action);
		}
		
		@Override
		public void setText(String text) {
			super.setText(null);
		}
		
		@Override
		public String getText() {
			return null;
		}
	}

	/**
	 * A JToggleButton with no text content
	 */
	private static class TitlelessToggleButton extends JToggleButton {
		public TitlelessToggleButton(Action action) {
			super(action);
		}
		
		@Override
		public void setText(String text) {
			super.setText(null);
		}
		
		@Override
		public String getText() {
			return null;
		}
	}
}
