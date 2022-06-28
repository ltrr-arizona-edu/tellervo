package org.tellervo.desktop.hardware;

import java.util.Vector;

public class ClassScope {
    private static  java.lang.reflect.Field LIBRARIES;
    static {
        try {
			LIBRARIES = ClassLoader.class.getDeclaredField("loadedLibraryNames");
		} catch (NoSuchFieldException e){
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        LIBRARIES.setAccessible(true);
    }
    public static String[] getLoadedLibraries(final ClassLoader loader) {
        Vector<String> libraries = null;
		try {
			libraries = (Vector<String>) LIBRARIES.get(loader);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
        return libraries.toArray(new String[] {});
    }
}