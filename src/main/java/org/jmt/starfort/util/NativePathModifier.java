package org.jmt.starfort.util;

import java.io.File;
import java.lang.reflect.Field;

public class NativePathModifier {

	
	/**
	 * Modifies the native path, adding the passed path to the front of the search path
	 */
	public static void modLibraryPath(String path) throws Exception {
		setLibraryPath(path + File.pathSeparatorChar + System.getProperty("java.library.path"));
	}
	
	/**
	 * Sets the java library path to the specified path
	 *
	 * @author http://fahdshariff.blogspot.co.uk/2011/08/changing-java-library-path-at-runtime.html
	 *
	 * @param path the new library path
	 * @throws Exception
	 */
	public static void setLibraryPath(String path) throws Exception {
	    System.setProperty("java.library.path", path);
	 
	    //set sys_paths to null
	    final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
	    sysPathsField.setAccessible(true);
	    sysPathsField.set(null, null);
	}
}
