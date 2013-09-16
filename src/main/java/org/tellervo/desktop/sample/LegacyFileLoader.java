package org.tellervo.desktop.sample;

import java.awt.Dialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;

import org.tellervo.desktop.gui.UserCancelledException;

public class LegacyFileLoader implements GUIAwareSampleLoader {

	File file;
	
	
	public LegacyFileLoader(String filename)
	{
		file = new File(filename);
	}
	
	public LegacyFileLoader(File file)
	{
		this.file = file;
	}
	
	@Override
	public Sample load() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseSample loadBasic() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean save(Sample s) throws IOException, UserCancelledException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void preload(BaseSample bs) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return file.getAbsolutePath();
	}

	@Override
	public String getShortName() {
		return file.getName();
	}

	@Override
	public SampleType getSampleType() {
		return SampleType.fromString("Unknown");
		
	}

	@Override
	public Sample load(Dialog dialog) throws IOException,
			UserCancelledException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseSample loadBasic(Dialog dialog) throws IOException,
			UserCancelledException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean save(Sample s, Dialog dialog) throws IOException,
			UserCancelledException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Sample load(Frame frame) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseSample loadBasic(Frame frame) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean save(Sample s, Frame frame) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

}
