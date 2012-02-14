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
package edu.cornell.dendro.corina.sample;

import java.awt.Dialog;
import java.awt.Frame;
import java.io.IOException;

import edu.cornell.dendro.corina.gui.UserCancelledException;

public interface GUIAwareSampleLoader extends SampleLoader {
	public Sample load(Dialog dialog) throws IOException, UserCancelledException;
	public BaseSample loadBasic(Dialog dialog) throws IOException, UserCancelledException;
	public boolean save(Sample s, Dialog dialog) throws IOException, UserCancelledException;

	public Sample load(Frame frame) throws IOException;
	public BaseSample loadBasic(Frame frame) throws IOException;
	public boolean save(Sample s, Frame frame) throws IOException;
}
