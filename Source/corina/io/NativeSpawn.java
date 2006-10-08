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
	
	public static void spawnProcess(String command, String workingDirectory) throws IOException {
		new NativeSpawn().doSpawn(command, workingDirectory);		
	}
	
	private native void doSpawn(String command, String workingDirectory) throws IOException;
}
