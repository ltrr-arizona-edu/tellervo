package edu.cornell.dendro.corina.gui.datawizard;

import edu.cornell.dendro.corina.site.TridasRadius;
import edu.cornell.dendro.corina.site.TridasObject;
import edu.cornell.dendro.corina.site.TridasSample;
import edu.cornell.dendro.corina.site.Subsite;
import edu.cornell.dendro.corina.site.TridasElement;

public class EditorPanelFactory {
    /**
     * Get an editor panel for this class
     * 
     * @param content
     * @return
     */
    public static BaseEditorPanel<?> createPanelForClass(Class<?> content) {
    	if(content.equals(TridasObject.class))
    		return (BaseEditorPanel<TridasObject>) new SiteEditorPanel();
    	if(content.equals(Subsite.class))
    		return (BaseEditorPanel<Subsite>) new SubsiteEditorPanel();
    	if(content.equals(TridasElement.class))
    		return (BaseEditorPanel<TridasElement>) new TreeEditorPanel();
    	if(content.equals(TridasSample.class))
    		return (BaseEditorPanel<TridasSample>) new SpecimenEditorPanel();
    	if(content.equals(TridasRadius.class))
    		return (BaseEditorPanel<TridasRadius>) new RadiusEditorPanel();
    	return null;
    }
}
