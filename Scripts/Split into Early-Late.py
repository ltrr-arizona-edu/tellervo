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

from edu.cornell.dendro.corina import Sample
from edu.cornell.dendro.corina import Range
from edu.cornell.dendro.corina.gui import FileDialog

from java.io import IOException
from java.util import Hashtable
from javax.swing import JOptionPane

def split_early_late():

    # select file
    filename = FileDialog.showSingle("Split")
    if filename == None:
        return

    # load it
    try:
        raw = Sample(filename)
    except IOException:
        JOptionPane.showMessageDialog(None,
                "The sample '" + filename + "' could not be loaded.",
                "Error loading sample",
                JOptionPane.ERROR_MESSAGE)
        return

    # make sure we're not summed
    if raw.isSummed():
        JOptionPane.showMessageDialog(None,
                "The sample '" + filename + "'\n" +
                    "is summed, and splitting only works on raw samples.",
                "Can't split summed sample",
                JOptionPane.ERROR_MESSAGE)
        return

    # split it: data
    early = Sample()
    late = Sample()
    for i in range(raw.data.size()):
        if i%2 == 0:
            early.data.add(raw.data[i])
        else:
            late.data.add(raw.data[i])

    # compute range
    start = raw.range.start
    early.range = Range(start, start.add(early.data.size() - 1))
    late.range = Range(start, start.add(late.data.size() - 1))

    # copy meta, and set titles
    early.meta = Hashtable(raw.meta)
    late.meta = Hashtable(raw.meta)
    early.meta["title"] = raw.meta["title"] + " - Earlywood"
    late.meta["title"] = raw.meta["title"] + " - Latewood"

    # make the new filenames
    earlyName = filename + " - early"
    lateName = filename + " - late"

    # save the halves
    try:
        early.save(earlyName)
        late.save(lateName)
    except IOException:
        JOptionPane.showMessageDialog(None,
            "There was an error while trying to " +
                "save one of the results:\n" +
            ioe.getMessage(),
            "Error saving sample",
            JOptionPane.ERROR_MESSAGE)
    return

# main: run it
split_early_late()
