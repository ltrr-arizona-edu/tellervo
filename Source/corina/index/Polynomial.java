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

import java.util.ArrayList;

import java.text.MessageFormat;

/**
   A polynomial-fit index to a data sample.  The user specifies the
   degree of polynomial to use.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

public class Polynomial extends Index implements Solver.Solveable {

    // should be private, but Index.java sets index_type from this --
    // yes, that should be in Corina.java, but that's for later.
    protected int degree;

    /** Evaluate the basis polynomial at x, for
	<code>Solver.leastSquares()</code>.  The basis polynomial is:
	<blockquote><i>f(x) = [ 1 x x<sup>2</sup> x<sup>3</sup> ... x<sup>degree</sup> ]</i></blockquote>
	@param x the x-value to evaluate the polynomial at
	@return the y-value of the basis polynomial at this x */
    public double[] f(double x) {
	double y[] = new double[degree+1];
	y[0] = 1.;
	for (int i=1; i<degree+1; i++)
	    y[i] = y[i-1] * x;
	return y;
    }

    /** Construct a new polynomial fit from a sample, given a degree
        polynomial to fit.
	@param s the Sample to index
	@param degree the degree polynomial to use */
    public Polynomial(Sample s, int degree) {
	super(s);
	this.degree = degree;
    }

    /** Return the name, e.g., "Polynomial (3<sup>o</sup>)" for a
	third-degree polynomial.
	@return name of this index */
    public String getName() {
	return MessageFormat.format(msg.getString("polynomial"),
				    new Object[] { new Integer(degree) });
    }

    /** Compute the index. */
    public void run() {
	// init x, y
	int n = target.data.size();
	double x[] = new double[n];
	double y[] = new double[n];
	for (int i=0; i<n; i++) {
	    x[i] = (double) i;
	    y[i] = ((Number) target.data.get(i)).doubleValue();
	}

	// compute coeffs
	double c[] = null;
	try {
	    c = Solver.leastSquares(this, x, y);
	} catch (Solver.SingularMatrixException sme) {
	    // how to deal with errors?
	    return;
	}

	// compute curve
	data = new ArrayList(n);
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
