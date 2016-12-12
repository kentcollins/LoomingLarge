package weaveit2me.raspberry;

import java.util.SortedSet;
import java.util.TreeSet;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import weaveit2me.core.LoomController;

/**
 * Translates loom commands into electronic signals for the Raspberry Pi
 * 
 * @author kentcollins
 *
 */
public class RPiLoomController implements LoomController {

	private static RPiLoomController currentInstance = null;
	private static int MAX_SHAFTS; // affects shift register ops
	private static Integer[] selected; // current selection

	private static final GpioController gpio = GpioFactory.getInstance();

	private GpioPinDigitalOutput dataPin, shiftPin, strobePin, oenablePin;

	/**
	 * Singleton controller using default pin assignments.
	 */
	private RPiLoomController() {
		// data pin provides serial input signals
		dataPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
		dataPin.setShutdownOptions(true, PinState.LOW);
		// shift pin (clock) advances signals internally
		shiftPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
		shiftPin.setShutdownOptions(true, PinState.LOW);
		// strobe pin latches current values to the register
		strobePin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
		strobePin.setShutdownOptions(true, PinState.LOW);
		// oenable pin makes values available 
		oenablePin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
		oenablePin.setShutdownOptions(true, PinState.LOW);
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
		selected = new Integer[] {zero};
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
		loadDataIntoShiftRegister(orderedPicks);
		latchShaftSelections();
		enableShaftPositioners();
		lift();
		disableShaftPositioners();
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

	private void loadDataIntoShiftRegister(SortedSet<Integer> orderedPicks) {
		dataPin.setState(PinState.LOW);
		shiftPin.setState(PinState.LOW);
		strobePin.setState(PinState.LOW);
		oenablePin.setState(PinState.LOW);
		for (Integer i = 1; i <= MAX_SHAFTS; i++) {
			if (orderedPicks.contains(i)) {
				dataPin.setState(PinState.HIGH);

			} else {
				dataPin.setState(PinState.LOW);
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			// provide rising clock edge
			shiftPin.setState(PinState.HIGH);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			shiftPin.setState(PinState.LOW);
		}
		strobePin.setState(PinState.HIGH);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		strobePin.setState(PinState.LOW);
		oenablePin.setState(PinState.HIGH);
	}

	private void latchShaftSelections() {
		// Apply latching signal to the register
		// enable output

	}

	private void enableShaftPositioners() {
		// TODO Auto-generated method stub

	}

	private void lift() {
		// TODO Auto-generated method stub

	}

	private void disableShaftPositioners() {
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
		System.out.println("Received custom command "+s);
		String[] commands = s.split(" ");
		String pin = commands[0].trim().toUpperCase();
		String signal = commands[1].trim().toUpperCase();
		GpioPinDigitalOutput chosen = null;
		PinState state = null;
		if (pin.equals("STROBE")) {
			chosen = shiftPin;
		} else if (pin.equals("CLOCK")) {
			chosen = strobePin;
		} else if (pin.equals("DATA")) {
			chosen = dataPin;
		} else if (pin.equals("OE")) {
			chosen = oenablePin;
		}
		if (signal.equals("HIGH")) {
			state = PinState.HIGH;
		} else if (signal.equals("LOW")) {
			state = PinState.LOW;
		}
		if (chosen!=null && state!=null) {
			System.out.println("Setting "+pin+" to "+signal);
			chosen.setState(state);
		}

	}

	@Override
	public void startup() {
		setup(8);
	}

}
