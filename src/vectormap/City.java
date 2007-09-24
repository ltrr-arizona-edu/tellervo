// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Taken from rmap:
 * http://www.reza.net/rmap/
 * Unused at the moment.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class City {
  public static final City[] CITIES = {
    new City(61.17,-150.00,"Anchorage"),
    new City(38.00,23.73,"Athens"),
    new City(33.4,44.4,"Baghdad"),
    new City(13.73,100.50,"Bangkok"),
    new City(39.92,116.43,"Beijing"),
    new City(52.53,13.42,"Berlin"),
    new City(32.3,-64.7,"Bermuda"),
    new City(42.33,-71.08,"Boston"),
    new City(-15.8,-47.9,"Brasilia"),
    new City(-4.2,15.3,"Brazzaville"),
    new City(-34.67,-58.50,"Buenos Aires"),
    new City(31.05,31.25,"Cairo"),
    new City(22.5,88.3,"Calcutta"),
    new City(-33.93,18.47,"Cape Town"),
    new City(33.6,-7.6,"Casablanca"),
    new City(41.83,-87.75,"Chicago"),
    new City(32.78,-96.80,"Dallas"),
    new City(28.63,77.20,"New Delhi"),
    new City(39.75,-105.00,"Denver"),
    new City(24.23,55.28,"Dubai"),
    new City(-27.1,-109.4,"Easter Island"),
    new City(-18.0,178.1,"Fiji"),
    new City(13.5,144.8,"Guam"),
    new City(60.13,25.00,"Helsinki"),
    new City(22.2,114.1,"Hong Kong"),
    new City(21.32,-157.83,"Honolulu"),
    new City(52.2,104.3,"Irkutsk"),
    new City(41.0,29.0,"Istanbul"),
    new City(-6.13,106.75,"Jakarta"),
    new City(31.8,35.2,"Jerusalem"),
    new City(34.5,69.2,"Kabul"),
    new City(27.7,85.3,"Kathmandu"),
    new City(50.4,30.5,"Kiev"),
    new City(3.13,101.70,"Kuala Lumpur"),
    new City(6.45,3.47,"Lagos"),
    new City(-12.10,-77.05,"Lima"),
    new City(51.50,-0.17,"London"),
    new City(40.42,-3.72,"Madrid"),
    new City(14.6,121.0,"Manila"),
    new City(21.5,39.8,"Mecca"),
    new City(19.4,-99.1,"Mexico City"),
    new City(25.8,-80.2,"Miami"),
    new City(6.2,-10.8,"Monrovia"),
    new City(45.5,-73.5,"Montreal"),
    new City(55.75,37.70,"Moscow"),
    new City(-1.28,36.83,"Nairobi"),
    new City(59.93,10.75,"Oslo"),
    new City(48.87,2.33,"Paris"),
    new City(-32.0,115.9,"Perth"),
    new City(45.5,-122.5,"Portland"),
    new City(-0.2,-78.5,"Quito"),
    new City(64.15,-21.97,"Reykjavik"),
    new City(-22.88,-43.28,"Rio de Janeiro"),
    new City(41.88,12.50,"Rome"),
    new City(11.0,106.7,"Ho Chi Minh City"),
    new City(37.75,-122.45,"San Francisco"),
    new City(9.98,-84.07,"San Jose"),
    new City(18.5,-66.1,"San Juan"),
    new City(-33.5,-70.7,"Santiago"),
    new City(1.2,103.9,"Singapore"),
    new City(42.67,23.30,"Sofia"),
    new City(59.33,18.08,"Stockholm"),
    new City(-33.92,151.17,"Sydney"),
    new City(-17.6,-149.5,"Tahiti"),
    new City(16.8,-3.0,"Timbuktu"),
    new City(35.67,139.75,"Tokyo"),
    new City(43.70,-79.42,"Toronto"),
    new City(32.9,13.2,"Tripoli"),
    new City(47.9,106.9,"Ulan Bator"),
    new City(49.22,-123.10,"Vancouver"),
    new City(48.22,16.37,"Vienna"),
    new City(38.9,-77.0,"Washington"),
    new City(-41.28,174.78,"Wellington"),
    new City(62.5,-114.3,"Yellowknife"),
    new City(90.00,0.00,"North Pole"),
    new City(-90.00,0.00,"South Pole"),
    new City(0.0, 0.0, null)
  };

  public double latitude;
  public double longitude;
  public String name;
  
  public City() {}
  public City(double lat, double lon, String name) {
    this.latitude = lat;
    this.longitude = lon;
    this.name = name;
  }
  public String toString() {
    return name + ": lat=" + latitude + ", long=" + longitude;
  }
  
  public static City[] parseCities(InputStream stream) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(stream));
    LinkedList list = new LinkedList();
    int lineno = 0;
    try {
      String line;
      while ((line = br.readLine()) != null) {
        lineno++;
        StringTokenizer st = new StringTokenizer(line, " ");
        if (st.countTokens() < 3) {
          // probably should replace this with real logging at some point
          System.err.println("Malformed city line " + lineno + ": '" + line + "'");
          continue;
        }
        String tmp = st.nextToken();
        double longitude;
        try {
          longitude = Double.parseDouble(tmp);
        } catch (NumberFormatException nfe) {
          System.err.println("Malformed city longitude: '" + tmp + "' on line " + lineno + " '" + line + "'");
          continue;
        }
        tmp = st.nextToken();
        double latitude;
        try {
          latitude = Double.parseDouble(tmp);
        } catch (NumberFormatException nfe) {
          System.err.println("Malformed city latitude: '" + tmp + "' on line " + lineno + " '" + line + "'");
          continue;
        }
        if (longitude > 648000 || longitude < -648000 || latitude > 324000 || latitude < -324000) {
          System.err.println("Declaration value out of range (long=" + longitude + ",lat=" + latitude + ") on line " + lineno + " '" + line + "'");
          continue;
        }
        tmp = st.nextToken().trim().toLowerCase();
        City city = new City(latitude, longitude, tmp);
        list.add(city);
      }
    } finally {
      br.close();
    }
    return (City[]) list.toArray(new City[list.size()]);
  }
}