package weaveit2me.network;

import java.nio.ByteBuffer;

/***
 * Packs loom status into a byte[] for incorporation into a variable length
 * datagram packet. Loom status is typically updated in response to an event.
 * 
 * 	Byte Contents 
 * 	0 triggering event 
 * 	1 shed status 
 * 	2 shaft status 
 * 	3 warp status 
 * 	4 weft status 
 * 	5 beater status 
 * 	6 control information 
 * 	7 fault information 
 * 	8-11 current liftplan step number 
 * 	var free form information (loom data?)
 */

public class LoomStatus {

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
	// information about the plan and switch controllers
	public static final int CTRL_NO_LIFTPLAN = 0;
	public static final int CTRL_LIFTPLAN_LOADED = 1;
	public static final int CTRL_DIRECTION_FWD = 2;
	public static final int CTRL_DIRECTION_REV = 4;
	public static final int CTRL_PLAN_LOOP = 8;
	public static final int CTRL_PLAN_NO_LOOP = 16;
	public static final int CTRL_TREADLE_RELEASED = 32;
	public static final int CTRL_TREADLE_PRESSED = 164;
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
	public static final int ALL_EVENTS = 0; // all changes
	public static final int TRANSITION_EVENT = 1; // transition in progress
	public static final int STATE_CHANGE_EVENT = 2; // transition completed
	public static final int FAULT_EVENT = 3; // new fault occurred
	public static final int CONTROL_CHANGE_EVENT = 4; // treadle or switch
	public static final int SHAFT_SELECTION_EVENT = 5; // picks changed
	public static final int PLAN_CHANGE = 6; // different wif loaded

	private static ByteBuffer loomStatus;

	private LoomStatus() {
	}

	public static void initialize() {
		loomStatus = ByteBuffer.allocate(256);
		setEvent(ALL_EVENTS);
	}

	public static void setEvent(int event) {
		loomStatus.put(0, (byte) event);
	}
	
	public static int getEvent(byte[] status) {
		return status[0];
	}

	public static void setShedStatus(int shedStatus) {
		loomStatus.put(1, (byte) shedStatus);
	}
	
	public static int getShedStatus(byte[] status) {
		return status[1];
	}

	public static void setShaftStatus(int shaftStatus) {
		loomStatus.put(2, (byte) shaftStatus);
	}
	
	public static int getShaftStatus(byte[] status) {
		return status[2];
	}

	public static void setWarpStatus(int warpStatus) {
		loomStatus.put(3, (byte) warpStatus);
	}
	
	public static int getWarpStatus(byte[] status) {
		return status[3];
	}

	public static void setWeftStatus(int weftStatus) {
		loomStatus.put(4, (byte) weftStatus);
	}
	
	public static int getWeftStatus(byte[] status) {
		return status[4];
	}
	
	public static void setBeaterStatus(int beaterStatus) {
		loomStatus.put(5, (byte) beaterStatus);
	}
	
	public static int getBeaterStatus(byte[] status) {
		return status[5];
	}
	
	public static void setControlStatus(int controlStatus) {
		loomStatus.put(6, (byte) controlStatus);
	}
	
	public static int getControlStatus(byte[] status) {
		return status[6];
	}
	
	public static void setFaultStatus(int faultStatus) {
		loomStatus.put(7, (byte) faultStatus);
	}
	
	public static int getFaultStatus(byte[] status) {
		return status[7];
	}
	
	public static void setCurrentLift(int currentLift) {
		loomStatus.putInt(8, currentLift);
	}
	
	public static int getCurrentLift(byte[] status) {
		ByteBuffer bb = ByteBuffer.wrap(status);
		return bb.getInt(8);
	}
	
	public static ByteBuffer read() {
		return loomStatus;
	}
	

	/**
	 * In response to an event, generate the data needed for a datagram packet
	 * 
	 * @param event
	 * @return
	 */
	public byte[] packData(int event) {
		return null;
	}

	public static void main(String[] args) {
		// Test the class
		LoomStatus.initialize();
		LoomStatus.setEvent(FAULT_EVENT);
		LoomStatus.setShedStatus(SHED_CLOSED);
		LoomStatus.setShaftStatus(SHAFTS_DISENGAGED);
		LoomStatus.setWarpStatus(WARP_NO_STATUS);
		LoomStatus.setWeftStatus(WEFT_NO_STATUS);
		LoomStatus.setBeaterStatus(BEATER_NO_STATUS);
		LoomStatus.setControlStatus(CTRL_NO_LIFTPLAN+CTRL_DIRECTION_FWD+CTRL_PLAN_LOOP);
		LoomStatus.setFaultStatus(FAULT_NONE);
		LoomStatus.setCurrentLift(52);
		byte[] status = LoomStatus.read().array();
		System.out.println(LoomStatus.read().array());
	}

}
