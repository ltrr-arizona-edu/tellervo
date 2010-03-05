package edu.cornell.dendro.corina.gui.datawizard;

import edu.cornell.dendro.corina.tridas.Subsite;
import edu.cornell.dendro.corina.tridas.TridasElement;
import edu.cornell.dendro.corina.tridas.TridasObject;
import edu.cornell.dendro.corina.tridas.TridasRadius;
import edu.cornell.dendro.corina.tridas.TridasSample;

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
