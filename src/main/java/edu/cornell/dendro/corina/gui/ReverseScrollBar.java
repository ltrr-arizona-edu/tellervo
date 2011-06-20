package edu.cornell.dendro.corina.gui;

// taken from http://forums.sun.com/thread.jspa?messageID=4230713

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.concurrent.locks.ReentrantLock;
 
import javax.swing.JScrollBar;
 
///////////////////////////////////////////////////////////////////
/**
 * This class implements a scrollbar whose default behavior is to
 * show the end of the area. If it is a vertical scrollbar, it will
 * continue to show the bottom of the scrolled area until the 
 * scrollbar is set to not show the bottom.
 */
///////////////////////////////////////////////////////////////////
public class ReverseScrollBar extends JScrollBar {
 
	/////////////////////////////////////////////////////////////////
	//	------------------------  Inner Classes ---------------------
	/////////////////////////////////////////////////////////////////
 
	/////////////////////////////////////////////////////////////////
	//	------------------------  Static Fields ---------------------
	/////////////////////////////////////////////////////////////////
 
	/////////////////////////////////////////////////////////////////
	//	------------------------  Static Methods --------------------
	/////////////////////////////////////////////////////////////////
 
	/////////////////////////////////////////////////////////////////
	//	------------------------  Public Fields ---------------------
	/////////////////////////////////////////////////////////////////
 
	/////////////////////////////////////////////////////////////////
	//	------------------------  Non-Public Fields -----------------
	/////////////////////////////////////////////////////////////////
 
	private static final long	serialVersionUID	= 1L;
	private int vertScrollPrevMax = 0;
	private int vertScrollPrevExtent = 0;
	private int vertScrollPrevValue = 0;
	
	private ReentrantLock		mutex	= new ReentrantLock();
	
	private boolean locked = false;
	
	/////////////////////////////////////////////////////////////////
	//	------------------------  Constructors ----------------------
	/////////////////////////////////////////////////////////////////
 
	///////////////////////////////////////////////////////////////////
	/**
	 * Creates a ReverseScrollBar with the following initial values:
	 * 
	 * minimum = 0 
	 * maximum = 100 
	 * value = 0
	 * extent = 10
	 */
	///////////////////////////////////////////////////////////////////
	public ReverseScrollBar () {
		super();
		
		localInit();
	}
	///////////////////////////////////////////////////////////////////
	/**
	 * Creates a ReverseScrollBar with the specified orientation and the 
	 * following initial values:
	 * 
	 * minimum = 0 
	 * maximum = 100 
	 * value = 0
	 * extent = 10
	 *
	 * @param orientation
	 */
	///////////////////////////////////////////////////////////////////
	public ReverseScrollBar (int orientation) {
		super (orientation);
		
		localInit();
	}
	
	///////////////////////////////////////////////////////////////////
	/**
	 * Creates a ReverseScrollBar with the specified orientation, value, 
	 * extent, minimum, and maximum. The "extent" is the size of the 
	 * viewable area. It is also known as the "visible amount".
	 *
	 * @param orientation
	 * @param value
	 * @param extent
	 * @param min
	 * @param max
	 */
	///////////////////////////////////////////////////////////////////
	public ReverseScrollBar (int orientation,
            int value,
            int extent,
            int min,
            int max) {
		super (orientation, value, extent, min, max);
		
		localInit();
	}
 
	/////////////////////////////////////////////////////////////////
	//	------------------------  Public Methods ----------------
	/////////////////////////////////////////////////////////////////
 
	///////////////////////////////////////////////////////////////////
	/**
	 * @return the locked
	 */
	///////////////////////////////////////////////////////////////////
	public boolean isLocked() {
	
		return locked;
	}
	///////////////////////////////////////////////////////////////////
	/**
	 * @param locked the locked to set
	 */
	///////////////////////////////////////////////////////////////////
	public void setLocked(boolean locked) {
	
		this.locked = locked;
	}
	
	/////////////////////////////////////////////////////////////////
	//	------------------------  Non-Public Methods ----------------
	/////////////////////////////////////////////////////////////////
 
	private void updatePrevModel() {
		vertScrollPrevMax = model.getMaximum();
		vertScrollPrevExtent = model.getExtent();
		vertScrollPrevValue = model.getMinimum();
	}
	
	private void updateOnAdjustment() {
		
		if (!locked) {
		
			mutex.lock();
			try {
				// see if something new was added or the window resized
				if (model.getMaximum() != vertScrollPrevMax
						|| model.getExtent() != vertScrollPrevExtent) {
					// Yup ...
 
					// See if prev version was maxed
					if (vertScrollPrevValue + vertScrollPrevExtent >= vertScrollPrevMax) {
						// Yup ...
 
						setValue(Integer.MAX_VALUE);
					}
				} // if (model.getMaximum()
 
				vertScrollPrevMax = model.getMaximum();
				vertScrollPrevExtent = model.getExtent();
				vertScrollPrevValue = model.getValue();
			} finally {
				mutex.unlock();
			}
		}
	}
	
	private void localInit() {
 
		updatePrevModel();
		
		addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				updateOnAdjustment();
			}
		});
	}
 
	/////////////////////////////////////////////////////////////////
	//	------------------------  Interface Implementations ---------
	/////////////////////////////////////////////////////////////////
 
}