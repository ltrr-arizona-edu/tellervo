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

package edu.cornell.dendro.corina.map;

/*
 TODO:
 - javadoc: multiply(), class
 - figure out if they should be floats, or doubles.  don't mix them.
 - write some unit tests for this class.
*/

    // BETTER: instead of making these methods public, why not just make rotate(vector,axis,angle) public?
    // that way, i can re-use the matrices safely, and lusers won't need to know how i do rotations.

/**
   Matrix math routines.

 <p>
 WRITEME: are they for floats, or doubles?  or either?  do i care?  be consistent!
 WRITEME: author, version, etc.
*/
public class Matrix {
    // don't instantiate me
    private Matrix() { }
    
    /**
       Make an x-axis rotation matrix.  If M is the x-axis rotation matrix,
       and v is a 3-vector, then v'=Mv is the vector v rotated around the x-axis
       by the specified angle.
    
       @param angle the angle to rotate by, in degrees
       @return a 3x3 rotation matrix
    */
    public static double[][] makeRotateX(float angle) {
        double radians = Math.toRadians(angle);
        double c = Math.cos(radians);
        double s = Math.sin(radians);
        return new double[][] {
        { 1, 0, 0 },
        { 0, c, s },
        { 0, -s, c }};
        // was: {1,0,0},{0,c,-s},{0,s,c}, but that looked buggy to me -- tell the rmap guy?
	// (he never responded to my first bug report)
    }

    /**
       Make an y-axis rotation matrix.  If M is the y-axis rotation matrix,
       and v is a 3-vector, then v'=Mv is the vector v rotated around the y-axis
       by the specified angle.

       @param angle the angle to rotate by, in degrees
       @return a 3x3 rotation matrix
    */
    public static double[][] makeRotateY(float deg) {
        double rad = Math.toRadians(deg);
        double c = Math.cos(rad);
        double s = Math.sin(rad);
        return new double[][] {
        { c, 0, s },
        { 0, 1, 0 },
        { -s, 0, c}};
    }

    /**
       Make an z-axis rotation matrix.  If M is the z-axis rotation matrix,
       and v is a 3-vector, then v'=Mv is the vector v rotated around the z-axis
       by the specified angle.

       @param angle the angle to rotate by, in degrees
       @return a 3x3 rotation matrix
     */
    public static double[][] makeRotateZ(float deg) {
        double rad = Math.toRadians(deg);
        double c = Math.cos(rad);
        double s = Math.sin(rad);
        return new double[][] {
        { c, s, 0 },
        { -s, c, 0 },
        { 0, 0, 1}};
    }

    /**
       Multiply two matrices.  Inputs are 2-dimensional arrays of
       doubles, as is the output.  The output matrix is created by
       this method.

       <p><b>Assumes</b> the matrices are rectangular.</p>
    
       @param A the first matrix to multiply
       @param B the second matrix to multiply
       @return the product of A and B
    */
    public static double[][] multiply(double a[][], double b[][]) {
        // verify sizes: common dimension
        if (a[0].length != b.length)
            throw new IllegalArgumentException();
        int r = b.length;
	// ASSUMES: matrices are rectangular

        // compute result size
        int m = a.length;
        int n = b[0].length;

        double[][] c = new double[m][n]; // all zeros

        for (int i=0; i<m; i++)
            for (int j=0; j<n; j++)
                for (int k=0; k<r; k++)
                    c[i][j] += a[i][k] * b[k][j];

        return c;
    }

    /**
       Scale all entries of a matrix by a factor.  Operates destructively on the matrix in-place.

       @param matrix the matrix to scale
       @param factor the scaling factor
    */
    public static void scale(double matrix[][], float factor) {
        for (int i=0; i<matrix.length; i++)
            for (int j=0; j<matrix[i].length; j++)
                matrix[i][j] *= factor;
    }
}
