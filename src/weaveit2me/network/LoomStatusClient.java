package weaveit2me.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class LoomStatusClient extends Thread {

	public static void main(String[] args) throws IOException {

		MulticastSocket socket = new MulticastSocket(LoomStatusServer.MULTICAST_PORT);
		InetAddress address = InetAddress.getByName(LoomStatusServer.MULTICAST_GROUP);
		socket.joinGroup(address);

		DatagramPacket packet;

		// get a few quotes
		for (int i = 0; i < 5; i++) {
			byte[] buf = new byte[LoomStatusServer.MAX_LENGTH];
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			String received = new String(packet.getData(), 0, packet.getLength());
			System.out.println("Loom update: " + received);
		}

		socket.leaveGroup(address);
		socket.close();
	}

}
