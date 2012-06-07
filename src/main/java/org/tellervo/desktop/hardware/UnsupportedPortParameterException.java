package org.tellervo.desktop.hardware;

public class UnsupportedPortParameterException extends Exception {

	private static final long serialVersionUID = 1L;
	private final String parameter;
	private final String value;
	
	public UnsupportedPortParameterException(String parameter, String value)
	{
		this.parameter = parameter;
		this.value = value;
	}
	
	public String getParameter()
	{
		return parameter;
	}
	
	@Override
	public String getMessage()
	{
		return "Value of '"+value+ "' is unsupported for the port parameter '"+parameter+"'";
	}
	
}
