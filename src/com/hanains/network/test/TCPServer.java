package com.hanains.network.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
			
			// 5. get IOStream
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			String data;
			StringBuilder nData = new StringBuilder();
			
			// 6. read data
			try {
				byte[] buffer = new byte[256];
				int readByteCount;
				while(true) {
					readByteCount = inputStream.read(buffer);
					
					if(readByteCount < 0) {
						System.out.println("[SERVER] Disconnected by Client.");
						break;
					}
					nData.append("[SERVER] echo : ");
					data = new String(buffer, 0, readByteCount);
					System.out.println("[SERVER] recieved : " + data);
					
					// 7. send data
					outputStream.write(data.getBytes("UTF-8"));
					outputStream.flush();
				}
			} catch(IOException ioe) {
				System.err.println("[SERVER] error : " + ioe.toString());
			} finally {
				// 8. clean resource
				if(outputStream != null) {
					outputStream.close();
				}
				
				if(socket != null && !socket.isClosed()) {
					socket.close();
				}
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