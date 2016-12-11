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

	private GpioPinDigitalOutput dataPin;
	private GpioPinDigitalOutput strobePin;

	/**
	 * Singleton controller using default pin assignments.
	 */
	private RPiLoomController() {
		dataPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
		dataPin.setShutdownOptions(true, PinState.LOW);
		strobePin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
		strobePin.setShutdownOptions(true, PinState.LOW);
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
	 * @param shafts
	 */
	public void setup(int shafts) {
		MAX_SHAFTS = shafts;
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
		latchShafts();
	}

	private SortedSet<Integer> sanitizeSelection() {
		// place shafts in order, remove duplicates
		SortedSet<Integer> picks = new TreeSet<Integer>();
		for (int i = 0; i < selected.length; i++) {
			// disregard shafts we cannot operate
			Integer proposed = selected[i];
			if (proposed >0 && proposed <= MAX_SHAFTS) {
				picks.add(proposed);
			}
		}
		return picks;
	}

	private void loadDataIntoShiftRegister(SortedSet<Integer> orderedPicks) {
		dataPin.setState(PinState.LOW);
		strobePin.setState(PinState.LOW);
		// pause
		for (Integer i = 1; i<=MAX_SHAFTS; i++) {
			if (orderedPicks.contains(i)) {
				dataPin.setState(PinState.HIGH);
				
			} else {
				dataPin.setState(PinState.LOW);
			}
			// pause
			strobePin.setState(PinState.HIGH);
			// pause
			strobePin.setState(PinState.LOW);
			//pause
		}
	}

	private void latchShafts() {
		// Apply latching power for a little while

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
			response+="no shafts selected yet.";
		}
		return response;
	}

}
