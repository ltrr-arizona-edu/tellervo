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

from corina.index import Exponential
from corina.gui import FileDialog

from java.io import File
from java.io import IOException

from javax.swing import JOptionPane

def indexMany():
    # select files
    tmp = FileDialog.showMulti("Index")
    if tmp == None:
        return

    # index each one as exponential, and save it as .IND
    for i in range(tmp.size()):
        e = tmp.get(i)
        try:
            s = e.load()
        except IOException:
            JOptionPane.showMessageDialog(null,
                                          "The sample '" + e.getFilename() + "' could not be loaded.",
                                          "Error loading sample",
                                          JOptionPane.ERROR_MESSAGE)
            continue

        # run an exp index
        index = Exponential(s)
	index.run()
	index.apply()

        # add "(indexed)" to the title, so they know later
	s.meta.put("title", s.meta.get("title") + " (indexed)")

	# take the filename, and make it end in .IND.
	oldFile = File(s.meta.get("filename"))
	oldName = oldFile.getName()
        lastDot = oldName.rfind('.')

	# change the extension, or add one if it didn't have one
        if lastDot == -1:
            newName = oldName + ".IND"
        else:
            newName = oldName[0:lastDot] + ".IND"

	# prepend the path
	newName = oldFile.getParent() + File.separator + newName

	# make sure it's not already there; if so, confirm
	newFile = File(newName)
        if newFile.exists():
            x = JOptionPane.showConfirmDialog(null,
                                              "File \"" + newName + "\"\n" +
                                              "already exists; overwrite?",
                                              "Already exists",
                                              JOptionPane.YES_NO_OPTION)
            if x == JOptionPane.NO_OPTION:
                continue

        # save it
        try:
            s.save(newName)
        except IOException:
            JOptionPane.showMessageDialog(null,
                                          "The sample `" + e.getFilename() + "' could not be saved.",
                                          "Error saving sample",
                                          JOptionPane.ERROR_MESSAGE)
            continue

# run it
indexMany()
