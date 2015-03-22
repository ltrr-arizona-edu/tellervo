/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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

package org.tellervo.desktop.manip;

import java.io.IOException;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleType;
import org.tellervo.desktop.sample.TellervoWsiTridasElement;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.tridasv2.SeriesLinkUtil;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;
import org.tellervo.desktop.wsi.tellervo.NewTridasIdentifier;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasDatingType;
import org.tridas.schema.TridasDating;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasInterpretation;


public class Redate extends AbstractUndoableEdit {
	private static final long serialVersionUID = 1L;

	// redate-force-relative (do i ever even use this?)
	/*public static Redate redate(Sample s, Range r) {
		return new Redate(s, r, getSampleDating(s));
		// (shouldn't this be a constant somewhere?)
	}*/

	public static Redate redate(Sample s, Range r, TridasDating dating) {
		return new Redate(s, r, dating);
	}
	
	private static TridasDating getSampleDating(Sample s) {
		ITridasSeries series = s.getSeries();
		TridasDating dating = null;
		
		if(series.isSetInterpretation() && series.getInterpretation().isSetDating())
			dating = series.getInterpretation().getDating();
		
		return dating;
	}
	
	// ----------------------------------------

	private Redate(Sample sample, Range range, TridasDating dating) {
		this.s = sample;
		this.oldRange = s.getRange();
		this.oldDating = getSampleDating(s);
		this.oldMod = s.isModified();

		oldHadInterpretation = s.getSeries().isSetInterpretation();
		
		this.newRange = range;
		this.newDating = dating;

		// do it a first time -- can't just call redo() because
		// that calls super.redo() (REFACTOR)
		s.setRange(newRange);
		if(!oldHadInterpretation) {
			TridasInterpretation interpretation = new TridasInterpretation();
			s.getSeries().setInterpretation(interpretation);
		}
		
		s.getSeries().getInterpretation().setDating(dating);
		
		// Set the interpretation first year field to new value.  
		// Required for redating in place.
		s.getSeries().getInterpretation().setFirstYear(range.getStart().tridasYearValue());
		
		s.fireSampleRedated();
		s.setModified();
		s.fireSampleMetadataChanged(); // for mod flag
	}

	// undo
	private Sample s;
	private Range oldRange, newRange;
	private TridasDating oldDating, newDating;
	private boolean oldHadInterpretation;
	private boolean oldMod;

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		s.setRange(oldRange);
		
		if(oldHadInterpretation)
			s.getSeries().getInterpretation().setDating(oldDating);
		else
			s.getSeries().setInterpretation(null);
		
		s.fireSampleRedated();
		if (!oldMod) {
			s.clearModified();
			s.fireSampleMetadataChanged(); // for mod flag
		}
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		s.setRange(newRange);
		
		if(!oldHadInterpretation) {
			TridasInterpretation interpretation = new TridasInterpretation();
			s.getSeries().setInterpretation(interpretation);
		}		
		s.getSeries().getInterpretation().setDating(newDating);
		
		s.fireSampleRedated();
		s.setModified();
		s.fireSampleMetadataChanged(); // for mod flag
	}

	@Override
	public String getPresentationName() {
		return I18n.getText("redate");
	}
	
	/**
	 * Create a new redate on the webservice
	 * @param dating
	 * @return true on success, false otherwise
	 */
	public static boolean performTellervoWsiRedate(Sample sample, 
			String newname, String newversion, String justification, NormalTridasDatingType datingType, 
			NormalTridasDatingType originalDatingType, Range newDateRange) {

		TridasDerivedSeries series = new TridasDerivedSeries();
		
		// set title (and version?)
		if(newname!=null) series.setTitle(newname);
		if(newversion!=null) series.setVersion(newversion);
		
		ControlledVoc voc = new ControlledVoc();
		voc.setValue(SampleType.REDATE.toString());
		series.setType(voc);
		
		// the identifier is based on the domain from the series
		series.setIdentifier(NewTridasIdentifier.getInstance(sample.getSeries().getIdentifier()));
		
		// set the parent
		SeriesLinkUtil.addToSeries(series, sample.getSeries().getIdentifier());
		
		// now, a redate has three other parameters
		TridasInterpretation interpretation = new TridasInterpretation();
		series.setInterpretation(interpretation);
		
		// 1: Dating type (but only if it changed)
		if(datingType != originalDatingType)
		{
			TridasDating dating = new TridasDating();
			dating.setType(datingType);
			interpretation.setDating(dating);
		}
		
		// 2: Relative start year
		interpretation.setFirstYear(newDateRange.getStart().tridasYearValue());
		// looks like the genericField is what's actually used?
		GenericFieldUtils.setField(series, "tellervo.newStartYear", Integer.parseInt(newDateRange.getStart().toString()));

		// 3: Justification
		GenericFieldUtils.setField(series, "tellervo.justification", justification);
		
		// make a new 'redate' dummy sample for saving
		Sample tmp = new Sample(series);		

		try {
			TellervoWsiTridasElement saver = new TellervoWsiTridasElement(series.getIdentifier());
			// here's where we do the "meat"
			if(saver.save(tmp)) {
				// put it in our menu
				OpenRecent.sampleOpened(new SeriesDescriptor(tmp));
				
				FullEditor editor = FullEditor.getInstance();
				editor.addSample(tmp);
				
				// get out of here! :)
				return true;
			}
		} catch (IOException ioe) {
			Alert.error(I18n.getText("error.couldNotRedate"), I18n.getText("error") + ": " + ioe.toString());
		}
		
		return false;
	}
}
