package org.jmt.starfort.game.registra;

import org.jmt.starfort.renderer.Colour;
import org.jmt.starfort.renderer.Renderer;
import org.jmt.starfort.world.material.IMaterial;
import org.jmt.starfort.world.material.IMaterialType;
import org.jmt.starfort.world.material.MaterialRegistry;

public class MaterialRegistra {

	public static void register() {
		
		//Register Types
		registerCommonWorldTypes();
		
		//Register Mats
		registerDebug();
		
		registerOrganicMaterial();
	}
	
	public static void registerCommonWorldTypes() {
		IMaterialType stony = new IMaterialType() {
			
			@Override
			public String getMaterialTypeName() {
				return "Stone";
			}
			
			@Override
			public String getMaterialTypeDescriptor() {
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
				return "A metalic material";
			}
		};
		
		IMaterialType flesh = new IMaterialType() {
			
			@Override
			public String getMaterialTypeName() {
				return "Flesh";
			}
			
			@Override
			public String getMaterialTypeDescriptor() {
				return "Soft Organic Material";
			}
		};
		
		IMaterialType bone = new IMaterialType() {
			
			@Override
			public String getMaterialTypeName() {
				return "Bone";
			}
			
			@Override
			public String getMaterialTypeDescriptor() {
				return "Hard Organic Material";
			}
		};
		
		MaterialRegistry.registerMaterialType(metal);
		MaterialRegistry.registerMaterialType(stony);
		MaterialRegistry.registerMaterialType(flesh);
		MaterialRegistry.registerMaterialType(bone);
	}
	
	public static void registerOrganicMaterial() {
		IMaterial bone = new IMaterial() {
			IMaterialType type = MaterialRegistry.getMaterialType("Bone");
			
			@Override
			public String getMaterialName() {
				return "Bone";
			}

			@Override
			public float getMaterialHardness() {
				return 2;
			}

			@Override
			public IMaterialType getMaterialType() {
				return type;
			}
			
		};
		
		IMaterial flesh = new IMaterial() {
			IMaterialType type = MaterialRegistry.getMaterialType("Flesh");
			
			@Override
			public String getMaterialName() {
				return "Flesh";
			}

			@Override
			public float getMaterialHardness() {
				return 1;
			}

			@Override
			public IMaterialType getMaterialType() {
				return type;
			}
			
		};
		
		IMaterial chitin = new IMaterial() {
			IMaterialType type = MaterialRegistry.getMaterialType("Bone");
			
			@Override
			public String getMaterialName() {
				return "Chitin";
			}

			@Override
			public float getMaterialHardness() {
				return 5;
			}

			@Override
			public IMaterialType getMaterialType() {
				return type;
			}
			
		};
		
		MaterialRegistry.registerMaterial(bone);
		MaterialRegistry.registerMaterial(flesh);
		MaterialRegistry.registerMaterial(chitin);
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
	
	/**
	 * Registers all materials with the passed renderer
	 * 
	 * @param r The renderer to set material colors with
	 */
	public static void registerRenderer(Renderer r) {
		IMaterial mat = MaterialRegistry.getMaterial("Debug");
		int matID = MaterialRegistry.getMaterialID(mat);	
		r.materialRenderReg.put(matID, new Colour(0.5f, 0.2f, 0.5f, 1f));
	}
}
