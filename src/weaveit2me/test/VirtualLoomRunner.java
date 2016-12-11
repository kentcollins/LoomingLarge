package weaveit2me.test;

import weaveit2me.core.LoomController;
import weaveit2me.core.LoomService;

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

			@Override
			public String getStatus() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void pickShafts(Integer[] selectedShafts) {
				// TODO Auto-generated method stub
				
			}
		};

		LoomService socket = new LoomService(1793, loom);
		socket.run();
		// socket blocks while listening for loom commands
		System.exit(0);

	}

}
