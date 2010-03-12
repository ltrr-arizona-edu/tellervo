package edu.cornell.dendro.corina.editor;

import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.TridasUnit;

import edu.cornell.dendro.corina.sample.Sample;

public class UnitAwareDecadalModel extends DecadalModel {

	
	private NormalTridasUnit displayUnits = NormalTridasUnit.HUNDREDTH_MM;
	
	public UnitAwareDecadalModel() {
		// TODO Auto-generated constructor stub
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
				if (displayUnits.equals(NormalTridasUnit.HUNDREDTH_MM))
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
	
		if (col==0 || col==11)
		{
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
