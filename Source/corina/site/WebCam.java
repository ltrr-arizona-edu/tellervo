package corina.site;

import java.net.URL;
import java.net.MalformedURLException;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

/*
  TODO list for webcams:
  -- use a real site.webcam field
  -- allow selecting of url -- dropping image url, for example
  -- allow using image elsewhere: drag it away, or right-click popup -> copy (full-sized image for both, not scaled-down one)
  -- (2 options: "copy image", "copy url")
  -- handle bad URLs intelligently
  -- handle network-not-available (or server-not-available) intelligently
  -- watch image and adapt to how often it changes -- if it only changes once a minute, only load it once a minute
  -- (store the how-often info with the url, so it doesn't have to recompute each time)

  -- write WebCam(Site) constructor
  -- and since multiple sites can have the same location, have it check the sitedb if this site has no webcam url (?)

  -- use MediaTracker to track loading; display "loading..." or progressbar or hourglass or something while it's loading

  BUG:
  -- need a way to either (1) change the url (easy), or (2) stop the update thread (will need this, too)
  -- (stop the update thread when the info window is closed)

  URLs:
  -- http://www.loutro.net/webcam/webcam-links.html
  -- http://www.camcentral.com/location.asp?item=Greece
*/

public class WebCam extends JPanel {

    // FOR TESTING
    private static final String VENICE = "http://turismo.regione.veneto.it/webcam/full.jpg";
    private static final String ITHACA = "http://128.253.30.11/cornellcont.mwc?images=1";
    public static WebCam makeVeniceWebcam() {
	try {
	    return new WebCam(ITHACA);
	} catch (MalformedURLException mue) {
	    // can't happen
	    return null;
	}
    }

    private JLabel label = new JLabel();

    private URL url;

    public WebCam(String url) throws MalformedURLException {
	setLayout(new FlowLayout(FlowLayout.CENTER));
	add(label);
	this.url = new URL(url);

	// update it every 10s -- THIS THREAD NEVER DIES!  (solution: use one updater thread for all views?)
	new Thread() {
	    public void run() {
		try {
		    for (;;) {
			update();
			Thread.sleep(10000);
		    }
		} catch (Exception e) {
		    // do something?
		    System.out.println("e -- " + e);
		}
	    }
	}.start();
    }

    private void update() {
	// if it's not showing, don't bother
	if (!label.isShowing())
	    return;

	// load the image from the url.
	// note: force no-cache, by using explicit createImage() call:
	// (this doesn't work: ImageIcon image = new ImageIcon(new java.net.URL(veniceURL));)
	ImageIcon image = new ImageIcon(Toolkit.getDefaultToolkit().createImage(url));

	// scale down to 200 pixels wide
	int height = image.getIconHeight(), width = image.getIconWidth();
	float ratio = 200f / (float) width; // REFACTOR: need a set of scale() methods
	image = new ImageIcon(image.getImage().getScaledInstance((int) (width*ratio),
								 (int) (height*ratio),
								 Image.SCALE_SMOOTH));

	// update the view
	label.setIcon(image);
	label.repaint();
    }
}
