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

package edu.cornell.dendro.corina.index;

import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.ui.I18n;

/**
   A cubic spline index.

   <p>The algorithm was (first?) discussed in "The Smoothing Spline: A
   New Approach to Standardizing Forest Interior Tree-Ring Width
   Series for Dendroclimatic Studies", Edward R. Cook and Kenneth
   Peters, Tree-Ring Bulletin, Vol. 41, 1981.  The algorithm they used
   comes from Reinsch, C. H., 1967: "Smoothing by spline functions",
   Numerische Mathematik 10: 177-83.</p>

   <p>The "S" term (smoothing factor?) can be set by the user in the
   preferences; a lower S-value results in a higher-frequency spline.
   S=1 is a linear fit, S=0 fits the data exactly, and S=1e-16 seems
   to be a good starting point for dendro datasets.</p>

   <p>The original algorithm was written in ALGOL.  I do not claim
   this is an efficient implementation of it, or that it is correct,
   or even that I understand it.  I copied the ALGOL into Java, and
   twiddled with it until it seemed to work.  If there is a newer
   algorithm or implementation available, I will be happy to use
   it.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class CubicSpline extends Index {
	/**
	 * Create a new cubic spline.
	 * 
	 * @param s the Sample to index
	 */
	public CubicSpline(Sample s) {
		super(s);
	}

	// smoothing factor
	private final double s = Double.parseDouble(App.prefs.getPref(
			"corina.index.cubicfactor", "1e-16"));

	// output coefficients
	private double a[];
	private double b[];
	private double c[];
	private double d[];

	// run reinsch's smooth
	private void smooth() {
		// my init stuff
		int N = source.data.size();
		final int n1 = 1, n2 = N;
		double x[] = new double[N + 1];
		double y[] = new double[N + 1];
		// double dy[] = new double[N+1];
		final double dy = 1.0;
		for (int i = 1; i < N + 1; i++) {
			x[i] = (double) i - 1;
			y[i] = ((Number) source.data.get(i - 1)).doubleValue();
			// dy[i] = 1.0;
		}
		N += 2;

		// i have no idea where this guy picks his variable names.
		double e, f, f2, g, h, p;
		a = new double[N];
		b = new double[N];
		c = new double[N];
		d = new double[N];
		double r[], r1[], r2[], t[], t1[], u[], v[];
		r = new double[N];
		r1 = new double[N];
		r2 = new double[N];
		t = new double[N];
		t1 = new double[N];
		u = new double[N];
		v = new double[N];

		int m1 = n1 - 1;
		int m2 = n2 + 1;
		r[m1] = r[n1] = r1[n2] = r2[n2] = r2[m2] = u[m1] = u[n1] = u[n2] = u[m2] = p = 0;
		m1 = n1 + 1;
		m2 = n2 - 1;
		g = h = m1 - n1 /* x[m1] - x[n1] */;
		f = (y[m1] - y[n1]) / h;

		// real code starts here
		for (int i = m1; i <= m2; i++) {
			g = h;
			h = 1.0; // x[i+1] - x[i];
			e = f;
			f = (y[i + 1] - y[i]); // / h;
			a[i] = f - e;
			t[i] = 2 * (g + h) / 3;
			t1[i] = h / 3;
			r2[i] = dy /* [i-1] *// g;
			r[i] = dy /* [i+1] */; // /h;
			r1[i] = -dy /* [i] *// g - dy/* [i] */; // /h;
		}

		for (int i = m1; i <= m2; i++) {
			b[i] = r[i] * r[i] + r1[i] * r1[i] + r2[i] * r2[i];
			c[i] = r[i] * r1[i + 1] + r1[i] * r2[i + 1];
			d[i] = r[i] * r2[i + 2];
		}
		f2 = -s;

		for (;;) { // "next iteration"
			for (int i = m1; i <= m2; i++) {
				r1[i - 1] = f * r[i - 1];
				r2[i - 2] = g * r[i - 2];
				r[i] = 1 / (p * b[i] + t[i] - f * r1[i - 1] - g * r2[i - 2]);
				u[i] = a[i] - r1[i - 1] * u[i - 1] - r2[i - 2] * u[i - 2];
				f = p * c[i] + t1[i] - h * r1[i - 1];
				g = h;
				h = d[i] * p;
			}
			for (int i = m2; i >= m1; i--)
				u[i] = r[i] * u[i] - r1[i] * u[i + 1] - r2[i] * u[i + 2];
			e = h = 0;
			for (int i = n1; i <= m2; i++) {
				g = h;
				h = (u[i + 1] - u[i]); // /(x[i+1] - x[i]);
				v[i] = (h - g) * dy/* [i] */* dy/* [i] */;
				e += v[i] * (h - g);
			}
			g = v[n2] = -h * dy/* [n2] */* dy/* [n2] */;
			e -= g * h;
			g = f2;
			f2 = e * p * p;
			if (f2 >= s || f2 <= g)
				break; // f2>=s
			f = 0;
			h = (v[m1] - v[n1]) / (m1 - n1); // (x[m1] - x[n1]);
			for (int i = m1; i <= m2; i++) {
				g = h;
				h = (v[i + 1] - v[i]); // /(x[i+1]-x[i]);
				g = h - g - r1[i - 1] * r[i - 1] - r2[i - 2] * r[i - 2];
				f = f + g * r[i] * g;
				r[i] = g;
			}
			h = e - p * f;
			if (h <= 0)
				break;
			p += (s - f2) / ((Math.sqrt(s / e) + p) / h);
		}
		// use negative branch of square root, if the sequence of
		// abscissae x[i] is strictly decreasing
		for (int i = n1; i <= n2; i++) {
			a[i] = y[i] - p * v[i];
			c[i] = u[i];
		}
		for (int i = n1; i <= m2; i++) {
			h = 1.0; // h = x[i+1] - x[i];
			d[i] = (c[i + 1] - c[i]) / (3 * h);
			b[i] = (a[i + 1] - a[i]) / h - (h * d[i] + c[i]) * h;
		}
	}

	/** Compute the cubic spline. */
	public void index() {
		// run computation
		smooth();

		// compute curve: since h=1, y[i] = d[i]+c[i]+b[i]+a[i]
		int n = source.data.size();
		for (int i = 0; i < n; i++) {
			double y = d[i] + c[i] + b[i] + a[i];
			data.add(new Double(y));
		}

		// the technical term for this is "faking it". yeah, i'm
		// assuming this cubic spline is linear at one endpoint.
		{
			double y2 = ((Number) data.get(2)).doubleValue();
			double y1 = ((Number) data.get(1)).doubleValue();
			double y0 = y1 - (y2 - y1);
			data.set(0, new Double(y0));
		}
	}

	public String getName() {
		return I18n.getText("cubic_spline");
	}

	public int getID() {
		return 10;
	}
}
