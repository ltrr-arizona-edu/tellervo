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

package corina.map;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import java.io.RandomAccessFile;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.zip.GZIPInputStream;

// an interface to earth.rez
public abstract class Map {
    // n.b., i don't need to be abstract, but everything is static, so this ensures
    // nobody thinks he can do something useful with an instance of it.

    private static RandomAccessFile rez; // earth.rez file
    public static Header[] headers; // all the segment headers -- NOW PUBLIC

    // right about here i'm wishing java had C-style error handling.  it'd be cleaner.
    // i suppose ugliness here and clean(ish) error-handling code everywhere else
    // is an acceptable tradeoff, but there's got to be a way to make everybody happy.
    static {
        try {
            open("earth.rez");
        } catch (FileNotFoundException fnfe) {
            try {
                decompress("earth.rez.gz", "earth.rez");
                new File("earth.rez.gz").delete();
                open("earth.rez");
            } catch (IOException ioe) {
                try {
                    // maybe it's a read-only directory ... try $TMP

                    // CHOICE: i can either use createTempFile() directly, which guarantees
                    // uniqueness, but means each user (and each run) creates its own earth.rez
                    // in $(TMP), or i can use the path from createTempFile(), which doesn't
                    // guarantee uniqueness (it'll kill any "earth.rez" file in $(TMP) that another
                    // program might have been using), but it means it only has to uncompress
                    // once.  since that's the whole point of this exercise, we'll do that.

                    File dummy = File.createTempFile("earth", ".rez");
                    dummy.delete(); // (i only want the folder to put it in)
                    String tmp = dummy.getParent() + File.separator + "earth.rez";
                    File f = new File(tmp);
                    if (!(f.exists() && f.length()==46638184)) // maybe it's already there!
                        decompress("earth.rez.gz", tmp);
                    open(tmp);
                } catch (IOException ioe2) {
                    System.out.println("Error opening map: " + ioe2);
                }
            }
        } catch (IOException ioe) {
                System.out.println("Error opening map: " + ioe);
        }
    }

    // --- decompression ---
    // on my 500mhz laptop, this takes 4.5s to blow 8.7mb up to 44.5mb, so it's worth compressing
    // earth.rez for download if your throughput is less than 64mbps, which is almost always the case
    private static void decompress(String infilename, String outfilename) throws IOException {
        // decompress
        InputStream in = new GZIPInputStream(new BufferedInputStream(new FileInputStream(infilename)));
        OutputStream out = new BufferedOutputStream(new FileOutputStream(outfilename));
        try {
            byte buf[] = new byte[32768]; // 32k ... if they're looking at the map, they have memory.
            int bytesRead;
            while ((bytesRead = in.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
        } finally {
            in.close();
            out.flush();
            out.close();
        }
        // true, a progress bar would be nice, but they're virtually impossible to use.  darn.
    }
    // --- end decompression ---

    // - gridlines ----------------------------------------------------------------------

    public static final int STEP=5; // a gridline is this many degrees long

    // a gridline factory
    static Header h = new Header(); // return the same one every time, because nobody cares
    static {
        h.category = 0;
        h.type = 3;
    }
    public static Header makeGridline(boolean horiz, int latitude, int longitude) {
        if (horiz) {
            h.minlat = h.maxlat = latitude * 3600;
            h.minlong = longitude * 3600;
            h.maxlong = (longitude + STEP ) * 3600;
        } else {
            // swap howFarAround and whichRing, which are probably misnomers
            h.minlong = h.maxlong = longitude * 3600;
            h.minlat = latitude * 3600;
            h.maxlat = (latitude + STEP) * 3600;
        }

        return h;
    }

    // a chunk of map data; one per segment.
    // actually, there should probably only ever be one of these (?) -- not sure
    // usage: once you know you want the data for a header h, just say
    //    Data myData = h.getData(); // takes care of caching, etc.
    // then
    //    g.drawPolyline(myData.x, myData.y, myData.x.length);
    private static byte buf3[] = new byte[3*4]; // x, y, n
    private static byte bufn[] = new byte[0];
    public static class Data { // NEW ONE ALLOCATED EACH TIME!  RECYCLE?  (no, that defeats the cache...)
        public int x[], y[];

        // load the data
        public static Data load(Header h) throws IOException {
            Data data = new Data();
            
            // jump to data
            rez.seek(h.address);

            // number of lines in this segment
            int n;

            // buffer for (x,y), n
            rez.read(buf3);
            n = decode(buf3, 2); // decode the last one first: n

            // alloc space
            data.x = new int[n+1];
            data.y = new int[n+1];

            // decode and store the first point, now that i have a place to put it
            data.x[0] = decode(buf3, 0);
            data.y[0] = decode(buf3, 1);

            // eat them all up at once -- allocing more space if needed
            int bufsize = n*2*4; // 2 ints per point, 4 bytes per int
            if (bufn.length < bufsize)
                bufn = new byte[bufsize]; // normalize to 2^n to minimize reallocs?
            rez.read(bufn, 0, bufsize);

            // and loop
            for (int i=0; i<n; i++) {
                // decode the bytes from the buffer
                int dx = decode(bufn, 2*i);
                int dy = decode(bufn, 2*i+1);

                // (dx,dy) vectors are relative
                data.x[i+1] = data.x[i] + dx;
                data.y[i+1] = data.y[i] + dy;
            }

            return data;
        }
    }

    // header data for a segment
    private static byte buf8[] = new byte[8*4]; // 8 ints in a segment header
    public static class Header {
        // raw stuff from the header
        public int minlat, maxlat; // min/max latitude, in seconds -- use "int minlat, byte/short dlat" to save a couple more?
        public int minlong, maxlong; // min/max longitude, in seconds
        public int address; // file offset to start reading data
        public byte continent, category, type; // they're <8bits, so scrunch them down as much as possible
        
        public static Header load() throws IOException { // read the segment at the file pointer
            Header h = new Header();
            rez.read(buf8); // better yet, why not snarf up all count*8 ints at once?  how much memory would that take?
            h.maxlat = decode(buf8, 0);
            h.minlat = decode(buf8, 1);
            h.maxlong = decode(buf8, 2);
            h.minlong = decode(buf8, 3);
            // are any/all of these packable?  if minlat<=2B, minlong<=2B, they could be packed into an int, for example.
            h.address = decode(buf8, 4);
            h.continent = (byte) decode(buf8, 5);
            h.category = log2(decode(buf8, 6));
            h.type = (byte) decode(buf8, 7);
            return h;
        }

        // returns the first bit of b that's set -- since i know only 1 is ever set, =log_2(b)
        private static byte log2(int b) {
            for (byte i=1; i<=32; i++) {
                if ((b & 1) != 0)
                    return i;
                b >>= 1;
            }
            return -1; // can't happen
        }

        private Reference ref = new SoftReference(null);
        public Data getData() {
            Data data = (Data) ref.get();

            if (data == null) { // cache miss
                try {
                    data = Data.load(this);
                    ref = new SoftReference(data);
                } catch (IOException ioe) {
                    System.out.println("error loading data: " + ioe);
                    // how to deal?
                }
            }

            return data;
        }

        public Location getInsideCorner() {
            return new Location(minlat, minlong);
        }
        public Location getOutsideCorner() {
            return new Location(maxlat, maxlong);
        }
        public int isVisible(Renderer r) {
            // compute corner points
            Vector3 p1 = r.project(getInsideCorner());
            Vector3 p2 = r.project(getOutsideCorner());

            // idea: project() returns null if z<0, or a Point2D, since z isn't needed except for z-clipping.
            // no, because sometimes one point of a segment might be invisible, which will cause a npe.

            if (!r.isVisible(p1, p2))
                return Renderer.VISIBLE_NO;

            // just a point?
            if (((int) p1.x == (int) p2.x) && ((int) p1.y == (int) p2.y))
                return Renderer.VISIBLE_POINT;

            return Renderer.VISIBLE_YES;
        }
    }
    
    private static final int MAGIC = 0x86460346;
    private static void open(String filename) throws IOException {
        // check its existence first
        if (!new File(filename).exists())
            throw new FileNotFoundException();

        // make sure it's the right size: this is a quick validity check
        if (new File(filename).length() != 46638184)
            throw new IOException("Not a map file (wrong length)");
        
        // start to read file: 3 ints at once
        rez = new RandomAccessFile(filename, "r");
        buf3 = new byte[3*4]; // are static blocks executed in arbitrary order?
        buf8 = new byte[8*4];
        rez.read(buf3);

        // first number is magic
        int magic = decode(buf3, 0);
        if (magic != MAGIC)
            throw new IOException("Not a map file (bad magic)");

        // second number is addr=12 (useless)

        // third number is number of segments: 31845
        int count = decode(buf3, 2);

        // load all the segment headers: ~250K raw, ~1MB in memory (and <<1s to load)
        headers = new Header[count];
        for (int i=0; i<count; i++)
            headers[i] = Header.load();
    }

    // pick the /n/th int, decoding from little-endian; used by map, too.
    // why isn't this built into the reader itself?  because i usually want to read a big chunk at once, and parse it later.
    private static int decode(byte buf[], int n) {
        int x = 0xFF000000 & (buf[4*n+3] << 24); // ARE THESE &'s NECESSARY?
        x |= 0x00FF0000 & (buf[4*n+2] << 16);
        x |= 0x0000FF00 & (buf[4*n+1] << 8);
        x |= 0x000000FF & (buf[4*n]); // CSE ON 4*n?
/*
         int x = buf[4*n+3]<<24;
        x |= buf[4*n+2]<<16;
        x |= buf[4*n+1]<<8;
        x |= buf[4*n];
   */
        return x;
    }

    // Close the map file. -- COMPLETELY UNUSED.
    public static void close() throws IOException { // would finalize be better?
        rez.close();
    }

    private static int sign(int x) {
        return (x > 0 ? 1 : (x < 0 ? -1 : 0));
    }
    public static void main(String args[]) throws IOException {
        int maxdlat=0, maxdlong=0;
        int maxx=0, maxy=0;
        int maxdx=0, maxdy=0;
        int numBad=0;
        for (int i=0; i<headers.length; i++) {
            Header h = headers[i];
//            if (h.maxlong>0 && h.minlong<0)
//                maxdlong = Math.max(maxdlong, h.maxlong - h.minlong - 3600*360); // special case
//            else
                maxdlong = Math.max(maxdlong, h.maxlong - h.minlong);
            maxdlat = Math.max(maxdlat, h.maxlat - h.minlat);
            Data d = h.getData();
            for (int j=0; j<d.x.length; j++) {
                maxx = Math.max(maxx, d.x[j]);
                maxy = Math.max(maxy, d.y[j]);
                if (j != 0) {
                    int dx = d.x[j] - d.x[j-1];
                    int dy = d.y[j] - d.y[j-1];
//                    if (sign(d.x[j]) != sign(d.x[j-1])) {
//                        dx = d.x[j] - d.x[j-1] - 3600*360;
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
