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

# Output the elements of a master so the output can
# be easily read by a spreadsheet.  For example, if
# sample KBH-1 is 1002-1004, KBH-2 is 1001-1003, and
# KBH-3 is 1004-1003, the output would look similar
# to this:
#
#     Year	KBH-1	KBH-2	KBH-3
#     1001		10	
#     1002	20	15	
#     1003	50	92	87
#     1004	21		77
#
# (Tabs between cells, newlines between rows.)

from edu.cornell.dendro.corina import Sample
from edu.cornell.dendro.corina.gui import FileDialog

from java.io import BufferedWriter
from java.io import FileWriter

from java.lang import System

def n_column_exporter():
    input_filename = FileDialog.showSingle("Master")
    if (input_filename == None):
        return

    # load it, and make sure it has elements
    master = Sample(input_filename)
    if (master.elements == None):
        return

    # count the number of elements
    n = master.elements.size()

    # load all elements into buffer
    buf = []
    for i in range(n):
        buf.append(master.elements.get(i).load())
        # None?  error!

    # generate output_filename
    output_filename = FileDialog.showSingle("Export")
    if (output_filename == None):
        return

    # open file, and write header
    w = BufferedWriter(FileWriter(output_filename))
    w.write("Year")
    for i in range(n):
        w.write("\t" + buf[i].meta.get("title"))
    w.write(System.getProperty("line.separator"))

    # save line-at-a-time
    y = master.range.getStart()
    while (y.compareTo(master.range.end) <= 0):
        # write year
        w.write(y.toString())

        # write each datum for this year
        for i in range(n):
            # tab
            w.write("\t")

            # data[y]
            if (buf[i].range.contains(y)):
                start = buf[i].range.getStart()
                index = y.diff(start)
                w.write(buf[i].data.get(index).toString())

        # write end-of-line
	w.write(System.getProperty("line.separator"))
	
        # increment year
        y = y.add(1)

    # close file
    w.close()

# main: run it
n_column_exporter()
