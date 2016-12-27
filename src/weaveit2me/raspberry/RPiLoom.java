package weaveit2me.raspberry;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import weaveit2me.core.Loom;
import weaveit2me.core.Status;

/**
 * Translates loom commands into electronic signals for the Raspberry Pi
 * 
 * @author kentcollins
 *
 */
public class RPiLoom implements Loom {

	private static RPiLoom currentInstance = null;

	private int numShafts = 8; // informs shift register ops
	private static SortedSet<Integer> currentShaftPicks; // latest data
	private static final SortedSet<Integer> SELECT_NO_SHAFTS = new TreeSet<Integer>();
	private Status status = new Status();

	private static final GpioController gpio = GpioFactory.getInstance();
	private static final Logger LOGGER = Logger
			.getLogger(RPiLoom.class.getName());

	private GpioPinDigitalOutput ssrData; // serial data
	private GpioPinDigitalOutput ssrClock; // clock performs shift
	private GpioPinDigitalOutput ssrStrobe; // latches (stores) data
	private GpioPinDigitalOutput ssrOEnable; // makes data available
	private GpioPinDigitalOutput servoEnable; // enables HC153s

	/**
	 * Returns a singleton controller.
	 */
	public static RPiLoom getInstance() {
		if (currentInstance == null)
			currentInstance = new RPiLoom();
		return currentInstance;
	}

	/**
	 * Constructor using default pin assignments.
	 */
	private RPiLoom() {
		ssrData = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00,
				PinState.LOW);
		ssrData.setShutdownOptions(true, PinState.LOW);
		ssrClock = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02,
				PinState.LOW);
		ssrClock.setShutdownOptions(true, PinState.LOW);
		ssrStrobe = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01,
				PinState.LOW);
		ssrStrobe.setShutdownOptions(true, PinState.LOW);
		ssrOEnable = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04,
				PinState.HIGH);
		ssrOEnable.setShutdownOptions(true, PinState.LOW);
		servoEnable = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03,
				PinState.LOW);
		servoEnable.setShutdownOptions(true, PinState.LOW);
	}

	@Override
	public void startup() {
		status.publish(Status.CONTROL_EVENT, "Loom has performed startup");
	}

	/**
	 * Translates a desired shaft pattern into corresponding signals for each
	 * GPIO pin.
	 * 
	 * @param String
	 *            a space delimited sequence of integers
	 */
	@Override
	public void pickShafts(Integer[] shafts) {
		SortedSet<Integer> picks = new TreeSet<Integer>();
		for (int i = 0; i < shafts.length; i++) {
			// disregard shafts we cannot operate
			Integer proposed = shafts[i];
			if (proposed > 0 && proposed <= numShafts) {
				picks.add(proposed);
			}
		}
		currentShaftPicks = picks;
		status.publish(Status.CONTROL_EVENT, "New shaft selection");
	}

	@Override
	public void openShed() {
		try {
			loadShaftDataRegister();
			engageSelectedShafts();
		} catch (InterruptedException ex) {
			logFault(ex);
		}
		lift();
		status.setShedStatus(Status.SHED_OPEN);
	}

	private void loadShaftDataRegister(SortedSet<Integer> picks)
			throws InterruptedException {
		ssrData.setState(PinState.LOW);
		ssrClock.setState(PinState.LOW);
		ssrStrobe.setState(PinState.LOW);
		ssrOEnable.setState(PinState.LOW);
		Thread.sleep(0, 500);
		for (Integer i = 1; i <= numShafts; i++) {
			if (picks.contains(i)) {
				ssrData.setState(PinState.HIGH);
			} else {
				ssrData.setState(PinState.LOW);
			}
			Thread.sleep(0, 500);
			ssrClock.setState(PinState.HIGH);
			Thread.sleep(0, 500);
			ssrClock.setState(PinState.LOW);
		}
		ssrStrobe.setState(PinState.HIGH);
		Thread.sleep(0, 500);
		ssrStrobe.setState(PinState.LOW);
		ssrOEnable.setState(PinState.HIGH);
	}

	private void loadShaftDataRegister() throws InterruptedException {
		loadShaftDataRegister(currentShaftPicks);
	}

	private void engageSelectedShafts() throws InterruptedException {
		status.setShaftStatus(Status.SHAFTS_ENGAGING);
		servoEnable.setState(PinState.HIGH);
		Thread.sleep(500);
		servoEnable.setState(PinState.LOW);
		loadShaftDataRegister(SELECT_NO_SHAFTS);
		Thread.sleep(1000);
		status.setShaftStatus(Status.SHAFTS_ENGAGED);
	}

	private void lift() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeShed() {
		status.setShedStatus(Status.SHED_CLOSING);
		status.setShedStatus(Status.SHED_CLOSED);

	}

	@Override
	public void beat() {
		status.setBeaterStatus(Status.BEATER_ENGAGING);
		status.setBeaterStatus(Status.BEATER_ENGAGED);
		status.setBeaterStatus(Status.BEATER_RELEASING);
		status.setBeaterStatus(Status.BEATER_RELEASED);

	}

	@Override
	public void weave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void wind() {
		status.setWarpStatus(Status.WARP_WINDING_ON);
		status.setWarpStatus(Status.WARP_WINDING_OFF);
	}

	public void shutdown() {
		status.publish(Status.CONTROL_EVENT, "Loom shutting down.");
		gpio.shutdown();
	}

	@Override
	public Object getStatus() {
		return status;
	}

	@Override
	public void custom(String s) {
		System.out.println("Received custom command " + s);
		String[] commands = s.split(" ");
		String arg1 = commands.length > 0
				? commands[0].trim().toUpperCase()
				: "ARG1";
		String arg2 = commands.length > 1
				? commands[1].trim().toUpperCase()
				: "ARG2";
		GpioPinDigitalOutput chosen = null;
		PinState state = null;
		if (arg1.equals("STROBE")) {
			chosen = ssrClock;
		} else if (arg1.equals("CLOCK")) {
			chosen = ssrStrobe;
		} else if (arg1.equals("DATA")) {
			chosen = ssrData;
		} else if (arg1.equals("OE")) {
			chosen = ssrOEnable;
		} else if (arg1.equals("SERVO")) {
			chosen = servoEnable;
		}
		if (arg2.equals("HIGH")) {
			state = PinState.HIGH;
		} else if (arg2.equals("LOW")) {
			state = PinState.LOW;
		}
		if (chosen != null && state != null) {
			System.out.println("Setting " + arg1 + " to " + arg2);
			chosen.setState(state);
		}

	}

	private void logFault(Exception ex) {
		LOGGER.log(Level.SEVERE, ex.toString(), ex);
	}

	public void setNumShafts(int n) {
		numShafts = n;
	}

}
