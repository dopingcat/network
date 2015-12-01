package com.hanains.network.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EchoServerReceive extends Thread {
	private Socket socket = null;
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	private byte[] buffer = new byte[256];
	private int readByteCount;
	
	public EchoServerReceive() {
	}
	
	public EchoServerReceive(Socket socket) {
		if(socket != null) {
			this.socket = socket;
		}
	}
	
	@Override
	public void run() {
		InetSocketAddress inetSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
		String remoteHostAddress = inetSocketAddress.getAddress().getHostAddress();
		int remoteHostPort = inetSocketAddress.getPort();
		System.out.println("[SERVER] connected from " + remoteHostAddress + ":" + remoteHostPort);
		
		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			
			while(true) {
				readByteCount = inputStream.read(buffer);
				
				if(readByteCount < 0) {
					System.out.println("[SERVER] Dissconnected by Client [" + socket.getRemoteSocketAddress() + "]");
					break;
				}
				
				// read
				String data = new String(buffer, 0, readByteCount);
				System.out.println("[SERVER] recieved msg by [" + socket.getRemoteSocketAddress() + "] Client : " + data);
				
				// echo
				outputStream.write(data.getBytes("UTF-8"));
				outputStream.flush();
			}
		} catch (IOException e) {
			System.err.println("[SERVER] error : " + e.toString());
		} finally {
			try {
				if(outputStream != null) {
					outputStream.close();
				}
				
				if(inputStream != null) {
					inputStream.close();
				}
				
				if(socket != null && !socket.isConnected()) {
					socket.close();
				}
			} catch (IOException ioe) {
				System.err.println("[SERVER] error : " + ioe.toString());
			}
		}
	}
}
