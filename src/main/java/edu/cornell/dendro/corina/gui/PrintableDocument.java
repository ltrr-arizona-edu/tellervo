/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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

package edu.cornell.dendro.corina.gui;

import java.awt.print.Printable;
import java.awt.print.Pageable;
import java.awt.print.PageFormat;

public interface PrintableDocument {

    /**
       Get an object which can be printed, either a Printable or a
       Pageable.  Users of this class must figure out which it is on
       their own.  If a non-Pageable, non-Printable object is
       returned, that is a bug; if an object which is both Pageable
       and Printable is returned, assume it's Pageable (because
       Pageables still need Printables).
       @see Printable
       @see Pageable
       @param pf the format of page to use
       @return a Printable or Pageable
    */
    public Object getPrinter(PageFormat pf);

    // FUTURE: would it help if getPrinter() could throw UCE?  i think it would...
    // ALSO: would setPageFormat(pf), getPrinter() be better?  (i.e., 2 methods)

    /**
       Get the name of this print job.  This only shows up in the
       print queue, and has no effect on the hard copy.
       @return the name of this print job
    */
    public String getPrintTitle();
}
