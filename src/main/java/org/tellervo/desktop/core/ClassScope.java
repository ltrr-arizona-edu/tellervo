package org.tellervo.desktop.core;

import java.util.Vector;

	public class ClassScope {
	    private static java.lang.reflect.Field LIBRARIES;
	    static {
	        try {
				LIBRARIES = ClassLoader.class.getDeclaredField("loadedLibraryNames");
				LIBRARIES.setAccessible(true);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    }
	    @SuppressWarnings("unchecked")
		public static String[] getLoadedLibraries(final ClassLoader loader) throws Exception {
	        final Vector<String> libraries = (Vector<String>) LIBRARIES.get(loader);
	        return libraries.toArray(new String[] {});
	    }
	}