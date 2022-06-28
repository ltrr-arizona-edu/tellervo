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
package org.tellervo.desktop.cross;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.tellervo.desktop.gui.ProgressMeter;
import org.tellervo.desktop.gui.SplashDialog;
import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;


/**
 * This class does the "meat" of the crossdating dialog - it prepares all the crosses into pairs
 * 
 * @author Lucas Madar
 */

public class CrossdateCollection {
	public static class Pairing {
		private Sample primary;
		private Sample secondary;
		private Map<Class<? extends Cross>, Cross> crosses;
		
		public Pairing(Sample primary, Sample secondary) {
			if(primary == null || secondary == null) 
				throw new IllegalArgumentException("Pairing must have two valid samples");
			
			this.primary = primary;
			this.secondary = secondary;
		}
		
		public Cross getCrossForClass(Class<?> clazz) {
			return crosses.get(clazz);
		}
		
		public Sample getPrimary() {
			return primary;
		}

		public Sample getSecondary() {
			return secondary;
		}
		
		@Override
		public boolean equals(Object o) {
			if(o instanceof Pairing) {
				if(((Pairing) o).primary.equals(primary) &&
				   ((Pairing) o).secondary.equals(secondary))
					return true;
			}
			return false;
		}
	}
	
	@SuppressWarnings("serial")
	public static class NoSuchPairingException extends Exception {
		public NoSuchPairingException(int row, int col) {
			super("No such pairing (" + row + ", " + col + ")");
		}
	}
	
	/** our set of pairings */
	
	private Map<String, Pairing> pairings;
	
	public CrossdateCollection() {
		pairings = new HashMap<String, Pairing>();
	}
	
	public CrossdateCollection(ElementList elements) {
		this();
		
		setElements(elements);
	}

	/**
	 * Given the row and column, get a pairing.
	 * 
	 * @param row
	 * @param col
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public Pairing getPairing(int row, int col) throws NoSuchPairingException {
		Pairing pairing = pairings.get(keyForRowCol(row, col));
		
		if(pairing == null)
			throw new NoSuchPairingException(row, col);
		
		return pairing;
	}
	
	/**
	 * Set our internal list of elements to crossdate
	 * This list should already be pre-loaded, but 
	 * the function will cope if it is not by ignoring 
	 * any load errors.
	 * 
	 * @param elements a new element list with the proper elements
	 */
	public ElementList setElements(final ElementList elements) {
		// show an indicator; this might be time consuming
		SplashDialog splash = new SplashDialog("Crossdating...");
		final ProgressMeter meter = new ProgressMeter(0, 1);
		meter.addProgressListener(splash);
		// force pop up!
		meter.setMillisToDecideToPopup(-1);
		meter.setMillisToPopup(-1);

		final ElementList el = new ElementList(); // our pre-loaded list.
		
		new Thread() {
			public void run() {
				Vector<Sample> samples = new Vector<Sample>();
				
				// first, ensure all elements are preloaded and of the correct type
				for(Element e : elements) {
					Sample s;
					try {
						s = e.load();
					} catch (IOException ioe) {
						System.out.println("failed loading element for xdate - should have been preloaded!");
						continue;
					}
					
					// make a list!
					samples.add(s);
					
					// now, stick it in our list. make it cached if it's not.
					if(e instanceof CachedElement)
						el.add(e);
					else
						el.add(new CachedElement(s));
				}
				
				// now, we know how long we'll have to wait...
				meter.setMaximum(samples.size() * samples.size());
				
				// and start the process!
				Map<String, Pairing> newPairings = new HashMap<String, Pairing>();
				for(int i = 0; i < samples.size(); i++) {
					meter.setNote(samples.get(i).toString());

					for(int j = 0; j < samples.size(); j++) {
						meter.setProgress((i * samples.size()) + j);
						// don't self-crossdate
						if(i == j)
							continue;

						// make a new pairing
						Pairing pairing = new Pairing(samples.get(i), samples.get(j));

						// check to see if it exists already, so we don't have to redo all the math
						Pairing	existingPairing = findPairing(pairing);
						if(existingPairing != null) {
							newPairings.put(keyForRowCol(i, j), existingPairing);
							continue;
						}

						// ok, we're using the new pairing. add it.
						newPairings.put(keyForRowCol(i, j), pairing);

						// make a new hashmap for the crosses
						pairing.crosses = new HashMap<Class<? extends Cross>, Cross>();

						// now, do the crossings!
						for(int k = 0; k < Cross.ALL_CROSSDATES.length; k++) {
							try {
								Cross cross = Cross.makeCross(Cross.ALL_CROSSDATES[k], pairing.primary, pairing.secondary);

								// do the crossdating
								cross.run();
								
								// and save it
								pairing.crosses.put(cross.getClass(), cross);
							} catch (IllegalArgumentException iae) {
								continue;
							}
						}

						// so we can see this working?
						try {
							Thread.sleep(50);
						} catch (Exception e) {}
					}

					// ok, copy the new vector over
					pairings = newPairings;
				}

				// and we're done.
				meter.setProgress(samples.size() * samples.size());
			}
		}.start();
		
		splash.setVisible(true);
		
		return el;
	}
	
	private String keyForRowCol(int row, int col) {
		return row + "," + col;
	}
	
	/**
	 * Find a pairing of the two given samples
	 * 
	 * This can be time consuming with a huge amount of pairs...
	 * 
	 * @param s1
	 * @param s2
	 * @return the pairing, or null if not found
	 */
	private Pairing findPairing(Pairing pairing) {
		for(Pairing p : pairings.values())
			if(p.equals(pairing))
				return p;
		
		return null;
	}
}
