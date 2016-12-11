package weaveit2me.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * Opens a network socket and passes incoming commands to a loom 
 * controller.  Sits between the network and the loom hardware.
 */

public class LoomService implements Runnable {

	private int requestedPort;
	private LoomController loom;

	public LoomService(int requestedPort, LoomController loom) {
		this.requestedPort = requestedPort;
		this.loom = loom;
	}

	/**
	 * Run as a standalone, blocking process -- myLoomServer.run();
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
						case PICK:
							if (commands.length > 1) {
								try {
									int shafts = commands.length-1;
									int[] picks = new int[shafts];
									for (int i = 0; i<commands.length-1; i++) {
										picks[i] = Integer.parseInt(commands[i+1]);
									}
									loom.pickShafts(picks);
								} catch (NumberFormatException e) {
									out.println("INVALID_ARGUMENT "
											+ Arrays.asList(commands));
									break;
								}
							} else {
								loom.pickShafts(new int[] {0});
							}
							break;
						case OPEN:
							loom.openShed();
							break;
						case CLOSE:
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
						case STATUS:
							out.println(loom.getStatus());
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
