package org.jmt.starfort.game.entity.human;

import org.jmt.starfort.world.entity.organs.IOrgan;
import org.jmt.starfort.world.entity.organs.Organ;
import org.jmt.starfort.world.entity.organs.OrganTypes;
import org.jmt.starfort.world.material.IMaterial;
import org.jmt.starfort.world.material.MaterialRegistry;

public class EntityHumanOrganFactory {

	public IOrgan getHumanOrganBody() {
		IMaterial boneMaterial = MaterialRegistry.getMaterial("Bone");
		IMaterial fleshMaterial = MaterialRegistry.getMaterial("Flesh");
		IOrgan body = new Organ(fleshMaterial, OrganTypes.Body);
		IOrgan spine = new Organ(boneMaterial, OrganTypes.Spine);
		IOrgan lLeg = new Organ(boneMaterial, OrganTypes.Leg);
		IOrgan lFoot = new Organ(boneMaterial, OrganTypes.Foot);
		IOrgan rLeg = new Organ(boneMaterial, OrganTypes.Leg);
		IOrgan rFoot = new Organ(boneMaterial, OrganTypes.Foot);
		body.getOrganChildren().add(spine);
		body.getOrganChildren().add(lLeg);
		body.getOrganChildren().add(rLeg);
		lLeg.getOrganChildren().add(lFoot);
		rLeg.getOrganChildren().add(rFoot);
		return body;
	}
}
