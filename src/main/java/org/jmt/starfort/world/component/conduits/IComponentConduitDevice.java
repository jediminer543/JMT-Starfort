package org.jmt.starfort.world.component.conduits;

import java.util.List;

public interface IComponentConduitDevice {

	public String getConduitDeviceName();
	
	public IConduitChannelType[] getConduitDeviceTypes();
	
	public void queueConduitDeviceRequest(ConduitMessage request);
	
	/**
	 * Gets all channels this device is connected to
	 * @return channels this is connected to
	 */
	public List<ConduitChannel> getConduitDeviceChannels();

}
