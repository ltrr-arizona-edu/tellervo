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

package corina.map;

// matrix math routines for double[][] matrices
abstract class Matrix {

    // rotateX: a rotate matrix for deg degrees
    public static double[][] makeRotateX(double deg) {
        double rad = Math.toRadians(deg);
        double c = Math.cos(rad);
        double s = Math.sin(rad);
        return new double[][] {
        { 1, 0, 0 },
        { 0, c, s },
        { 0, -s, c }};
        // was: {1,0,0},{0,c,-s},{0,s,c}, but that looked buggy to me
    }

    // rotateY: a rotate matrix for deg degrees
    public static double[][] makeRotateY(double deg) {
        double rad = Math.toRadians(deg);
        double c = Math.cos(rad);
        double s = Math.sin(rad);
        return new double[][] {
        { c, 0, s },
        { 0, 1, 0 },
        { -s, 0, c}};
    }

    // rotateZ: a rotate matrix for deg degrees
    public static double[][] makeRotateZ(double deg) {
        double rad = Math.toRadians(deg);
        double c = Math.cos(rad);
        double s = Math.sin(rad);
        return new double[][] {
        { c, s, 0 },
        { -s, c, 0 },
        { 0, 0, 1}};
    }

    public static double[][] multiply(double a[][], double b[][]) {
        // verify sizes: common dimension
        if (a[0].length != b.length)
            throw new IllegalArgumentException();
        int r = b.length;

        // compute result size
        int m = a.length;
        int n = b[0].length;

        double[][] c = new double[m][n];

        for (int i=0; i<m; i++)
            for (int j=0; j<n; j++)
                for (int k=0; k<r; k++)
                    c[i][j] += a[i][k] * b[k][j];

        return c;
    }
    
    public static void scale(double matrix[][], double s) {
        for (int i=0; i<matrix.length; i++)
            for (int j=0; j<matrix[0].length; j++)
                matrix[i][j] *= s;
    }
}
