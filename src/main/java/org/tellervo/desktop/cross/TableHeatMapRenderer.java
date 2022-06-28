package org.tellervo.desktop.cross;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.util.ColorUtils;

public class TableHeatMapRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(TableHeatMapRenderer.class);
	private float maxvalue = 0;
	private Double minvalue = null;
	
    public TableHeatMapRenderer() {

    	
    	
    	setOpaque(true); //MUST do this for background to show up.
    }
	
    public void setMaxValue(float val)
    {
    	maxvalue = val;
    }
    
    public void setMinValue(Double val)
    {
    	minvalue = val;
    }
    
	@Override
	public Component getTableCellRendererComponent( JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
			
		
		setText(" ");
		Color newColor = Color.WHITE;
		setBackground(newColor);
		
		if(value==null)
		{
			setText(" ");
			return this;
		}
		
		Cross cross = (Cross) value;
		
		float score;
		
		try{
			TopScores scores = cross.getHighScores();
			score = scores.getScores().get(0).score;
		} catch (Exception e)
		{
			
			//log.error("error getting scores: "+e.getLocalizedMessage());
			return this;
		}
		
		
		DecimalFormat format = new DecimalFormat("#.##");
		format.setRoundingMode(RoundingMode.HALF_UP);
		
		setText(format.format(score));
		setBackground(ColorUtils.generateHeatMapColor(maxvalue, score));
	
		return this;
	}
	
	
	
	
}
