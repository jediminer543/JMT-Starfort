package org.jmt.starfort.game.registra;

import org.jmt.starfort.world.material.IMaterial;
import org.jmt.starfort.world.material.IMaterialType;
import org.jmt.starfort.world.material.MaterialRegistry;

public class MaterialRegistra {

	public static void register() {
		
		//Register Types
		registerCommonTypes();
		
		//Register Mats
		registerDebug();
	}
	
	public static void registerCommonTypes() {
		IMaterialType stony = new IMaterialType() {
			
			@Override
			public String getMaterialTypeName() {
				return "Stone";
			}
			
			@Override
			public String getMaterialTypeDescriptor() {
				// TODO Auto-generated method stub
				return "A stonelike material";
			}
		};
		
		IMaterialType metal = new IMaterialType() {
			
			@Override
			public String getMaterialTypeName() {
				return "Metal";
			}
			
			@Override
			public String getMaterialTypeDescriptor() {
				// TODO Auto-generated method stub
				return "A metalic material";
			}
		};
		
		MaterialRegistry.registerMaterialType(metal);
		MaterialRegistry.registerMaterialType(stony);
	}
	
	public static void registerDebug() {
		IMaterial mat = new IMaterial() {
			
			@Override
			public IMaterialType getMaterialType() {
				return MaterialRegistry.getMaterialType("Metal");
			}
			
			@Override
			public String getMaterialName() {
				return "Debug";
			}
			
			@Override
			public float getMaterialHardness() {
				return 1;
			}
		};
		
		 MaterialRegistry.registerMaterial(mat);
	}
}
