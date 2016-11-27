package weaveit2me.test;

import weaveit2me.looms.LoomController;
import weaveit2me.looms.LoomSocket;

/**
 * Sets up a dummy loom controller on a network socket. Suitable for testing
 * loom command software in the absence of loom hardware.
 * 
 * @author kentcollins
 *
 */

public class VirtualLoomRunner {

	public static void main(String[] args) {
		final LoomController loom = new LoomController() {

			@Override
			public void setShafts(int bitPattern) {
				// TODO Auto-generated method stub

			}

			@Override
			public void openShed() {
				// TODO Auto-generated method stub

			}

			@Override
			public void closeShed() {
				// TODO Auto-generated method stub

			}

			@Override
			public void weave() {
				// TODO Auto-generated method stub

			}

			@Override
			public void beat() {
				// TODO Auto-generated method stub

			}

			@Override
			public void wind() {
				// TODO Auto-generated method stub

			}
		};

		LoomSocket socket = new LoomSocket(1793, loom);
		socket.run();
		// socket blocks while listening for loom commands
		System.exit(0);

	}

}
