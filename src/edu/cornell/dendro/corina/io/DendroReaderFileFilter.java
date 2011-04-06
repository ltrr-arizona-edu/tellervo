package edu.cornell.dendro.corina.io;


public class DendroReaderFileFilter extends AbstractDendroReaderFileFilter
  {
  	  private String name;
	  public DendroReaderFileFilter(String name)
	  {
		this.name = name;  
	  }
	  
	  public String getDescription()
	  {
		  return name;
	  }
	  
  }