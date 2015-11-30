package com.hanains.network.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	private static final int PORT = 8324;
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;

		try {
			// 1. create server socket
			serverSocket = new ServerSocket();
			
			// 2. socket bind
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhost = inetAddress.getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhost, PORT));
			System.out.println("[SERVER] Binding " + localhost + ":" + PORT);
			
			// 3. accept
			Socket socket = serverSocket.accept();
			
			// 4. succeed
			InetSocketAddress inetSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
			String remoteHostAddress = inetSocketAddress.getAddress().getHostAddress();
			int remoteHostPort = inetSocketAddress.getPort();
			
			System.out.println("[SERVER] connected from " + remoteHostAddress + ":" + remoteHostPort);
			
			// 7. close
			if(socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(serverSocket != null && !serverSocket.isClosed()) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
