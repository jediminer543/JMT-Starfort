package org.jmt.starfort.world.component.conduits;

import java.util.ArrayList;
import java.util.List;

public class ConduitChannel {
	
	ArrayList<IComponentConduitDevice> devices = new ArrayList<>();
	IConduitChannelType type;
	
	public ConduitChannel(IConduitChannelType type) {
		this.type = type;
	}
	
	public List<IComponentConduitDevice> getConduitChannelDevices() {
		return devices;
	}
	
	
	public IConduitChannelType getConduitChennelType() {
		return type;
	}
	
	public void addDevice(IComponentConduitDevice ccd) {
		devices.add(ccd);
	}
	
	public void pushRequest(ConduitMessage request) {
		for (IComponentConduitDevice ccd : devices) {
			ccd.queueConduitDeviceRequest(request);
		}
	}
}
