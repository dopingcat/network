package com.hanains.network.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClient {
	private static final String SERVER_IP = "192.168.1.12";
	private static final int SERVER_PORT = 8324;
	
	public static void main(String[] args) {
		Socket socket = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		try {
			// 1. create socket
			socket = new Socket();
			
			// 2. connect
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[CLIENT] connected");
			
			// 3. get IOStream
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			
			// 4. read/write
			String data = "Hello World";
			outputStream.write(data.getBytes("UTF-8"));
			
			byte[] buffer = new byte[256];
			int readByteRead = inputStream.read(buffer);
			
			data = new String(buffer, 0, readByteRead, "UTF-8");
			System.out.println("[CLIENT] received : " + data);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("[CLIENT] error : " + e.toString());
		} finally {
			try {
				if(inputStream != null) {
					inputStream.close();
				}
				
				if(outputStream != null) {
					outputStream.close();
				}
				
				if(socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				System.err.println("[CLIENT] error : " + e.toString());
			}
		}
	}
}
