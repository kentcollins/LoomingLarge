package weaveit2me.raspberry;

import weaveit2me.core.CommandServer;

/**
 * Main method for launching a Raspberry Pi based loom server.
 * 
 */
public class RPiLoomServiceLauncher {

	/**
	 * 
	 * @param args
	 *            - the TCP port number for commands
	 *            - the UDP port for status broadcasts
	 *            - the multicast group for broadcasts
	 */
	public static void main(String[] args) {
		int port1=1793, port2 = 1884;
		String group = "225.6.7.8";
		try {
			port1 = Integer.parseInt(args[0]);
			port2 = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out
					.println("The requested port is not a valid number: "
							+ args[0]);
			System.exit(-1);
		}
		final RPiLoomController loom = RPiLoomController.getInstance();
		loom.startup(); // default: 8 shafts 
		CommandServer socket = new CommandServer(port1, loom);
		socket.run();
		// socket blocks while listening for loom commands
		loom.shutdown();
		System.exit(0);
	}
}
