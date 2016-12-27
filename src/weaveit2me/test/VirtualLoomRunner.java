package weaveit2me.test;

import weaveit2me.core.Loom;
import weaveit2me.core.CommandServer;

/**
 * Sets up a dummy loom controller on a network socket. Suitable for
 * testing loom command software in the absence of loom hardware.
 * 
 * @author kentcollins
 *
 */

public class VirtualLoomRunner {

	public static void main(String[] args) {
		final Loom loom = new Loom() {

			@Override
			public void openShed() {
				System.out.println("Virtual Loom received command to open shed");

			}

			@Override
			public void closeShed() {
				System.out.println("Virtual Loom received command to close shed");

			}

			@Override
			public void weave() {
				System.out.println("Virtual Loom received command to weave");

			}

			@Override
			public void beat() {
				System.out.println("Virtual Loom received command to beat");

			}

			@Override
			public void wind() {
				System.out.println("Virtual Loom received command to wind warp");

			}

			@Override
			public byte[] getStatus() {
				return "The Virtual Loom Runner is alive.".getBytes();
			}

			@Override
			public void pickShafts(Integer[] selectedShafts) {
				System.out.println("Virtual Loom received command to pick shafts\n\t");
				for (Integer i : selectedShafts) {
					System.out.print(i+" ");
				}
				System.out.println();
			}

			@Override
			public void custom(String s) {
				System.out.println("Virtual Loom received custom command: \n\t"+s);
			}

			@Override
			public void startup() {
				System.out.println("Virtual Loom received command to startup");

			}

			@Override
			public void shutdown() {
				System.out.println("Virtual Loom received command to shutdown");

			}
		};

		CommandServer socket = new CommandServer(1793, loom);
		socket.run();
		// socket blocks while listening for loom commands
		System.exit(0);

	}

}
