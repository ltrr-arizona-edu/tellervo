// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Java implementation of rmap vector map:
 * http://www.reza.net/rmap/
 * Many options/constants are not used as they are not relevant to usage here
 * and should probably only belong in the rendering layer not parsing layer.
 * for now this is covered under GPL:
 * http://www.gnu.org/copyleft/gpl.html
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public final class VectorMap {
  public static final String MAPFILE = "earth.rez";

  public static final float CONV = (float) Math.PI / 180; // float is probably good enough
  /*meters at equator */
  public static final int EARTH_RADIUS = 250;
  /*meters at equator */  
  public static final int CIRCLE_SIZE = 8;
  //random number that i chose
  private static final int HEADER_MAGIC = 0x86460346;

  // The following are the Continent Codes
  public static final int AFRICA = 1;
  public static final int ASIA = 2;
  public static final int EUROPE = 3;
  public static final int NORTH_AMERICA = 4;
  public static final int SOUTH_AMERICA = 5;
  // And these are the categories
  public static final int PBY = 1;
  public static final int RIV = 2;
  public static final int BDY = 3;
  public static final int CIL = 5;
  
  public static final class Segment {
    int maxlat;
    int minlat;
    int maxlong;
    int minlong;
    int segment_address;
    int continent;
    int category;
    int type;
    double[] longitude;
    double[] latitude;
  };

  /*public static final class Options  {
    int cities;
    int gridlines;
    double zoom;
    double xrot;
    double yrot;
    double zrot;
    String mapfile;
    String outfile;
    String colorfile;
    String cityfile;
    int desired_categories;
    int desired_continents; 
  };*/
  
  private Segment[] segments; 
  
  public VectorMap(InputStream stream) throws IOException {
    // read header
    int magic = readLittleEndianInt(stream);
    int segment_index_address = readLittleEndianInt(stream);
    int segment_index_count = readLittleEndianInt(stream);
    
    if (magic != HEADER_MAGIC) {
      throw new IOException("The magic was not valid for the input file. " +
                            "This could be an endian problem, or you " +
                            "could be pointing to the wrong file. " +
                            "(magic = " + Integer.toHexString(magic) + ", " +
                            " and it's supposed to be " + Integer.toHexString(HEADER_MAGIC));
    }
    
    segments = new Segment[segment_index_count];
    
    for (int i = 0; i < segment_index_count; i++) {
      Segment segment = new Segment();
      segment.maxlat = (int) (readLittleEndianInt(stream) / 3600.0d);
      segment.minlat = (int) (readLittleEndianInt(stream) / 3600.0d);
      segment.maxlong = (int) (readLittleEndianInt(stream) / 3600.0d);
      segment.minlong = (int) (readLittleEndianInt(stream) / 3600.0d);
      segment.segment_address = readLittleEndianInt(stream);
      segment.continent = readLittleEndianInt(stream);
      segment.category = readLittleEndianInt(stream);
      segment.type = readLittleEndianInt(stream);
      
      //System.out.println("Segment address: "+ segment.segment_address);
      
      segments[i] = segment;
    }
    for (int i = 0; i < segment_index_count; i++) {      
      // XXX: check if in desired continent or category, continue if not
      // XXX: then check if it is in the viewport or is just a point, and continue if not
      // XXX: instead we are just going to load it all into memory and do the masking
      // at draw time.

      Segment segment = segments[i];

      int orgx = readLittleEndianInt(stream);
      int orgy = readLittleEndianInt(stream);
      int nstrokes = readLittleEndianInt(stream);
      
      segment.longitude = new double[nstrokes + 1];
      segment.latitude = new double[nstrokes + 1];

      //System.out.println("Segment header nstrokes: " + nstrokes);

      //System.out.println("read segheader: " + orgx + " " + orgy + " " + nstrokes);

     
      double lastlong = segment.longitude[0] = orgx/3600.0d;
      double lastlat = segment.latitude[0] = orgy/3600.0d;
      //point.z = EARTH_RADIUS;

      for (int j=0; j < nstrokes; j++) {
        //Point3D nextpoint = new Point3D();
        //nextpoint.z = EARTH_RADIUS;

        int dx = readLittleEndianInt(stream);
        int dy = readLittleEndianInt(stream);
        
        lastlong = segment.longitude[j + 1] = lastlong + (dx/3600.0d);
        lastlat = segment.latitude[j + 1] = lastlat + (dy/3600.0d);
        
        /*int val = (int) lastlong;
        if (val > segment.maxlong || val  < segment.minlong) {
          System.err.println("Segment min/max long lies! " + segment.minlong + "," + segment.maxlong + " " + lastlong);
        }
        val = (int) lastlat;
        if (val  > segment.maxlat || val  < segment.minlat) {
          System.err.println("Segment min/max lat lies! " + segment.minlat+ "," + segment.maxlat + " " + lastlat);
        }*/
      }
    }
  }
  
  private static double cleanLongitude(double degree) {
    if (degree > 179 || degree < -180) return -180d;
    return degree;
    /*degree += 180;
    degree %= 360;
    if (degree <= 0)
      degree += 180;
    else
      degree -= 180;
    
    return degree;*/
  }
  
  public Segment[] getSegments() {
    return segments;
  }
  
  private static int readLittleEndianInt(InputStream is) throws IOException {
    int byte1 = is.read();
    int byte2 = is.read();
    int byte3 = is.read();
    int byte4 = is.read();

    if (byte4 == -1) {
      throw new java.io.EOFException();
    }
    return (byte4 << 24) 
     + ((byte3 << 24) >>> 8) 
     + ((byte2 << 24) >>> 16) 
     + ((byte1 << 24) >>> 24);
  }
  
  public static final VectorMap load(String filename) throws IOException {
    InputStream is;
    String tmpdir = System.getProperty("java.io.tmpdir", ".");
    File tmpfile = new File(tmpdir, MAPFILE);
    if (tmpfile.isFile()) {
      is = new BufferedInputStream(new FileInputStream(tmpfile));
    } else {
      File file = new File(filename);
      if (file.isFile()) {
        is = new FileInputStream(file);
      } else {
        is = VectorMap.class.getResourceAsStream(VectorMap.MAPFILE);
      }
      if (is == null) throw new FileNotFoundException(VectorMap.MAPFILE);
      is = new SurveillingInputStream(
               new java.util.zip.GZIPInputStream(new BufferedInputStream(is)),
               new BufferedOutputStream(new FileOutputStream(tmpfile))
           );
    }
    long start = System.currentTimeMillis();
    VectorMap map = new VectorMap(is);
    long time = System.currentTimeMillis() - start; 
    System.out.println("Loaded vector map in " + time + " milliseconds (" + (time / 1000) + " seconds)");
    is.close();
    return map;
  }
}