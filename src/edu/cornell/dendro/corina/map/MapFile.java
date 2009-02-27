//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.zip.GZIPInputStream;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

import edu.cornell.dendro.corina.tridas.Location;

// an interface to earth.rez-format files.
// ABSTRACT: make interface for getting Segments(?) from a disk file,
// then make RezMapFile implement it.  that way, i can play around with
// putting maps in different formats.
// (make raw-to-<type> part of this interface?)

/**
    An interface to the map line-data file.

    <p>Overview of how to use it:</p>
    <ul>
        <li>call MapFile.getHeaders() to get an Iterator

        <li>go through that Iterator to get MapFile.Header objects

        <li>by their bounding boxes (getMinLatitude(), etc) or their type
        (getCategory()/getType()), figure out which ones you're interested in

        <li>call getNumberOfPoints() to figure out how many points are in that segment

        <li>call projectData() with a Projection, and 2 arrays at least that big,
        and it'll give you rendered pixels from disk (this is the potentially slow
        part, since the actual data may be on disk still)
    </ul>

    <p>The other slow part is loading all the headers from disk, the first time
    MapFile is referenced.  That might take about 1/2 a second.</p>
 
    <h2>Used by:</h2>
    <ul>
        <li>MapLayer
        <li>Palette - but only to switch on segments' type/subtype
    </ul>

    <h2>Left to do:</h2>
    <ul>
        <li>Get rid of Data.latitude/longitude, which get re-alloc'd every time for no reason -- or do they?
        <li>Merge Data into Header, and rename to Segment
        <li>JavaDoc everything
        <li>Change interface, so users asy "new MapFile()" / "mapfile.getHeaders()".  (Still class data, though.)
        <li>Fix synchronization bugs -- how to find?
        <li>Write unit tests!
        <li>Future: Enable this to work with different line-map file formats?
        <li>Get rid of main()
        <li>Header.isVisible() is weird
        <li>Profile some more
        <li>Am I guaranteed headers[] is filled before getHeaders() is called?
 
        <li>Use a better line-data format; it's possible to store it more efficiently

        <li>Only re-copy earth.rez if it doesn't already exist
        <li>Put earth.rez in some platform-dependent, user-agnostic location
        (like /Users/Shared/Corina/earth.rez on Mac)

        <li>Have 3 different modes: no caching (segment data always on disk, only headers
        are loaded), with caching (some segment data in memory), memory only (all segments
        loaded into memory).  Decide between them based on user preference or a simple test
    </ul>
*/

/*
 IDEA:
 - class MapFile
 - class Rez -- can load and save .rez files
 - class Pack (pick a better name) -- can load and save .pack files (compressed: 10 MB, hopefully)
 - ...
 - (what's their relationship?)
 - could this work?
*/
public class MapFile {
  private static final String COMPRESSED_FILENAME = "earth.rez.gz";
  private static final String UNCOMPRESSED_FILENAME = "corina-earth.rez";
  private static final File CACHED_FILE = new File(System.getProperty("java.io.tmpdir") + File.separator + UNCOMPRESSED_FILENAME);

  static {
    try {
      // FIXME: don't do this in a static block!
      unpackDataFile();
      open(CACHED_FILE);
      // WAS: open("Libraries/earth.rez");
    } catch (IOException ioe) {
      System.out.println("Error opening map: " + ioe);
    }
  }

    private MapFile() {
        // don't instantiate me
    }

    private static Object lock = new Object();

    private static RandomAccessFile rez; // earth.rez file

    private static Header[] headers; // all the segment headers

    // if /tmp/corina/earth.rez doesn't exist, make it.
    // earth.rez is 44.4 MB; earth.rez.gz is 8.7 MB.
    private static void unpackDataFile() throws IOException {
        // see if it's there, first
        if (CACHED_FILE.isFile()) {

            /* disregard length check for now
            // check length
            boolean lengthOk = (file.length() == 46638184);
            */

            // TODO: check magic
            boolean magicOk = true; // WRITEME

            // if length && magic, it's good, so just return now
            //if (lengthOk && magicOk)
                return;
        }

        // TODO: (lock it?)
        // TODO: put close/close in finally clause

        // copying from a resource to $(TMP) can take 4 or 5 seconds,
        // so we'll put up a progress monitor so users know we haven't died.
        ProgressMonitor progress = new ProgressMonitor(null, // parent
                                                       "Preparing map for first use...", // message
                                                       "", // note
                                                       0, 45*1024*1024); // round up to 45 MB

        // after 0.25 sec, if it looks like it'll take
        // longer than 0.5 sec, show the monitor
        progress.setMillisToDecideToPopup(250);
        progress.setMillisToPopup(500);

        // where i'm going to get the data from
        ClassLoader loader = MapFile.class.getClassLoader();
        InputStream rawInput = loader.getResourceAsStream(COMPRESSED_FILENAME);

        if (rawInput == null) {
          JOptionPane.showMessageDialog(null, "Could not find '" + COMPRESSED_FILENAME + "' in class loader resource path", "File not found", JOptionPane.ERROR_MESSAGE);
          return;
        }

        // where i'm going to put the data
        InputStream in = new GZIPInputStream(rawInput);
        OutputStream out = new FileOutputStream(CACHED_FILE);

        // buffer, for fast reading -- is 16 KB ok?
        byte buf[] = new byte[16*1024];
        int total = 0; // total number of bytes written
        
        try {
          // copy everything from in to out
          int bytesRead;
          while ((bytesRead = in.read(buf)) != -1) {
            out.write(buf, 0, bytesRead);
            total += bytesRead;
            progress.setProgress(total);
          }

          // get rid of the progress monitor
          progress.setProgress(progress.getMaximum());
        } finally {
          // close the streams
          if (in != null) try {
            in.close();
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
          if (out != null) try {
            out.close();
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        }
    }

    /**
       Get an Iterator which will return each Header.
        
       @return an Iterator which returns each Header in turn
    */
    public static Iterator getHeaders() {
        return new Iterator() {
            int next = 0;
            public boolean hasNext() {
                return (next < headers.length);
            }
            public Object next() throws NoSuchElementException {
                if (next == headers.length)
                    throw new NoSuchElementException();

                return headers[next++];
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /* like read(), but guaranteed to read all */
    private static void readAll(RandomAccessFile in, byte buf[]) throws IOException {
	readAll(in, buf, 0, buf.length);
    }
    private static void readAll(RandomAccessFile in,
				byte buf[], int off, int bytes) throws IOException {
	int leftToRead = bytes;
	int whereToWrite = off;

	while (leftToRead > 0) {
	    int actuallyRead = in.read(buf, whereToWrite, leftToRead);
	    leftToRead -= actuallyRead;
	    whereToWrite += actuallyRead;
	}
    }

    // a chunk of map data; one per segment.
    // actually, there should probably only ever be one of these (?) -- not sure
    // usage: once you know you want the data for a header h, just say
    //    Data myData = h.getData(); // takes care of caching, etc.
    // then
    //    g.drawPolyline(myData.longitude, myData.latitude, myData.longitude.length); -- or myData.n?
    // oops: but you have to project it first.
    private static byte bufn[] = new byte[0];

    private static Location loc = new Location();
    private static Point3D vec = new Point3D();

    private static class Data {

        // PERF: latitude[]/longitude[] don't need to be re-alloc'd each time (as long as they're synch'd)
        
        private int longitude[], latitude[]; // in seconds
        private int n;

        // TODO: will i need a version of projectData() that operates on floats, too?
        // if so, call it projectData(Projection, float[], float[]).

        /**
           Project each point of this segment to a pixel location.  The output
           are 2 arrays of ints (x and y) which are already allocated.  They
           should be big enough for all of the data (from getNumberOfPoints()); if
           they aren't, an IllegalArgumentException is thrown.
            
           @param projection the projection to use
           @param x an array to put the x pixel coordinates in
           @param y an array to put the y pixel coordinates in
           @exception IllegalArgumentException if either x or y isn't big enough
           to hold all of the data
        */
        public void projectData(Projection r, int x[], int y[]) throws IllegalArgumentException {
            if (x.length < n || y.length < n)
                throw new IllegalArgumentException("x/y arrays not big enough!");

            // PERF: is this synchro hurting my performance?  (try one loc per Data, no synchro needed)
            synchronized (loc) {
                for (int i=0; i<n; i++) {
                    // put point [i] into |loc|
                    loc.setLatitudeAsSeconds(latitude[i]);
                    loc.setLongitudeAsSeconds(longitude[i]);

                    // project |loc| into |vec|
                    r.project(loc, vec);

                    // extract x/y from |vec|
                    // BUG?: should i round these instead of just casting them?  (i assume casting truncates ... ?)
                    x[i] = (int) vec.getX();
                    y[i] = (int) vec.getY();
                }
            }
        }
        
        // load the data
        // addr: address to start loading segment data from
        // n: number of points that make up this segment
        public static Data load(int address, int n) throws IOException {
            Data data = new Data();

            // alloc space for location data as ints
            // PERF: this is really wasteful! -- maybe not... (cache)
            data.n = n;
            data.longitude = new int[data.n];
            data.latitude = new int[data.n];

            // make sure i have space for this data -- raw bytes from disk.
            // how many bytes to read?  it's (x0,y0,n,dx,dy,dx,dy,...),
            // with each x0,y0,dx,dy 4 bytes, and n is 4 bytes.
            // so it's 4 + 8*(number of points).
            int bufsize = 4 + 8*n;
            if (bufn.length < bufsize) {
                bufn = new byte[(int) (bufsize * 1.15)];
                // add 15% extra; this keeps the number of allocations down (to about 5),
                // while not eating up way too much memory (as, say, bufsize*2 would).
            }

            // read the whole segment at once
            synchronized (rez) {
                rez.seek(address);
                readAll(rez, bufn, 0, bufsize);
            }

            // decode and store the first point, now that i have a place to put it
            data.longitude[0] = decode(bufn, 0);
            data.latitude[0] = decode(bufn, 1);

            // and put raw data into latitude/longitude arrays
            for (int i=0; i<n-1; i++) {
                // decode the bytes from the buffer
                int dx = decode(bufn, 3 + 2*i);
                int dy = decode(bufn, 3 + 2*i+1);

                // (dx,dy) vectors are relative
                data.longitude[i+1] = data.longitude[i] + dx;
                data.latitude[i+1] = data.latitude[i] + dy;
            }
            
            return data;
        }
    }

    // PERFORMANCE: if it comes down to pure number-crunching speed,
    // make Map, *Renderer, Location, Point3D, and Matrix abstract (?), and add JNI-backed native versions.
    // specifically, this won't help JPanel or any swinging, so make sure it's worth it first.

    // header data for a segment
    public static class Header {
        // raw stuff from the header
        private int minlat, maxlat; // min/max latitude, in seconds
        public int getMinLatitude() {
            return minlat;
        }
        public int getMaxLatitude() { // outside of here, only one of these is ever needed!
            return maxlat;
        }
        private int minlong, maxlong; // min/max longitude, in seconds
        public int getMinLongitude() {
            return minlong;
        }
        public int getMaxLongitude() {
            return maxlong;
        }

        private int address; // file offset to start reading data

        // they're <8bits, so scrunch them down as much as possible
        // public byte continent; -- nobody uses this, currently
        private byte category=0;
        private byte type=3;
        public byte getCategory() {
            return category;
        }
        public byte getType() {
            return type;
        }
        // SPEED: if i store nsegs here the first time i read it, data.load() is just one read()
        // SPACE: bytes don't get packed.  ptth.

        // buf: the bytes that make up the segment's header
        // offset: where in buf to start reading/parsing
        public Header(byte[] buf, int offset) { // read starting at offset
            offset /= 4; // decode() wants which INT to parse, not which BYTEs to parse
            maxlat = decode(buf, offset+0);
            minlat = decode(buf, offset+1);
            // MEMORY: maybe store only minlat(int) and (maxlat-minlat)(short) -- but saves only 2B x 30000 = 60K
//            if (Math.abs(maxlat - minlat) > 127*256)
//                System.out.println("d_lat = " + (maxlat - minlat));
            maxlong = decode(buf, offset+2);
            minlong = decode(buf, offset+3);
//            if (Math.abs(maxlong - minlong) > 127*256)
//                System.out.println("d_long = " + (maxlong - minlong));
            // MEMORY: maybe store only minlon(int) and (maxlon-minlon)(short) -- but saves only 2B x 30000 = 60K

            // are any/all of these packable?  if minlat<=2B, minlong<=2B, they could be packed into an int, for example.
            address = decode(buf, offset+4);
            // continent = (byte) decode(buf, offset+5); -- nobody uses this, currently
            category = log2(decode(buf, offset+6));
            type = (byte) decode(buf, offset+7);

            // QUESTION: are all headers in order on disk?  if so, i don't have to do 2 read()s when i load the data,
            // because the number of bytes to load is just (header[i+1].addr-header[i].addr).
            // THIS WOULD CUT SEGMENT LOAD TIME IN HALF!
            // (you'd need to store n_lines in header, then, right?  that's probably easiest.)
        }

        // returns the first bit of b that's set
	// -- since i know only 1 is ever set, =log_2(b)
        private static byte log2(int b) {
            for (byte i=1; i<=32; i++) {
                if ((b & 1) != 0)
                    return i;
                b >>= 1;
            }
            return -1; // can't happen
        }

        private Data getData() {
            // (caching was here; now removed)
            synchronized (rez) {
                try {
                    return Data.load(address, points);
                } catch (IOException ioe) {
                    System.out.println("error loading data: " + ioe);
                    return null;
                }
            }
        }

        // TODO: add caching again

        // really horrible caching!
        Data d = null;

        // number of points that make up this segment
        private int points;

        // PERF: i also make Data.load() faster now: since i already know how many
        // bytes to load, i don't need to call read() twice.

        // TODO: mark on zoom slider where codes/names threshold is?

        public int getNumberOfPoints() {
            return points;
        }

        public void projectData(Projection r, int x[], int y[]) throws IllegalArgumentException {
            try {
                if (d == null) d = getData();
                d.projectData(r, x, y);
            } catch (Throwable t) {
                System.out.println("t=" + t);
                t.printStackTrace();
            }
        }

        // used to figure out where the corners are.
        // BUG(fixed?): need to synch if you're going to use a class member.
        private static Location corner = new Location();

        public int isVisible(Projection r) {
            synchronized (corner) {
                // compute min/min corner
                corner.setLatitudeAsSeconds(minlat);
                corner.setLongitudeAsSeconds(minlong);
                r.project(corner, p1);

                // compute max/max corner
                corner.setLatitudeAsSeconds(maxlat);
                corner.setLongitudeAsSeconds(maxlong);
                r.project(corner, p2);
            }

            // IDEA: project() returns null if z<0, or a Point2D, since z isn't needed except for z-clipping.
            // no, because sometimes one point of a segment might be invisible, which will cause a npe.

            { // WAS: if !r.isVisible2(p1,p2) return Projection.VISIBLE_NO;
              // z-clipping: THIS DOESNT EVEN MAKE SENSE IN RECTANGULAR PROJECTION!
                if (p1.getZ() < 0 || p2.getZ() < 0)
                    return VISIBLE_NO;

                // check for entirely outside viewport -- even this isn't entirely correct...
                if ((p1.getX() > r.view.size.width && p2.getX() > r.view.size.width) ||
                    (p1.getY() > r.view.size.height && p2.getY() > r.view.size.height) ||
                    (p1.getX() < 0 && p2.getX() < 0) ||
                    (p1.getY() < 0 && p2.getY() < 0))
                    return VISIBLE_NO;
            }

            // just a point?
            if (((int) p1.getX() == (int) p2.getX()) && ((int) p1.getY() == (int) p2.getY()))
                return VISIBLE_POINT;

            return VISIBLE_YES;
        }
    }
    private static Point3D p1=new Point3D(), p2=new Point3D(); // weird!

    /** Not visible. */
    public static final int VISIBLE_NO = 0;

    /** Visible. */
    public static final int VISIBLE_YES = 1;

    /** Visible, but only as a point (1 pixel). */
    public static final int VISIBLE_POINT = 2;

    // TODO: with high-res printing (600 dpi laser printers) and antialiasing,
    // is VISIBLE_POINT helping?  is it doing more harm than good?  what sort
    // of performance gain does it offer?

    private static final int MAGIC = 0x86460346;

    // opening the map file and loading all the headers takes a couple hundred milliseconds.
    // enough for an hourglass pointer, perhaps, but not enough for a progress dialog.
    private static void open(File f) throws IOException {
        // check its existence first
        if (!f.exists())
            throw new FileNotFoundException();

        // make sure it's the right size: this is a quick validity check
        /*if (f.length() != 46638184)
            throw new IOException("Not a map file (wrong length)");*/

	// BUG? shouldn't this be in the synch block, too?
	// -- synch on something else?  synch on class?  not needed?

        rez = new RandomAccessFile(f, "r");
        synchronized (rez) {
	    // start to read file: 3 ints at once
            byte buf3[] = new byte[3*4]; // magic, address of header[0], number of segments
	    readAll(rez, buf3);

	    // first number is magic
	    int magic = decode(buf3, 0);
	    if (magic != MAGIC)
		throw new IOException("Not a map file (bad magic)");

	    // second number is addr=12 (useless)

	    // third number is number of segments: 31845
	    int count = decode(buf3, 2);

	    // load all the segment headers
	    // -- (~250K raw, ~1MB in memory, around 500ms to load)
	    // -- load chunks at a time, and then parse them.
	    headers = new Header[count];
	    final int CHUNK = 1024; // load 1024 headers at a time from disk (32K blocks)
	    byte buf[] = new byte[CHUNK*8*4]; // 8 4-byte ints in a segment header
	    int left = count;
	    int done = 0; // sort of redundant now, huh?  (hint: it's count-left)
	    while (left > 0) {
		int len = Math.min(left, CHUNK);
		readAll(rez, buf, 0, len*8*4);
                for (int j=0; j<len; j++) {
		    headers[done] = new Header(buf, j*8*4);
                    done++;
                }
		left -= CHUNK;
	    }
            // PERF: this would be simpler -- and probably faster -- if i just snarfed up all the headers at once.

            // now fill in the sizes for the header
            for (int i=0; i<count-1; i++) {
                headers[i].points = (headers[i+1].address - headers[i].address) / 8;
            }
            headers[count-1].points = ((int) rez.length() - headers[count-1].address) / 8;
	}
    }

    // pick the /n/th int, decoding from little-endian; used by map, too.
    // why isn't this built into the reader itself?  because i usually want to read a big chunk at once, and parse it later.
    private static int decode(byte buf[], int n) {
        // the "0x... &" stuff is necessary, though my brain must be fried because i'm not sure why.
        int x = 0xFF000000 & (buf[4*n+3] << 24);
        x |= 0x00FF0000 & (buf[4*n+2] << 16);
        x |= 0x0000FF00 & (buf[4*n+1] << 8);
        x |= 0x000000FF & (buf[4*n]);
        return x;
    }

    // Close the map file. -- COMPLETELY UNUSED.
    public static void close() throws IOException { // would finalize be better?
        synchronized (rez) {
            rez.close();
        }
    }

    private static int sign(int x) {
        return (x > 0 ? 1 : (x < 0 ? -1 : 0));
    }
    public static void main(String args[]) throws Exception {
        int maxdlat=0, maxdlong=0;
        int maxx=0, maxy=0;
        int maxdx=0, maxdy=0;
        int numBad=0;

        Iterator iter = MapFile.getHeaders();
        while (iter.hasNext()) {
            Header h = (Header) iter.next();

//            if (h.maxlong>0 && h.minlong<0)
//                maxdlong = Math.max(maxdlong, h.maxlong - h.minlong - 3600*360); // special case
//            else
                maxdlong = Math.max(maxdlong, h.maxlong - h.minlong);
            maxdlat = Math.max(maxdlat, h.maxlat - h.minlat);
            Data d = h.getData();
            for (int i=0; i<d.longitude.length; i++) {
                maxx = Math.max(maxx, d.longitude[i]);
                maxy = Math.max(maxy, d.latitude[i]);
                if (i != 0) {
                    int dx = d.longitude[i] - d.longitude[i-1];
                    int dy = d.latitude[i] - d.latitude[i-1];
//                    if (sign(d.longitude[i]) != sign(d.longitude[i-1])) {
//                        dx = d.longitude[i] - d.longitude[i-1] - 3600*360;
//                        numBad++;
//                    }
                    maxdx = Math.max(maxdx, dx);
                    maxdy = Math.max(maxdy, dy);
                }
            }
        }
        System.out.println("maxdlat = " + maxdlat);
        System.out.println("maxdlong = " + maxdlong);
        System.out.println("maxx = " + maxx);
        System.out.println("maxy = " + maxy);
        System.out.println("maxdx = " + maxdx);
        System.out.println("maxdy = " + maxdy);
        System.out.println("numBad = " + numBad);
    }
}
