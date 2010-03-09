package edu.cornell.dendro.corina.io;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

public class VelmexQC10SerialMeasuringDevice extends AbstractSerialMeasuringDevice{

	/*TODO
	 * implement reset serial writing
	 */
	
	private static final int EVE_ENQ = 5;
	private static final int EVE_ACK = 6;
	//private static final int EVE_NAK = 7;
	
	/** serial NUMBER of the last data point... */
	private int lastSerial = -1;
	
	public VelmexQC10SerialMeasuringDevice(String portName) throws IOException {
		super(portName);
		//setStopBits(SerialPort.STOPBITS_2);
		//setFlowControl(SerialPort.FLOWCONTROL_RTSCTS_OUT);
	}

	public VelmexQC10SerialMeasuringDevice() {
	}

	@Override
	protected boolean doesInitialization() {
		return true;
	}

	@Override
	public String getMeasuringDeviceName() {
		return "Velmex QC-10 IO device";
	}

	@Override
	protected void doInitialize() {
		boolean waiting_for_init = true;
		int tryCount = 0;
		
		while(waiting_for_init) {
			synchronized(this) {
				if(getState() == PortState.WAITING_FOR_ACK) {
					
					if(tryCount++ == 25) {
						fireSerialSampleEvent(SerialSampleIOEvent.ERROR, "Failed to initialize reader device.");
						System.out.println("init tries exhausted; giving up.");
						break;
					}
					
					try {
						System.out.println("Initializing reader, try " + tryCount + "...");
						fireSerialSampleEvent(SerialSampleIOEvent.INITIALIZING_EVENT, new Integer(tryCount));
						getPort().getOutputStream().write(EVE_ENQ);
					}
					catch (IOException e) {	}
				} else {
					waiting_for_init = false;
					continue;
				}
				
				// no response yet.. wait.
				try {
					this.wait(300);
				} catch (InterruptedException e) {}						
			}					
		}				
	}
	
	public void serialEvent(SerialPortEvent e) {
		if(e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			InputStream input;
			OutputStream output;
			
			try {
				input = getPort().getInputStream();
				//TODO remove the following debugging code
				
				String home = System.getProperty("user.home");
				
				if (!home.endsWith(File.separator))
					home = home + File.separator;
				
				//String path = 
				File f=new File(home+"outFile.txt");
			    OutputStream outToFile=new FileOutputStream(f,true);
			    				
			    byte buf[]=new byte[1024];
			    int len;
			    while((len=input.read(buf))==7)
			    {
			    	//Debug values to text file
			    	outToFile.write(buf,0,len);
			    	outToFile.close();
			    		
			    	
			    	// Read byte buffer into string
			    	String value = new String(buf);
			    	
			    	// Trim off blanks and last two characters (\n\r)
			    	value = value.substring(0, len-2);
			    	
			    	// Round up to integer of 1/100th mm
			    	Float fltValue = new Float(value)*100;
			    	Integer intValue = Math.round(fltValue);
			    	
			    	// Fire event
			    	fireSerialSampleEvent(SerialSampleIOEvent.NEW_SAMPLE_EVENT, intValue);
			    	
			    	//zero the data with "@3"
			    	try {
				    output = getPort().getOutputStream();
				    OutputStream outToPort=new DataOutputStream(output);
				    String strZeroDataCommand = "@3";
				    outToPort.write(strZeroDataCommand.getBytes());
				    
			    	}
			    	catch (IOException ioe) {
						System.out.println("Error writing to serial port: " + ioe);
			    	}			    	
			    }

			  
			}
	
			catch (IOException ioe) {
				System.out.println("Error reading from serial port: " + ioe);
			}
		}		
	}
}