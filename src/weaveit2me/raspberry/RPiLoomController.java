package weaveit2me.raspberry;

import java.util.SortedSet;
import java.util.TreeSet;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

import weaveit2me.core.LoomController;

/**
 * Translates loom commands into electronic signals for the Raspberry Pi
 * 
 * @author kentcollins
 *
 */
public class RPiLoomController implements LoomController {

	private static RPiLoomController currentInstance = null;
	private static int MAX_SHAFTS; // informs shift register ops
	private static Integer[] selected; // current selection

	private static final GpioController gpio = GpioFactory.getInstance();

	private GpioPinDigitalOutput ssrData; // serial data 
	private GpioPinDigitalOutput ssrClock; // clock performs shift
	private GpioPinDigitalOutput ssrStrobe; // latches (stores) data
	private GpioPinDigitalOutput ssrOEnable; // makes data available
	private GpioPinDigitalOutput servoEnable; // enables HC153s

	/**
	 * Singleton controller using default pin assignments.
	 */
	private RPiLoomController() {
		ssrData = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
		ssrData.setShutdownOptions(true, PinState.LOW);
		ssrClock = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
		ssrClock.setShutdownOptions(true, PinState.LOW);
		ssrStrobe = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
		ssrStrobe.setShutdownOptions(true, PinState.LOW);
		ssrOEnable = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
		ssrOEnable.setShutdownOptions(true, PinState.LOW);
		servoEnable = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
		servoEnable.setShutdownOptions(true, PinState.LOW);
	}

	/**
	 * Returns the active controller.
	 */
	public static RPiLoomController getInstance() {
		if (currentInstance != null)
			return currentInstance;
		currentInstance = new RPiLoomController();
		return currentInstance;
	}

	/**
	 * Take care of platform specific initialization.
	 * 
	 * @param shafts
	 */
	public void setup(int shafts) {
		MAX_SHAFTS = shafts;
		Integer zero = new Integer(0);
		selected = new Integer[] { zero };
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
		selected = shafts;
	}

	@Override
	public void openShed() {
		SortedSet<Integer> orderedPicks = sanitizeSelection();
		loadShaftDataRegister(orderedPicks);
		latchSelectedShafts();
		lift();
	}

	private SortedSet<Integer> sanitizeSelection() {
		// place shafts in order, remove duplicates
		SortedSet<Integer> picks = new TreeSet<Integer>();
		for (int i = 0; i < selected.length; i++) {
			// disregard shafts we cannot operate
			Integer proposed = selected[i];
			if (proposed > 0 && proposed <= MAX_SHAFTS) {
				picks.add(proposed);
			}
		}
		return picks;
	}

	private void loadShaftDataRegister(SortedSet<Integer> orderedPicks) {
		ssrData.setState(PinState.LOW);
		ssrClock.setState(PinState.LOW);
		ssrStrobe.setState(PinState.LOW);
		ssrOEnable.setState(PinState.LOW);
		for (Integer i = 1; i <= MAX_SHAFTS; i++) {
			if (orderedPicks.contains(i)) {
				ssrData.setState(PinState.HIGH);

			} else {
				ssrData.setState(PinState.LOW);
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			// provide rising clock edge
			ssrClock.setState(PinState.HIGH);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ssrClock.setState(PinState.LOW);
		}
		ssrStrobe.setState(PinState.HIGH);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ssrStrobe.setState(PinState.LOW);
		ssrOEnable.setState(PinState.HIGH);
	}

	private void latchSelectedShafts() {
		enableServos();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disableServos();

	}

	private void enableServos() {
		// mux is low enable
		servoEnable.setState(PinState.LOW);
	}

	private void disableServos() {
		// no signal to servos
		servoEnable.setState(PinState.HIGH);

	}

	private void lift() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeShed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void beat() {
		// TODO Auto-generated method stub

	}

	@Override
	public void weave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void wind() {
		// TODO Auto-generated method stub

	}

	public void shutdown() {
		gpio.shutdown();
	}

	@Override
	public String getStatus() {
		String response = "";
		response += "A-OK\tPicked for latching: ";
		if (selected != null) {
			for (int i : selected) {
				response += i + " ";
			}
		} else {
			response += "no shafts selected yet.";
		}
		return response;
	}

	@Override
	public void custom(String s) {
		System.out.println("Received custom command " + s);
		String[] commands = s.split(" ");
		String arg1 = commands.length > 0 ? commands[0].trim().toUpperCase() : "ARG1";
		String arg2 = commands.length > 1 ? commands[1].trim().toUpperCase() : "ARG2";
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
		} else if (arg1.equals("LATCH")) {
			enableServos();
		} else if (arg1.equals("UNLATCH")) {
			disableServos();
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

	@Override
	public void startup() {
		setup(8);
	}

}
