# 
# This file is part of Corina.
# 
# Corina is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
# 
# Corina is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with Corina; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
#
# Copyright 2001 Ken Harris <kbh7@cornell.edu>
#

# Export all elements of a master into (filename, start, end) tuples.
# Note, I don't do much exception handling here.  I should report a
# list of elements that couldn't be loaded.

# Ken Harris <kbh7@cornell.edu>
# 24 July 2001
# $Id$

from edu.cornell.dendro.corina import Sample
from edu.cornell.dendro.corina.gui import FileDialog

from java.io import BufferedWriter
from java.io import FileWriter
from java.io import IOException

from java.lang import System

from javax.swing import JOptionPane

# put it in a function so i can bail with "return"
def exporter():
    # input filename
    f = FileDialog.showSingle("Master")
    if f == None:
        return

    # load master
    try:
        m = Sample(f)
    except IOException:
        JOptionPane.showMessageDialog(None, "Can't open master.", "Error", JOptionPane.ERROR_MESSAGE)
        return
    
    # make sure it's summed
    if m.elements == None or m.elements.size() == 0:
        JOptionPane.showMessageDialog(None, "Not a summed file.", "Error", JOptionPane.ERROR_MESSAGE)
        return

    # get output file
    filename = FileDialog.showSingle("Export")
    if filename == None:
        return

    # create output file
    o = BufferedWriter(FileWriter(filename))

    # for each element, load...
    for i in range(m.elements.size()):
        try:
            e = m.elements[i].load()
        except IOException:
            pass # deal with it!

        # output (name, start, end)
        if e.meta.containsKey("title"):
            o.write(e.meta["title"])
        else:
            o.write(e.meta["filename"])
        o.write("\t")
        o.write(e.range.start.toString())
        o.write("\t")
        o.write(e.range.end.toString())
        o.write(System.getProperty("line.separator"))

    # close
    o.close()

# run it
exporter()
