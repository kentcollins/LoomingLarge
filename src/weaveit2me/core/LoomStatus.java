package weaveit2me.core;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import weaveit2me.network.StatusServer;

/***
 * Packs loom status into a byte[] for incorporation into a datagram packet.
 * Loom status is typically updated in response to an event.
 * 
 * <pre>
 * 	Byte 	Contents 
 * 	0 		triggering event 
 * 	1 		shed status 
 * 	2 		shaft status 
 * 	3 		warp status 
 * 	4 		weft status 
 * 	5 		beater status 
 * 	6 		control information 
 * 	7 		fault information 
 * 	8	 	current liftplan step number (4 bytes)
 * 	12	 	free form information (remaining bytes)
 * </pre>
 */

public class LoomStatus {

	// structure of the ByteBuffer
	public static final int EVENT_BYTE = 0;
	public static final int SHED_BYTE = 1;
	public static final int SHAFT_BYTE = 2;
	public static final int WARP_BYTE = 3;
	public static final int WEFT_BYTE = 4;
	public static final int BEATER_BYTE = 5;
	public static final int CONTROL_BYTE = 6;
	public static final int FAULT_BYTE = 7;
	public static final int LIFT_BYTE = 8; // four bytes long
	public static final int MESSAGE_START = 12;
	public static final int TOTAL_BYTES = 256;
	// information about the shed
	public static final int SHED_NO_STATUS = 0;
	public static final int SHED_CLOSED = 1;
	public static final int SHED_OPENING = 2;
	public static final int SHED_OPEN = 3;
	public static final int SHED_CLOSING = 4;
	// information about the shafts
	public static final int SHAFTS_NO_STATUS = 0;
	public static final int SHAFTS_DISENGAGED = 1;
	public static final int SHAFTS_ENGAGING = 2;
	public static final int SHAFTS_ENGAGED = 3;
	public static final int SHAFTS_DISENGAGING = 4;
	// information about the warp
	public static final int WARP_NO_STATUS = 0;
	public static final int WARP_WIND_FORWARD = 1;
	public static final int WARP_WIND_REVERSE = 2;
	public static final int WARP_WINDING_ON = 3;
	public static final int WARP_WINDING_OFF = 4;
	// information about the weft
	public static final int WEFT_NO_STATUS = 0;
	public static final int WEFT_MOVING_RIGHT = 1;
	public static final int WEFT_ON_RIGHT = 2;
	public static final int WEFT_MOVING_LEFT = 3;
	public static final int WEFT_ON_LEFT = 4;
	// information about the beater
	public static final int BEATER_NO_STATUS = 0;
	public static final int BEATER_RELEASED = 1;
	public static final int BEATER_ENGAGING = 2;
	public static final int BEATER_ENGAGED = 3;
	public static final int BEATER_RELEASING = 4;
	// flags for control switches
	public static final int PLAN_FLAG = 1;
	public static final int DIRECTION_FLAG = 2;
	public static final int LOOPING_FLAG = 4;
	public static final int TREADLE_FLAG = 8;
	// errors and faults
	public static final int FAULT_NONE = 0;
	public static final int FAULT_SHED = 1;
	public static final int FAULT_SHAFTS = 2;
	public static final int FAULT_WARP = 4;
	public static final int FAULT_WEFT = 8;
	public static final int FAULT_BEATER = 16;
	public static final int FAULT_CONTROLS = 32;
	public static final int FAULT_UNDEFINED = 64;
	// broadcast event indicates why transmission is being sent
	public static final int SHED_STATE = 1;
	public static final int SHED_TRANSITION = 2;
	public static final int SHAFT_STATE = 3;
	public static final int SHAFT_TRANSITION = 4;
	public static final int WARP_STATE = 5;
	public static final int WARP_TRANSITION = 6;
	public static final int WEFT_STATE = 7;
	public static final int WEFT_TRANSITION = 8;
	public static final int BEATER_STATE = 9;
	public static final int BEATER_TRANSITION = 10;
	public static final int FAULT_RECEIVED = 11;
	public static final int FAULT_CLEARED = 12;
	public static final int CONTROL_EVENT = 13;

	private ByteBuffer loomStatus;
	private StatusServer server;
	private static final Logger LOGGER = Logger
			.getLogger(LoomStatus.class.getName());

	public LoomStatus() {
		loomStatus = ByteBuffer.allocate(TOTAL_BYTES);
	}

	public void registerServer(StatusServer server) {
		this.server = server;
	}

	public void setEvent(int event) {
		loomStatus.put(EVENT_BYTE, (byte) event);
	}

	public int getEvent() {
		return loomStatus.get(EVENT_BYTE);
	}

	public static int getEvent(byte[] status) {
		return status[EVENT_BYTE];
	}

	public void setShedStatus(int shedStatus) {
		loomStatus.put(SHED_BYTE, (byte) shedStatus);
	}

	public int getShedStatus() {
		return loomStatus.get(SHED_BYTE);
	}

	public static int getShedStatus(byte[] status) {
		return status[SHED_BYTE];
	}

	public void setShaftStatus(int shaftStatus) {
		loomStatus.put(SHAFT_BYTE, (byte) shaftStatus);
	}

	public int getShaftStatus() {
		return loomStatus.get(SHAFT_BYTE);
	}
	public static int getShaftStatus(byte[] status) {
		return status[SHAFT_BYTE];
	}

	public void setWarpStatus(int warpStatus) {
		loomStatus.put(WARP_BYTE, (byte) warpStatus);
	}

	public int getWarpStatus() {
		return loomStatus.get(WARP_BYTE);
	}

	public static int getWarpStatus(byte[] status) {
		return status[WARP_BYTE];
	}

	public void setWeftStatus(int weftStatus) {
		loomStatus.put(WEFT_BYTE, (byte) weftStatus);
	}

	public int getWeftStatus() {
		return loomStatus.get(WEFT_BYTE);
	}

	public static int getWeftStatus(byte[] status) {
		return status[WEFT_BYTE];
	}

	public void setBeaterStatus(int beaterStatus) {
		loomStatus.put(BEATER_BYTE, (byte) beaterStatus);
	}

	public int getBeaterStatus() {
		return loomStatus.get(BEATER_BYTE);
	}
	public static int getBeaterStatus(byte[] status) {
		return status[BEATER_BYTE];
	}

	public void setControlStatus(int controlStatus) {
		loomStatus.put(CONTROL_BYTE, (byte) controlStatus);
	}

	public int getControlStatus() {
		return loomStatus.get(CONTROL_BYTE);
	}

	public static int getControlStatus(byte[] status) {
		return status[CONTROL_BYTE];
	}

	public void setControlFlag(int flag) {
		byte flags = loomStatus.get(CONTROL_BYTE);
		if ((flags & flag) == 0)
			flags += flag;
		loomStatus.put(CONTROL_BYTE, flags);
	}

	public void clearControlFlag(int flag) {
		byte flags = loomStatus.get(CONTROL_BYTE);
		if ((flags & flag) == flag)
			flags -= flag;
		loomStatus.put(CONTROL_BYTE, flags);
	}

	public boolean checkFlag(int flag) {
		byte flags = (byte) loomStatus.get(CONTROL_BYTE);
		return (flags & flag) == flag;
	}

	public static boolean checkFlag(byte[] data, int flag) {
		byte flags = (byte) getControlStatus(data);
		return (flags & flag) == flag;
	}

	public void setFaultStatus(int faultStatus) {
		loomStatus.put(FAULT_BYTE, (byte) faultStatus);
	}

	public int getFaultStatus() {
		return loomStatus.get(FAULT_BYTE);
	}

	public static int getFaultStatus(byte[] status) {
		return status[FAULT_BYTE];
	}

	public void setCurrentLift(int currentLift) {
		loomStatus.putInt(LIFT_BYTE, currentLift);
	}

	public int getCurrentLift() {
		return loomStatus.getInt(LIFT_BYTE);
	}

	public static int getCurrentLift(byte[] status) {
		ByteBuffer bb = ByteBuffer.wrap(status);
		return bb.getInt(LIFT_BYTE);
	}

	public void setMessage(String msg) {
		char[] chars = msg.toCharArray();
		int charIndex = 0;
		int bufIndex = MESSAGE_START;
		while (charIndex < chars.length) {
			if (bufIndex < TOTAL_BYTES - 2) {
				loomStatus.putChar(bufIndex, chars[charIndex]);
				charIndex++;
				bufIndex += 2;
			} else {
				break;
			}
		}
		// overwrite any previous message contents
		while (bufIndex < loomStatus.capacity()) {
			loomStatus.put(bufIndex, (byte) 0);
			bufIndex++;
		}
	}

	public String getMessage() {
		StringBuffer sb = new StringBuffer();
		for (int i = MESSAGE_START; i < loomStatus.capacity(); i += 2) {
			sb.append(loomStatus.getChar(i));
		}
		return sb.toString();
	}

	public static String getMessage(byte[] packet) {
		StringBuffer sb = new StringBuffer();
		ByteBuffer bb = ByteBuffer.wrap(packet);
		for (int i = MESSAGE_START; i < bb.capacity(); i += 2) {
			sb.append(bb.getChar(i));
		}
		return sb.toString();
	}

	/**
	 * Format the current loom information as a structured byte array
	 * 
	 */
	public byte[] toByteArray() {
		return loomStatus.array();
	}

	/**
	 * In response to an event, prepare status information for broadcast
	 * 
	 * @param event
	 *            what has occurred that warrants a status update?
	 * @param msg
	 *            a descriptive string suitable for a tool tip or similar
	 *            notification. ~120 character limit.
	 * @return
	 */
	public byte[] prepareBroadcast(int event, String msg) {
		setEvent(event);
		setMessage(msg);
		return loomStatus.array();
	}
	
	public void log(int event, String msg) {
		byte[] body = prepareBroadcast(event, msg);
		LOGGER.log(Level.INFO, msg);
		if (server!= null) server.send(body);
	}

	public String toString() {
		return toString(loomStatus.array());
	}

	public static String toString(byte[] status) {
		StringBuffer sb = new StringBuffer();
		sb.append("Event Code: " + getEvent(status) + "\n");
		sb.append("Shed: " + getShedStatus(status) + "\t");
		sb.append("Shaft: " + getShaftStatus(status) + "\t");
		sb.append("Warp: " + getWarpStatus(status) + "\t");
		sb.append("Weft: " + getWeftStatus(status) + "\t");
		sb.append("Beater: " + getBeaterStatus(status) + "\n");
		sb.append("Liftplan: " + (checkFlag(status, 1) ? "no plan" : "loaded")
				+ "\t");
		sb.append("Dir: " + (checkFlag(status, 2) ? "reverse" : "forward")
				+ "\t");
		sb.append("Loop: " + (checkFlag(status, 4) ? true : false) + "\t");
		sb.append("Treadle: " + (checkFlag(status, 8) ? "pressed" : "released")
				+ "\n");
		sb.append("Current lift/step: " + getCurrentLift(status) + "\n");
		sb.append("Message: " + getMessage(status));
		return sb.toString();
	}

	// public static void main(String[] args) { // Test the class
	// LoomStatus.initialize();
	// LoomStatus.setEvent(SHED_TRANSITION);
	// LoomStatus.setShedStatus(SHED_CLOSED);
	// LoomStatus.setShaftStatus(SHAFTS_DISENGAGED);
	// LoomStatus.setWarpStatus(WARP_NO_STATUS);
	// LoomStatus.setWeftStatus(WEFT_NO_STATUS);
	// LoomStatus.setBeaterStatus(BEATER_NO_STATUS);
	// LoomStatus.setControlFlag(PLAN_FLAG);
	// LoomStatus.setControlFlag(LOOPING_FLAG);
	// LoomStatus.setFaultStatus(FAULT_NONE);
	// LoomStatus.setCurrentLift(52);
	// System.out.println(LoomStatus.toString(LoomStatus.toByteArray()));
	// LoomStatus.clearControlFlag(LOOPING_FLAG);
	// System.out.println(LoomStatus
	// .toString(LoomStatus.prepareBroadcastPacket(0, "Test2")));
	//
	// }

}
