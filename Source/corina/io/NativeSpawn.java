/**
 * 
 */
package corina.io;

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
	
	public static void spawnConsoleProcess(String command, String workingDirectory, String consoleTitle) throws IllegalArgumentException {
		new NativeSpawn().doConsoleSpawn(command, workingDirectory, consoleTitle);		
	}
	
	private native void doConsoleSpawn(String command, String workingDirectory, String consoleTitle) throws IllegalArgumentException;
}
