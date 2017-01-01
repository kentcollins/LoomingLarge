package weaveit2me.core;

import java.io.PrintWriter;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class IOSocketAdapter implements Runnable {

	io.socket.client.Socket nodeClient;
	java.net.Socket loomService;
	PrintWriter toLoom;
	boolean keepRunning = true;

	public IOSocketAdapter() {
	}

	@Override
	public void run() {
		try {
			nodeClient = IO.socket("http://192.168.0.28:3000/");
			loomService = new java.net.Socket("192.168.0.28", 1793);
			toLoom = new PrintWriter(loomService.getOutputStream(), true);
		} catch (Exception e) {
			
		}
		nodeClient.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				nodeClient.emit("loom command", "hi");
				System.out.println("Adapter EVENT_CONNECT callback processed");
			}
		}).on("loom command", new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("Loom command received by adapter: "+args[0]);
				toLoom.println(args[0]);
			}

		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				System.out.println("Adapter EVENT_DISCONNECT callback processed");
				keepRunning = false;
			}
		});
		while(keepRunning) {}
	}
}
