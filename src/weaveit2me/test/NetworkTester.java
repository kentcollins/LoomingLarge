package weaveit2me.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Tests the ability to connect with a loom socket over TCP and execute commands
 * from the test pattern. Prior to running this class, a loom controller should
 * be listening at the specified address and port.  If no loom hardware is actually
 * available, the VirtualLoomController will emulate a generic controller.
 * 
 * @author kentcollins
 *
 */
public class NetworkTester {

	/**
	 * Usage -- java NetClient ip_address port"
	 * 
	 * @param args
	 *            args[0] ip_address of loom socket (String) args[1] port number
	 *            (int)
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		String ip = "192.168.0.27"; //args[0];
		int port = 1793; //Integer.parseInt(args[1]);

		try (Socket client = new Socket(ip, port);
				PrintWriter out = new PrintWriter(client.getOutputStream(),
						true);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						client.getInputStream()));
				BufferedReader stdIn = new BufferedReader(
						new InputStreamReader(System.in))) {
			String userInput;
			System.out.println(in.readLine());
			//testBatch(out, in);
			while ((userInput = stdIn.readLine()) != null) {
				out.println(userInput);
				out.println("echo: " + in.readLine());
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//	private static void testBatch(PrintWriter out, BufferedReader in)
//			throws InterruptedException, IOException {
//		Pattern p = new Pattern();
//		p.addTreadle(new Treadle());
//		System.out.println("Current pattern is: "
//				+ pp.getCurrentPattern().getName());
//		System.out.println("Current repeat is: "
//				+ pp.getCurrentRepeat().getName());
//		System.out.println("Current lift sequence is: " + pp.getCurrentLift());
//		for (int i = 0; i < 10; i++) {
//			out.println("SET_SHAFTS " + pp.advance());
//			System.out.println(in.readLine());
//		}
//		for (int i = 0; i < 10; i++) {
//			out.println("SET_SHAFTS " + pp.reverse());
//			System.out.println(in.readLine());
//		}
//		pp.nextRepeat();
//		System.out.println("Current repeat is: "
//				+ pp.getCurrentRepeat().getName());
//		for (int i = 0; i < 10; i++) {
//			out.println("SET_SHAFTS " + pp.advance());
//			System.out.println(in.readLine());
//		}
//		for (int i = 0; i < 10; i++) {
//			out.println("SET_SHAFTS " + pp.reverse());
//			System.out.println(in.readLine());
//		}
//		pp.nextRepeat();
//		System.out.println("Current repeat is: "
//				+ pp.getCurrentRepeat().getName());
//		for (int i = 0; i < 10; i++) {
//			out.println("SET_SHAFTS " + pp.advance());
//			System.out.println(in.readLine());
//			Thread.sleep(100);
//		}
//		for (int i = 0; i < 10; i++) {
//			out.println("SET_SHAFTS " + pp.reverse());
//			System.out.println(in.readLine());
//		}
//		pp.nextRepeat();
//		System.out.println("Current repeat is: "
//				+ pp.getCurrentRepeat().getName());
//		for (int i = 0; i < 10; i++) {
//			out.println("SET_SHAFTS " + pp.advance());
//			System.out.println(in.readLine());
//		}
//		for (int i = 0; i < 10; i++) {
//			out.println("SET_SHAFTS " + pp.reverse());
//			System.out.println(in.readLine());
//		}
//	}

}
