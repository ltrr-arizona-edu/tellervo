package org.tellervo.desktop.tridasv2.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CancellationException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.ui.Builder;

public class ImagePreviewPanel extends JPanel {

	private final static Logger log = LoggerFactory
			.getLogger(ImagePreviewPanel.class);

	private static final long serialVersionUID = 1L;
	private Image image;
	private Image scaledImage;
	private int imageWidth = 0;
	private int imageHeight = 0;
	private int border = 15;
	

	// private long paintCount = 0;

	// constructor
	public ImagePreviewPanel() {
		super();
		setLayout(new MigLayout("", "[10px,grow,center]",
				"[232.00px,grow,center]"));
		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {
				scaleImage();
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				scaleImage();
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				scaleImage();
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				scaleImage();
			}

		});
	}

	public void clearImage() {
		image = null;
		scaledImage = null;
		imageWidth = 0;
		imageHeight = 0;

	}

	public void loadImage(String file) throws IOException {

		image = ImageIO.read(new File(file));
		// might be a situation where image isn't fully loaded, and
		// should check for that before setting...
		imageWidth = image.getWidth(this);
		imageHeight = image.getHeight(this);
		setScaledImage();
	}

	public void loadPlaceholderImage() {
		image = Builder.getIconAsImage("previewnotavailable.png", 128);
		if (image == null) {

			log.error("Unable to load placeholder image");
			return;
		}

		imageWidth = image.getWidth(this);
		imageHeight = image.getHeight(this);
		setScaledImage();

	}
	
	public void loadLoadingImage() {
		image = Builder.getIconAsImage("loading.gif", 128);
		if (image == null) {

			log.error("Unable to load loading image");
			return;
		}

		imageWidth = image.getWidth(this);
		imageHeight = image.getHeight(this);
		setScaledImage();

	}

	public boolean loadImage(URI uri) {
		
		
		/*if (uri == null) {
			loadPlaceholderImage();
			return false;
		}

		File f = null;
		try {
			f = new File(uri);
		} catch (IllegalArgumentException ex) {
			loadPlaceholderImage();
			return false;
		}

		if (f.length() > 9437184.0) {
			// > 9mb
			log.debug("Image too large to preview.");
			loadPlaceholderImage();
			return false;
		}

		try {
			image = ImageIO.read(f);
		} catch (IOException ex) {
			loadPlaceholderImage();
			return false;

		}

		try {
			imageWidth = image.getWidth(this);
			imageHeight = image.getHeight(this);

			if (imageWidth == -1) {
				log.debug("Image width not yet known");
			}

			double maxdim = 600;
			if (imageWidth > maxdim || imageHeight > maxdim) {

				log.debug("Large image - resampling");
				double ratio = (double) imageWidth / (double) imageHeight;

				log.debug("Original width = " + imageWidth);
				log.debug("Original height = " + imageHeight);
				log.debug("Ratio = " + ratio);

				int w = (int) maxdim;
				int h = (int) maxdim;
				if (imageWidth > imageHeight) {
					log.debug("Image wider than tall");
					w = (int) maxdim;
					h = (int) (maxdim / ratio);
				} else {
					log.debug("Image taller than wide");
					h = (int) maxdim;
					w = (int) (maxdim * ratio);
				}

				log.debug("Rescaling to " + w + " x " + h);
				log.debug("Replacing full size image with rescaled image");
				image = ImageIO.read(f).getScaledInstance(w, h,
						BufferedImage.SCALE_FAST);

			}
		} catch (Exception e) {
		}

		if (image == null) {
			loadPlaceholderImage();
			return false;
		}*/
		
		loadLoadingImage();
		setScaledImage();
		scaleImage();
		
		final LoadImageWorker thread = new LoadImageWorker(uri);
		
		thread.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				
	
		        switch (event.getPropertyName()) {
		        case "progress":

		          break;
		        case "state":
		          switch ((StateValue) event.getNewValue()) {
		          case DONE:

		            try {
		              image = thread.get();
		              if(image==null)
		              {
		            	  loadPlaceholderImage();
		              }
		           
		            } catch (final CancellationException e) {
		           
		            	loadPlaceholderImage();
		            	
		            } catch (final Exception e) {
		             
		            	loadPlaceholderImage();
		            }
		            break;
		          case STARTED:
		          case PENDING:

		            break;
		          }
		          break;
		        }
		        
				setScaledImage();
				scaleImage();
		        
		      }
			

			
		});
		
		thread.execute();
		
		return true;
	}

	// e.g., containing frame might call this from formComponentResized
	public void scaleImage() {
		setScaledImage();
	}

	// override paintComponent
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (scaledImage != null) {
			// System.out.println("ImagePanel paintComponent " + ++paintCount);

			int scaledWidth = scaledImage.getWidth(this);
			int scaledHeight = scaledImage.getHeight(this);

			Float pw = (float) (this.getWidth() - border - border); // panel
																	// width
			Float ph = (float) (this.getHeight() - border - border); // panel
																		// height

			int x = border;
			int y = border;

			if ((pw.intValue() - scaledWidth) > 0) {
				x = (pw.intValue() - scaledWidth) / 2;
			}
			if ((ph.intValue() - scaledHeight) > 0) {
				y = (ph.intValue() - scaledHeight) / 2;
			}

			if (x < border)
				x = border;
			if (y < border)
				y = border;
			g.drawImage(scaledImage, x, y, this);
		}
	}

	private void setScaledImage() {
		if (image != null) {

			// use floats so division below won't round
			float iw = imageWidth;
			float ih = imageHeight;
			float pw = this.getWidth() - border - border; // panel width
			float ph = this.getHeight() - border - border; // panel height

			if (pw < iw || ph < ih) {

				/*
				 * compare some ratios and then decide which side of image to
				 * anchor to panel and scale the other side (this is all based
				 * on empirical observations and not at all grounded in theory)
				 */

				// System.out.println("pw/ph=" + pw/ph + ", iw/ih=" + iw/ih);

				if ((pw / ph) > (iw / ih)) {
					iw = -1;
					ih = ph;
				} else {
					iw = pw;
					ih = -1;
				}

				// prevent errors if panel is 0 wide or high
				if (iw == 0) {
					iw = -1;
				}
				if (ih == 0) {
					ih = -1;
				}

				scaledImage = image.getScaledInstance(new Float(iw).intValue(),
						new Float(ih).intValue(), Image.SCALE_DEFAULT);

			} else {
				scaledImage = image;
			}

			// System.out.println("iw = " + iw + ", ih = " + ih + ", pw = " + pw
			// + ", ph = " + ph);
		}
	}

	public class LoadImageWorker extends SwingWorker<Image, Integer> {

		private URI uri;

		public LoadImageWorker(URI uri) {
			this.uri = uri;
		}

		@Override
		protected Image doInBackground() throws Exception {

			Image image;

			if (uri == null) {
				return null;
			}

			File f = null;
			try {
				f = new File(uri);
			} catch (IllegalArgumentException ex) {
				return null;
			}

			if (f.length() > 9437184.0) {
				// > 9mb
				log.debug("Image too large to preview.");
				return null;
			}

			try {
				image = ImageIO.read(f);
			} catch (IOException ex) {
				return null;

			}

			try {
				imageWidth = image.getWidth(null);
				imageHeight = image.getHeight(null);

				if (imageWidth == -1) {
					log.debug("Image width not yet known");
				}

				double maxdim = 600;
				if (imageWidth > maxdim || imageHeight > maxdim) {

					log.debug("Large image - resampling");
					double ratio = (double) imageWidth / (double) imageHeight;

					log.debug("Original width = " + imageWidth);
					log.debug("Original height = " + imageHeight);
					log.debug("Ratio = " + ratio);

					int w = (int) maxdim;
					int h = (int) maxdim;
					if (imageWidth > imageHeight) {
						log.debug("Image wider than tall");
						w = (int) maxdim;
						h = (int) (maxdim / ratio);
					} else {
						log.debug("Image taller than wide");
						h = (int) maxdim;
						w = (int) (maxdim * ratio);
					}

					log.debug("Rescaling to " + w + " x " + h);
					log.debug("Replacing full size image with rescaled image");
					image = ImageIO.read(f).getScaledInstance(w, h,
							BufferedImage.SCALE_FAST);

				}
			} catch (Exception e) {
			}

			if (image == null) {
				return null;
			}

			return image;
		}

		@Override
		protected void process(List<Integer> chunks) {
			// Messages received from the doInBackground() (when invoking the
			// publish() method)
		}
	}
}
