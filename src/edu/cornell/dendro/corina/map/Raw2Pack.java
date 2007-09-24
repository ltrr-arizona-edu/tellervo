package edu.cornell.dendro.corina.map;

import java.io.*;
import java.util.StringTokenizer;

/*
  STRATEGY:
  -- load into array of segments
  -- perform desired segment-crunching (e.g., split at IDL)
  -- loop, emitting segments

  -- (next revision: break down by continent)
 */

public class Raw2Pack {
    private static class Segment {
	// construct a segment from a raw line
	Segment(String line) {
	    // WRITEME
	}

	// emit the binary encoding of a segment
	void emit(OutputStream o) {
	    // WRITEME
	}
    }

    public static void main(String args[]) throws IOException {
	final String filename = "earth.raw";

	int size[] = new int[] { 0, 0, 0, 0, 0, 0, };

	BufferedReader r = new BufferedReader(new FileReader(filename));
	String line;
	for (;;) {
	    line = r.readLine();
	    if (line == null)
		break;
	    StringTokenizer tok = new StringTokenizer(line, "=, ");

	    // "cat", $(cat)
	    tok.nextToken();
	    tok.nextToken();
	    
	    // "type", $(type)
	    tok.nextToken();
	    tok.nextToken();

	    // (x,y)
	    int x = Integer.parseInt(tok.nextToken());
	    int y = Integer.parseInt(tok.nextToken());

	    while (tok.hasMoreTokens()) {
		int x2 = Integer.parseInt(tok.nextToken());
		int y2 = Integer.parseInt(tok.nextToken());

		int dx = Math.abs(x2 - x);
		int dy = Math.abs(y2 - y);

		// how many 7-bit bytes will i need to store dx?  dy?
		// (answer: log base 7 of dx = log dx / log 7)

		try {
		    int s1 = (dx==0 ? 0 : (int) Math.ceil(Math.log(dx) / Math.log(7)));
		    size[s1]++;
		} catch (ArrayIndexOutOfBoundsException aioobe) {
		    System.out.println("oops, dx=" + dx);
		}

		try {
		    int s2 = (dy==0 ? 0 : (int) Math.ceil(Math.log(dy) / Math.log(7)));
		    size[s2]++;
		} catch (ArrayIndexOutOfBoundsException aioobe) {
		    System.out.println("oops, dx=" + dx);
		}

		x = x2;
		y = y2;
	    }
	}

	int total = 0;
	for (int i=0; i<size.length; i++) {
	    System.out.println("number of moves that fit in " + i + " bytes: " + size[i]);
	    total += i * size[i];
	}
	System.out.println("total size for all: " + total + " B = " +
			   (total/1024) + " KB = " +
			   (total/(1024*1024)) + " MB");
    }
}
