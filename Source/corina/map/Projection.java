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
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package corina.map;

import corina.map.projections.RectangularProjection;

import java.awt.Point;

/**
   A projection from Locations (latitude, longitude) to Points (x, y).

   <h2>Using Projections</h2>

   <p>Projection is abstract.  To make a Projection, call Projection.makeProjection(view).</p>

   <p>The View you make your Projection with never changes.  If you want to do some projections
   on a different View, make a new Projection.</p>
 
   <p>To use a Projection, use p.project() and p.unproject().  For performance reasons
   (they get called so many times) they don't return new objects, but get passed both
   input and output objects.</p>

   <h2>Writing a Projection</h2>
 
   <p>To write a new Projection, you just need to write a constructor, and project() and
   unproject() methods.  See RectangularProjection for an example.</p>
 
   <p>When drawing a map, project() is called thousands of times per map, with
   possibly several maps per second.  It should be as fast as possible.  Don't
   allocate any new objects in project(): it'll swamp the GC, and performance
   will suffer.</p>

   <p>(You don't need to worry as much about unproject().  It should still be as fast
   as possible, but it's not called nearly as frequently, so performance is not quite
   as critical.  For example, when the user drags or double-clicks on the map,
   unproject() is called just once to figure out where the mouse event was.)</p>

   <h2>Left To Do</h2>
   <p>(From most important to least.)</p>
   <ul>
     <li>Switch from Point3D to Point; Point3D should be internal to the spherical projection
     <li>javadoc the View object
     <li>Let me pick which projection makeProjection() returns
     <li>Cache projections in makeProjection()
     <li>My view field used to be protected; should it be?
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public abstract class Projection {
    /**
       Make a new projection.

       <p>Currently, this simply returns a new RectangularProjection, but in the future
       you'll be able to pick which projection to use and it may offer other features like caching.</p>
    
       @param view the View to use
    */
    public static Projection makeProjection(View view) {
        return new RectangularProjection(view);
    }

    /** The view for this projection. */
    public View view;

    /**
       Make a new projection.  This just stores the view parameter; subclass constructors
       should call super(view).
    
       @param view the View to use
    */
    protected Projection(View view) {
        this.view = view;
    }
    
    /**
       Project a Location to a Point.

       <p>(Because this method is called so many times when drawing maps,
       it takes 2 arguments, and implementations store the result in the
       second one.  Returning a new value each time would swamp the GC.)</p>

       @see #unproject
       @param location the Location to project
       @param point the result of the projection
    */
    public abstract void project(Location location, Point3D point);

    /**
       Unproject a Point back to a Location.

       <p>(Because this method is called so often,
       it takes 2 arguments, and implementations store the result in the
       second one, similar to how project() works.)</p>

       @see #project
       @param point the Point to unproject
       @param location the Location to store it in
    */
    public abstract void unproject(Point point, Location location);

    /*
     i'll need to switch from projecting to Point3Ds to projecting to Points.
     who uses project()?
     generally:
     - all the layers
     - all the tools
     - all the projections
     - MapFile, MapPanel
     specifically:
     - GridlinesLayer.java (no issues)
     - LegendLayer.java (no issues)
     - MapFile.java
     - MapLayer.java
     - MapPanel.java
     - Projection.java
     - RectangularProjection.java
     - SitesLayer.java
     - SphericalProjection.java
     - tools/ArrowTool.java
     - tools/HandTool.java
     - tools/RulerTool.java
     - tools/ZoomInTool.java
     - tools/ZoomOutTool.java
     this would be a great application of CVS.  check out a new copy, change project()
     interface, start changing things.  when done, check in everything at once.  if
     something screws up, just delete the folder.
    */
}
