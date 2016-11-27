package weaveit2me.looms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Opens a network socket and listens for loom commands. Passes incoming
 * commands to a loom controller until an exit signal is received.
 * 
 * @author kentcollins
 *
 */

public class LoomSocket implements Runnable {

	private int requestedPort;
	private LoomController loom;

	public LoomSocket(int requestedPort, LoomController loom) {
		this.requestedPort = requestedPort;
		this.loom = loom;
	}

	/**
	 * Run as a standalone, blocking process -- myLoomSocket.run();
	 * 
	 * Run as a nonblocking thread -- (new Thread(myLoomSocket)).run();
	 */
	@Override
	public void run() {
		System.out.println("Opening port: " + requestedPort);
		try (ServerSocket serverSocket = new ServerSocket(requestedPort);
				// next line blocks, awaiting a connection
				Socket clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(
						clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));) {
			out.println("CONNECTION_OK");
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				// empty string concludes the session
				if (inputLine.equals("") || inputLine.equalsIgnoreCase("exit")) {
					out.println("COMMAND_OK EXIT");
					break;
				} else {
					String[] commands = inputLine.trim().split(" ");
					LoomCommand command = null;
					try {
						command = LoomCommand.valueOf(commands[0]);
						out.println("COMMAND_OK " + inputLine);
						switch (command) {
						case SET_SHAFTS:
							if (commands.length > 1) {
								try {
									int desiredBits = Integer
											.parseInt(commands[1]);
									loom.setShafts(desiredBits);
								} catch (NumberFormatException e) {
									out.println("INVALID_ARGUMENT "
											+ commands[1]);
									break;
								}
							} else {
								loom.setShafts(0);
							}
							break;
						case OPEN_SHED:
							loom.openShed();
							break;
						case CLOSE_SHED:
							loom.closeShed();
							break;
						case BEAT:
							loom.beat();
							break;
						case WEAVE:
							loom.weave();
							break;
						case WIND:
							loom.wind();
							break;
						default:
							break;
						}
					} catch (IllegalArgumentException e) {
						out.println("UNKNOWN_COMMAND " + inputLine);
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
}
