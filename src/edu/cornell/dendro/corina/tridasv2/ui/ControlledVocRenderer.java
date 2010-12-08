/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.ui;

import java.util.ArrayList;

import org.tridas.schema.ControlledVoc;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;

/**
 * renderer for a tridas controlled vocabulary
 * 
 * Contains a dictionary that specifies specific behaviors
 * for different enums, based on qname
 * 
 * @author Lucas Madar
 */
public class ControlledVocRenderer extends DefaultCellRenderer {
	private static final long serialVersionUID = 1L;

	public enum Behavior {
		/** 
		 * if normal is not null<br>
		 *    normal (<i>normalStandard</i>)<br>
		 * <b>else</b><br>
		 *    value
		 */
		DEFAULT,
		/** normal (<i>normalStandard</i>) */
		NORMAL,
		/** normal */
		NORMAL_ONLY,
		/** value */
		VALUE
	}
	
	/** Our behavior */
	private final Behavior behavior;
	
	public ControlledVocRenderer() {
		this(Behavior.DEFAULT);
	}
	
	public ControlledVocRenderer(Behavior behavior) {
		this.behavior = behavior;
	}

	@Override
	protected String convertToString(Object value) {
		
		if(value instanceof ArrayList)
		{
			return ((ArrayList)value).get(0).toString();
		}
		
		ControlledVoc voc = null;
		try{
		 voc = (ControlledVoc) value;
		} catch (Exception e)
		{
			System.out.println("Unable to convert value" + value);
			System.out.println("Class " + value.getClass());
			return value.toString();
		}
		
		if(value == null){
			return "";
		}
		
		switch(behavior) {
		case DEFAULT: {
			if(voc.isSetNormal()) {
				if(voc.isSetNormalStd())
					return "<html>" + voc.getNormal() + " (<i>" + voc.getNormalStd() + "</i>)";
				
				return voc.getNormal();
			}
			return voc.getValue();
		}
		
		case NORMAL: {
			if(voc.isSetNormal()) {
				if(voc.isSetNormalStd())
					return "<html>" + voc.getNormal() + " (<i>" + voc.getNormalStd() + "</i>)";
				
				return voc.getNormal();
			}
			return "";
		}

		case NORMAL_ONLY:
			if(voc.isSetNormal())
				return voc.getNormal();
			return "";
			
		case VALUE:
			if(voc.isSetValue())
				return voc.getValue();	
			return "";
		}

		
		return super.convertToString(value);
	}
}
