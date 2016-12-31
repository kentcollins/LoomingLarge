package weaveit2me.raspberry;

import java.io.IOException;

import weaveit2me.client.LiftPlan;
import weaveit2me.core.PickProvider;
import weaveit2me.core.ServiceManager;

/**
 * Main method for launching a Raspberry Pi loom. Initializes the loom and the
 * service manager and ensures one can reference the other.
 * 
 */
public class LoomLauncher {

	private static int servicePort = 1793;

	/**
	 * May be called with a TCP port number; if not, will use default
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length > 0) {
			try {
				servicePort = Integer.parseInt(args[0]);
			} catch (NumberFormatException ex) {
				servicePort = 1793;
			}
		}
		RPiLoom loom = RPiLoom.getInstance();
		PickProvider picker = new PickProvider();
		LiftPlan defaultPlan = new LiftPlan();
		picker.setPatternMap(defaultPlan.getPicks());
		picker.setPatternMode(PickProvider.LIFTPLAN_MODE);
		picker.setLoop(PickProvider.LOOP);
		loom.setPickProvider(picker);
		ServiceManager mgr = new ServiceManager(servicePort, loom, picker);
		mgr.run();
		// blocks while ServiceManger handles loom commands
		// continues when ServiceManager stops running
		loom.shutdown();
		System.exit(0);
	}
}
