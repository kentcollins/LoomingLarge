package weaveit2me.raspberry;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import weaveit2me.core.Loom;
import weaveit2me.core.PickProvider;
import weaveit2me.core.ServiceManager;

/**
 * Translates loom commands into electronic signals for the Raspberry Pi
 * 
 * @author kentcollins
 *
 */
public class RPiLoom implements Loom {

	private static RPiLoom currentInstance = null;
	private ServiceManager mgr = null;
	private PrintWriter out = null;

	private int numShafts = 8; // affects shift register operations
	private static List<Integer> currentShaftPicks; // latest data
	private static final List<Integer> SELECT_NO_SHAFTS = Arrays
			.asList(new Integer[]{0});
	//private Status status = new Status();
	private PickProvider pickProvider;

	private static final GpioController gpio = GpioFactory.getInstance();
	private static final Logger LOGGER = Logger
			.getLogger(RPiLoom.class.getName());

	private GpioPinDigitalOutput ssrData; // serial data
	private GpioPinDigitalOutput ssrClock; // clock performs shift
	private GpioPinDigitalOutput ssrStrobe; // latches (stores) data
	private GpioPinDigitalOutput ssrOEnable; // makes data available
	private GpioPinDigitalOutput servoEnable; // enables HC153s
	private GpioPinDigitalInput treadleSwitch; // pressed or released

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
		treadleSwitch = gpio.provisionDigitalInputPin(RaspiPin.GPIO_11);
		treadleSwitch.setDebounce(500);
		treadleSwitch.setPullResistance(PinPullResistance.PULL_DOWN);
		treadleSwitch.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(
					GpioPinDigitalStateChangeEvent event) {
				out.println("Treadle pressed");
				if (event.getEdge().equals(PinEdge.RISING)) {
					pickShafts();
				}
			}
		});

	}

	@Override
	public void startup() {
		out.print("Loom startup complete");
	}

	public void shutdown() {
		out.println("Received shutdown command");
		gpio.shutdown();
	}

	/**
	 * Translates a desired shaft pattern into corresponding signals for each
	 * GPIO pin.
	 * 
	 * @param String
	 *            a space delimited sequence of integers
	 */
	@Override
	public void pickShafts() {
		currentShaftPicks = pickProvider.getNextPick();
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
		out.println("Opened shed");
	}

	private void loadShaftDataRegister(List<Integer> currentShaftPicks2)
			throws InterruptedException {
		ssrData.setState(PinState.LOW);
		ssrClock.setState(PinState.LOW);
		ssrStrobe.setState(PinState.LOW);
		ssrOEnable.setState(PinState.LOW);
		Thread.sleep(0, 500);
		for (Integer i = 1; i <= numShafts; i++) {
			if (currentShaftPicks2.contains(i)) {
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
		out.println("Shafts engaging");
		servoEnable.setState(PinState.HIGH);
		Thread.sleep(500);
		servoEnable.setState(PinState.LOW);
		loadShaftDataRegister(SELECT_NO_SHAFTS);
		Thread.sleep(1000);
		out.println("Shafts engaged");
	}

	private void lift() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeShed() {
		out.println("Closed shed");

	}

	@Override
	public void beat() {
		out.println("Beat not implemented");

	}

	@Override
	public void weave() {
		out.println("Weave not implemented");

	}

	@Override
	public void wind() {
		out.println("Wind not implemented");
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

	public void setPickProvider(PickProvider picker) {
		this.pickProvider = picker;

	}

	public void setResponseSocket(PrintWriter out) {
		this.out = out;
	}

	public ServiceManager getServiceManager() {
		return mgr;
	}

	public void setServiceManager(ServiceManager mgr) {
		this.mgr = mgr;
	}

}
