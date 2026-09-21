package org.tellervo.desktop.nativeloader;

import com.fazecast.jSerialComm.SerialPort;

/**
 * Standalone entry point invoked by the packaging CI (desktop-packaging.yml) after installing
 * the packaged app, to confirm jSerialComm's native library actually extracts and initializes
 * against the installed app's bundled runtime/jars, rather than only checking that a DLL file
 * exists on disk (which is what let the RXTX 64-bit pointer-truncation crash ship unnoticed).
 */
public class SerialLibrarySmokeTest {

	public static void main(String[] args) {
		SerialPort[] ports = SerialPort.getCommPorts();
		System.out.println("Serial library loaded OK; " + ports.length + " port(s) detected.");
	}

}
