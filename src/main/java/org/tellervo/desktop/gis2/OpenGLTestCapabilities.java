package org.tellervo.desktop.gis2;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesImmutable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

public class OpenGLTestCapabilities implements GLEventListener{

	public StringBuilder messages;
	public boolean fail = false;
	
	private static boolean previouslyTestedAsOpenGLCapable = false;
	private static boolean testedPreviously = false;

	public static String getOpenGLProblems()
	{
	    GLCapabilities caps = new GLCapabilities(GLProfile.getMaxFixedFunc(true));
		
	    caps.setAlphaBits(8);
	    caps.setRedBits(8);
	    caps.setGreenBits(8);  
	    caps.setBlueBits(8);
	    caps.setDepthBits(24);
	    caps.setDoubleBuffered(true);
	    GLCanvas canvas = new GLCanvas(caps);

	    OpenGLTestCapabilities testClass = new OpenGLTestCapabilities();
	    canvas.addGLEventListener(testClass);
	    
	    
	    return testClass.messages.toString();

	}
	
	
	
	public static boolean isOpenGLCapable()
	{
		if(testedPreviously)
		{
			return previouslyTestedAsOpenGLCapable;
		}
		
		return testOpenGL();
	}
	
	private static boolean testOpenGL()
	{
	    GLCapabilities caps = new GLCapabilities(GLProfile.getMaxFixedFunc(true));
		
	    caps.setAlphaBits(8);
	    caps.setRedBits(8);
	    caps.setGreenBits(8);
	    caps.setBlueBits(8);
	    caps.setDepthBits(24);
	    caps.setDoubleBuffered(true);
	    GLCanvas canvas = new GLCanvas(caps);

	    OpenGLTestCapabilities testClass = new OpenGLTestCapabilities();
	    canvas.addGLEventListener(testClass);

	    testedPreviously = true;
	    previouslyTestedAsOpenGLCapable = !testClass.fail;
	    
	    return !testClass.fail;

	}
	
	 public void init(GLAutoDrawable glAutoDrawable)
	    {
		 	messages = new StringBuilder();
	        fail = false;

	        for (String funcName : this.getRequiredOglFunctions())
	        {
	            if (!glAutoDrawable.getGL().isFunctionAvailable(funcName))
	            {
	                messages.append("OpenGL function " + funcName + " is not available.");
	                fail = true;
	            }
	        }

	        for (String extName : this.getRequiredOglExtensions())
	        {
	            if (!glAutoDrawable.getGL().isExtensionAvailable(extName))
	            {
	            	messages.append("OpenGL extension " + extName + " is not available.");
	                fail = true;
	            }
	        }

	        GLCapabilitiesImmutable caps = glAutoDrawable.getChosenGLCapabilities();
	        if (caps.getAlphaBits() != 8 || caps.getRedBits() != 8 || caps.getGreenBits() != 8 || caps.getBlueBits() != 8)
	        {
	        	messages.append("Device canvas color depth is inadequate.");
	            fail = true;
	        }

	        if (caps.getDepthBits() < 16)
	        {
	        	messages.append("Device canvas depth buffer depth is inadequate.");
	            fail = true;
	        }

	        if (caps.getDoubleBuffered() == false)
	        {
	        	messages.append("Device canvas is not double buffered.");
	            fail = true;
	        }

	        return;
	    }


	@Override
	public void display(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub
		
	}
	
	
    protected String[] getRequiredOglFunctions()
    {
        return new String[] {"glActiveTexture", "glClientActiveTexture"};
    }

    protected String[] getRequiredOglExtensions()
    {
        return new String[] {"GL_EXT_texture_compression_s3tc"};
    }
	

}
