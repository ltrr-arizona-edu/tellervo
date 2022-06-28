/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
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

package org.tellervo.desktop.maventests;

import junit.framework.TestCase;

import org.apache.commons.lang.StringEscapeUtils;
import org.tellervo.desktop.nativeloader.RxTxNativeLoader;
import org.tellervo.desktop.tridasv2.NumberThenStringComparator2;
import org.tellervo.desktop.util.StringUtils;


public class UtilTest extends TestCase {
    public UtilTest(String name) {
        super(name);
    }


    public void testMyTest()
    {
    	String strReadBuffer = "01A+00036.26";
    	
    	String sign = strReadBuffer.substring(3, 4);
        String value = strReadBuffer.substring(4, 12);
        
        System.out.println(sign);
        System.out.println(value);
    	
    }
    
    public void testComparator()
    {
    	NumberThenStringComparator2 comparator = new NumberThenStringComparator2();
    	
    	String a = "02-A";
    	String b = "02-B";
    	int result = comparator.compare(a, b);
    	System.out.println(a +" v "+b+" = "+result);
    	
    	a = "02-B";
    	b = "08-A";
    	result = comparator.compare(a, b);
    	System.out.println(a +" v "+b+" = "+result);
    	
    	a = "08-B";
    	b = "10";
    	result = comparator.compare(a, b);
    	System.out.println(a +" v "+b+" = "+result);    	
    	
    	a = "10";
    	b = "4-A";
    	result = comparator.compare(a, b);
    	System.out.println(a +" v "+b+" = "+result); 
    	
    	a = "4-A";
    	b = "3";
    	result = comparator.compare(a, b);
    	System.out.println(a +" v "+b+" = "+result); 
    	
    	
    	a = "31-A";
    	b = "5";
    	result = comparator.compare(a, b);
    	System.out.println(a +" v "+b+" = "+result); 
    	
    }
    
  /*  public void testGetHTTPTest(){
    
    	App.init();
    	
    	try {
    		URI url = new URI("https://tellervo.ltrr.arizona.edu/adp/odk/fetchInstances");
			
			String tempfile = org.tellervo.desktop.bulkdataentry.command.PopulateFromODKFileCommand.doRequest(url);
			System.out.println(tempfile);
			
		} catch (IOException | URISyntaxException e) {
			fail();
		}
    	
    	
    }*/
    
    public void testEscape(){
    	
    	String str = "<hello>Some & text </hello";
    	System.out.println(StringEscapeUtils.escapeHtml(str));
    }
    
    //
    // testing StringUtils.java
    //
    public void testExtractInts() {
        int x[] = StringUtils.extractInts("1 2 3");
        assertEquals(x.length, 3);
        assertEquals(x[0], 1);
        assertEquals(x[1], 2);
        assertEquals(x[2], 3);
    }
    

    /*public void testGeonamesCountry(){
    	
    	TridasLocation location = new TridasLocation();
    	TridasLocationGeometry geom = SpatialUtils.getWGS84LocationGeometry(32.2, -110.9, false);
    	
    	location.setLocationGeometry(geom);
    	
    	String country = GeonamesUtil.getCountryForLocation(location);
    
    	if(country==null) fail();
    	
    	System.out.println("Country = "+country);
    }
    
    public void testGeonamesCity(){
    	
    	TridasLocation location = new TridasLocation();
    	TridasLocationGeometry geom = SpatialUtils.getWGS84LocationGeometry(32.2, -110.9, false);
    	
       	
    	location.setLocationGeometry(geom);
    	
    	String city = GeonamesUtil.getCityForLocation(location);
    
    	if(city==null) fail();
    	
    	System.out.println("City = "+city);
    }*/
    
    public void testLoadSerialLib(){
    	
    	try {    
    	      RxTxNativeLoader.loadNativeLib();
    	    } catch (Exception e) {  
    	    	
    	      e.printStackTrace(); 
    	      fail();
    	    }    
    	
    }
    
}

