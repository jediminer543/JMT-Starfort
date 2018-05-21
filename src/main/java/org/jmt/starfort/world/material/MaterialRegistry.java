package org.jmt.starfort.world.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is the static registry of all materials.
 * 
 * It provides static storage of both materials and material
 * types, and can be searched through by name, and by ID.
 * 
 * Also assigns materials ID's
 * 
 * How the frack was this not documented
 * 
 * @author jediminer543
 *
 */
public class MaterialRegistry {

	public static ArrayList<IMaterial> mats = new ArrayList<>();
	public static Map<String, Integer> matIndex = new HashMap<>();
	
	public static ArrayList<IMaterialType> matTypes = new ArrayList<>();
	public static Map<String, Integer> matTypeIndex = new HashMap<>();
	
	/**
	 * Register a material with the material registry
	 * 
	 * @param mat The material to register
	 * @return The ID of that material, allowing for quick finding
	 */
	public static int registerMaterial(IMaterial mat) {
		if (matIndex.containsKey(mat.getMaterialName())) {
			return -1;
		} else {
			mats.add(mat);
			matIndex.put(mat.getMaterialName(), mats.indexOf(mat));
			return mats.indexOf(mat);
		}
	}
	
	/**
	 * Register a material type with the material registry
	 * 
	 * @param mat The material type to register
	 * @return The ID of that material type, allowing for quick finding
	 */
	public static int registerMaterialType(IMaterialType mat) {
		if (matTypeIndex.containsKey(mat.getMaterialTypeName())) {
			return -1;
		} else {
			matTypes.add(mat);
			matTypeIndex.put(mat.getMaterialTypeName(), matTypes.indexOf(mat));
			return matTypes.indexOf(mat);
		}
	}
	
	public static IMaterial getMaterial(String name) {
		return getMaterial(matIndex.get(name));
	}
	
	public static IMaterial getMaterial(int id) {
		return mats.get(id);
	}
	
	public static IMaterialType getMaterialType(String name) {
		return getMaterialType(matTypeIndex.get(name));
	}
	
	public static IMaterialType getMaterialType(int id) {
		return matTypes.get(id);
	}
	
	public static int getMaterialID(String matName) {
		return matIndex.get(matName);
	}
	
	public static int getMaterialTypeID(String matName) {
		return matTypeIndex.get(matName);
	}
	
	public static int getMaterialID(IMaterial matName) {
		return matIndex.get(matName.getMaterialName());
	}
	
	public static int getMaterialTypeID(IMaterialType matName) {
		return matTypeIndex.get(matName.getMaterialTypeName());
	}
	
	public static boolean materialsEqual(IMaterial imat1, IMaterial imat2) {
		return imat1.getMaterialName() == imat2.getMaterialName();
	}
}
