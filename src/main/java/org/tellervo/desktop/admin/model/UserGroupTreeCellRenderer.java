package org.tellervo.desktop.admin.model;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.schema.WSISecurityGroup;
import org.tellervo.desktop.schema.WSISecurityUser;
import org.tellervo.desktop.ui.Builder;


public class UserGroupTreeCellRenderer extends DefaultTreeCellRenderer {

	private final static Logger log = LoggerFactory.getLogger(UserGroupTreeCellRenderer.class);

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**  
     * Constructor 
     * 
     * @return void 
     * @exception 
     */
     public UserGroupTreeCellRenderer()
     {
         super();
     }    
     
     
     /** 
      * getTreeCellRendererComponent 
      * This method is overridden to set the node specific icons and tooltips
      *    
      * @return The Component object used to render the cell value
      * @exception 
      */ 
      public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                    boolean selection, boolean expanded,
                                                    boolean leaf, int row, boolean hasFocus)
      {
          super.getTreeCellRendererComponent(tree, value, selection, expanded, 
                                             leaf, row, hasFocus);
          
          //The value object is nothing but the DefaultMutableTreeNode.
          DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
          
          setIconAndToolTip(node.getUserObject(), tree);
          
          return this;
      }
      
      /** 
      * setIconAndToolTip 
      * This method checks the userobject and appropiately sets the icons and tooltip
      *    
      * @return void
      * @exception 
      */
      private void setIconAndToolTip(Object obj, JTree tree)
      {
    	  //log.debug("Node type: "+obj.getClass().toString());
    	  
          if(obj instanceof TransferableUser)
          {
              setIcon(Builder.getIcon("edit_user.png", 16));
          }
          else if(obj instanceof TransferableGroup)
          {
        	  setIcon(Builder.getIcon("edit_group.png", 16));
          }
      } 
      
      
	
}
