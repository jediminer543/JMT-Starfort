package org.jmt.starfort.world.component.conduits;

import java.util.ArrayList;

public class ConduitChannel {

	ArrayList<IComponentConduitDevice> devices = new ArrayList<>();
	IConduitChannelType type;
	
	public IComponentConduitDevice[] getConduitChannelDevices() {
		return (IComponentConduitDevice[]) devices.toArray(new IComponentConduitDevice[devices.size()]);
	}
	
	
	public IConduitChannelType getConduitChennelType() {
		return type;
	}
	
	public void pushRequest(ConduitMessage request) {
		for (IComponentConduitDevice ccd : devices) {
			ccd.queueConduitDeviceRequest(request);
		}
	}
}
