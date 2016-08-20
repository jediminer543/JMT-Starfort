package org.jmt.starfort.world.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MaterialRegistry {

	static ArrayList<IMaterial> mats = new ArrayList<>();
	static Map<String, Integer> matIndex = new HashMap<>();
	
	static ArrayList<IMaterialType> matTypes = new ArrayList<>();
	static Map<String, Integer> matTypeIndex = new HashMap<>();
	
	public int registerMaterial(IMaterial mat) {
		if (matIndex.containsKey(mat.getMaterialName())) {
			return -1;
		} else {
			mats.add(mat);
			matIndex.put(mat.getMaterialName(), mats.indexOf(mat));
			return mats.indexOf(mat);
		}
	}
	
	public int registerMaterialType(IMaterialType mat) {
		if (matTypeIndex.containsKey(mat.getMaterialTypeName())) {
			return -1;
		} else {
			matTypes.add(mat);
			matTypeIndex.put(mat.getMaterialTypeName(), matTypes.indexOf(mat));
			return matTypes.indexOf(mat);
		}
	}
	
	public IMaterial getMaterial(String name) {
		return getMaterial(matIndex.get(name));
	}
	
	public IMaterial getMaterial(int id) {
		return mats.get(id);
	}
	
	public IMaterialType getMaterialType(String name) {
		return getMaterialType(matTypeIndex.get(name));
	}
	
	public IMaterialType getMaterialType(int id) {
		return matTypes.get(id);
	}
	
}
