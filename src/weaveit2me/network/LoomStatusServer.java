package weaveit2me.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import weaveit2me.core.LoomStatus;
import weaveit2me.raspberry.RPiLoomController;

/***
 * A multicast server for sharing loom status with concerned devices.
 * 
 * @author kentcollins
 *
 */
public class LoomStatusServer extends Thread {

	protected MulticastSocket socket = null;
	protected boolean inService = true;
	public static final String MULTICAST_GROUP = "225.6.7.8";
	public static final int MULTICAST_PORT = 1884;
	public static final int MAX_LENGTH = 1024;
	private InetAddress group;
	private static final Logger LOGGER = Logger
			.getLogger(LoomStatusServer.class.getName());

	public LoomStatusServer() throws IOException {
		this("LoomStatusServer");
	}

	public LoomStatusServer(String name) throws IOException {
		super(name);
		socket = new MulticastSocket(MULTICAST_PORT);
		group = InetAddress.getByName(MULTICAST_GROUP);
	}
	
	public void run() {
		while (inService) {}
		socket.close();
	}

	public void send(byte[] msg) {
		DatagramPacket p = new DatagramPacket(msg, msg.length, group, MULTICAST_PORT);
		try {
			socket.send(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendAndLog(int event, String msg) {
		byte[] body = LoomStatus.prepareBroadcast(event, msg);
		send(body);
		LOGGER.log(Level.INFO, msg);

	}
	
	public void shutdown() {
		sendAndLog(LoomStatus.CONTROL_EVENT, "Shutting down the status server");
		inService = false;
	}

}
