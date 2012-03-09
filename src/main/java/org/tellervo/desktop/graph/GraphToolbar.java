/**
 * 
 */
package org.tellervo.desktop.graph;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import org.tellervo.desktop.ui.TellervoAction;
import org.tellervo.desktop.ui.I18n;


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
		

		addPlotTypeButtons(actions);
		addToggle(actions.showVerticalAxis, "vert_show");
		addToggle(actions.showGridlines, "grid_show");
		addToggle(actions.showComponentNames, "compn_show");
		
		addSeparator();
		
		if(actions.hasController()) {
			addButton(actions.squeezeVertically, "baselines_align");
			addButton(actions.fitHorizontally, "fit_horiz");
		
			addSeparator();
		
			addButton(actions.zoomIn, "zoom_in");
			addButton(actions.zoomOut, "zoom_out");
			addButton(actions.zoomInHorizontally, "zoom_in2");
			addButton(actions.zoomOutHorizontally, "zoom_out2");
			
		}
	}
	
	private void addPlotTypeButtons(GraphActions actions)
	{
		for(int i = 0; i < actions.plotTypes.length; i++) {
			JRadioButtonMenuItem sa = new JRadioButtonMenuItem(actions.plotTypes[i]);
			actions.plotTypes[i].connectToggleableButton(sa);
			
			add(sa);
		}
		
	}
	
	
	private void addToggle(TellervoAction action, String key) {
		AbstractButton button = new TitlelessToggleButton(action);
		action.connectToggleableButton(button);

		button.setToolTipText(getToolTip(key));

		add(button);
	}
	
	private void addButton(TellervoAction action, String key) {
		AbstractButton button = new TitlelessButton(action);

		button.setToolTipText(getToolTip(key));

		add(button);
	}
	
	private String getToolTip(String key) {
		try {
			return (key == null) ? null : I18n.getText("graph."+key);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * A JButton with no text content
	 */
	public static class TitlelessButton extends JButton {
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
