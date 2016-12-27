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
public class StatusServer implements Runnable {

	protected MulticastSocket socket = null;
	protected boolean inService = true;
	private InetAddress group;
	private int port;

	public StatusServer(String address, int port) throws IOException {
		group = InetAddress.getByName(address);
		socket = new MulticastSocket(port);
		this.port = port;

	}
	
	public void run() {
		System.out.println("Status server is up");
		while (inService) {}
		socket.close();
	}

	public void send(byte[] msg) {
		DatagramPacket p = new DatagramPacket(msg, msg.length, group, port);
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
