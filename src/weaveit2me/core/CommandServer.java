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

public class CommandServer implements Runnable {

	private Loom loom;
	private int commandPort;


	public CommandServer(int commandPort, Loom loom) {
		this.commandPort = commandPort;
		this.loom = loom;
	}

	/**
	 * Run as a standalone, blocking process -- myLoomServer.run();
	 * 
	 * Run as a nonblocking thread -- (new Thread(myLoomSocket)).start();
	 */
	@Override
	public void run() {
		System.out.println("Opening loom service on port: " + commandPort);
		try (ServerSocket serverSocket = new ServerSocket(commandPort);
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
					Commands command = null;
					try {
						command = Commands.valueOf(commands[0].toUpperCase());
						out.println("COMMAND_OK " + inputLine);
						switch (command) {
						case PICK:
							loom.pickShafts();
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
						case STARTUP:
							loom.startup();
						case SHUTDOWN:
							loom.shutdown();
						case CUSTOM:
							StringBuilder sb = new StringBuilder();
							for (int i = 1; i<commands.length; i++) {
								sb.append(commands[i]+" ");
							}
							loom.custom(sb.toString());
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
