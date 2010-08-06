package edu.cornell.dendro.corina.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * A class that implements the Java FileFilter interface.
 */
public abstract class AbstractDendroReaderFileFilter extends FileFilter
{

  public boolean accept(File file)
  {
	  return true;
  }
  
}

