package edu.cornell.dendro.corina.cross;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import edu.cornell.dendro.corina.gui.ProgressMeter;
import edu.cornell.dendro.corina.gui.SplashDialog;
import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;

/**
 * This class does the "meat" of the crossdating dialog - it prepares all the crosses into pairs
 * 
 * @author Lucas Madar
 */

public class CrossdateCollection {
	public static class Pairing {
		private Sample floating;  // The series that is being dated
		private Sample reference; // The series that is being used as the reference against which the floating is dated
		private Map<Class<? extends Cross>, Cross> crosses;
		
		public Pairing(Sample floating, Sample reference) {
			if(floating == null || reference == null) 
				throw new IllegalArgumentException("Pairing must have two valid samples");
			
			this.floating = floating;
			this.reference = reference;
		}
		
		public Cross getCrossForClass(Class<?> clazz) {
			return crosses.get(clazz);
		}
		
		public Sample getFloating() {
			return floating;
		}

		public Sample getReference() {
			return reference;
		}
		
		@Override
		public boolean equals(Object o) {
			if(o instanceof Pairing) {
				if(((Pairing) o).floating.equals(floating) &&
				   ((Pairing) o).reference.equals(reference))
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
								Cross cross = Cross.makeCross(Cross.ALL_CROSSDATES[k], pairing.floating, pairing.reference);

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
