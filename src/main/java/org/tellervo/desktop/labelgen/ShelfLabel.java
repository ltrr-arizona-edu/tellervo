package org.tellervo.desktop.labelgen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ShelfLabel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ShelfLabelStyle ls = new ShelfLabelStyle();
		//ShelfEndLabelStyle ls = new ShelfEndLabelStyle();
		
		ArrayList<String> items = new ArrayList<String>();
		

		  try {
			  File file = new File("/home/pbrewer/shelflabels2.txt"); 
			  //File file = new File("/home/pbrewer/shelfendlabels.txt"); 

			  BufferedReader br = new BufferedReader(new FileReader(file)); 
			  
			  String st; 
			  
			while ((st = br.readLine()) != null) 
				  items.add(st);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		  

		
		File outputFile;
		try {
			//outputFile = File.createTempFile("shelf", ".pdf");
			outputFile = new File("/tmp/shelf.pdf");
			//outputFile.deleteOnExit();
			FileOutputStream output = new FileOutputStream(outputFile);
		
			ls.outputPDFToStream(output, items);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}

}
