package weaveit2me.raspberry;

import java.io.IOException;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import weaveit2me.client.LiftPlan;
import weaveit2me.client.WIFReader;
import weaveit2me.core.PickProvider;

/**
 * Main method for launching a Raspberry Pi loom. Initializes the
 * loom and the service manager and ensures one can reference the
 * other.
 * 
 */
public class LoomLauncher {

	private static boolean shouldBeRunning = true;


	/**
	 * May be called with a TCP port number; if not, will use default
	 * 
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
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
				String s = (String) args[0];
				String[] commands = s.split(" ");
				System.out.println("Commands in the string: ");
				for(String s2:commands) System.out.println(s2);
				String majorCommand = commands[0].trim().toUpperCase();
				System.out.println("Major command "+majorCommand);
				if ("EXIT".equals(majorCommand)) {
					shouldBeRunning = false;
				} else if ("LOAD".equals(majorCommand)) {
					String url = (String) commands[1];
					LiftPlan lp = new LiftPlan();
					try {
						lp.loadFromURL(url);
						System.out.println("New Liftplan: "+lp);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Should have printed a lift plan");
				}
			}

		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

			public void call(Object... args) {
				System.out.println("EVENT_DISCONNECT callback processed");
			}

		});
		loom.setStatusSocket(sock);
		sock.connect();
		System.out.println("Entering continuous operating loop");
		while(shouldBeRunning) {
			// EXIT command will set to false
		}
		loom.shutdown();
		System.exit(0);
	}
}
