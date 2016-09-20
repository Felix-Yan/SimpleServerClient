package imp;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple controller to send user actions to the server and receive notifications from the server.
 * The server will notify the current game state to the player that will be stored into the local copy of map.
 * @author yanlong
 *
 */
public class Client extends Thread {
	private OutputStreamWriter output;
	private InputStreamReader input;
	private final Socket socket;
	private int uid;
	private String IPaddress;
	public boolean setup;

	/**
	 * The following constructs a client with a socket.
	 * @param s
	 */
	public Client(Socket s){
		socket = s;
		try {
			output = new OutputStreamWriter(socket.getOutputStream());
			input = new InputStreamReader(socket.getInputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * The following sends the client id to the server and then keep receiving messages from the server
	 */
	public void run(){
		try {
			if(!setup){
				output.write("J"+uid);
				output.flush();
				setup=true;
			}
			boolean exit = false;
			//System.out.println("Client 55: after sending id");
			while(!exit){
				char[] message = new char[3072];
				input.read(message);
				processMessage(message);
			}
			socket.close();

		} catch (SocketException ex){//handles the exception that the client does not get respond from the server
			System.out.println("Server is busy! Restart the game!");//debug
			System.exit(0);
		}
		catch (IOException e) {
			//e.printStackTrace();
			try {
				socket.close();
				System.exit(0);
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
		}

	}


	/**
	 * a getter for the outputstreamwriter of the client
	 * @return
	 */
	public OutputStreamWriter getOutput() {
		return output;
	}

	/**
	 * The following sends a string to the server
	 * @param s
	 * @throws IOException
	 */
	public void send(String s) throws IOException {
		//somehow, sometimes the send will be executed before the start run of the client
		if(!setup){
			output.write("J"+uid);
			output.flush();
			setup=true;
		}
		try {
			output.write(s);
			output.flush();

		} catch (SocketException ex){//this handles the disconnection of the server
			System.out.println("The server has been closed");
			System.exit(0);
		}
	}


	/**
	 * a getter for uid
	 * @return
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * a getter for IPaddress
	 * @return
	 */
	public String getIPaddress() {
		return IPaddress;
	}

	/**
	 * a setter for IPaddress
	 * @param iPaddress
	 */
	public void setIPaddress(String iPaddress) {
		IPaddress = iPaddress;
	}

	/**
	 * a setter for uid
	 * @param uid
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * This processes messages received from the server in the client.
	 * At the moment it is simply printing out what is received.
	 * @param message
	 */
	public void processMessage(char[] message){
		List<String> messages = new ArrayList<String>();
		String receive = "";
		int i = 0;
		for(; i<message.length; i++){
			if(message[i] == 'X'){
				messages.add(receive);
				receive = "";
			}
			if(message[i] != 'X'){
				receive+=message[i];
			}
		}
			for(String m: messages){
				System.out.println("client received: "+m);
			}
			
	}
}
