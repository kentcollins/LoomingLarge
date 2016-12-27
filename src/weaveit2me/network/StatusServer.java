package weaveit2me.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/***
 * A multicast server for broadcasting status to concerned devices.
 * 
 * @author kentcollins
 *
 */
public class StatusServer extends Thread {

	protected MulticastSocket socket = null;
	protected boolean inService = true;
	private InetAddress group;

	public StatusServer(String name, String address, int port) throws IOException {
		super(name);
		group = InetAddress.getByName(address);
		socket = new MulticastSocket(port);

	}
	
	public void run() {
		while (inService) {}
		socket.close();
	}

	public void send(byte[] msg) {
		DatagramPacket p = new DatagramPacket(msg, msg.length, group, socket.getPort());
		try {
			socket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		inService = false;
	}

}
