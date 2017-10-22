package org.jmt.starfort.world.component.conduits;

import java.util.List;

import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentTickable;

public interface IComponentConduitDevice extends IComponent, IComponentTickable {

	public String getConduitDeviceName();
	
	public IConduitChannelType[] getConduitDeviceTypes();
	
	/**
	 * Queue up a message to be processed in this devices tick
	 * 
	 * Done to distribute load accross threads.
	 * 
	 * @param request
	 */
	public void queueConduitDeviceRequest(ConduitMessage request);
	
	/**
	 * Gets all channels this device is connected to
	 * 
	 * This may be used to add and remove connected channels (make certain
	 * list is linked to backend dataset)
	 * 
	 * @return channels this is connected to
	 */
	public List<ConduitChannel> getConduitDeviceChannels();

}
