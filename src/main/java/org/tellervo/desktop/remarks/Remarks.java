/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.remarks;

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

import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.util.ListUtil;
import org.tridas.schema.NormalTridasRemark;
import org.tridas.schema.TridasRemark;
import org.tridas.schema.TridasValue;


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
		List<TridasRemark> tellervoRemarks = ListUtil.subListOfType(dictionaryRemarks, TridasRemark.class);
		
		for(TridasRemark r : tellervoRemarks) {
			// only tellervo remarks for now
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
			String displayName = remark.getDisplayName().substring(0,1).toUpperCase()+remark.getDisplayName().substring(1);
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
	
	public static Map<String, String> getTellervoRemarkIcons() {
		return tellervoRemarkIconMap;
	}
	
	/** A map from tridas remarks -> icons */
	private final static Map<NormalTridasRemark, String> tridasRemarkIconMap = new EnumMap<NormalTridasRemark, String>(NormalTridasRemark.class);
	static {
		tridasRemarkIconMap.put(NormalTridasRemark.FIRE_DAMAGE, "fire.png");
		tridasRemarkIconMap.put(NormalTridasRemark.FROST_DAMAGE, "frostring.png");
		tridasRemarkIconMap.put(NormalTridasRemark.FALSE_RINGS, "falsering.png");
		tridasRemarkIconMap.put(NormalTridasRemark.COMPRESSION_WOOD, "compressionwood.png");
		tridasRemarkIconMap.put(NormalTridasRemark.TENSION_WOOD, "tensionwood.png");
		tridasRemarkIconMap.put(NormalTridasRemark.UNSPECIFIED_INJURY, "injury.png");		
		tridasRemarkIconMap.put(NormalTridasRemark.TRAUMATIC_DUCTS, "Burn.png");
		tridasRemarkIconMap.put(NormalTridasRemark.CRACK, "crack.png");
		tridasRemarkIconMap.put(NormalTridasRemark.SINGLE_PINNED, "singlepin.png");
		tridasRemarkIconMap.put(NormalTridasRemark.DOUBLE_PINNED, "doublepin.png");
		tridasRemarkIconMap.put(NormalTridasRemark.TRIPLE_PINNED, "triplepin.png");
		tridasRemarkIconMap.put(NormalTridasRemark.MISSING_RING, "missingring.png");
		tridasRemarkIconMap.put(NormalTridasRemark.RADIUS_SHIFT_UP, "up.png");
		tridasRemarkIconMap.put(NormalTridasRemark.RADIUS_SHIFT_DOWN, "down.png");
		tridasRemarkIconMap.put(NormalTridasRemark.MOON_RINGS, "moon.png");
		tridasRemarkIconMap.put(NormalTridasRemark.DIFFUSE_LATEWOOD, "note.png");
		tridasRemarkIconMap.put(NormalTridasRemark.DENSITY_FLUCTUATION, "densityfluctuation.png");
		tridasRemarkIconMap.put(NormalTridasRemark.WIDE_LATE_WOOD, "wide-late-wood.png");
		tridasRemarkIconMap.put(NormalTridasRemark.WIDE_EARLY_WOOD, "wide-early-wood.png");
		
	};
	
	private final static Map<String, String> tellervoRemarkIconMap = new HashMap<String, String>();
	static {
		tellervoRemarkIconMap.put("micro ring", "microring.png");
		tellervoRemarkIconMap.put("insect damage", "bug.png");
		

	}
	
	private Remarks() {
	}
}
