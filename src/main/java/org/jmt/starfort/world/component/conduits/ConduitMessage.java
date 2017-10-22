package org.jmt.starfort.world.component.conduits;

/**
 * A message being sent over a conduit
 * 
 * @author jediminer543
 *
 */
public class ConduitMessage {

	
	/**
	 * Message source, preferably instance, or null for non-origined Message
	 * (Or hacking; because hacking is fun)
	 */
	public Object messageSrc;
	/**
	 * Message destination, either specific instance
	 * or a class for a type, or null for broadcast
	 */
	public Object messageDst;
	
	/**
	 * Channel of the message
	 */
	public ConduitChannel messageChannel;
	
	public int ackCount = 0;
	public int nacCount = 0;
	
	public String message = "";
	public int id = 0;
	
	public void ack() {
		ackCount++;
	}
	
	public void nac() {
		nacCount++;
	}
}
