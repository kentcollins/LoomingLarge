package weaveit2me.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import weaveit2me.core.LoomController;

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

	public void send(byte[] status) {
		DatagramPacket p = new DatagramPacket(status, status.length, group, MULTICAST_PORT);
		try {
			socket.send(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		inService = false;
	}

}
