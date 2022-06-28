/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GUIDGenerator {
	
	private final static Logger log = LoggerFactory.getLogger(GUIDGenerator.class);
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
			log.debug("Local IP: " + addr);
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
