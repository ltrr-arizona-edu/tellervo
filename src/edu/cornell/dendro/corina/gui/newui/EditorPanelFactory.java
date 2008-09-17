package edu.cornell.dendro.corina.gui.newui;

import edu.cornell.dendro.corina.site.Radius;
import edu.cornell.dendro.corina.site.Site;
import edu.cornell.dendro.corina.site.Specimen;
import edu.cornell.dendro.corina.site.Subsite;
import edu.cornell.dendro.corina.site.Tree;

public class EditorPanelFactory {
    /**
     * Get an editor panel for this class
     * 
     * @param content
     * @return
     */
    public static BaseEditorPanel<?> createPanelForClass(Class<?> content) {
    	if(content.equals(Site.class))
    		return (BaseEditorPanel<Site>) new SiteEditorPanel();
    	if(content.equals(Subsite.class))
    		return (BaseEditorPanel<Subsite>) new SubsiteEditorPanel();
    	if(content.equals(Tree.class))
    		return (BaseEditorPanel<Tree>) new TreeEditorPanel();
    	if(content.equals(Specimen.class))
    		return (BaseEditorPanel<Specimen>) new SpecimenEditorPanel();
    	if(content.equals(Radius.class))
    		return (BaseEditorPanel<Radius>) new RadiusEditorPanel();
    	return null;
    }
}
