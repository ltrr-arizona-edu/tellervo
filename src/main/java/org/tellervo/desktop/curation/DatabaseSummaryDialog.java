package org.tellervo.desktop.curation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXTable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;
import org.tellervo.desktop.dictionary.StatisticsResource;
import org.tellervo.desktop.ui.Builder;

public class DatabaseSummaryDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JXTable table;
	StatisticsResource res = new StatisticsResource();
	

	/**
	 * Create the dialog.
	 */
	public DatabaseSummaryDialog() {
		this.setIconImage(Builder.getApplicationIcon());
		this.setTitle("Database statistics");
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane);
			{
				JPanel panel = new JPanel();
				tabbedPane.addTab("Records", null, panel, null);
				panel.setLayout(new BorderLayout(0, 0));
				{
					JScrollPane scrollPane = new JScrollPane();
					panel.add(scrollPane);
					{
						table = new JXTable();
						table.setSortable(true);
						table.setShowHorizontalLines(false);
						table.setModel(new StatsTableModel(res));
						
						scrollPane.setViewportView(table);
					}
				}
			}
			{
				JPanel panel = new JPanel();
				
				// This will create the dataset 
		        PieDataset dataset = createDataset();
		        // based on the dataset we create the chart
		        JFreeChart chart = createChart(dataset, "Current status of samples");
		        // we put the chart into a panel
		        ChartPanel chartPanel = new ChartPanel(chart);
		        
		        panel.setLayout(new BorderLayout());
		        panel.add(chartPanel);
				
				tabbedPane.addTab("Sample Status", null, panel, null);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnOK = new JButton("OK");
				btnOK.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						dispose();
						
					}
					
				});
				
				buttonPane.add(btnOK);
				getRootPane().setDefaultButton(btnOK);
			}
		}
		
		this.setBounds(0, 0, 400, 300);
		this.setLocationRelativeTo(null);
	}

	
    private  PieDataset createDataset() {
        DefaultPieDataset result = new DefaultPieDataset();
        
        Integer totalSamples = res.getValueAsInteger("entities.samples");
        Integer prepped = res.getValueAsInteger("samplestatus.Prepped");
        Integer unprepped = res.getValueAsInteger("samplestatus.Unprepped");
        Integer dated = res.getValueAsInteger("samplestatus.Dated");
        Integer tfr = res.getValueAsInteger("samplestatus.Too few rings");
        
        if(prepped==null) prepped=0;
        if(unprepped==null) unprepped=0;
        if(dated==null) dated=0;
        if(tfr==null) tfr=0;
        
        Integer nostatus = totalSamples - prepped-unprepped-dated-tfr;
        
        if(nostatus!=0)
        {
        	result.setValue("Unknown", nostatus);
        }
        if(prepped!=0)
        {
        	result.setValue("Prepped", prepped);
        }
        if(prepped!=0)
        {
        	result.setValue("Unprepped", unprepped);
        }
        if(dated!=0)
        {
        	result.setValue("Dated", dated);
        }
        if(tfr!=0)
        {
        	result.setValue("Too few rings", tfr);
        }

        return result;
        
    }
    
    private JFreeChart createChart(PieDataset dataset, String title) {
        
        JFreeChart chart = ChartFactory.createPieChart3D(title,          // chart title
            dataset,                // data
            true,                   // include legend
            true,
            true);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
            plot.setLabelGenerator(gen);
      
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
        
    }
	
}
