/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
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
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
/**
 * Created at Oct 8, 2010, 12:52:44 PM
 */
package org.tellervo.desktop.bulkdataentry.view;

import java.beans.PropertyVetoException;
import java.util.GregorianCalendar;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tridas.schema.Certainty;
import org.tridas.schema.Date;

import com.michaelbaranov.microba.calendar.DatePicker;
import com.michaelbaranov.microba.calendar.DatePickerCellEditor;
import com.michaelbaranov.microba.calendar.ui.DatePickerUI;

/**
 * Very coupled and specific, this is here so the editor is specific to each
 * row.
 * 
 * @author pbrewer
 *
 */
public class DateEditor extends DefaultCellEditor {

	private final static Logger log = LoggerFactory.getLogger(DateEditor.class);

	/**
	 * Constructor.
	 * <p>
	 * Note: you probably will want to set the property of the
	 * {@link DatePicker} {@value DatePicker#PROPERTY_NAME_DROPDOWN_FOCUSABLE}
	 * to <code>false</code> before using it to construct
	 * {@link DatePickerCellEditor}.
	 * 
	 * @param datePicker
	 *            the editor component
	 */
	public DateEditor(final DatePicker datePicker) {
		// trick: supply a dummy JCheckBox
		super(new JCheckBox());
		// get back the dummy JCheckBox
		JCheckBox checkBox = (JCheckBox) this.editorComponent;
		// remove listeners installed by super()
		checkBox.removeActionListener(this.delegate);
		// replace editor component with own
		this.editorComponent = datePicker;

		// set simple look
		((DatePickerUI) datePicker.getUI()).setSimpeLook(true);

		// replace delegate with own
		this.delegate = new EditorDelegate() {
			
			private static final long serialVersionUID = 1L;

			public void setValue(Object value) {
				try {
					java.util.Date v = schemaDateToJavaDate((Date) value);
					if(v!=null){
						log.debug("Setting date in component to "+ v.toString());
					}else
					{
						log.debug("Setting date to null");
					}
					
					((DatePicker) editorComponent).setDate(v);
					
					log.debug("DatePicker says: "+((DatePicker) editorComponent).getDate());	
					
				} catch (PropertyVetoException e) {
				}
			}

			public Object getCellEditorValue() {
				return javaDateToSchemaDate(((DatePicker) editorComponent).getDate());
			}

			public void cancelCellEditing() {
				((DatePicker) editorComponent).commitOrRevert();
				super.cancelCellEditing();
			}

			public boolean stopCellEditing() {
				((DatePicker) editorComponent).commitOrRevert();
				return super.stopCellEditing();
			}

		};
		// install listeners
		datePicker.addActionListener(delegate);
		// do not set it to 1
		setClickCountToStart(2);
	}

	public static Date javaDateToSchemaDate(java.util.Date dataFromGUI)
	{
		if(dataFromGUI==null) {
			log.debug("Date provided to javaDateToSchemaDate was null");
			return null;
		}
		
		GregorianCalendar c = new GregorianCalendar();		
		c.setTime(dataFromGUI);
		XMLGregorianCalendar xmlgc;
		xmlgc = App.datatypeFactory.newXMLGregorianCalendar(c);
		xmlgc.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
		org.tridas.schema.Date d = new org.tridas.schema.Date();
		d.setValue(xmlgc);
		d.setCertainty(Certainty.EXACT);
		return d;
	}

	public static java.util.Date schemaDateToJavaDate(Date date)
	{
		if(date==null){
			log.debug("Date provided to schemaDateToJavaDate was null");
			return null;
		}
		return date.getValue().toGregorianCalendar().getTime();
		
	}
}
