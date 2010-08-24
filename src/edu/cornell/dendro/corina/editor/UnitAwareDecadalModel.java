package edu.cornell.dendro.corina.editor;

import org.tridas.schema.NormalTridasUnit;

import edu.cornell.dendro.corina.sample.Sample;

public class UnitAwareDecadalModel extends DecadalModel {


	private static final long serialVersionUID = 1L;
	private NormalTridasUnit displayUnits = NormalTridasUnit.HUNDREDTH_MM;
	
	public UnitAwareDecadalModel() {
	
	}

	public UnitAwareDecadalModel(Sample s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	public void setDisplayUnits(NormalTridasUnit unit)
	{
		displayUnits = unit;
		
	}
	
	public NormalTridasUnit getDisplayUnits()
	{
		return displayUnits;
	}
	
	public Object getValueAt(int row, int col) {
		
		Object obj = super.getValueAt(row, col);
		
		if(obj==null) {
			return obj;
		}
		
		
		if (col==0 || col==11)
		{
			// Year or count column so just return
			return obj;
		}
		else
		{
			Number val = (((Number) (obj)).intValue());
			
			if(val!=null) 
			{
				if (s.getTridasUnits()==null)
				{
					// No units so leave values as they are
				}
				
				else if (displayUnits.equals(NormalTridasUnit.HUNDREDTH_MM))
				{
					val = val.intValue() / 10;
				}
				else if (displayUnits.equals(NormalTridasUnit.MICROMETRES))
				{
					// do nothing as microns is the internal default units
				}
				else
				{
					System.out.println("Unsupported display units. Ignoring and defaulting to microns");
				}
				return val;

			}
			else
			{
				return obj;
			}
		}
	}
	
	public void setValueAt(Object value, int row, int col) {
	
		if (value==null)
		{
			// Ignore null values
			return;
		}
		
		else if (col==0 || col==11)
		{
			// First and last columns don't need to be unit handled
			super.setValueAt(value, row, col);
		}
		
		else
		{
			Number val = (((Number) (value)).intValue());
			
			if(val!=null) 
			{
				if (displayUnits.equals(NormalTridasUnit.HUNDREDTH_MM))
				{
					val = val.intValue() * 10;
				}
				else if (displayUnits.equals(NormalTridasUnit.MICROMETRES))
				{
					// do nothing as microns is the internal default units
				}
				else
				{
					System.out.println("Unsupported display units. Ignoring and defaulting to microns");
				}
				super.setValueAt(val, row, col);

			}
			else
			{
				super.setValueAt(value, row, col);
			}
		}
		
	}
	
}
