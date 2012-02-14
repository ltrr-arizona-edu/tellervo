/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
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
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina_indexing;

/**
    A basis function for fitting data points to, using
    least-squares.  If an object implements this interface,
    <code>Solver.leastSquares()</code> can be used to fit it to data.

    <p>For example, if you want to fit some data to a quadratic,
    your basis vector would be [ 1 x x<sup>2</sup> ], and your code
    might look like:</p>

<pre>
    public double[] f(double x) {
        return new double[] { 1, x, x*x };
    }
</pre>

    @see Solver

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id: Function.java 815 2007-09-24 03:02:02Z Lucas Madar $
*/
public interface SolverFunction {
    /**
        Evaluate the basis function(s) at a point.  The size of
        the return array is assumed to always be the same size.
        This must return a new array every call, because it it
        used to fill in the rows of an array.

        @param x the point to evaluate
        @return the basis functions evaluated at x
    */

    public double[] f(double x);
}
