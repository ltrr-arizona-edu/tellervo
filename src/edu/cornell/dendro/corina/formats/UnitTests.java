package edu.cornell.dendro.corina.formats;

import edu.cornell.dendro.corina.sample.Sample;

import java.io.File;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Random;

import junit.framework.TestCase;

public class UnitTests extends TestCase {
    public UnitTests(String name) {
        super(name);
    }

    // make a dummy Sample object, with a bunch of random data
    private static Sample makeDummy() {
	Sample s = new Sample();
	Random r = new Random();

	// add n (random length) new random ints
	int n = r.nextInt(100) + 500;
	for (int i=0; i<n; i++) {
	    s.getData().add(new Integer(1 + r.nextInt(200))); // no 0's!
	}
	return s;
    }

    //
    // testing heidelberg
    //
    public void testHeidelberg() {
	// strategy: make sample, save to disk, load from disk, see if
	// it's the same.

	try {
	    // get a temporary filename to use
	    File file = File.createTempFile("heidelberg", null);
	    file.deleteOnExit();
	    String filename = file.getPath();

	    Sample s1 = makeDummy();
	    Heidelberg h1 = new Heidelberg();
	    BufferedWriter w  = new BufferedWriter(new FileWriter(filename));
	    h1.save(s1, w);
	    w.close();

	    Heidelberg h2 = new Heidelberg();
	    BufferedReader r = new BufferedReader(new FileReader(filename));
	    Sample s2 = h2.load(r);
	    r.close();

	    // WRITEME: give Sample an export(filename, type) method (save()?)
	    // -- this way, save(f) = export(f, DEFAULT_TYPE),
	    // where DEFAULT_TYPE can be set in the prefs (and defaults to corina)

	    // assertEquals(s1, s2);
	    // WRITEME: need Sample.equals() (and unit test for *that*?)

	    // but, since i don't have a Sample.equals(), and more
	    // importantly, because i only want to test some parts of
	    // the sample (those which heidelberg can handle), i'll do
	    // this myself.
	    assertEquals(s1.getData(), s2.getData());
	} catch (IOException ioe) {
	    fail();
	}
    }
}
