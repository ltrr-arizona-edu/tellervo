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
package org.tellervo.desktop.util.labels.ui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.tellervo.schema.WSIBox;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.tridasv2.LabCode;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;


public class TridasListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 5452092569212454680L;

	public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        if(value instanceof WSIBox) {
            value = ((WSIBox) value).getTitle();
        }
        
        if(value instanceof TridasProject) {
        	
        	TridasProject p =((TridasProject) value);
        	String s;
            s = p.getTitle();
            if(p.getInvestigator()!=null){
            	s+=" - "+p.getInvestigator();
            }
            
            value = s;
        }
        
        if(value instanceof TridasMeasurementSeries){
        	TridasMeasurementSeries ms = (TridasMeasurementSeries) value;
        	TridasGenericField f = GenericFieldUtils.findField(ms, "tellervo.internal.labcodeText");
                	
        	value = (f != null) ? f.getValue() : ms.getTitle();
        }
        
        if(value instanceof TridasRadius){
        	TridasRadius r = (TridasRadius) value;
        	TridasGenericField f = GenericFieldUtils.findField(r, "tellervo.internal.labcodeText");
                	
        	value = (f != null) ? f.getValue() : r.getTitle();
        }
        
        if(value instanceof TridasSample){
        	TridasSample s = (TridasSample) value;
               
        	LabCode labcode = new LabCode();
        	boolean set = false;
        	if(GenericFieldUtils.findField(s, "tellervo.objectLabCode")!=null)
        	{
        		labcode.appendSiteCode(GenericFieldUtils.findField(s, "tellervo.objectLabCode").getValue());
            	if(GenericFieldUtils.findField(s, "tellervo.elementLabCode")!=null)
            	{
            		labcode.setElementCode(GenericFieldUtils.findField(s, "tellervo.elementLabCode").getValue());
                	labcode.setSampleCode(s.getTitle());
                	set=true;
                	
            	}
        	}

        	if(set)
        	{
        		value = LabCodeFormatter.getRadiusPrefixFormatter().format(labcode);
        	}
        	else
        	{
	        	TridasGenericField f = GenericFieldUtils.findField(s, "tellervo.internal.labcodeText");
	        	value = (f != null) ? f.getValue() : s.getTitle();
        	}
        }
        
        if(value instanceof TridasElement){
        	TridasElement e = (TridasElement) value;
        	TridasGenericField f = GenericFieldUtils.findField(e, "tellervo.internal.labcodeText");
                	
        	value = (f != null) ? f.getValue() : e.getTitle();
        } 
        
        /*else
        {
        	System.out.println("The object type "+ value.getClass().toString()+" is unsupported in TridasListCellRenderer");
        }*/


        return super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);
    } 
	
	
}
