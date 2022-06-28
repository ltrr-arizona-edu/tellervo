/**
 * 
 */
package org.tellervo.desktop.graph;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;


/**
 * @author Lucas Madar
 *
 */
@SuppressWarnings("serial")
public class GraphFileMenu extends JMenu {
	private GraphWindow window;
	
    public GraphFileMenu(GraphWindow win) {
        super(I18n.getText("menus.file"));

    	this.window = win;	
    	addPrintMenu();
    	addSeparator();
    	addCloseMenu();
    	
    }

	public void addPrintMenu() {
		JMenuItem print1 = Builder.makeMenuItem("menus.graph.printplot", true, "printer.png");
		print1.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.plot.tryPrint(GraphPrintDialog.PRINT_PRINTER);
			}
		});
		this.add(print1);		
		this.addSeparator();

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
	
	public void addCloseMenu(){
		
		JMenuItem close = Builder.makeMenuItem("Close", true, "exit.png");
		close.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				window.close();
			}
		});
		this.add(close);
	}
}
