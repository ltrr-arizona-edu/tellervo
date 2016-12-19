package org.tellervo.desktop.hardware.device;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HidTest implements HidServicesListener{
	private final static Logger log = LoggerFactory.getLogger(HidTest.class);

	  static final int PACKET_LENGTH = 32;
	  //static final short uparserVendorId = 0x7c0;
	  //static final short uparserProductId = 0x1501;
	  
	  static final short uparserVendorId = 0x24f0;
	  static final short uparserProductId = 0x137;
	  private HidDevice uparser = null;
	public HidTest(){
		
		    	// Get HID services
		    	HidServices hidServices = HidManager.getHidServices();
		    	hidServices.addHidServicesListener(this);

		    	// Provide a list of attached devices
		    	
		    	log.debug("List of devices: ");
		    	for (HidDevice hidDevice : hidServices.getAttachedHidDevices()) {
		    	  log.debug("   -"+hidDevice.toString());
		    	  
		    	  // 
		    	  if(hidDevice.getVendorId()==uparserVendorId && hidDevice.getProductId()==uparserProductId)
		    	  {
		    		  uparser = hidDevice;
		    	  }
		    	}

		    	
		    	if(uparser==null)
		    	{
		    		log.debug("Device is null");
		    	}
		    	
		    	/*log.debug("UParser device = "+uparser.toString());

		    	// Send the Initialise message
		    	byte[] message = new byte[64];
		    	message[0] = 0x3f;
		    	message[1] = 0x23;
		    	message[2] = 0x23;

		    	int val = uparser.write(message, 64, (byte) 0);
		    	if (val != -1) {
		    	  System.out.println("> [" + val + "]");
		    	} else {
		    	  System.err.println(uparser.getLastErrorMessage());
		    	}*/
				
		    	JFrame frame = new JFrame();
		    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    	JButton button = new JButton("Read");
		    	button.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(uparser==null)
						{
							log.debug("uParSer device not attached!");
							return;
						}
						
						// TODO Auto-generated method stub
						 byte data[] = new byte[PACKET_LENGTH];
						 int val = uparser.read(data, 500);
				          switch (val) {
				            case -1:
				              log.error(uparser.getLastErrorMessage());
				              break;
				            case 0:
				              log.debug("No more data");
				              break;
				            default:
				              System.out.print("< [");
				              for (byte b : data) {
				                System.out.printf(" %02x", b);
				              }
				              System.out.println("]");
				              break;
				          }
					}
		    		
		    	});
		    	
		    	frame.add(button);
		    	
		    	frame.setVisible(true);
		    	frame.pack();
		    	
		        // Prepare to read a single data packet
		        /*boolean moreData = true;
		        while (moreData) {
		          byte data[] = new byte[PACKET_LENGTH];
		          // This method will now block for 500ms or until data is read
		          val = uparser.read(data, 500);
		          switch (val) {
		            case -1:
		              log.error(uparser.getLastErrorMessage());
		              break;
		            case 0:
		              moreData = false;
		              log.debug("No more data");
		              break;
		            default:
		              System.out.print("< [");
		              for (byte b : data) {
		                System.out.printf(" %02x", b);
		              }
		              System.out.println("]");
		              break;
		          }
		        }*/

		    	
		    	

		    	
		    	
		    	
		    }

	@Override
	public void hidDeviceAttached(HidServicesEvent event) {
		log.debug("Device Attached");
		HidDevice device = event.getHidDevice();
		log.debug(device.toString());
		

		  if(device.getVendorId()==uparserVendorId && device.getProductId()==uparserProductId)
		  {
			  uparser = device;
			  log.debug("uParSer device attached");
		  }
	    	
		
	}

	@Override
	public void hidDeviceDetached(HidServicesEvent event) {
		log.debug("Device Dettached");
		HidDevice device = event.getHidDevice();

		  if(device.getVendorId()==uparserVendorId && device.getProductId()==uparserProductId)
		  {
			  uparser = null;
			  log.debug("uParSer device removed");
		  }
	}

	@Override
	public void hidFailure(HidServicesEvent event) {
		log.debug("Device failed");
		
	}
	
	public static void main(String[] args) {
		HidTest test = new HidTest();
		
	}
		
	
}
