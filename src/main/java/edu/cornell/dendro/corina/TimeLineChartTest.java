package edu.cornell.dendro.corina;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarPainter;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.formats.heidelberg.HeidelbergReader;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasTridas;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.FontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;


public class TimeLineChartTest extends ApplicationFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public TimeLineChartTest() {

        super("Time line");

       
		String folder = "/home/pwb48/dev/java3/DendroFileIO/TestData/Heidelberg";
		String[] files = getFilesFromFolder(folder);
		
		
		for (String filename : files) {
			if(!filename.equals("SET03.FH")) continue;
			

			
			HeidelbergReader reader = new HeidelbergReader();
			
			// Parse the legacy data file
			try {
				// TridasEntitiesFromDefaults def = new TridasEntitiesFromDefaults();
				reader.loadFile(folder, filename);
				// reader.loadFile("TestData/Heidelberg", "UAKK0530.fh");
			} catch (IOException e) {
				// Standard IO Exception

			} catch (InvalidDendroFileException e) {
				// Fatal error interpreting file

			}
			
			// Extract the TridasProject
			TridasTridas container = reader.getTridasContainer();
			
			
			TridasProject p = reader.getProjects()[0];
			this.setTitle(p.getTitle());
	        final IntervalCategoryDataset dataset = createTimelineDataset(p);
	        final JFreeChart chart = createChart(dataset);

	        // add the chart to a panel...
	        final ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        setContentPane(chartPanel);
		}
        
        


    }
		
		private String[] getFilesFromFolder(String folder) {
			File dir = new File(folder);
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return !name.startsWith(".");
				}
			};
			return dir.list(filter);
		}

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    

    private static Task getTaskFromSeries(ITridasSeries ser)
    {
    	Task task = null;
		if(ser.isSetInterpretation())
		{
			if(ser.getInterpretation().isSetFirstYear() && ser.getInterpretation().isSetLastYear())
			{
				task = new Task(ser.getTitle(),
						new SimpleTimePeriod(date(1, Calendar.JANUARY, 
								ser.getInterpretation().getFirstYear().getValue()),
								date(1, Calendar.JANUARY,
								ser.getInterpretation().getLastYear().getValue())
						));
			}
			else
			{
				task = new Task(ser.getTitle(), new SimpleTimePeriod(null, null));
			}
		}
		else
		{
			task = new Task(ser.getTitle(), new SimpleTimePeriod(null, null));
		}
		
		return task;
    }
    
    public static IntervalCategoryDataset createTimelineDataset(TridasProject p)
    {
    	final TaskSeries s1 = new TaskSeries("Timelines");
    	
    	ArrayList<TridasMeasurementSeries> serlist = TridasUtils.getMeasurementSeriesFromTridasProject(p);
    	ArrayList<ITridasSeries> serlist2 = new ArrayList<ITridasSeries>();

    	for(TridasMeasurementSeries ser: serlist)
    	{
    		serlist2.add(ser);
    	}
    	
    	for(TridasDerivedSeries ser : p.getDerivedSeries())
    	{
    		serlist2.add(ser);
    	}
    	
    	Collections.sort(serlist2, new SeriesTimeSorter());
    	
    	
    	for(ITridasSeries ser : serlist2)
    	{
    		s1.add(getTaskFromSeries(ser));
    	}
     	
        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);


        return collection;
    	
    }

	/**
     * Creates a sample dataset for a Gantt chart.
     *
     * @return The dataset.
     */
 /*   public static IntervalCategoryDataset createDataset() {

        final TaskSeries s1 = new TaskSeries("Scheduled");

        s1.add(new Task("YMK-2",
               new SimpleTimePeriod(date(9, Calendar.APRIL, -5),
                                    date(9, Calendar.APRIL, 5))));
        s1.add(new Task("YMK-3",
               new SimpleTimePeriod(date(10, Calendar.APRIL, 10),
                                    date(5, Calendar.MAY, 20))));
        s1.add(new Task("YMK-4",
               new SimpleTimePeriod(date(6, Calendar.MAY, 10),
                                    date(30, Calendar.MAY, 30))));
        s1.add(new Task("YMK-5",
               new SimpleTimePeriod(date(2, Calendar.JUNE, 14),
                                    date(2, Calendar.JUNE, 35))));
        s1.add(new Task("YMK-6",
               new SimpleTimePeriod(date(3, Calendar.JUNE, 20),
                                    date(31, Calendar.JULY, 120))));
        s1.add(new Task("YMK-7",
               new SimpleTimePeriod(date(1, Calendar.AUGUST, 100),
                                    date(8, Calendar.AUGUST, 200))));
        s1.add(new Task("YMK-8",
               new SimpleTimePeriod(date(10, Calendar.AUGUST, 100),
                                    date(10, Calendar.AUGUST, 200))));
        s1.add(new Task("YMK-9",
               new SimpleTimePeriod(date(12, Calendar.AUGUST, 100),
                                    date(12, Calendar.SEPTEMBER, 200))));
        s1.add(new Task("YMK-10",
               new SimpleTimePeriod(date(13, Calendar.SEPTEMBER, 100),
                                    date(31, Calendar.OCTOBER, 200))));
        s1.add(new Task("YMK-11",
               new SimpleTimePeriod(date(1, Calendar.NOVEMBER, 100),
                                    date(15, Calendar.NOVEMBER, 200))));
        s1.add(new Task("YMK-12",
               new SimpleTimePeriod(date(28, Calendar.NOVEMBER, 100),
                                    date(30, Calendar.NOVEMBER, 200))));

        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);


        return collection;
    }*/

    /**
     * Utility method for creating <code>Date</code> objects.
     *
     * @param day  the date.
     * @param month  the month.
     * @param year  the year.
     *
     * @return a date.
     */
    private static Date date(final int day, final int month, final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        return result;

    }
        
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(final IntervalCategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createGanttChart(
            "Corina time line demo",  // chart title
            "Sample",              // domain axis label
            "Year",              // range axis label
            dataset,             // data
            false,                // include legend
            true,                // tooltips
            false                // urls
        );    
        chart.getCategoryPlot().getDomainAxis().setMaximumCategoryLabelWidthRatio(10.0f);

        
        StandardChartTheme theme = new StandardChartTheme("mytheme");
        theme.setShadowVisible(false);
        BarPainter painter = new StandardBarPainter();
        
        // disable bar outlines...
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        
        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, new Color(0, 0, 64)
        );
        GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, new Color(0, 64, 0)
        );
        GradientPaint gp2 = new GradientPaint(
            0.0f, 0.0f, Color.red, 
            0.0f, 0.0f, new Color(64, 0, 0)
        );
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        
        
        theme.setBarPainter(painter);
        theme.setPlotBackgroundPaint(Color.WHITE);

        
        theme.apply(chart);
        
        
	     // write the chart to a PDF file...
        File fileName = new File("/tmp/jfreechart1.pdf");
        
        
        try {
			saveChartAsPDF(fileName, chart, 1000, 800, new DefaultFontMapper());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return chart;    
    }
    
    public static void writeChartAsPDF(BufferedOutputStream out,
    		JFreeChart chart,
    		Integer width, int height,
    		FontMapper mapper) throws IOException {
    		Rectangle pagesize = new Rectangle(width, height);
    		
    		Document document = new Document(PageSize.A3, 50, 50, 50, 50);
    		
    		width = (int) document.getPageSize().getWidth();
    		height = (int) document.getPageSize().getHeight();
    		
    		//Document document = new Document(pagesize, 50, 50, 50, 50);
    		try {
    		PdfWriter writer = PdfWriter.getInstance(document, out);
    		document.addAuthor("JFreeChart");
    		document.addSubject("Demonstration");
    		document.open();
    		PdfContentByte cb = writer.getDirectContent();
    		PdfTemplate tp = cb.createTemplate(width, height);
    		Graphics2D g2 = tp.createGraphics(width, height, mapper);
    		Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);
    		chart.draw(g2, r2D, null);
    		g2.dispose();
    		cb.addTemplate(tp, 0, 0);
    		}
    		catch(DocumentException de) {
    		System.err.println(de.getMessage());
    		}
    		document.close();
    		}
    
    public static void saveChartAsPDF(File file,
    		JFreeChart chart,
    		int width, int height,
    		FontMapper mapper) throws IOException {
    		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
    		final CategoryPlot plot = (CategoryPlot) chart.getPlot();
            final CategoryItemRenderer renderer = plot.getRenderer();
            renderer.setSeriesPaint(0, Color.BLACK);
    		
    		writeChartAsPDF(out, chart, width, height, mapper);
    		out.close();
    		}
    
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {

        final TimeLineChartTest demo = new TimeLineChartTest();
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }
    
 
}