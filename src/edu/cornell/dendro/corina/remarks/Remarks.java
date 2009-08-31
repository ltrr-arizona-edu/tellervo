package edu.cornell.dendro.corina.remarks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

import org.tridas.schema.NormalTridasRemark;
import org.tridas.schema.TridasRemark;
import org.tridas.schema.TridasValue;

import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.util.ListUtil;

public class Remarks {
	
	private static List<Remark> remarks;
	
	/**
	 * Lazily-build the list of remarks
	 * @return a list of remarks
	 */
	private static List<Remark> getRemarks() {
		if(remarks != null)
			return remarks;
		
		remarks = new ArrayList<Remark>();
		
		// add tridas remarks
		for(NormalTridasRemark n : NormalTridasRemark.values())
			remarks.add(new TridasReadingRemark(n));
		
		// add dictionary remarks
		List<?> dictionaryRemarks = Dictionary.getDictionary("readingNoteDictionary");
		List<TridasRemark> corinaRemarks = ListUtil.subListOfType(dictionaryRemarks, TridasRemark.class);
		
		for(TridasRemark r : corinaRemarks) {
			// only corina remarks for now
			if("Corina".equals(r.getNormalStd()))
				remarks.add(new CorinaReadingRemark(r));
		}
		
		return remarks;
	}

	/**
	 * Append a list of remarks to the given menu
	 * @param menu
	 * @param value the associated TridasValue for the given reading
	 * @param onRemarkChange an action to run when a remark is changed
	 * @param isChangeable are these menu items selectable (enabled?)
	 */
	public static void appendRemarksToMenu(final JPopupMenu menu, final TridasValue value, 
			final Runnable onRemarkChange, final boolean isChangeable) {
		List<Remark> remarks = getRemarks();
		
		for(final Remark remark : remarks) {
			String displayName = remark.getDisplayName();
			Icon icon = remark.getIcon();
			
			final JCheckBoxMenuItem item = new JCheckBoxMenuItem(displayName, icon, remark.isRemarkSet(value));
			
			// disable non-editable remarks
			item.setEnabled(isChangeable);
			
			// set or remove the mark on selection
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean remove = remark.isRemarkSet(value);
					boolean inherited = remark.isRemarkInherited(value);
					
					if(remove) {
						item.setSelected(false);
						
						if(inherited)
							remark.overrideRemark(value);
						else
							remark.removeRemark(value);
					}
					else {
						item.setSelected(true);
						remark.applyRemark(value);
					}
					
					if(onRemarkChange != null)
						onRemarkChange.run();
				}
			});
			
			menu.add(item);
		}
	}

	public static Map<NormalTridasRemark, String> getTridasRemarkIcons() {
		return tridasRemarkIconMap;
	}
	
	public static Map<String, String> getCorinaRemarkIcons() {
		return corinaRemarkIconMap;
	}
	
	/** A map from tridas remarks -> icons */
	private final static Map<NormalTridasRemark, String> tridasRemarkIconMap = new EnumMap<NormalTridasRemark, String>(NormalTridasRemark.class);
	static {
		tridasRemarkIconMap.put(NormalTridasRemark.FIRE_DAMAGE, "fire.png");
		tridasRemarkIconMap.put(NormalTridasRemark.FROST_DAMAGE, "frostring.png");
	};
	
	private final static Map<String, String> corinaRemarkIconMap = new HashMap<String, String>();
	static {
		corinaRemarkIconMap.put("Single pinned", "singlepin.png");
		corinaRemarkIconMap.put("Double pinned", "doublepin.png");
		corinaRemarkIconMap.put("Triple pinned", "triplepin.png");
	}
	
	private Remarks() {
	}
}
