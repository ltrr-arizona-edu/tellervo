/**
 * Created at Sep 21, 2010, 3:06:41 PM
 */
package edu.cornell.dendro.corina.components.table;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox.KeySelectionManager;

/**
 * @author Daniel
 *
 */
public class HTMLKeySelectionManager implements KeySelectionManager{
	public static final int DEFAULT_TIMEOUT = 1000;
	
	private int timeout;

	private String builtString = "";
	private long lastMillis = 0;
	
	public HTMLKeySelectionManager(){
		this(DEFAULT_TIMEOUT);
	}
	
	/**
	 * timout for autoselect word building, in milliseconds
	 * @param argTimeout
	 */
	public HTMLKeySelectionManager(int argTimeout){
		timeout = argTimeout;
	}
	
	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param argTimeout the timeout to set
	 */
	public void setTimeout(int argTimeout) {
		timeout = argTimeout;
	}

	@Override
	public int selectionForKey(char aKey, ComboBoxModel aModel) {
		int i,c;
        int currentSelection = -1;
        Object selectedItem = aModel.getSelectedItem();
        String v;
        String pattern;

        if ( selectedItem != null ) {
            for ( i=0,c=aModel.getSize();i<c;i++ ) {
                if ( selectedItem == aModel.getElementAt(i) ) {
                    currentSelection  =  i;
                    break;
                }
            }
        }
        pattern = ("" + aKey).toLowerCase();
        aKey = pattern.charAt(0);
        
        // build or clear the string
        long nowMillis = System.currentTimeMillis();
        if(nowMillis - lastMillis < timeout){
        	builtString += aKey;
        	System.out.println("built string '"+builtString+"'");
        }else{
        	builtString = aKey+"";
        	System.out.println("built string '"+builtString+"'");
        }
        lastMillis = nowMillis;

        for ( i = ++currentSelection, c = aModel.getSize() ; i < c ; i++ ) {
            Object elem = aModel.getElementAt(i);
			if (elem != null && elem.toString() != null) {
			    v = elem.toString().toLowerCase();
			    v = v.substring(v.indexOf('>')+1);
			    if ( v.length() > 0 && v.substring(0, builtString.length()).equals(builtString)  )
			    	return i;
			}
        }

        for ( i = 0 ; i < currentSelection ; i ++ ) {
            Object elem = aModel.getElementAt(i);
			if (elem != null && elem.toString() != null) {
			    v = elem.toString().toLowerCase();
			    v = v.substring(v.indexOf('>')+1);
			    if ( v.length() > 0 && v.substring(0, builtString.length()).equals(builtString) )
			    	return i;
			}
        }
        return -1;
	}

}
