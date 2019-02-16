package org.jmt.starfort.world.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Stack;

import org.jmt.starfort.world.World;
import org.jmt.starfort.world.material.IMaterial;
import org.jmt.starfort.world.material.MaterialRegistry;

/**
 * The hero we all need
 * 
 * Saves the world using an implemented serialiser.
 * 
 * Currently the default java one.
 * 
 * Then applies patches to work around saving issues.
 * 
 * @author jediminer543
 *
 */
public class WorldSaver {

	/**
	 * Version attached to all saves.
	 */
	public static final int SAVE_VERSION = 0;
	
	/**
	 * In all efects a struct, who's job it is to
	 * store save info.
	 * 
	 * @author jediminer543
	 *
	 */
	static class SaveInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 0L;
		
		public int worldCount; //TODO IS THIS NECESARY
		public int saveVersion;
	}
	
	public static boolean saveWorld(File f, World w) {
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		SaveInfo si = new SaveInfo();
		si.worldCount = 1;
		si.saveVersion = SAVE_VERSION;
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(si);
			w = preFix(w);
			oos.writeObject(w);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static World loadWorld(File f) {
		World w = null;
		SaveInfo si = null;
		if (!f.exists()) {
			throw new IllegalArgumentException("File does not exist");
		}
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			si = (SaveInfo) ois.readObject();
			w = (World) ois.readObject();
			w = postFix(w);
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return w;
	}
	
	private static World preFix(World w) {
		return w;
	}
	
	private static void recursiveFix(Object o, ArrayList<Object> dedupe) throws IllegalArgumentException, IllegalAccessException {
		try {
		if (o == null || o.getClass() == null || o.getClass().getName() == null) {
			return;
		}
		if (o instanceof Number || o instanceof Boolean || o instanceof Class<?> || o instanceof String 
				|| o instanceof boolean[] || o instanceof char[] || o instanceof byte[] 
				|| o instanceof short[] || o instanceof int[] || o instanceof long[] 
				|| o instanceof float[] || o instanceof double[]) {
			return;
		}
		//System.out.println(o.getClass().getName());
		if (dedupe.contains(o)) {
			return;
		}
		dedupe.add(o);
		if (o.getClass().isArray() && !o.getClass().isPrimitive()) {
			for (Object p : (Object[]) o) {
				recursiveFix(p, dedupe);
			}
			return;
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			if ((f.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
				continue;
			}
			f.setAccessible(true);
			Class<?> type = f.getType();
			if (type == Object.class) {
				type = f.get(o).getClass();
			}
			if (type == (IMaterial.class)) {
				IMaterial mat = (IMaterial) f.get(o);
				mat = MaterialRegistry.getMaterial(mat.getMaterialName());
				f.setAccessible(true);
				f.set(o, mat);
				assert f.get(o) == MaterialRegistry.getMaterial(((IMaterial)f.get(o)).getMaterialName());
				continue;
			} else {
				recursiveFix(f.get(o), dedupe);
			}
		}} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed on class: " + o.getClass());
			System.err.println("Class: Primative: " + o.getClass().isPrimitive() + " Array: " + o.getClass().isArray());
		}
	}
	
	private static World postFix(World w) {
		try {
			ArrayList<Object> dedupe = new ArrayList<>();
			recursiveFix(w, dedupe);
			return w;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
