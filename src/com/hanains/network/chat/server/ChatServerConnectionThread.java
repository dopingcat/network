package com.hanains.network.chat.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class ChatServerConnectionThread implements Runnable {
	private static final int PORT = 8324;
	
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket();
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhost = inetAddress.getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhost, PORT));
			
			System.out.println("[Chat Server ver 0.0] Binding " + localhost + " : " + PORT);
			while (true) {
				new ChatServerRegistThread(serverSocket.accept()).start();
			}
		} catch (IOException ioe) {
			System.err.println("[ConnectionThread] error : " + ioe.toString());
		}
	}
}
