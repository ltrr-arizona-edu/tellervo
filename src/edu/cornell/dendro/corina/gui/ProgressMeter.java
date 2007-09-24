// Copyright (c) 2004-2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package edu.cornell.dendro.corina.gui;

import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ChangeEvent;

/**
 * A headless ProgressMonitor that simply notifies a listener
 * when progress should be shown and hidden.
 * @author Aaron Hamid
 */
public class ProgressMeter {
  public static interface ProgressListener {
    public void stateChanged(ProgressEvent event);
    public void displayProgress(ProgressEvent event);
    public void closeProgress(ProgressEvent event);
  }
  public static class ProgressEvent extends ChangeEvent {
    private int min;
    private int max;
    private String note;
    private int value;
    public ProgressEvent(Object source, int min, int max, String note, int value) {
      super(source);
      this.min = min;
      this.max = max;
      this.note = note;
      this.value = value;
    }
    public int getMinimum() {
      return min;
    }
    public int getMaximum() {
      return max;
    }
    public String getNote() {
      return note;
    }
    public int getValue() {
      return value;
    }
  }

  private String note;
  private long T0;
  private int millisToDecideToPopup = 500;
  private int millisToPopup = 2000;
  private int min;
  private int max;
  private int v;
  private int lastDisp;
  private int reportDelta;
  private Set listeners = new HashSet();
  private boolean popped;

  public ProgressMeter() {
    this(0, 0);
  }
  public ProgressMeter(int min, int max) {
    this(min, max, null);
  }
  private ProgressMeter(int min, int max, String note) {
    this.min = min;
    this.max = max;
    reportDelta = (max - min) / 100;
    if (reportDelta < 1) reportDelta = 1;
    v = min;
    T0 = System.currentTimeMillis();
  }

  public void addProgressListener(ProgressListener listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }
  public void removeProgressListener(ProgressListener listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  private void notifyDisplay() {
    ProgressListener[] l;
    synchronized (listeners) {
      l = (ProgressListener[]) listeners.toArray(new ProgressListener[listeners.size()]);
    }
    ProgressEvent e = new ProgressEvent(this, min, max, note, v);
    for (int i = 0; i < l.length; i++) {
      l[i].displayProgress(e);
    }
  }
  private void notifyClosed() {
    ProgressListener[] l;
    synchronized (listeners) {
      l = (ProgressListener[]) listeners.toArray(new ProgressListener[listeners.size()]);
    }
    ProgressEvent e = new ProgressEvent(this, min, max, note, v);
    for (int i = 0; i < l.length; i++) {
      l[i].closeProgress(e);
    }
  }
  private void notifyStateChanged() {
    ProgressListener[] l;
    synchronized (this) {
      l = (ProgressListener[]) listeners.toArray(new ProgressListener[listeners.size()]);
    }
    ProgressEvent e = new ProgressEvent(this, min, max, note, v);
    for (int i = 0; i < l.length; i++) {
      l[i].stateChanged(e);
    }
  }

  /** 
   * Indicate the progress of the operation being monitored.
   * If the specified value is >= the maximum, the progress
   * meter is closed. 
   * @param value an int specifying the current value, between the
   *        maximum and minimum specified for this component
   * @see #setMinimum
   * @see #setMaximum
   * @see #close
   */
  public void setProgress(int value) {
    v = value;
    if (value >= max) {
      close();
    } else if (value >= lastDisp + reportDelta) {
      lastDisp = value;
      if (popped) {
        notifyStateChanged();
      } else {
        long T = System.currentTimeMillis();
        long dT = (int)(T-T0);
        if (dT >= millisToDecideToPopup) {
          int predictedCompletionTime;
          if (value > min) {
            predictedCompletionTime = (int)((long)dT *
                                            (max - min) /
                                            (value - min));
          } else {
            predictedCompletionTime = millisToPopup;
          }
          if (predictedCompletionTime >= millisToPopup) {
            popped = true;
            notifyDisplay();
          }
        }
      }
    }
  }

  /**
   * Indicate that the operation is complete.  This happens automatically
   * when the value set by setProgress is >= max, but it may be called
   * earlier if the operation ends early.
   */
  public void close() {
    if (!popped) return;
    popped = false;
    notifyClosed();
  }

  /**
   * Returns the minimum value -- the lower end of the progress value.
   * @return an int representing the minimum value
   * @see #setMinimum
   */
  public int getMinimum() {
    return min;
  }

  /**
   * Specifies the minimum value.
   * @param m an int specifying the minimum value
   * @see #getMinimum
   */
  public void setMinimum(int m) {
    min = m;
  }

  /**
   * Returns the maximum value -- the higher end of the progress value.
   * @return an int representing the maximum value
   * @see #setMaximum
   */
  public int getMaximum() {
    return max;
  }

  /**
   * Specifies the maximum value.
   * @param m an int specifying the maximum value
   * @see #getMaximum
   */
  public void setMaximum(int m) {
    max = m;
  }

  /**
   * Specifies the amount of time to wait before deciding whether or
   * not to notify listeners that they should display.
   * @param millisToDecideToPopup an int specifying the time to wait,
   *        in milliseconds
   * @see #getMillisToDecideToPopup
   */
  public void setMillisToDecideToPopup(int millisToDecideToPopup) {
    this.millisToDecideToPopup = millisToDecideToPopup;
  }

  /**
   * Returns the amount of time this object waits before deciding whether
   * or not to notify listeners to display.
   * @see #setMillisToDecideToPopup
   */
  public int getMillisToDecideToPopup() {
    return millisToDecideToPopup;
  }

  /**
   * Specifies the amount of time it will take for listeners to be notified
   * to display. (If the predicted time remaining is less than this time, then
   * listeners won't be notified to display)
   * @param millisToPopup  an int specifying the time in milliseconds
   * @see #getMillisToPopup
   */
  public void setMillisToPopup(int millisToPopup) {
    this.millisToPopup = millisToPopup;
  }

  /**
   * Returns the amount of time it will take before listeners are notified
   * to display.
   * @see #setMillisToPopup
   */
  public int getMillisToPopup() {
    return millisToPopup;
  }

  /**
   * Specifies the additional note that is displayed along with the
   * progress message.
   * @param note a String specifying the note to display
   * @see #getNote
   */
  public void setNote(String note) {
    this.note = note;
    if (popped) {
      notifyStateChanged();
    }
  }

  /**
   * Returns the additional note that is displayed along with the
   * progress message.
   * @return a String specifying the note to display
   * @see #setNote
   */
  public String getNote() {
    return note;
  }
}