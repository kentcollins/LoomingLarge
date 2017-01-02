package weaveit2me.raspberry;

import java.io.IOException;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import weaveit2me.client.LiftPlan;
import weaveit2me.core.PickProvider;

/**
 * Main method for launching a Raspberry Pi loom. Initializes the
 * loom and the service manager and ensures one can reference the
 * other.
 * 
 */
public class LoomLauncher {

//	private static int servicePort = 1793;
	private static boolean shouldBeRunning = true;


	/**
	 * May be called with a TCP port number; if not, will use default
	 * 
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
//		if (args.length > 0) {
//			try {
//				servicePort = Integer.parseInt(args[0]);
//			} catch (NumberFormatException ex) {
//				servicePort = 1793;
//			}
//		}
		RPiLoom loom = RPiLoom.getInstance();
		PickProvider picker = new PickProvider();
		LiftPlan defaultPlan = new LiftPlan();
		picker.setPatternMap(defaultPlan.getPicks());
		picker.setPatternMode(PickProvider.LIFTPLAN_MODE);
		picker.setLoop(PickProvider.LOOP);
		loom.setPickProvider(picker);
		System.out.println("Starting the socket adapter");
		final Socket sock = IO.socket("http://192.168.0.28:3000/");

		sock.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

			public void call(Object... args) {
				sock.emit("loom command", "hi");
				System.out.println("EVENT_CONNECT callback processed");
			}

		}).on("loom command", new Emitter.Listener() {

			public void call(Object... args) {
				System.out.println("Received a loom command: " + args);
				String command = ((String) args[0]).trim().toUpperCase();
				if ("EXIT".equals(command)) {
					shouldBeRunning = false;
				}
			}

		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

			public void call(Object... args) {
				System.out.println("EVENT_DISCONNECT callback processed");
			}

		});
		sock.connect();
		System.out.println("Entering continuous operating loop");
		while(shouldBeRunning) {
			// EXIT command will set to false
		}
		loom.shutdown();
		System.exit(0);
	}
}
