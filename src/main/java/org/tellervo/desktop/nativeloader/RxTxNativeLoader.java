package org.tellervo.desktop.nativeloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RxTxNativeLoader {
	private static final Logger log = LoggerFactory.getLogger(RxTxNativeLoader.class);

	
	public static void loadNativeLib() throws Exception
	{
		
		String lib = null;
		
		String os = System.getProperty("os.name");
		String arch = System.getProperty("os.arch");
		
		log.debug("OS = "+os);
		log.debug("Architecture = "+arch);
		
		if(os.startsWith("Windows"))
		{
			// The jpackage build (scripts/package-desktop.sh) already places rxtxSerial.dll
			// directly alongside the application jars and sets -Djava.library.path to that
			// directory, so RXTX's own driver classes (RXTXCommDriver, RXTXPort, etc.) load it
			// themselves via their usual System.loadLibrary("rxtxSerial") calls. Extracting the
			// copy embedded in our jar to a second, randomly-named temp file and System.load()-ing
			// it here would load a *second*, independent instance of the same native library into
			// the process. That has been confirmed (via a user-submitted hs_err_pid crash log) to
			// cause native EXCEPTION_ACCESS_VIOLATION crashes inside rxtxSerial.dll, since native
			// global/static state ends up split across the two loaded copies. So on Windows we
			// deliberately do nothing here and let RXTX load its own library exactly once.
			log.debug("Windows RXTX native library is bundled next to the app and resolved via java.library.path; skipping manual extraction to avoid a duplicate native library load");
			return;
		}
		else if(os.startsWith("MacOSX"))
		{
			if(arch.startsWith("PowerPC"))
			{
				throw new Exception(arch+" architecture is not supported");
			}
			else
			{
				lib = "/Libraries/macosx-universal";
			}

		}
		else if(os.startsWith("Linux"))
		{
			log.debug("Linux RXTX dependencies should be handled by native package manager");
			return;
			/*if(arch.equals("x86") || arch.equals("i386"))
			{
				lib = "/Libraries/linux-i586/librxtxSerial.so";
			}
			else if (arch.equals("x86-64") || arch.equals("amd64"))
			{
				lib = "/Libraries/linux-amd64/librxtxSerial.so";
			}
			else
			{
				throw new Exception(arch+" architecture is not supported");
			}*/
		}
		else
		{
			throw new Exception(os+" OS is not supported");
		}

		log.debug("Loading lib from "+lib);
		
		NativeUtils nu = new NativeUtils();
		nu.loadLibraryFromJar(lib);
		

	}
	
	
	  
}
