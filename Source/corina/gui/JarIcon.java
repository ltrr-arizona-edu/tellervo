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

package corina.gui;

import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
   <p>Helper class for loading icons from a jarfile.</p>

   <p>Unfortunately, you can't just put a jar in your classpath and
   expect to read members like any other file, though if you can put
   classes in there they act like normal files in your classpath.</p>

   <p>So after hunting around on Javasoft, I ran across <a
   href="http://developer.java.sun.com/developer/onlineTraining/GUI/Swing1/Magercises/M4/solution/Order.java">a
   demo</a> that shows how to do this.  It's packaged here in a handy
   static method for all to use.</p>

   <p>Note that it returns <code>null</code> on fail instead of
   throwing a more descriptive exception.  Methods that use this
   usually interpret nulls to mean "don't use an icon", so it silently
   works (albeit without icons) if the icon jarfile isn't in the
   classpath.</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class JarIcon {

    /** The jarfile to load icons from.  This refers to the <a
        href="http://developer.java.sun.com/developer/techDocs/hi/repository/">Java
        Look-and-Feel Graphics Repository</a>.  It shouldn't be
        hardcoded, so when version 1.1 comes out, users can upgrade
        without recompiling. */
    public final static String JARFILE = "Libraries/jlfgr-1_0.jar";

    /** Return the icon with the specified filename from JARFILE, or
	<code>null</code> if the file can't be opened or the icon
	wasn't found.
	@param filename the filename of the Icon to load
	@return the requested Icon */
    public static Icon getJavaIcon(String filename) {
        try {
            ZipFile zip = new ZipFile(JARFILE);
            ZipEntry entry = zip.getEntry(filename);
            InputStream is = zip.getInputStream(entry);
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            byte data[] = new byte[(int) entry.getSize()];
            dis.readFully(data);
            Image image = Toolkit.getDefaultToolkit().createImage(data);
            return new ImageIcon(image);

            // that code could throw either a ZipException, or a more
            // generic IOException.  i don't really care; i'm going to
            // return null either way.
        } catch (IOException e) {
            return null;
        }
    }

    // it's not hard, but it's conceptually similar to getJavaIcon(), so it belongs here.
    public static Icon getIcon(String filename) {
        return new ImageIcon(ClassLoader.getSystemResource(filename));
    }
}
