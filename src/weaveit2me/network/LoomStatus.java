package weaveit2me.network;

/***
 * Packs loom status into a variable length datagram packet
 *
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
	public static final int WARP_NO_STATUS=0;
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
	public static final int BEATER_ENGAGING= 2;
	public static final int BEATER_ENGAGED = 3;
	public static final int BEATER_RELEASING = 4;
	// information about the liftplan
	public static final int CTRL_NO_PLAN = 0;
	public static final int CTRL_LIFTPLAN_LOADED = 1;
	public static final int CTRL_SEQUENCE_FWD = 2;
	public static final int CTRL_SEQUENCE_REV = 4;
	public static final int CTRL_SEQUENCE_LOOP = 8;
	public static final int CTRL_SEQUENCE_NO_LOOP = 16;
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

}
