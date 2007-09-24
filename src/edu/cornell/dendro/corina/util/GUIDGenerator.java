package edu.cornell.dendro.corina.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;

public class GUIDGenerator {
	
	private static String myAddr = getInetAddress();
	private static String myHash = Integer.toHexString(new Object().hashCode());
	private static Random myRandomState = new Random(new SecureRandom().nextLong());
	
	public static String makeGUID() {
		StringBuffer guid = new StringBuffer();
		
		guid.append("-");
		guid.append(myAddr);
		guid.append("-");
		guid.append(myHash);
	
		// pad to 32 hex bytes at the front
		int padsz = (32 -  guid.length()) / 2;
		if(padsz > 0) {
			byte[] padding = new byte[padsz];
			
			myRandomState.nextBytes(padding);
			guid.insert(0, bytesToHex(padding));
		}
	
		return guid.toString();
	}
	
	// get our IP, or a random number if that fails
	private static String getInetAddress() {
		InetAddress addr = null;
		byte[] byteAddr = null;

		// try and get our local host.
		try {
			addr = InetAddress.getLocalHost();
			System.out.println("Local IP: " + addr);
			if(addr.isLoopbackAddress())
				throw new UnknownHostException();
			byteAddr = addr.getAddress();
		} catch (UnknownHostException e) {
			// no host? let's make up something random
			byteAddr = new byte[4];
			new Random().nextBytes(byteAddr);
		}

		return bytesToHex(byteAddr);
	}
	
	private static String bytesToHex(byte bytes[]) {
        StringBuffer hexstr = new StringBuffer(bytes.length * 2);
        if (bytes != null) {
            for (int i = 0; i < bytes.length; i++) {
                String hexbyte = Integer.toHexString(bytes[i] & 0xFF);
                if (hexbyte.length() % 2 == 1) {
                    hexbyte = "0" + hexbyte;
                }
                hexstr.append(hexbyte);
            }
        }
        return hexstr.toString();
    }
	
}
