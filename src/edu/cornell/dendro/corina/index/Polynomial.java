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

package corina.index;

import corina.Sample;
import corina.ui.I18n;

import java.text.MessageFormat;

/**
   A polynomial-fit index to a data sample.  The user specifies the
   degree of polynomial to use.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Polynomial extends Index implements Function {

    // degree of polynomial to use
    private int degree;

    /**
       Evaluate the basis polynomial at x, for
       <code>Solver.leastSquares()</code>.  The basis polynomial is:
       <blockquote><i>f(x) = [ 1 x x&#x00B2; x&#x00B3;
                               ... x<sup>degree</sup> ]</i></blockquote>

       @param x the x-value to evaluate the polynomial at
       @return the y-value of the basis polynomial at this x
    */
    public double[] f(double x) {
	double y[] = new double[degree+1];
	y[0] = 1.;
	for (int i=1; i<degree+1; i++)
	    y[i] = y[i-1] * x;
	return y;
    }

    /**
       Construct a new polynomial fit from a sample, given a degree
       polynomial to fit.

       @param s the Sample to index
       @param degree the degree polynomial to use
    */
    public Polynomial(Sample s, int degree) {
	super(s);
	this.degree = degree;
    }

    /**
       Return the name, e.g., "Polynomial (3&#x00B0;)" for a
       third-degree polynomial.

       @return name of this index
    */
    public String getName() {
	return MessageFormat.format(I18n.getText("polynomial"),
				    new Object[] { new Integer(degree) });
    }

    /** Compute the index. */
    public void index() {
	// init x, y
	int n = source.data.size();
	double x[] = new double[n];
	double y[] = new double[n];
	for (int i=0; i<n; i++) {
	    x[i] = (double) i;
	    y[i] = ((Number) source.data.get(i)).doubleValue();
	}

	// compute coeffs
	double c[] = null;
	try {
	    c = Solver.leastSquares(this, x, y);
	} catch (SingularMatrixException sme) {
	    // how to deal with errors?
	    return;
	}

	// compute curve
	for (int i=0; i<n; i++) {
	    double f[] = f(x[i]);
	    double yp = 0.;
	    for (int j=0; j<degree+1; j++)
		yp += c[j] * f[j];
	    data.add(new Double(yp));
	}
    }

    public int getID() {
        return degree;
    }
}
