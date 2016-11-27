package weaveit2me.looms.rpi;

import weaveit2me.looms.LoomSocket;

/**
 * Initializes a RaspberryPi loom controller and makes it available on the local
 * network at the specified port.
 * 
 * @author kentcollins
 *
 */
public class RPiLoomRunner {

	/**
	 * 
	 * @param args
	 *            - the port number on which to listen for connections
	 */
	public static void main(String[] args) {
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out
					.println("The requested port is not a valid port address: "
							+ args[0]);
			System.exit(-1);
		}
		final RPiLoomController loom = RPiLoomController.getInstance();
		loom.setup();
		LoomSocket socket = new LoomSocket(port, loom);
		socket.run();
		// socket blocks while listening for loom commands
		loom.shutdown();
		System.exit(0);
	}
}
