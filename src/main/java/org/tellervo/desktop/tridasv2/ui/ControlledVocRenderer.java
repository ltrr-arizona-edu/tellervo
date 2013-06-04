/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
/**
 * 
 */
package org.tellervo.desktop.tridasv2.ui;

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

	@SuppressWarnings("unchecked")
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
