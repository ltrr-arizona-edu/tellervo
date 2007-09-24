/**
 * 
 */
package corina.graph;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import corina.ui.Builder;

/**
 * @author Lucas Madar
 *
 */
public class GraphToolbar extends JToolBar {
	private GraphWindow window;
	
	private ImageIcon sizedIcon(String icon) {
		Icon i = Builder.getIcon(icon);
		return new ImageIcon(((ImageIcon) i).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
	}
	
	public GraphToolbar(GraphWindow window) {
		super(JToolBar.HORIZONTAL);
		this.window = window;
		
		// oh god, this is annoying!
		setFloatable(false);
		
		// initialize the menu bar...
		ImageIcon i;
		i = sizedIcon("arrow.png");
		
		JButton button = new JButton(i);
		add(button);
	}
}
