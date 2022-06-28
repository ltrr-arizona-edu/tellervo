/**
 * Created at Oct 1, 2010, 4:24:12 PM
 */
package org.tellervo.desktop.components.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.tridasv2.ui.DefaultCellRendererEx;
import org.tellervo.desktop.tridasv2.ui.TellervoPropertySheetPanel;
import org.tellervo.schema.CurationStatus;



public class WSICurationStatusRenderer extends DefaultCellRendererEx {
	private final static Logger log = LoggerFactory.getLogger(WSICurationStatusRenderer.class);

	private static final long serialVersionUID = 1L;

	/**
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object argValue) {
		
		log.debug("Rendering value with WSICurationStatusRenderer");

		
		if(argValue==null)
		{
			log.debug("Value is null");
			return "";
		}

		log.debug("Value = "+argValue+ " of class "+argValue.getClass().getSimpleName());
		
		CurationStatus status= null;
		if(argValue.getClass().equals(CurationStatus.class))
		{
			status = (CurationStatus) argValue;
			
			
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
					status = CurationStatus.fromValue((String) argValue);
				} catch (Exception e)
				{	
					log.error("Invalid CurationStatus '"+argValue+"'");
	
				}
			}
		}
		else
		{
			log.error("Unsupported data type: "+argValue.getClass().getSimpleName());
		}
		
		
		
		if(status == null){
			return "";
		}
		return status.toString();
	}
}
