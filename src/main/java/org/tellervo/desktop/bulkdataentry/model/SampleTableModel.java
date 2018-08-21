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
ss * Created at Aug 23, 2010, 3:35:03 AM
 */
package org.tellervo.desktop.bulkdataentry.model;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.UserExtendableDataType;
import org.tellervo.schema.UserExtendableEntity;
import org.tellervo.schema.WSIBoxDictionary;
import org.tellervo.schema.WSISampleStatusDictionary;
import org.tellervo.schema.WSISampleTypeDictionary;
import org.tellervo.schema.WSIUserDefinedField;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.odk.ODKParser;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tridas.schema.Date;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.model.MVCArrayList;


/**
 * @author Daniel
 *
 */
public class SampleTableModel extends AbstractBulkImportTableModel {
	private static final long serialVersionUID = 2L;
	private static final Logger log = LoggerFactory.getLogger(SampleTableModel.class);

	public SampleTableModel(SampleModel argModel){
		super(argModel);
	}
	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.AbstractBulkImportTableModel#getColumnClass(java.lang.String)
	 */
	public Class<?> getColumnClass(String argColumn){
		if(argColumn.equals(SingleSampleModel.TYPE)){
			return WSISampleTypeDictionary.class;
		}else if(argColumn.equals(SingleSampleModel.IMPORTED)){
			return ImportStatus.class;
		}else if(argColumn.equals(SingleSampleModel.SAMPLING_DATE)){
			return String.class;
		}else if(argColumn.equals(SingleSampleModel.KNOTS)){
			return Boolean.class;
		}else if(argColumn.equals(SingleRadiusModel.AZIMUTH)){
			return BigDecimal.class;
		}else if(argColumn.equals(SingleSampleModel.ELEMENT)){
			return TridasElementOrPlaceholder.class;
		}else if(argColumn.equals(SingleSampleModel.BOX)){
			return WSIBoxDictionary.class;
		}else if(argColumn.equals(SingleSampleModel.OBJECT)){
			return TridasObjectOrPlaceholder.class;
		}else if (argColumn.equals(SingleSampleModel.FILES)){
			return TridasFileList.class;
		}else if (argColumn.equals(SingleSampleModel.SAMPLE_STATUS))
		{
			return WSISampleStatusDictionary.class;
		}
		
		
		// Get data type of user defined fields
		
		
		
		
		MVCArrayList<WSIUserDefinedField> udfdictionary = App.dictionary.getMutableDictionary("userDefinedFieldDictionary");
		for(WSIUserDefinedField fld : udfdictionary)
		{
			if(fld.getAttachedto().equals(UserExtendableEntity.SAMPLE))
			{
				if(fld.getLongfieldname().equals(argColumn))
				{
					UserExtendableDataType dt = fld.getDatatype();
					if(dt.equals(UserExtendableDataType.XS___STRING))
					{
						return String.class;
					}
					else if(dt.equals(UserExtendableDataType.XS___BOOLEAN))
					{
						return Boolean.class;
					}
					else if(dt.equals(UserExtendableDataType.XS___FLOAT))
					{
						return Float.class;
					}
					else if(dt.equals(UserExtendableDataType.XS___INT))
					{
						return Integer.class;
					}
				}
			}
		}
		
		//log.debug("Looking for data type of field "+argColumn);

		return String.class;
	}
	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.AbstractBulkImportTableModel#setValueAt(java.lang.Object, java.lang.String, org.tellervo.desktop.bulkdataentry.model.IBulkImportSingleRowModel, int)
	 */
	@Override
	public void setValueAt(Object argAValue, String argColumn, IBulkImportSingleRowModel argModel, int argRowIndex) {
		
		
		// FIXME this all should be in a command!!!
		if(argColumn.equals(SingleSampleModel.OBJECT))
		{		
			
			final SingleSampleModel ssm = (SingleSampleModel) argModel;
			ssm.getPossibleElements().clear();
			
			if(argAValue==null)
			{
				argModel.setProperty(argColumn, argAValue);
				return;
			}
			
			if (argAValue instanceof String)
			{
				// Not a proper object only a code
				
				TridasObjectOrPlaceholder toph = new TridasObjectOrPlaceholder((String) argAValue);
				
				MVCArrayList<TridasObjectEx> objectList = App.tridasObjects.getMutableObjectList();
				for(TridasObjectEx obj: objectList)
				{
					if(obj.getLabCode().equals(argAValue))
					{
						toph = new TridasObjectOrPlaceholder(obj);
						break;
					}
				}

				argAValue = toph;
			}
						
			if(argAValue instanceof TridasObjectOrPlaceholder)
			{					
				TridasObjectOrPlaceholder toph = (TridasObjectOrPlaceholder) argAValue;
				
				if(toph.getTridasObject()!=null)
				{
					argModel.setProperty(argColumn, argAValue);
					final TridasObject o = toph.getTridasObject();
					
					Thread t = new Thread(new Runnable() {
						
						@Override
						public void run() {
							SearchParameters param = new SearchParameters(SearchReturnObject.ELEMENT);
					    	param.addSearchConstraint(SearchParameterName.OBJECTID, SearchOperator.EQUALS, o.getIdentifier().getValue().toString());
	
					    	EntitySearchResource<TridasElement> resource = new EntitySearchResource<TridasElement>(param, TridasElement.class);
							resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
							
							TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(BulkImportModel.getInstance().getMainView(), resource);
							resource.query();	
							dialog.setVisible(true);
							
							if(!dialog.isSuccessful()) 
							{ 
								System.out.println("Error getting elements");
								return;
							}
							
							List<TridasElement> elList = resource.getAssociatedResult();
							ssm.getPossibleElements().addAll(elList);
						}
					}, "Elements Fetch Thread");
					
					t.start();
				}
				else	
				{
					argModel.setProperty(argColumn, argAValue);
				}
			}

			return;
		}
		
		else if (argColumn.equals(SingleSampleModel.ELEMENT))
		{
			final SingleSampleModel ssm = (SingleSampleModel) argModel;
			final String col = argColumn;
			String val2 = null;
			
			if(argAValue==null)
			{
				argModel.setProperty(argColumn, argAValue);
				return;
			}
			
			if(argAValue instanceof String)
			{
				TridasElementOrPlaceholder teop = new TridasElementOrPlaceholder((String) argAValue);
				argAValue = teop;
				
			}
			
			if(argAValue instanceof TridasElementOrPlaceholder)
			{
				TridasElementOrPlaceholder teop = (TridasElementOrPlaceholder) argAValue;
				
				if(teop.getTridasElement()!=null)
				{
					if(!((TridasElement)teop.getTridasElement()).isSetIdentifier())
					{
						val2 = ((TridasElement) argAValue).getTitle();
					}
				}
				else
				{
					val2 = teop.getCode();
				}
				
				final String val = val2;


				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						int i=0;
						while(!ssm.isElementListReady())
						{
							try {
								Thread.sleep(10);
								i++;	
								if(i>500) return;
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return;
							}

						}
						
						// Possible element list is populated so try and find item in list
						for(TridasElement el : ssm.getPossibleElements())
						{
							if(el.getTitle().equals(val))
							{
								ssm.setProperty(col, el);
							}
						}
						return;
					}
				}, "Element populate thread");
				
				t.start();
				
				
				
			}
		}
		
		argModel.setProperty(argColumn, argAValue);
	}
}
