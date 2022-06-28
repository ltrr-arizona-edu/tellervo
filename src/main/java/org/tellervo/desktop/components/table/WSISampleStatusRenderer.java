/**
 * Created at Oct 1, 2010, 4:24:12 PM
 */
package org.tellervo.desktop.components.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.tridasv2.ui.DefaultCellRendererEx;
import org.tellervo.desktop.tridasv2.ui.TellervoPropertySheetPanel;
import org.tellervo.schema.SampleStatus;



public class WSISampleStatusRenderer extends DefaultCellRendererEx {
	private final static Logger log = LoggerFactory.getLogger(WSISampleStatusRenderer.class);

	private static final long serialVersionUID = 1L;

	/**
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object argValue) {
		
		if(argValue==null)
		{
			return "";
		}
		
		
		SampleStatus status= null;
		if(argValue.getClass().equals(SampleStatus.class))
		{
			status = (SampleStatus) argValue;
		}
		else if (argValue.getClass().equals(String.class))
		{
			String strval = (String) argValue;
			
			if(strval.trim().length()==0)
			{
				
			}
			else
			{
				try{
					status = SampleStatus.fromValue((String) argValue);
				} catch (Exception e)
				{
					log.error("Invalid SampleStatus");
	
				}
			}
		}
		else
		{
			log.error("Unsupported data type");
		}
		
		
		
		if(status == null){
			return "";
		}
		return status.value();
	}
}
