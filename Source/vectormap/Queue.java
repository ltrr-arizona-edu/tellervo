// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.util.*;

/**
 * A FIFO queue of Objects for communication between producer and consumer threads.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class Queue {

  private LinkedList list = new LinkedList();

  public synchronized void queue(Object v) throws InterruptedException {
    list.addLast(v);
    if (list.size() == 1) notifyAll();
  }

  public synchronized Object dequeue() throws InterruptedException {
    while (list.size() == 0) wait();
    return list.removeFirst();
  }

  public synchronized List dequeueAll() throws InterruptedException {
    while (list.size() == 0) wait();
    List dequeuedlist = list;
    list = new LinkedList();
    return dequeuedlist;
  }
}