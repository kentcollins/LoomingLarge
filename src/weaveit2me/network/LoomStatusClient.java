package weaveit2me.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import weaveit2me.core.LoomStatus;

/***
 * Listens for loom status broadcasts. On OSx may need to enable multicasting
 * specifically for the given port. Additional details:
 * http://www.thomasqvarnstrom.com/2015/06/multicasting-routing-on-mac.html and
 * also run client with VM option: -Djava.net.preferIPv4Stack=true
 *
 */

public class LoomStatusClient extends Thread {
	
	private static final String DEFAULT_GROUP = "225.6.7.8";
	private static final int DEFAULT_PORT = 1884;

	public static void main(String[] args) throws IOException {

		try (MulticastSocket socket = new MulticastSocket(
				DEFAULT_PORT);) {
			InetAddress address = InetAddress
					.getByName(DEFAULT_GROUP);
			socket.joinGroup(address);

			DatagramPacket packet;

			while (true) {
				byte[] buf = new byte[LoomStatus.TOTAL_BYTES];
				packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				System.out.println(LoomStatus.toString(packet.getData()));
			}
		}
	}
}
