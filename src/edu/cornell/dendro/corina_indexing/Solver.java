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

package edu.cornell.dendro.corina_indexing;

/**
    A collection of matrix solvers.

    <p><code>solveNxN</code> will solve any square matrix system.  In
    solving Ax=b, it will destroy A (and b?).  The
    routines were adapted from chapter 6 of <i>Introduction to
    Scientific Computing</i>, second edition, Charles van Loan.  The
    solver uses LU decomposition with pivoting.</p>

    <p>Also contains a least-squares fitter,
    <code>leastSquares()</code>, from <i>Introduction to
    Algorithms</i>, Cormen, Leiserson, and Rivest, pp. 768-771, which
    uses the matrix solvers.</p>
    
    @see SolverFunction
    @see SingularMatrixException

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id: Solver.java 888 2007-11-10 07:22:51Z lucasm $
*/
public class Solver {

	private Solver() {
		// don't instantiate me
	}

	/** Lower-upper decomposition of a matrix; returns the permutation
	vector.  See van Loan, p. 228.
	@param A the matrix to decompose
	@return the permutation vector
	@exception IllegalArgumentException if A is not square
	@exception SingularMatrixException if A is singular */
	private static int[] GEpiv(double A[][]) throws SingularMatrixException {
		// if there's ever a licensing issue with this code, see
		// octave.  it uses the free LAPACK for this and many other
		// routines.

		// make sure it's square
		int n = A.length;
		if (A[0].length != n)
			throw new IllegalArgumentException("Not square");

		int piv[] = new int[n];

		for (int i = 0; i < n; i++)
			piv[i] = i;

		for (int k = 0; k < n - 1; k++) {
			// get max(A[k:n][k])
			double maxr = 0.;
			int r = 0;
			for (int i = k; i < n; i++)
				if (Math.abs(A[i][k]) > maxr) {
					maxr = Math.abs(A[i][k]);
					r = i;
				}

			// no need for q, here: all array indexing is absolute.

			// pivot k, r
			{
				int tmp = piv[k];
				piv[k] = piv[r];
				piv[r] = tmp;
			}
			for (int i = 0; i < n; i++) {
				double tmp = A[k][i];
				A[k][i] = A[r][i];
				A[r][i] = tmp;
			}

			// adjust remainder
			if (A[k][k] != 0.) {
				for (int i = k + 1; i < n; i++) {
					A[i][k] /= A[k][k];
					for (int j = k + 1; j < n; j++)
						A[i][j] -= A[i][k] * A[k][j];
				}
			}
		}

		// L-U extraction is done later, because java can't return 2
		// values like matlab can.

		return piv;

		/*
		  state:

		  - L is the lower half of this matrix, with 1's on the main
		  diagonal, and 0's in the upper half

		  - U is the upper half of this matrix, with the diagonal
		  as-is, and 0's in the lower half

		  - A is the original matrix

		  - P is a matrix with all zeros, except in row i, column
		  pi[i] is a 1

		  then:

		  PA = LU

		 */
	}

	/** Solves the nonsingular lower-triangular system Lx=b.  See van
	Loan, p. 211.
	@param L
	@param b
	@return x
	@exception IllegalArgumentException if L isn't square, or b
	isn't the same size */
	private static double[] LTriSol(double L[][], double b[]) {
		// make sure it's an acceptable size
		int n = L.length;
		if (L[0].length != n || b.length != n)
			throw new IllegalArgumentException("Wrong size");

		// make vector for output
		double x[] = new double[n];

		// loop to create x
		for (int j = 0; j < n - 1; j++) {
			x[j] = b[j] / L[j][j];
			for (int i = j + 1; i < n; i++)
				b[i] -= L[i][j] * x[j];
		}
		x[n - 1] = b[n - 1] / L[n - 1][n - 1];

		// return it
		return x;
	}

	/** Solves the nonsingular upper-triangular system Ux=b.  See van
	Loan, p. 212.
	@param U
	@param b
	@return x
	@exception IllegalArgumentException if L isn't square, or b
	isn't the same size */
	private static double[] UTriSol(double U[][], double b[]) {
		// make sure it's an acceptable size
		int n = U.length;
		if (U[0].length != n || b.length != n)
			throw new IllegalArgumentException("Wrong size");

		// make vector for output
		double x[] = new double[n];

		// loop to create x
		for (int j = n - 1; j > 0; j--) {
			x[j] = b[j] / U[j][j];
			for (int i = 0; i < j; i++)
				b[i] -= x[j] * U[i][j];
		}
		x[0] = b[0] / U[0][0];

		// return it
		return x;
	}

	/** Solve the general equation Ax=b for x, given square matrix A
	and vector b.  (Intended to be partially compatible with
	Numerical Recipes' <code>gaussj</code>, which it replaces.)
	See van Loan for derivation.
	@param A the "A" matrix in Ax=b
	@param b the "b" matrix in Ax=b; it is replaced with x
	@exception IllegalArgumentException if A is not square, or b
	is a different size
	@exception SingularMatrixException if A is singular */
	public static double[] solveNxN(double A[][], double b[])
			throws SingularMatrixException {
		// is it a 2x2?  i know a really fast way to do those...
		if (A.length == 2 && A[0].length == 2 && b.length == 2)
			return solve2x2(A, b);

		// make sure it's an acceptable size
		int n = A.length;
		if (A[0].length != n || b.length != n)
			throw new IllegalArgumentException("Wrong size");

		// LU decompose, and get pi (this smashes A)
		int piv[] = GEpiv(A);

		// extract L and U
		double L[][] = new double[n][n];
		double U[][] = new double[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (j >= i)
					U[i][j] = A[i][j];
				else
					L[i][j] = A[i][j];
		for (int i = 0; i < n; i++)
			L[i][i] = 1.;

		// with polyfit in its current setup, piv is n-1:0.  possible
		// to take advantage of this?  sure, save allocating [n] and
		// one loop and just say: for (int i=0; i<n; i++) { double tmp
		// = b[i]; b[i] = b[n-1-i]; b[n-1-i] = tmp; } (i won't use
		// this until i'm convinced piv is ALWAYS n-1:0, though.)

		// pivot b using piv.
		{
			double tmp[] = new double[n];
			for (int i = 0; i < n; i++)
				tmp[i] = b[piv[i]];
			for (int i = 0; i < n; i++)
				b[i] = tmp[i];
		}

		// back-substitute
		double y[] = LTriSol(L, b);
		double x[] = UTriSol(U, y);

		// return it
		return x;
	}

	/** A special case of <code>solveNxN</code> for 2x2 matrices.  In
	the equation Ax=b, given A and b, x is found; its value is
	written back into b.  Direct substitution is used, so it is
	very fast (only 10 floating-point operations).
	@param A the "A" matrix in Ax=b; it is untouched
	@param b the "b" matrix in Ax=b; it is replaced with x
	@exception IllegalArgumentException if A is any size other
	than 2x2 or b is any size other than 2
	@exception SingularMatrixException if A is singular */
	public static double[] solve2x2(double A[][], double b[])
			throws SingularMatrixException {
		// make sure it's 2x2
		if (A.length != 2 || A[0].length != 2 || b.length != 2)
			throw new IllegalArgumentException("Wrong size");

		try {
			// compute result
			double x0 = (b[1] - b[0] * A[1][1] / A[0][1])
					/ (A[1][0] - A[1][1] * A[0][0] / A[0][1]);
			double x1 = (b[0] - A[0][0] * x0) / A[0][1];

			// return it in an array
			return new double[] { x0, x1 };

			// watch for singularities (== divide-by-zeros)
		} catch (ArithmeticException ae) {
			throw new SingularMatrixException();
		}
	}

	/** A least-squares solver.  See <i>Introduction to
	    Algorithms</i>, Cormen, Leiserson, and Rivest, pp. 768-771.
	    This uses <code>solveNxN</code> or <code>solve2x2</code>.
	@param s an object that can evaluate the basis functions
	@param x the x-coordinates of the data
	@param y the y-coordinates of the data
	@return the coefficients of the basis functions
	@exception SingularMatrixException (can this happen?) */
	public static double[] leastSquares(SolverFunction s, double x[], double y[])
			throws SingularMatrixException {
		// n = nr of (x,y) points; m = nr of basis functions
		int n = x.length;
		int m = s.f(0.).length;

		// A: each row is f(x[i]), x.length rows (A is n x m)
		double A[][] = new double[n][];
		for (int i = 0; i < n; i++)
			A[i] = s.f(x[i]);

		// now, compute coefficients c by solving (A^t A) c = A^t y

		// compute S = A^t A
		double S[][] = new double[m][m];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j <= i; j++) {
				S[i][j] = 0.;
				for (int k = 0; k < n; k++)
					S[i][j] += A[k][i] * A[k][j];
				S[j][i] = S[i][j]; // result is symmetric, so save the effort
			}
		}

		// compute T = A^t y
		double T[] = new double[m];
		for (int i = 0; i < m; i++) {
			T[i] = 0.;
			for (int j = 0; j < n; j++)
				T[i] += A[j][i] * y[j];
		}

		// solve Sx=T; this throws SingularMatrixException
		return solveNxN(S, T);
	}
}
