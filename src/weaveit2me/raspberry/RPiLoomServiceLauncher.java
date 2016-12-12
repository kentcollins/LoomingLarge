package weaveit2me.raspberry;

import weaveit2me.core.LoomService;

/**
 * Main method for launching a Raspberry Pi based loom server.
 * 
 */
public class RPiLoomServiceLauncher {

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
					.println("The requested port is not a valid number: "
							+ args[0]);
			System.exit(-1);
		}
		final RPiLoomController loom = RPiLoomController.getInstance();
		loom.startup(); // assumes RPi connected to one 8-bit shift register
		LoomService socket = new LoomService(port, loom);
		socket.run();
		// socket blocks while listening for loom commands
		loom.shutdown();
		System.exit(0);
	}
}
