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
		
		log.debug("Attempting to load RXTX native library");
		log.debug("OS = "+os);
		log.debug("Architecture = "+arch);
		
		if(os.startsWith("Windows"))
		{
			if(arch.equals("x86"))
			{
				lib = "/Libraries/windows-i586/rxtxSerial.dll";
			}
			else if (arch.equals("x86-64"))
			{
				lib = "/Libraries/windows-amd64/rxtxSerial.dll";
			}
			else
			{
				throw new Exception(arch+" architecture is not supported");
			}
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
			if(arch.equals("x86") || arch.equals("i386"))
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
			}
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
