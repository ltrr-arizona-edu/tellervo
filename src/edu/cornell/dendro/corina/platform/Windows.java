package edu.cornell.dendro.corina.platform;

import java.io.File;

public class Windows {
	private Windows() {}

	/**
	 * Open a file using rundll32's fileprotocol handler
	 * @param file
	 */
	public static void openFile(File file) {
		String[] cmdArray = {
				"rundll32",
				"url.dll,FileProtocolHandler",
				file.getAbsolutePath()
		};
		
		try {
			Runtime.getRuntime().exec(cmdArray);
		} catch (Exception e) {
		}
	}
}
