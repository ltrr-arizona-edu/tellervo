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

import corina.map.layers.GridlinesLayer;
import corina.map.layers.MapLayer;
import corina.map.layers.LegendLayer;
import corina.map.layers.SitesLayer;
import corina.util.Overwrite;
import corina.util.Platform;
import corina.util.OKCancel;
import corina.util.DocumentListener2;
import corina.ui.Builder;
import corina.gui.FileDialog;
import corina.gui.Layout;
import corina.gui.layouts.DialogLayout;
import corina.gui.Bug;
import corina.gui.UserCancelledException;

import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.dom.GenericDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DOMImplementation;

// import com.keypoint.PngEncoder;

/**
   Take snapshots of a map, and export as PNG or SVG.

   <h2>Left to do:</h2>
   <ul>
     <li>Javadoc
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Snapshot {
    private Snapshot() {
        // don't instantiate me
    }

    public static void exportSVG(View view, LabelSet labels) {
        try {
            // figure out a filename
            String filename = FileDialog.showSingle("Export SVG");

            // overwrite?
            Overwrite.overwrite(filename);

            // (based on code from http://xml.apache.org/batik/svggen.html)

            // Get a DOMImplementation
            DOMImplementation domImpl =
                GenericDOMImplementation.getDOMImplementation();

            // Create an instance of org.w3c.dom.Document
            Document document = domImpl.createDocument(null, "svg", null);

            // Create an instance of the SVG Generator
            SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

            // draw the map to it:
            // get center,zoom from view, but use my size.
            // TODO: set view.size here?  (clone first!) (clone in getView()?)

            drawMapStuff(view, labels, svgGenerator);

            // stream to an output file; need to use OutputStreamWriter +
            // FileOutputStream myself (instead of FileWriter) so i can guarantee
            // it's UTF-8, and not "platform default", whatever that is (but
            // usually not UTF-8).
            Writer out = new OutputStreamWriter(new FileOutputStream(filename),
                                                "UTF-8");
            svgGenerator.stream(out, false); // (false=don't use css)

            // when done, open containing folder in finder/explorer,
            // and select this file.
            Platform.open(filename);

        } catch (UserCancelledException uce) {
            // do nothing
        } catch (IOException ioe) {
            // WRITEME
            Bug.bug(ioe);
        }
    }

    public static void exportPNG(View view, LabelSet labels, JFrame parent) {
        try {
            // make a copy of the view to work with
            View myView = (View) view.clone();

            // ask for dimensions
            Dimension size = askDimensionsPNG(myView, parent);
            int width = size.width; // 1280;
            int height = size.height; // 1024;

            // adjust the user's height/width here so the view is the same
            float widthZoom = width / (float) myView.size.width;
            float heightZoom = height / (float) myView.size.height;
            float zoom = Math.min(widthZoom, heightZoom);

            // figure out a filename
            String filename = FileDialog.showSingle("Export PNG");

            // overwrite?
            Overwrite.overwrite(filename);

            // DESIGN: combine both exports into a single dialog
            // (ask for type, dimensions, oversampling)

            // keep center,zoom from view, but use my size and zoom
            myView.size = new Dimension(width, height);
            myView.setZoom(myView.getZoom() * zoom);

            // make a buffer, and draw the layers there
            BufferedImage buf = new BufferedImage(width, height,
                                                  BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D g2 = buf.createGraphics();

            // white background
            g2.setColor(Color.white);
            g2.fillRect(0, 0, width, height);

            // antialiasing, and high-quality rendering
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                                RenderingHints.VALUE_RENDER_QUALITY);

            drawMapStuff(myView, labels, g2);

            // dump the buf to a png
            PngEncoder enc = new PngEncoder(buf);
            enc.setCompressionLevel(9);
            byte p[] = enc.pngEncode();
            buf = null; // won't need buf later -- dump it before writing

            // dump the png to disk
            OutputStream o = new BufferedOutputStream(new FileOutputStream(filename));
            try {
              o.write(p);
            } finally {
              try {
                o.close();
              } catch (IOException ioe) {
                ioe.printStackTrace();
              }
            }

            // when done, open containing folder in finder/explorer,
            // and select this file.
            Platform.open(filename);

        } catch (UserCancelledException uce) {
            // do nothing
        } catch (IOException ioe) {
            // WRITEME
            Bug.bug(ioe);
        }
    }

    private static void drawMapStuff(View view, LabelSet labels, Graphics2D g2) {
        // make a projection for it
        Projection r = Projection.makeProjection(view);

        // make some layers
        Layer layers[] = new Layer[] {
            new MapLayer(),
            new GridlinesLayer(),
            new SitesLayer(labels),
            new LegendLayer(),
        };

        // FIXME: just calling g2.scale here doesn't fix my chunkiness problem.
        // it's deeper than that...
        for (int i=0; i<layers.length; i++)
            layers[i].draw(g2, r);
    }

    // QUALITY: turn down thresh for this?  it'll
    // probably be insignificantly small for this, but...
    // nope: it's REALLY chunky.  FIXME!

    // SVG EXPORT: lines too thin?

    // SVG EXPORT: need to mask (maplayer, at least) at edges of visible

    // SVG EXPORT: allow setting size/proportions

    // SVG EXPORT: map ends up way off left edge, due to masked ??? -- what can i do about this?

    // put up a (modal) dialog, and ask the user how big
    // (width x height, in px) to make the PNG.
    // returns it as a Dimension, or throws a UCE.
    private static Dimension askDimensionsPNG(View view, JFrame parent) throws UserCancelledException {
        // TODO:
        // -- center over map frame?
        // -- height/pixels labels don't quite line up (nested panel? dialog layout?)
        // -- store last size, so it's the same next time?  (corina.map.png-size)
        // -- completely disallow typing non-digits?
        // -- add a popup:
        /*
         Presets?: [ Web, large (600x400) ]
         [ Web, small (300x200) ]
         [ PowerPoint (800x600) ]
         [ Word (500x400)       ]
         [ Custom               ]
         */

        final JTextField width = new JTextField(4);
        width.setText(String.valueOf(view.size.width));
        final JTextField height = new JTextField(4);
        height.setText(String.valueOf(view.size.height));

        JLabel label = new JLabel("Choose a size for this PNG:");

        JPanel content = new JPanel(new DialogLayout());
        content.add(Layout.flowLayoutL(width, new JLabel(" pixels")),
                    "Width:");
        content.add(Layout.flowLayoutL(height, new JLabel(" pixels")),
                    "Height:");

        JButton cancel = Builder.makeButton("cancel");
        final JButton ok = Builder.makeButton("ok");
        JPanel buttons = Layout.buttonLayout(cancel, ok);

        // don't let numbers be typed
        final Color defaultColor = width.getForeground(); // ???
        DocumentListener numbersOnly = new DocumentListener2() {
            private boolean checkField(JTextField f) {
                boolean valid;
                try {
                    Integer.parseInt(f.getText().trim());
                    valid = true;
                } catch (NumberFormatException nfe) {
                    valid = false;
                }
                f.setForeground(valid ? defaultColor : Color.red);
                return valid;
            }
            public void update(DocumentEvent e) {
                // check both -- if one becomes valid,
                // i still need to know the state of the other one.

                boolean widthValid = checkField(width);
                boolean heightValid = checkField(height);

                ok.setEnabled(widthValid && heightValid);
            }
        };
        width.getDocument().addDocumentListener(numbersOnly);
        height.getDocument().addDocumentListener(numbersOnly);

        Component strut1 = Box.createHorizontalStrut(16);
        Component strut2 = Box.createHorizontalStrut(8);

        JPanel all = Layout.borderLayout(label,
                                         strut1, content, strut2,
                                         buttons);
        all.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final JDialog dialog = new JDialog(parent, true); // =modal
        dialog.setTitle("Export PNG");
        dialog.setContentPane(all);

        final boolean okClicked[] = new boolean[1];
        okClicked[0] = false;

        AbstractAction buttonListener = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                okClicked[0] = (e.getSource() == ok);
                dialog.dispose();
            }
        };

        ok.addActionListener(buttonListener);
        cancel.addActionListener(buttonListener);

        dialog.pack();
        width.selectAll();
        OKCancel.addKeyboardDefaults(ok);
        dialog.setResizable(false);
        dialog.show();

        // ...then...

        if (okClicked[0])
            return new Dimension(Integer.parseInt(width.getText()),
                                 Integer.parseInt(height.getText()));
        else
            throw new UserCancelledException();
    }
}
