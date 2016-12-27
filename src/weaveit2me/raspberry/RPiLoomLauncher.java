package weaveit2me.raspberry;

import java.io.IOException;

import weaveit2me.core.CommandServer;
import weaveit2me.core.LoomStatus;
import weaveit2me.network.StatusServer;

/**
 * Main method for launching a Raspberry Pi controlled loom service. Spawns two
 * threads -- one for listening to commands and the other for broadcasting
 * status change events.
 * 
 */
public class RPiLoomLauncher {

	/**
	 * 
	 * @param args
	 *            - the TCP port number for commands - the UDP port for status
	 *            broadcasts - the multicast group for broadcasts
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		int commandPort = 1793;
		int multicastPort = 1884;
		String multicastGroup = "225.6.7.8";

		RPiLoom loom = RPiLoom.getInstance();
		LoomStatus status = (LoomStatus) loom.getStatus();
		StatusServer statusServer = new StatusServer("Status Server",
				multicastGroup, multicastPort);
		statusServer.start();
		status.registerServer(statusServer);
		loom.startup(); // default: 8 shafts

		CommandServer socket = new CommandServer(commandPort, loom);
		socket.run();
		// socket blocks while listening for loom commands
		loom.shutdown();
		System.exit(0);
	}
}
