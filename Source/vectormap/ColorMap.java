// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Parses an RMap rmap.colors file.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public final class ColorMap {
  public static final Color[][] DEFAULT_COLORS = new Color[5][16];
  static {
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 16; j++) {
        DEFAULT_COLORS[i][j] = Color.BLACK;
      }
    }
  }
  public static final String COLORFILE = "rmap.colors";
   
  //colormap defines
  //public static final int NOPRINT = -1;
  public static final int CONF = 0;
  public static final int C_BACKGROUND = 1;
  public static final int C_LAT_GRID = 2;
  public static final int C_LONG_GRID = 3;
  public static final int C_TEXT_CIRCLE = 4;
  public static final int C_TEXT = 5;

  
  public static final Color[][] parseColorMap() throws IOException {
    InputStream is;
    File file = new File(COLORFILE);
    if (file.isFile()) {
      is = new FileInputStream(file);
    } else {
      is = ColorMap.class.getResourceAsStream(COLORFILE);
    }
    if (is != null) return parseColorMap(is);

    return DEFAULT_COLORS;
  }
  
  public static final Color[][] parseColorMap(InputStream stream) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(stream));
    Color[][] colormap = new Color[5][16];
    int lineno = 0;
    try {
      String line;
      while ((line = br.readLine()) != null) {
        lineno++;
        StringTokenizer st = new StringTokenizer(line, " \t");
        int toks = st.countTokens();
        if (toks < 1) {
          continue;
        }
        String tmp = st.nextToken();
        if (tmp.trim().startsWith("#")) continue;
        if (toks < 3) {
          // probably should replace this with real logging at some point
          System.err.println("Malformed colormap line " + lineno + ": '" + line + "'");
          continue;
        }
        int cat;
        try {
          cat = Integer.parseInt(tmp);
        } catch (NumberFormatException nfe) {
          System.err.println("Malformed colormap category number: '" + tmp + "' on line " + lineno + " '" + line + "'");
          continue;
        }
        tmp = st.nextToken();
        int type;
        try {
          type = Integer.parseInt(tmp);
        } catch (NumberFormatException nfe) {
          System.err.println("Malformed colormap type number: '" + tmp + "' on line " + lineno + " '" + line + "'");
          continue;
        }
        if (cat > 4 || type > 15 || cat < 0 || type < 0) {
          System.err.println("Declaration value out of range (" + cat + "," + type + ") on line " + lineno + " '" + line + "'");
          continue;
        }
        tmp = st.nextToken().trim().toLowerCase();
        Color c = null;
        if (!"none".equals(tmp)) {
          c = ColorTable.getColor(tmp);
          if (c == null) {
            try {
              c = Color.decode(tmp);
            } catch (NumberFormatException nfe) {
              System.err.println("Error decoding color '" + tmp + "' on line " + lineno + " '" + line + "'");
              c = Color.BLACK;
            }
          }
        }
        colormap[cat][type] = c;
      }
    } finally {
      br.close();
    }
    return colormap;
  }
}
