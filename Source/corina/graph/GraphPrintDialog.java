/**
 * 
 */
package corina.graph;

import javax.swing.*;

import corina.prefs.PrefsDialog;
import corina.util.Center;

import java.awt.FlowLayout;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import java.awt.print.*;

import javax.swing.text.*;
import javax.swing.event.*;

/**
 * @author Lucas
 *
 */
public class GraphPrintDialog extends JPanel {
	
	public final static int PRINT_PRINTER = 1;
	public final static int PRINT_PDF = 2;
	public final static int PRINT_PNG = 3;
	
	// this is what actually does the graphing for us...
	private GrapherPanel plot;
	private GraphInfo gInfo;
	
	public GraphPrintDialog(JFrame parent, List graphs, GrapherPanel plot, int printType) {
		final JDialog d;
		final PreviewPanel preview;
		final ParamsPanel params;
		
		gInfo = plot.getPrinterGraphInfo();
		
		d = new JDialog(parent, "Printing options...", true);
		d.setContentPane(this);
		d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());
		
		preview = new PreviewPanel(plot, gInfo);
		params = new ParamsPanel(gInfo);
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Parameters", params);
		tabs.addTab("Colors, Names, and Widths", new ElemColorPanel(graphs, gInfo.isPrinting()));
		tabs.addTab("Preview", new JScrollPane(preview));
		add(tabs, BorderLayout.CENTER);
		
		tabs.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
	            JTabbedPane pane = (JTabbedPane)evt.getSource();
	            
	            if(pane.getSelectedIndex() == 2) {
	            	// preview pane selected now...
	            	preview.preparePreview(params.getDPI());
	            }
			}
		});
		
	    JPanel okButtonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));

	    String i18text = (printType == PRINT_PRINTER) ? "print" : "save_as";
	    String deftext = (printType == PRINT_PRINTER) ? "Print..." : "Save as...";
	    
	    String oktext = corina.ui.I18n.getText(i18text);
	    if (oktext == null) oktext = deftext;
	    JButton okButton = new JButton(oktext);
	    okButtonContainer.add(okButton);
	    
	    final GraphInfo _info = gInfo;
	    final GrapherPanel _plotter = plot;
	    final int _printType = printType;
	    okButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	    	  if(_printType == PRINT_PRINTER) {
	    		  if(new GraphPrinter(_info, _plotter, params.getDPI()).doPrint())	    	  	    	  
	    			  d.dispose();
	    	  }
	      }
	    });

	    String canceltext = corina.ui.I18n.getText("cancel");
	    if (canceltext == null) oktext = "Cancel";
	    JButton cancelButton = new JButton(canceltext);
	    okButtonContainer.add(cancelButton);
	    cancelButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	    	  d.dispose();
	      }
	    });
	    
	    add(okButtonContainer, BorderLayout.SOUTH);	  
	    
	    d.pack();
	    Center.center(d);
	    d.setVisible(true);	    
	}
	
	private class ParamsPanel extends JPanel implements DocumentListener {		
		JTextField pixelsperyear;
		JTextField dpi;
		JTextField yearsperinch;
		JTextField pixelheight; 
		JTextField inchheight;
		JTextField inchwidth;
		
		GraphInfo gInfo;
		
	    DecimalFormat dfmt = new DecimalFormat("0.00");
	    
	    public int getDPI() {
	    	return Integer.parseInt(dpi.getText());
	    }
		
		public ParamsPanel(GraphInfo g) {
			super();
		
			gInfo = g;
			
			setLayout(new BorderLayout());
			JComponent co;
			JLabel jl;
			GridBagConstraints gbc;		    
		    
			co = new JPanel(new GridBagLayout());
			co.setToolTipText("<html>Sets parameters required for graph rendering");			
			co.setBorder(BorderFactory.createTitledBorder("Graph basic constraints"));
			
			gbc = new GridBagConstraints();
		    gbc.anchor = GridBagConstraints.WEST;
		    gbc.fill = GridBagConstraints.NONE;
		    gbc.insets = new Insets(2, 2, 2, 2);
		    gbc.gridy = 0;
			
		    // start a row
		    gbc.gridx = 0;
		    
		    jl = new JLabel("<html>Pixel width of a year and<br>Pixel height of 10 units");
		    co.add(jl, gbc);
		    gbc.gridx++;
		    
		    pixelsperyear = new JTextField(6);
		    pixelsperyear.setText(Integer.toString(g.getYearSize()));
		    pixelsperyear.getDocument().addDocumentListener(this);
		    pixelsperyear.getDocument().putProperty("propname", "pixelsperyear");
		    jl.setLabelFor(pixelsperyear);
		    co.add(pixelsperyear, gbc);

		    // start a row
		    gbc.gridy++;
		    gbc.gridx = 0;
		    
		    jl = new JLabel("Pixels per inch (DPI)");
		    co.add(jl, gbc);
		    gbc.gridx++;
		    
		    dpi = new JTextField(6);
		    dpi.setText(Integer.toString(72));
		    dpi.getDocument().addDocumentListener(this);
		    dpi.getDocument().putProperty("propname", "dpi");
		    jl.setLabelFor(dpi);
		    co.add(dpi, gbc);

		    // start a row
		    gbc.gridy++;
		    gbc.gridx = 0;
		    
		    jl = new JLabel("<html>Years per inch and<br>10 Unit count per inch");
		    co.add(jl, gbc);
		    gbc.gridx++;
		    
		    yearsperinch = new JTextField(6);
		    yearsperinch.setEditable(false);
		    float ypi = Float.parseFloat(dpi.getText()) / Float.parseFloat(pixelsperyear.getText());
		    yearsperinch.setText(dfmt.format(ypi));
		    yearsperinch.getDocument().addDocumentListener(this);
		    yearsperinch.getDocument().putProperty("propname", "yearsperinch");
		    jl.setLabelFor(yearsperinch);
		    co.add(yearsperinch, gbc);
		    
		    add(co, BorderLayout.NORTH);
		    
			co = new JPanel(new GridBagLayout());
			co.setToolTipText("<html>View parameters for graph output");			
			co.setBorder(BorderFactory.createTitledBorder("Graph size"));
			
			gbc = new GridBagConstraints();
		    gbc.anchor = GridBagConstraints.WEST;
		    gbc.fill = GridBagConstraints.NONE;
		    gbc.insets = new Insets(2, 2, 2, 2);
		    gbc.gridy = 0;
			
		    // start a row
		    gbc.gridx = 0;
		    
		    jl = new JLabel("<html>Graph height, in pixels");
		    co.add(jl, gbc);
		    gbc.gridx++;
		    
		    pixelheight = new JTextField(6);
		    pixelheight.setText(Integer.toString(gInfo.getPrintHeight()));
		    pixelheight.getDocument().addDocumentListener(this);
		    pixelheight.getDocument().putProperty("propname", "pixelheight");
		    jl.setLabelFor(pixelheight);
		    co.add(pixelheight, gbc);

		    // start a row
		    gbc.gridy++;
		    gbc.gridx = 0;
		    
		    jl = new JLabel("<html>Graph height, in inches");
		    co.add(jl, gbc);
		    gbc.gridx++;
		    
		    inchheight = new JTextField(6);
		    float ih = 0;
		    try {
		    	ih = (float) gInfo.getPrintHeight() / Float.parseFloat(dpi.getText());
		    } catch (Exception e) { }
		    inchheight.setText(dfmt.format(ih));
		    inchheight.getDocument().addDocumentListener(this);
		    inchheight.getDocument().putProperty("propname", "inchheight");
		    jl.setLabelFor(inchheight);
		    co.add(inchheight, gbc);

		    // start a row
		    gbc.gridy++;
		    gbc.gridx = 0;
		    
		    jl = new JLabel("<html>Graph width, in inches");
		    co.add(jl, gbc);
		    gbc.gridx++;
		    
		    inchwidth = new JTextField(6);
		    inchwidth.setEditable(false);
		    float iw = 0;
		    try {
		    	iw = ((float) gInfo.getDrawRange().span() * gInfo.getYearSize()) / 
		    	Float.parseFloat(dpi.getText());
		    } catch (Exception e) { }
		    inchwidth.setText(dfmt.format(iw));
		    inchwidth.getDocument().addDocumentListener(this);
		    inchwidth.getDocument().putProperty("propname", "inchwidth");
		    jl.setLabelFor(inchwidth);
		    co.add(inchwidth, gbc);
		    
		    add(co, BorderLayout.CENTER);		    
		}
		
		private void recalc(DocumentEvent d) {
			Document doc = (Document) d.getDocument();
			String source = (String) doc.getProperty("propname");
			
			if(source.equals("pixelsperyear")) {
				try {
					float fdpi = Float.parseFloat(dpi.getText());
					float fppy = Float.parseFloat(pixelsperyear.getText());
					yearsperinch.setText(dfmt.format(fdpi / fppy));
					gInfo.setYearSize((int) fppy);
					
				    float iw = 0;
				    try {
				    	iw = ((float) gInfo.getDrawRange().span() * gInfo.getYearSize()) / 
				    	Float.parseFloat(dpi.getText());
				    } catch (Exception e) { }
				    inchwidth.setText(dfmt.format(iw));
					
				} catch (Exception e) {					
				}
			}
			else if(source.equals("dpi")) {
				try {
					float fdpi = Float.parseFloat(dpi.getText());
					float fppy = Float.parseFloat(pixelsperyear.getText());
					yearsperinch.setText(dfmt.format(fdpi / fppy));
					
			    	float ih = (float) gInfo.getPrintHeight() / Float.parseFloat(dpi.getText());
					inchheight.setText(dfmt.format(ih));
					
				    float iw = 0;
				    try {
				    	iw = ((float) gInfo.getDrawRange().span() * gInfo.getYearSize()) / 
				    	Float.parseFloat(dpi.getText());
				    } catch (Exception e) { }
				    inchwidth.setText(dfmt.format(iw));
					
				} catch (Exception e) {					
				}
			}
			else if(source.equals("pixelheight")) {
				try {
					gInfo.setPrintHeight(Integer.parseInt(pixelheight.getText()));
			    	float ih = (float) gInfo.getPrintHeight() / Float.parseFloat(dpi.getText());
					inchheight.setText(dfmt.format(ih));
				} catch (Exception e) {					
				}
			}
			else if(source.equals("inchheight")) {
				try {
			    	int npix = (int) (Float.parseFloat(inchheight.getText()) * 
			    					  Float.parseFloat(dpi.getText()));
			    	gInfo.setPrintHeight(npix);
					pixelheight.setText(Integer.toString(npix));
				} catch (Exception e) {					
				}
			}
			/*
			else if(source.equals("dpi")) {
			    yearsperinch.setValue(new Float(
			    		((Integer)dpi.getValue()).floatValue() /
			    		((Integer)pixelsperyear.getValue()).floatValue() 
			    		));								
			}
			*/
		}
		
		public void insertUpdate(DocumentEvent d) {
			recalc(d);
		}
		public void removeUpdate(DocumentEvent d) {
			recalc(d);
		}
		public void changedUpdate(DocumentEvent d) {
			recalc(d);
		}
	}
	
	private class PreviewPanel extends JPanel {
	
		private PreviewInsidePane inpane;
		private JScrollPane scrollerpane;
		
		private class PreviewInsidePane extends JPanel {
			private GrapherPanel plotter;
			private GraphInfo pinfo;
			private StandardPlot agent;
			private double scale;
			
			public PreviewInsidePane(GrapherPanel p, GraphInfo g) {
				super();
				
				setBackground(g.getBackgroundColor());
				
				plotter = p;
				pinfo = g;
				g.setPrintHeight(plotter.getHeight());
			}
			
			public void preparePreview(int scale) {
				plotter.computeRange(pinfo, null);

				this.scale = 72.0 / (float) scale;
				
				setPreferredSize(new Dimension(
						(int) (pinfo.getDrawRange().span() * pinfo.getYearSize() * this.scale), 
						(int) (pinfo.getPrintHeight() * this.scale)));
				agent = new StandardPlot(pinfo.getDrawRange(), pinfo);
				revalidate();
				repaint();
			}
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				((Graphics2D)g).scale(scale, scale);
				plotter.paintGraph(g, pinfo, agent);
			}				
		}
		
		public PreviewPanel(GrapherPanel p, GraphInfo g) {

			inpane = new PreviewInsidePane(p, g);
			scrollerpane = new JScrollPane(inpane);
			scrollerpane.setPreferredSize(new Dimension(400, 300));
			add(scrollerpane);
			
			preparePreview(72);
		}
		
		public void preparePreview(int scale) {
			inpane.preparePreview(scale);
			
			// TODO: Move the scrollpane to the lower left corner
		}
	}
	
	private class ElemColorPanel extends JPanel {
		public ElemColorTable table;
		
		public ElemColorPanel(List graphs, boolean isPrinting) {
			super();
			
			table = new ElemColorTable(graphs, isPrinting);
			add(table);
		}
	}
	
	
	private class GraphPrinter implements Pageable, Printable {
		private PrinterJob job;
		private PageFormat format;
		private GraphInfo info;
		
		private GrapherPanel plotter;
		
		private double pscale;
		
		public GraphPrinter(GraphInfo info, GrapherPanel plotter, int DPI) {
		    job = PrinterJob.getPrinterJob();
		    Paper paper = new Paper();
		    PageFormat pfmt = new PageFormat();
		    double h, w;
		    
		    this.info = info;
		    this.plotter = plotter; 
		    
		    // we need to make a scale from 72nds of an inch to DPInds of an inch...
		    w = gInfo.getDrawRange().span() * gInfo.getYearSize();
		    h = gInfo.getPrintHeight();
		    pscale = 72.0 / (double) DPI;
		    
		    // width, height is now in pixels.. 
		    // convert it to inches and then multiply by 72
		    h *= pscale;
		    w *= pscale;
		    		    
		    //paper.setSize(h, w);
		    paper.setImageableArea(18, 18, h + 18, w + 18);
		    pfmt.setOrientation(PageFormat.LANDSCAPE);
		    pfmt.setPaper(paper);		    
		    format = pfmt;

		    job.setJobName("Corina plot");
		    job.setPageable(this); 
		}
		
		public boolean doPrint() {
			if(job.printDialog()) {
				try {
					job.print();
				} catch (PrinterException pe) {
					return false;
				}
				return true;
			}
			else
				return false;
		}
		
		public int print(Graphics g, PageFormat format, int pagenum) {
			if(pagenum != 0)
				return NO_SUCH_PAGE;
			
			// move out of our margins...
			((Graphics2D) g).translate(format.getImageableX(), 
					format.getImageableY());
			((Graphics2D) g).scale(pscale, pscale);			
			
			plotter.computeRange(info, g);
			StandardPlot agent = new StandardPlot(info.getDrawRange(), info);
			plotter.paintGraph(g, info, agent);
			
			return PAGE_EXISTS;
		}
		
		public int getNumberOfPages() {
			return 1;
		}
		
		public PageFormat getPageFormat(int pageIndex) {
			if(pageIndex != 0)
				return null;
			
			return format;
		}
		
		public Printable getPrintable(int pageIndex) {
			if(pageIndex != 0)
				return null;
			
			return this;
		}
	}
}
