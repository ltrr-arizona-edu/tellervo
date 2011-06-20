package edu.cornell.dendro.corina.gui;

import javax.swing.JTextArea;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class TextAreaLogger extends AppenderSkeleton {

	private JTextArea myTextArea;
	private PatternLayout layout;
	
	public TextAreaLogger(PatternLayout lyout, JTextArea myTextArea)
	{
		this.myTextArea = myTextArea;
		this.layout = lyout;
	}
	
	@Override
	protected void append(LoggingEvent arg0) {
		
		myTextArea.append(layout.format(arg0));

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return false;
	}

}
