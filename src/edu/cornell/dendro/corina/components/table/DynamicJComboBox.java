/**
 * Created at Aug 20, 2010, 2:58:08 AM
 */
package edu.cornell.dendro.corina.components.table;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.dmurph.mvc.IEventListener;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;

/**
 * This class is for use inside of JTables, where data might have to be loaded or modified dynamically
 * @author Daniel
 *
 */
public class DynamicJComboBox extends JComboBox implements IEventListener{
	private static final long serialVersionUID = 1L;
	
	private final String key;
	private final boolean once;
	private final DefaultComboBoxModel model;
	
	public DynamicJComboBox(String argSetItemsKey, boolean argOnce){
		model = new DefaultComboBoxModel();
		setModel(model);
		key = argSetItemsKey;
		once = argOnce;
		MVC.addEventListener(argSetItemsKey, this);
	}
	
	/**
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		MVC.removeEventListener(key, this);
		super.finalize();
	}

	/**
	 * @see com.dmurph.mvc.IEventListener#eventReceived(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void eventReceived(MVCEvent argEvent) {
		if(once){
			MVC.removeEventListener(key, this);
		}
		if(argEvent instanceof DynamicJComboBoxEvent){
			DynamicJComboBoxEvent event = (DynamicJComboBoxEvent) argEvent;
			model.removeAllElements();
			if(event.comboBoxItems == null){
				MVC.removeEventListener(key, this);
				return;
			}
			for(String s : event.comboBoxItems){
				model.addElement(s);
			}
		}
	}
}
