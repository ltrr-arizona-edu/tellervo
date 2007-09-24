/**
 * 
 */
package edu.cornell.dendro.corina.io;

import java.io.IOException;

/**
 * Provides a native spawning method.
 * This allows us to open a win32 console application from java, which is *impossible* with the exec command.
 * 
 * @author Lucas Madar
 */
public class NativeSpawn {

	private NativeSpawn() {
		System.loadLibrary("NativeSpawn");
	}
	
	public static void spawnConsoleProcess(String command, String workingDirectory, String consoleTitle) throws IllegalArgumentException, IOException {
		new NativeSpawn().doConsoleSpawn(command, workingDirectory, consoleTitle);		
	}
	
	public static void spawnCofecha(String command, String workingDirectory, String inFilename, String outFilename) throws IllegalArgumentException, IOException {
		new NativeSpawn().doCofechaSpawn(command, workingDirectory, inFilename, outFilename);		
	}
	
	private native void doConsoleSpawn(String command, String workingDirectory, String consoleTitle) throws IllegalArgumentException, IOException;	
	private native void doCofechaSpawn(String command, String workingDirectory, String inFilename, String outFilename) throws IllegalArgumentException, IOException;
	
}
