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

package corina.manip;

import corina.Sample;

import javax.swing.JDialog;

public class ReconcileDialog extends JDialog {

    private Reconcile r;

    /*
    private void makeTopPart() {
	JPanel panel = new JPanel();
	panel.set
    }
    */

    public ReconcileDialog(Sample s1, Sample s2) {
	r = new Reconcile(s1, s2);
    }

    /*
      Design:

      First reading: ---
      Second reading: ---

      3% rule
          Year   A   C
          1001  142 150
      1 bad year

      Trend rule
          Interval    A           C
	  1001-1002  increasing  decreasing
      1 bad interval

      [ OK ]

      (2 static tables)
    */

}
