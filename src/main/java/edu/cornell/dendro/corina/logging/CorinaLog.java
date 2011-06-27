/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
// Copyright (c) 2004-2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package edu.cornell.dendro.corina.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.SimpleLog;

/**
 * Log implementation that subclasses SimpleLog and stores entries in a circular
 * buffer to be presented in a GUI window.
 * TODO: write factory class or method to obtain named <code>CorinaLog</code>s to
 * save instantiations.
 * 
 * @author Aaron Hamid
 */
@SuppressWarnings("serial")
public class CorinaLog extends SimpleLog {
	/**
	 * The maximum number of log messages that will be kept in memory.
	 * TODO: grab this from prefs...
	 */
	public static final int MAXLINES;
	
	public final static PrintStream realOut;
	public final static PrintStream realErr;
	
	static {
		realOut = System.out;
		realErr = System.err;
		String s = System.getProperty("corina.log.maxlines");
		int maxlines = 500;
		if (s != null) {
			try {
				maxlines = Integer.parseInt(s);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
		}
		MAXLINES = maxlines;
	}
	// protected visibility to avoid synthetic accessors
	protected static String[] log = new String[MAXLINES];
	protected static int logLineCount;
	protected static boolean wrap;
	private static LogListModel listModel = new LogListModel();
	private static CorinaLog STDOUT = new CorinaLog("STDOUT", true);
	private static CorinaLog STDERR = new CorinaLog("STDERR", true);
	private static Object LOCK = new Object();
	
	public static void init() {
	
		System.setOut(new PrintStream(new SplitOutput( createPrintStream(STDOUT, false), realOut)));
		System.setOut(new PrintStream(new SplitOutput( createPrintStream(STDERR, true), realErr)));
	}
	
	private static PrintStream createPrintStream(final Log log, final boolean error) {
		return new PrintStream(new OutputStream() {
			@Override
			public void write(int b) {
				byte[] barray = {(byte) b};
				write(barray, 0, 1);
			}
			
			@Override
			public void write(byte[] b, int off, int len) {
				String str = new String(b, off, len);
				// skip any trailing EOL
				if ("\r".equals(str) || "\n".equals(str) || "\r\n".equals(str))
					return;
				if (error) {
					log.error(str);
				}
				else {
					log.info(str);
				}
			}
		});
	}
	
	public static ListModel getLogListModel() {
		return listModel;
	}
	
	/**
	 * ListModel for a JList-based log viewer
	 * 
	 * @author Aaron Hamid
	 */
	static class LogListModel implements ListModel {
		@SuppressWarnings("unchecked")
		ArrayList listeners = new ArrayList();
		
		// protected visibility to avoid synthetic accessor
		protected void fireIntervalAdded(int index1, int index2) {
			for (int i = 0; i < listeners.size(); i++) {
				ListDataListener listener = (ListDataListener) listeners.get(i);
				listener.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index1, index2));
			}
		}
		
		// protected visibility to avoid synthetic accessor
		protected void fireIntervalRemoved(int index1, int index2) {
			for (int i = 0; i < listeners.size(); i++) {
				ListDataListener listener = (ListDataListener) listeners.get(i);
				listener.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index1, index2));
			}
		}
		
		@SuppressWarnings("unchecked")
		public void addListDataListener(ListDataListener listener) {
			listeners.add(listener);
		}
		
		@SuppressWarnings("unchecked")
		public void removeListDataListener(ListDataListener listener) {
			listeners.add(listener);
		}
		
		public Object getElementAt(int index) {
			if (wrap) {
				if (index < MAXLINES - logLineCount)
					return log[index + logLineCount];
				else
					return log[index - MAXLINES + logLineCount];
			}
			else
				return log[index];
		}
		
		public int getSize() {
			if (wrap)
				return MAXLINES;
			else
				return logLineCount;
		}
		
		void update(final int lineCount, final boolean oldWrap) {
			if (lineCount == 0 || listeners.size() == 0)
				return;
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if (wrap) {
						if (oldWrap)
							fireIntervalRemoved(0, lineCount - 1);
						else {
							fireIntervalRemoved(0, logLineCount);
						}
						fireIntervalAdded(MAXLINES - lineCount + 1, MAXLINES);
					}
					else {
						fireIntervalAdded(logLineCount - lineCount + 1, logLineCount);
					}
				}
			});
		}
	}
	
	private Log chained;
	
	@SuppressWarnings("unchecked")
	public CorinaLog(Class clazz) {
		super(clazz.toString());
		this.chained = LogFactory.getLog(clazz);
	}
	
	public CorinaLog(String name) {
		super(name);
		this.chained = LogFactory.getLog(name);
	}
	
	public CorinaLog(String name, boolean chain) {
		super(name);
		if (chain)
			this.chained = LogFactory.getLog(name);
	}
	
	/**
	 * Log to central log buffer.
	 */
	@Override
	protected void write(StringBuffer buf) {
		String str = buf.toString();
		// If multiple threads log stuff, we don't want
		// the output to get mixed up
		synchronized (LOCK) {
			StringTokenizer st = new StringTokenizer(str, "\r\n");
			int lineCount = 0;
			boolean oldWrap = wrap;
			while (st.hasMoreTokens()) {
				lineCount++;
				log[logLineCount] = st.nextToken().replace('\t', ' ');
				if (++logLineCount >= log.length) {
					wrap = true;
					logLineCount = 0;
				}
			}
			listModel.update(lineCount, oldWrap);
		}
	}
	
	/**
	 * Override default dispatching to also call chained instance, if any.
	 */
	@Override
	protected void log(int type, Object message, Throwable t) {
		super.log(type, message, t);
		if (chained == null)
			return;
		switch (type) {
			case SimpleLog.LOG_LEVEL_TRACE :
				chained.trace(message, t);
				break;
			case SimpleLog.LOG_LEVEL_DEBUG :
				chained.debug(message, t);
				break;
			case SimpleLog.LOG_LEVEL_INFO :
				chained.info(message, t);
				break;
			case SimpleLog.LOG_LEVEL_WARN :
				chained.warn(message, t);
				break;
			case SimpleLog.LOG_LEVEL_ERROR :
				chained.error(message, t);
				break;
			case SimpleLog.LOG_LEVEL_FATAL :
				chained.fatal(message, t);
				break;
			default :
				chained.info(message, t);
				break;
		}
	}
}

class SplitOutput extends OutputStream {

	OutputStream o1;
	OutputStream o2;
	
	public SplitOutput(OutputStream arg1, OutputStream arg2){
		o1 = arg1;
		o2 = arg2;
	}
	
	
	/**
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int argB) throws IOException {
		o1.write(argB);
		o2.write(argB);
	}
}
