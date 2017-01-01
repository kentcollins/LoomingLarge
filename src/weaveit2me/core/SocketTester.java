package weaveit2me.core;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketTester {

	public static void main(String[] args) throws URISyntaxException {
		final Socket sock = IO.socket("http://192.168.0.28:3000/");

		sock.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

			public void call(Object... args) {
				sock.emit("loom command", "hi");
				System.out.println("EVENT_CONNECT callback processed");
			}

		}).on("loom command", new Emitter.Listener() {

			public void call(Object... args) {
				System.out.println("Received a loom command: "+args[0]);
			}

		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

			public void call(Object... args) {
				System.out.println("EVENT_DISCONNECT callback processed");
			}

		});
		sock.connect();
		System.out.println("socket appears to have connected"+sock.toString());
		while(true) {}

	}
}
