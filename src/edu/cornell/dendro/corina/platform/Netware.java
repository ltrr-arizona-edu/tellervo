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

package edu.cornell.dendro.corina.platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import edu.cornell.dendro.corina.core.App;

/**
   Deal with ickiness that comes up when running Java programs under
   Windows with a Netware login.  In particular, set
   <code>user.name</code> and <code>user.home</code> properly.
*/

// BUGS:
// - don't watch for end-of-stream in getLogin()
// - should throw a nicer exception if that does occur
// - what happens if WHOAMI exists, but we're not logged in?
// - getPathForDirectory() fails if it's not a mapped drive (e.g., if it's H:\<username>\)
// - getPathForDirectory() fails if it's not mapped at all (should i map it?)

// TODO:
// - write stubs for testing?
// - document more completely (javadoc)

// ALTERNATIVE:
// - get netware jar
// - ask it if we're logged in, and what the user.* vars are
// - (but: download an extra jar, probably with a different license, probably
// fairly big, to do one thing on a smalll fraction of the systems?  will that really be any faster?)

// PERFORMANCE:
// -- takes ~5ms (though occasionally up to 18ms) to do nothing on Mac (G4, 500MHz, 768MB)
// -- takes ~1900ms to talk to netware on win2k (P3, 1000MHz, 128MB)

// PREFS:
// - there's no need for any non-"corina.*" prefs now
// - (yay! -- but i still need prefs)
// - so add a label somewhere (prefs? help menu item?)
//         According to Netware (Windows, ...)
//              User name: Ken Harris
//         Home directory: H:\
// - but i'll need a more flexible prefs dialog framework first

/*
  FIXME: performance is bad, because it calls exec() 3 times each time corina is run.
  that sucks.

  instead, put the information you find there in "H:\Netware User Info".
  if that file exists, use it.  if not, do the exec() dance to create it.

  the file can be as simple as:
    user.name = KENH
    user.fullname = Ken Harris
    user.home = H:\

  (why's a file in $HOME say where $HOME is?  because this is just a guess
  as to where $HOME is, and this confirms it.)
*/

public class Netware {
    /**
       Return true iff it looks like we're logged in to a Netware file
       server (by trying to run <code>WHOAMI.EXE</code> -
       <code>NVER</code> would be more appropriate, perhaps, but it
       takes a long time to run, for some reason).

       @return true iff we're logged in to a Netware system
    */
    private static boolean isNetware() {
	// OBSOLETE: getLogin() no longers throws IOE.
	// better: check for Z:\WHOAMI.EXE.
	boolean hasWhoami = new File("Z:\\WHOAMI.EXE").exists();
	if (!hasWhoami)
	    return false;

	// try to run "WHOAMI.EXE" to get the user's login
	// WAS: login = getLogin();
	// BUG: assumes it worked?

	return true;
    }
    private static String login=null;

    public static void workaround() {
	// see if it looks like we're on netware
	boolean netwarep = isNetware();

	// we're not, so don't do anything
	if (!netwarep)
	    return;

	// ok, it seems we are.  let's do a couple more checks to make sure we're in
	// ithaca.
	boolean ithaca = new File("G:\\DATA").exists();
	ithaca &= new File("G:\\DATA\\FOREST").exists();

	// not in ithaca?
	if (!ithaca)
	    return;

	// ok, we're really in ithaca.  let's use a hardcoded H:\ for the home directory.
	System.setProperty("user.home", "H:\\");
    }

    /* figure out the (netware) login name (like "KENH").
       throws an IOE if we don't appear to have any netware utils.

       the output of WHOAMI looks like:

<pre>
C:\WINDOWS\Desktop>whoami

Current tree:  CU-DENDRO
Other names: KENNETH B. HARRIS

User ID:    KENH
Server:     PITH  NetWare 4.11
Connection: 9 (Directory Services)
</pre>
    */
    private static String getLogin() { // throws IOException {
	try {

	    // call "WHOAMI"
	    Process p = Runtime.getRuntime().exec(new String[] { "CMD.EXE", "/C", "WHOAMI.EXE" });

	    // watch output -- REFACTOR: combine these 2?
	    Pacman out = new Pacman(p.getInputStream());
	    Pacman err = new Pacman(p.getErrorStream());
	    out.start();
	    err.start();

	    // wait for it
	    int x = p.waitFor();

	    // look for "User ID:" in output
	    BufferedReader r = new BufferedReader(new StringReader(out.get()));
	    String line = null;
	    while ( (line = r.readLine()) != null) {
		if (line.startsWith("User ID:")) {
		    login = afterColon(line);
		    return login;
		}
	    }

	} catch (IOException ioe) {
		edu.cornell.dendro.corina.gui.Bug.bug(ioe);
	    return "--bad--";
	} catch (Exception e) {
		edu.cornell.dendro.corina.gui.Bug.bug(e);
	    return "-- really bad --";
	    }

	return "suckage!";

	// nope?  throw ioe...
	// DISABLED: throw new IOException("bad output from WHOAMI.EXE"); // BUG: make this nicer
    }

    /* figure out the (netware) user name and home directory.

    the output of NLIST USER=KENH SHOW "FULL NAME" "HOME DIRECTORY" looks like:
<pre>
C:\WINDOWS\Desktop>nlist user=kenh show "full name" "home directory"
Object Class: User
Current context: Dendro.Arts
User: KENH
        Home Directory:
                Volume Name: PITH_VOL1
                Path: HOME/KENH
                Name Space Type: DOS
        Full Name: Ken Harris
One User object was found in this context.

One User object was found.
</pre>
     */
    public static void workaroundSlow() throws IOException { // DESIGN: shouldn't throw ioe
	// not on a netware system?  bye bye.
	if (!(App.platform.isWindows() && isNetware())) { // the second call here sets |login| -- refactor me
	    return;
	}

	// call "NLIST USER=getLogin() SHOW \"full name\" \"home directory\""
	Process p = Runtime.getRuntime().exec(new String[] { "CMD.EXE",
							     "/C",
							     "NLIST.EXE",
							     "USER="+login,
							     "SHOW",
							     "FULL NAME",
							     "HOME DIRECTORY"});

	// watch output
	Pacman out = new Pacman(p.getInputStream());
	Pacman err = new Pacman(p.getErrorStream());
	out.start();
	err.start();

	// wait for it
	try {
	    int x = p.waitFor();
	} catch (InterruptedException ie) {
		edu.cornell.dendro.corina.gui.Bug.bug(ie);
	}

	// look for name, etc. in output
	String name=null, home_vol=null, home_path=null;
	BufferedReader r = new BufferedReader(new StringReader(out.get()));
	String line = null;
	while ( (line = r.readLine()) != null) {
	    if (line.trim().startsWith("Full Name:")) {
		name = afterColon(line);
		continue;
	    }

	    if (line.trim().startsWith("Volume Name:")) {
		home_vol = afterColon(line);
		continue;
	    }

	    if (line.trim().startsWith("Path:")) {
		home_path = afterColon(line);
		continue;
	    }
	}

	// set user.name = name
	System.setProperty("user.name", name);

	// set user.home
	String home = getPathForDirectory(home_vol, home_path);
	System.setProperty("user.home", home);

	// BUG (but not here): if WHOAMI exists, but we're not logged in, what happens?

	edu.cornell.dendro.corina.gui.Bug.bug(new IllegalArgumentException("done with workaround: name=" + name +
							", home=" + home));
    }

    /*
      the output of MAP looks like:

<pre>
C:\WINDOWS\Desktop>map

Drives A,B,C,D,E map to a local disk.
Drive F: = PITH_SYS: \
Drive G: = PITH_VOL1:SHARED\DENDRO \
Drive H: = PITH_VOL1:HOME\KENH \
Drive I: = PITH_VOL1:SHARED\ADMIN \
Drive J: = PITH_VOL1:APPS\WP51 \
Drive K: = PITH_VOL1:APPS \
Drive M: = PITH_VOL1:SHARED\SOURCE \
      -----    Search Drives    -----
S1: = C:\NOVELL\CLIENT32
S2: = C:\WINDOWS
S3: = C:\WINDOWS\COMMAND
S4: = C:\WINDOWS\TWAIN_32\SCANWIZ
S5: = Z:. [PITH_SYS:PUBLIC \]
S6: = Y:. [PITH_VOL1:APPS\UTIL \]
S7: = X:. [PITH_SYS: \PUBLIC\NLS]
</pre>
     */
    private static String getPathForDirectory(String volume, String path) throws IOException {
	// build target string
	String target = volume + ":" + path.replace('/', '\\') + " \\";
	target = target.toUpperCase(); // UPPER

	// call MAP, look for drive
	Process p = Runtime.getRuntime().exec(new String[] { "CMD.EXE", "/C", "MAP.EXE" });

	// watch output -- REFACTOR: combine these 2?
	Pacman out = new Pacman(p.getInputStream());
	Pacman err = new Pacman(p.getErrorStream());
	out.start();
	err.start();

	// wait for it
	try {
	    int x = p.waitFor();
	} catch (InterruptedException ie) {
		edu.cornell.dendro.corina.gui.Bug.bug(ie);
	}

	// look for "User ID:" in output
	BufferedReader r = new BufferedReader(new StringReader(out.get()));
	String line = null;
	while ( (line = r.readLine()) != null) {
	    if (line.endsWith(target)) {
		String drive = line.charAt("Drive ".length()) + ":\\";
		return drive;
	    }
	}

	return ""; // drive;

	// BUG: this fails if it's not a mapped drive itself (e.g., if it's H:\<username>\)
	// BUG: this fails if it's not mapped at all (should i map it?)
    }

    // return everything after the first ':', whitespace-trimmed.
    private static String afterColon(String source) {
	int i = source.indexOf(':');
	String result = source.substring(i+1);
	return result.trim();
    }

    // --------------------------------------------------
    // using NJCL:
    // (note: this can't be exported to Cuba, Iran, Iraq, Libya, North
    // Korea, Syria, or Sudan.  so if any user is using netware (hah!)
    // in any of those countries, they can't legally run corina.)

    /*
    private static void workaround2() {
	try {
	    com.novell.java.security.Identity i[] = com.novell.java.security.Authenticator.getIdentities();

	    // uh, more than one -- just take the first one?

	    // this is just the login name
	    String name = i[0].getName();
	    System.setProperty("user.name", name);

	    // STILL NEED: full name, home dir
	    System.out.println("got user.name=" + name);
	} catch (NullPointerException npe) {
	    System.out.println("got npe");
	    return; // no identities => not netware
	}
    }
    */

    // --------------------------------------------------
    // why can't i just look for the presense of g:\data and h:\,
    // and realize i'm in b-48 then?  because i don't get the username.
    // ...
    // exec notes:
    // -- "%COMSPEC%" says where cmd/command is on all win32 platforms
    // -- need to use full path?
    // -- need to call it (the shell) with /C
    // -- need to consume all of out/err, or shell locks(?)
    //
    // exec("%COMSPEC%", "/C", "WHOAMI");
    // ...

    private static class Pacman extends Thread {
	private InputStream i;
	Pacman(InputStream i) {
	    this.i = i;
	}
	StringBuffer b = new StringBuffer();
	@Override
	public void run() {
	    try {
		InputStreamReader isr = new InputStreamReader(i);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ( (line = br.readLine()) != null) {
		    b.append(line);
		    b.append('\n');

		    // grep line here?
		}
	    } catch (IOException ioe) {
		ioe.printStackTrace(); // lousy!
	    }
	}
	public String get() {
	    return b.toString();
	}
    }

    // other things to REFACTOR: -- "cmd.exe" (so i can try start, command?)
    // new Pacman, new Pacman
    // (???)
}
