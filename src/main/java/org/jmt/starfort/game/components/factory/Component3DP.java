package org.jmt.starfort.game.components.factory;

import java.util.ArrayList;
import java.util.List;

import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.InlineFunctions;
import org.jmt.starfort.world.component.conduits.ConduitChannel;
import org.jmt.starfort.world.component.conduits.ConduitMessage;
import org.jmt.starfort.world.component.conduits.DefaultConduitChannels;
import org.jmt.starfort.world.component.conduits.IComponentConduitDevice;
import org.jmt.starfort.world.component.conduits.IConduitChannelType;
import org.jmt.starfort.world.material.IMaterial;

/**
 * First factory thing (WOOT)
 * 
 * A 3d printer, for automatically making infinite cheap plastic tat.
 * 
 * And parts. The parts part is impartant (yes this joke should
 * get me shot).
 * 
 * @author jediminer543
 *
 */
public class Component3DP implements IComponentConduitDevice {

	String name;
	IMaterial material;
	
	List<ConduitMessage> messages = new ArrayList<ConduitMessage>();
	List<ConduitChannel> channels = new ArrayList<ConduitChannel>();
	
	@Override
	public String getComponentName() {
		return getComponentMaterial().getMaterialName() + " 3D Printer";
	}

	@Override
	public IMaterial getComponentMaterial() {
		return material;
	}

	@Override
	public ComplexRunnable getTick() {
		return (Object[] stuff) -> {
			//TODO
		};
	}

	@Override
	public String getConduitDeviceName() {
		return getComponentName();
	}

	@Override
	public IConduitChannelType[] getConduitDeviceTypes() {
		return InlineFunctions.inlineArray(DefaultConduitChannels.Energy);
	}

	@Override
	public void queueConduitDeviceRequest(ConduitMessage request) {
		messages.add(request);
	}

	@Override
	public List<ConduitChannel> getConduitDeviceChannels() {
		return channels;
	}

}
