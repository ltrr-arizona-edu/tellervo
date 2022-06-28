package org.tellervo.desktop.wsi.tellervo;

/**
 * Class for translating generic SQL exceptions into meaningful messages for users
 * 
 * @author pbrewer
 *
 */
public class FriendlyExceptionTranslator {

	private enum DBUniqueConstraint{
		
		UNIQ_PARENTOBJECT_CODE("unique_parentobject-code", "An element with this code already exist for this object."),
		UNIQ_BOXTITLE("uniq_boxtitle", "A box with this name already exists.  Box names must be unique."),
		UNIQ_PARENT_TITLE("unique_parent-title", "An object with this name already exists.  Object names must be unique."),
		UNIQ_TBLPROJECT_UNIQTITLE("tblproject-uniqtitle", "A project with this name already exists.  Project names must be unique."),
		//UNIQ_PROJECTPROJECTTYPE("uniq_projectprojecttype", ""),
		UNIQ_PARENTSAMPLE_CODE("unique_parentsample-code", "A radius with this code already exists for this sample."),
		//uniq_readingreadingnote_notesperreading
		//uniq_redate_vmeasurement
		UNIQ_PARENTELEMENT_CODE("unique_parentelement-code", "An sample with this code already exists for this element."),
		//uniq_defaultperm
		//uniq-element-group-permission
		//uniq_securitygroupmembership_parentchildsecuritygroups
		//uniq-object-group-permission
		UNIQ_SECURITYUSER_USERNAME("uniq_securityuser_username", "A user with this username already exists."),
		//uniq_securityvmeasurement
		//uniq_supportclient
		//uniq_truncate_vmeasurement
		//only_unique_derivations
		//uniq_vmeasurementgroup_members
		//uniq_tagforvmeasurement
		//uniq_curationstatus
		//uniq_datecertainty
		//uniq_tlkpdomain
		//tlkpobjecttype-nodupsinvocab
		//tlkpprojectcategory-nodupsinvocab
		//tlkpprojecttype-nodupsinvocab
		//uniq_sampletype_label
		//uniq_taxon-label
		//uniq_rankorder
		//uniq_taxonrank
		//uniq_unit
		//uniq_vmeasurementop_name
		//wmsserver-uniqname
		//wmsserver-uniqurl
		;

		private String code;
		private String description;
		
		DBUniqueConstraint(String code, String description) {
			
			this.code = code;
			this.description = description;
		}
		
		/**
		 * Get the description for this DBConstraint.
		 */
		@Override
		public String toString() {
			
			return description;
		}
		
		/**
		 * Create a DBConstraint from a code. If there is no DBConstraint that matches the string then null is returned.
		 * 
		 * @param name
		 * @return
		 */
		public static DBUniqueConstraint fromCode(String code) {
			
			for (DBUniqueConstraint type : DBUniqueConstraint.values())
			{
				if (type.code.equals(code))
					return type;
			}
			
			return null;
		}
		
	}
	
private enum DBCheckConstraint{
		
		CHK_CONFIDENCE_MAX("chk_confidence-max", "Maximum confidence rating for a crossdate is 5."),
		CHK_CONFIDENCE_MIN("chk_confidence-min", "Minimim confidence rating for a crossdate is 0."),
		CHK_PRECISIONPOSITIVE("check_precisionpositive", "Location precision must be positive."),
		//CHK_ENFORCE_DIMS_LOCATIOIN("enforce_dims_location", "There must be 2 dimensions to a location"),
		CHK_ENFORCE_UNITS("enforce_units", "When the dimensions of an element are supplied the units must also be specified."),
		CHK_ENFORCE_VALID_DIMENSIONS("enforce_valid_dimensions", "Missing one or more dimension values.  Either: height and diameter; or height, width and depth must be specified."),
		//chk_powerIsMicrons
		//chk_unitsAreMicrons
		//enforce_dims_objectextent
		CHK_ENFORCE_OBJ_UNIQUEPARENTS("enforce_obj_uniqueparents", "An object can not be it's own parent!"),
		//enforce_srid_objectextent
		//enforce_samplingyearwithmonth
		//enforce_tblsample_samplemonthbound
		//enforce_tblsample_sampleyearbound
		//enforce_tblsample_sampleyearbound2
		//enforce_dims_vmextent
		//enforce_srid_vmextent
		;

		private String code;
		private String description;
		
		DBCheckConstraint(String code, String description) {
			
			this.code = code;
			this.description = description;
		}
		
		/**
		 * Get the description for this DBCheckConstraint.
		 */
		@Override
		public String toString() {
			
			return description;
		}
		
		/**
		 * Create a DBCheckConstraint from a code. If there is no DBCheckConstraint that matches the string then null is returned.
		 * 
		 * @param name
		 * @return
		 */
		public static DBCheckConstraint fromCode(String code) {
			
			for (DBCheckConstraint type : DBCheckConstraint.values())
			{
				if (type.code.equals(code))
					return type;
			}
			
			return null;
		}
		
	}
	
	
	public static Exception translate(Exception e)
	{
		
		
		String msg = e.getLocalizedMessage();

		if(e instanceof WebInterfaceException)
		{
			WebInterfaceException wie = (WebInterfaceException) e;
			
			if(wie.getMessageCode().equals(WebInterfaceCode.UNIQUE_CONSTRAINT_VIOLATION) || wie.getMessageCode().equals(WebInterfaceCode.GENERIC_SQL)) 
			{	
				for(DBUniqueConstraint constraint : DBUniqueConstraint.values())
				{
					if(msg.contains(constraint.code)) return new Exception (constraint.description);
				}
			}
			
			if(wie.getMessageCode().equals(WebInterfaceCode.CHECK_CONSTRAINT_VIOLATION) || wie.getMessageCode().equals(WebInterfaceCode.GENERIC_SQL)) 
			{	
				for(DBCheckConstraint constraint : DBCheckConstraint.values())
				{
					if(msg.contains(constraint.code)) return new Exception (constraint.description);
				}
			}
			
			if(wie.getMessageCode().equals(WebInterfaceCode.INVALID_XML_REQUEST) || wie.getMessageCode().equals(WebInterfaceCode.GENERIC_SQL))
			{
				return new Exception("The request sent to the server was invalid.  This typically means mandatory fields were missing.");
				
			}
		}

		return e;
	}
	
}
