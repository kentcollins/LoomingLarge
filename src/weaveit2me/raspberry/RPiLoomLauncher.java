package weaveit2me.raspberry;

import java.io.IOException;

import weaveit2me.core.CommandServer;
import weaveit2me.core.PickProvider;
import weaveit2me.core.Status;
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
	 *            if present, three values: TCP port number for commands, UDP
	 *            port and alternate multicast group for status broadcasts
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		int commandPort = 1793;
		int multicastPort = 1884;
		String multicastGroup = "225.6.7.8";
		RPiLoom loom = RPiLoom.getInstance();
		PickProvider picker = new PickProvider();
		loom.setPickProvider(picker);
		Status status = (Status) loom.getStatus();
		StatusServer statusServer = new StatusServer(multicastGroup,multicastPort);
		status.registerServer(statusServer);
		loom.startup(); // default: 8 shafts
		CommandServer commandServer = new CommandServer(commandPort, loom);
		commandServer.run();
		// socket blocks while listening for loom commands
		loom.shutdown();
		System.exit(0);
	}
}
