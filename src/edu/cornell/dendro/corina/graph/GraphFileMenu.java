/**
 * 
 */
package edu.cornell.dendro.corina.graph;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import edu.cornell.dendro.corina.gui.menus.FileMenu;
import edu.cornell.dendro.corina.ui.Builder;

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

    @Override
	public void addPrintMenu() {
		JMenuItem print1 = Builder.makeMenuItem("menus.graph.printplot", true, "printer.png");
		print1.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.plot.tryPrint(GraphPrintDialog.PRINT_PRINTER);
			}
		});
		this.add(print1);			

		JMenuItem print2 = Builder.makeMenuItem("menus.graph.exportPDFPlot", true, "pdf.png");
		print2.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.plot.tryPrint(GraphPrintDialog.PRINT_PDF);
			}
		});
		this.add(print2);			

		JMenuItem print3 = Builder.makeMenuItem("menus.graph.exportPNGPlot", true, "png.png");
		print3.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.plot.tryPrint(GraphPrintDialog.PRINT_PNG);
			}
		});
		this.add(print3);						
    }
}
