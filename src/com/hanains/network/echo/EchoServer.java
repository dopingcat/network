package com.hanains.network.echo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class EchoServer {
	private static final int PORT = 8324;
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket();
			
//			serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), PORT));
			
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhost = inetAddress.getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhost, PORT));
			
			System.out.println("[SERVER] Binding " + localhost + ":" + PORT);
			while (true) {
				new EchoServerReceive(serverSocket.accept()).start();
			}
		} catch (IOException e) {
			System.err.println("[SERVER] error : " + e.toString());
		}
	}
}
