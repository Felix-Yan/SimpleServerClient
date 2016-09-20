package imp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Main {
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
		Server server = new Server(0, 0, 0, "");
		String ipAddress = server.getAddress().getHostAddress();
		System.out.println(ipAddress);
		server.start();
	}

	private static void startClient(String ipAddress){
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
