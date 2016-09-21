package imp;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;

public class Main{

	public static void main(String[] args){
		if(args.length < 1) return;
		switch(args[0]){
		case "server":
			startServer();
			break;
		case "client":
			if(args.length<2) return;
			startClient(args[1]);
			break;
		default:
			System.out.println("Wrong argument");
		}
	}

	private static void startServer(){
		Server server = new Server(0,0,0,"");
		InetAddress ad = server.getAddress();
		while(ad == null){
			ad = server.getAddress();
		}
		String ipAddress = ad.getHostAddress();
		System.out.println(ipAddress);
		server.start();
		//debug
		startClient("130.195.7.143");
	}

	private static void startClient(String ipAddress){
		//client 130.195.7.144
		try {
			InetAddress inet = InetAddress.getByName(ipAddress);
			Socket socket = new Socket(inet, Server.PORT);
			Client client = new Client(socket);
			client.setUid(0);//set uid as 0
			client.start();
			client.send("This is a testX");
			client.send("This is the second line.X");
			client.send("This is the third line.X");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
