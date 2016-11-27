package weaveit2me.looms.rpi;

import weaveit2me.looms.LoomController;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Translates loom commands into electronic signals for the Raspberry Pi
 * 
 * @author kentcollins
 *
 */
public class RPiLoomController implements LoomController {

	private static final GpioController gpio = GpioFactory.getInstance();
	private static GpioPinDigitalOutput[] shafts;
	public static final Pin[] DEFAULTS = { RaspiPin.GPIO_00, RaspiPin.GPIO_01,
			RaspiPin.GPIO_02, RaspiPin.GPIO_03, RaspiPin.GPIO_04,
			RaspiPin.GPIO_05, RaspiPin.GPIO_06, RaspiPin.GPIO_07 };
	private static RPiLoomController currentInstance = null;

	/**
	 * Singleton controller using default pin assignments.
	 */
	private RPiLoomController() {
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

	public void setup() {
		mapShaftsToPins(RPiLoomController.DEFAULTS);
	}

	/**
	 * Translates a desired shaft pattern into corresponding signals for each
	 * GPIO pin.
	 * 
	 * @param bitPattern
	 *            a pattern of 1's and 0's specifying which shafts will be
	 *            prepared for lifting. The least significant bit corresponds to
	 *            the first shaft.
	 */
	@Override
	public void setShafts(int bitPattern) {
		/*
		 * Starting from the least significant bit, test each bit in the
		 * provided integer to see if it holds a 1. If so, set the corresponding
		 * GPIO pin to HIGH; otherwise, set to LOW. Reads only the number of
		 * places for which we have shafts -- disregards remaining upper bits
		 * (ie for an eight shaft loom, reads only the eight least significant
		 * bits.)
		 */
		for (int p = 0; p < shafts.length; p++) {
			boolean shouldBeEnergized = ((bitPattern >> p) & 1) == 1;
			if (shouldBeEnergized) {
				shafts[p].high();
			} else
				shafts[p].low();
		}
	}

	@Override
	public void openShed() {
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

	/**
	 * Maps the shafts on the loom to GPIO pins.
	 * 
	 * @param pins
	 *            an array of hardware pin numbers. The number of pins provided
	 *            here determines the number of shafts that will be controlled.
	 *            gpios[0] identifies the first shaft, gpios[1] the second, etc.
	 * 
	 */
	public void mapShaftsToPins(Pin[] pins) {
		shafts = new GpioPinDigitalOutput[pins.length];
		for (int i = 0; i < shafts.length; i++) {
			GpioPinDigitalOutput p = gpio.provisionDigitalOutputPin(pins[i],
					"Shaft_" + i, PinState.LOW);
			p.setShutdownOptions(true, PinState.LOW);
			shafts[i] = p;
		}
	}

}
