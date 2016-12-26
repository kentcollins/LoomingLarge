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
	protected boolean broadcastRequested = false;
	public static final String MULTICAST_GROUP = "203.17.93.0";
	public static final int MULTICAST_PORT = 1793;
	public static final int MAX_LENGTH = 1024;
	private LoomController loom = null;

	public LoomStatusServer() throws IOException {
		this("LoomStatusServer");
	}

	public LoomStatusServer(String name) throws IOException {
		super(name);
		socket = new MulticastSocket(MULTICAST_PORT);
	}

	public void run() {
		while (inService) {
			if (loom!=null && broadcastRequested) {
				try {
					byte[] buf = loom.getStatus();
					InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
					DatagramPacket packet;
					packet = new DatagramPacket(buf, buf.length, group, MULTICAST_PORT);
					socket.send(packet);
					broadcastRequested = false;
				} catch (IOException e) {
					e.printStackTrace();
					inService = false;
				}
			}
		}
		socket.close();
	}

	public void requestBroadcast() {
		broadcastRequested = true;
	}
	
	public void attach(LoomController loom) {
		this.loom = loom;
	}

}
