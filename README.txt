                _____________________________________
                     ____           _             
                    / ___|___  _ __(_)_ __   __ _ 
                   / |   / _ \| '__| | '_ \ / _` |
                   \ |__| (_) | |  | | | | | (_| |
                    \____\___/|_|  |_|_| |_|\__,_|
                
                The Cornell Tree-Ring Analysis System
                _____________________________________

----------------------------------------------------------------------

This is not a final release.  It still has many limitations and bugs.
You are encouraged to use it and report bugs and feature requests.

----------------------------------------------------------------------

INSTALLATION

There is no install program.  Simply copy the Corina directory to
your disk.  To make it easier to run, you can make a shortcut in a
convenient place.  On Windows, right-click, copy, right-click, paste
shortcut.

----------------------------------------------------------------------

RUNNING

To run Corina:

  - in Windows 95, upgrade, then see directions below.  (You can
type "java -jar Corina.jar" from an MS-DOS prompt, but Windows 95
crashes so often trying to run Corina, I have no desire to make
this any easier.)

  - in Windows 98/2000, double-click Corina.jar (if this fails, you
can use Corina.bat)

  - on Mac OS X, there's no double-clickable icon (yet), but you can
either double-click the Corina.jar file (in the Corina folder), or
type "ant run" from a terminal window.  (Double-clicking Corina.jar
won't get you hardware acceleration, a dock icon, or the "Corina" name
in the menubar.)

   - on Unix, type "ant run".  You can make a shell script that does
a "java -jar Corina.jar", put this shell script in /usr/bin/, and put
Corina.jar and Libraries/* in a

Corina has been tested -- to varying degrees -- on Windows 2000, Linux
2.4, and Mac OS X, with Java 1.3.

----------------------------------------------------------------------

LICENSE(S)

Corina is licensed under the GNU GPL version 2, available as file
COPYING.

Corina uses code and data from other projects with different licenses.
The licenses for these may be found in the "Licenses" folder.

Corina itself is Copyright 2001 Ken Harris <kbh7@cornell.edu>.

----------------------------------------------------------------------

WHAT'S ALL THIS, THEN?

They are:

-- README: this file

-- COPYING: the license of Corina (it's the GNU GPL2)


-- Libraries: extra files used for running Corina

-- build.xml: Ant build file, used for building Corina from source.
Some rules:
	ant clean	remove program and temp files
	ant jar		compile, and build a jar
	ant test	run all unit tests
	ant run		run Corina
	ant javadoc	compile Javadoc reference

-- Source: source code for Corina

-- Scripts: Python scripts used by Corina

-- Demo Data: demo data files (from the ITRDB)

-- Manual: user manual for Corina.  Well, I'll put it there eventually.

-- Corina.jar: the Corina program itself

----------------------------------------------------------------------

- kbh 11.4.2002 3:45pm
