/**
 * 
 */
package corina.graph;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenuItem;


import corina.gui.menus.FileMenu;
import corina.ui.Builder;

/**
 * @author Lucas Madar
 *
 */
public class GraphFileMenu extends FileMenu {
	private GraphWindow window;
	
    public GraphFileMenu(GraphWindow win) {
    	super(win);
    	this.window = win;
    }

    public void addPrintMenu() {
		JMenuItem print1 = Builder.makeMenuItem("plot_print");
		print1.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.plot.tryPrint(GraphPrintDialog.PRINT_PRINTER);
			}
		});
		this.add(print1);			

		JMenuItem print2 = Builder.makeMenuItem("plot_exportpdf");
		print2.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.plot.tryPrint(GraphPrintDialog.PRINT_PDF);
			}
		});
		this.add(print2);			

		JMenuItem print3 = Builder.makeMenuItem("plot_exportpng");
		print3.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.plot.tryPrint(GraphPrintDialog.PRINT_PNG);
			}
		});
		this.add(print3);						
    }
}
