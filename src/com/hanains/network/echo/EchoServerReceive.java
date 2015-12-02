package com.hanains.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EchoServerReceive extends Thread {
	private Socket socket = null;
	private BufferedReader bufferedReader = null;
	private PrintWriter printWriter = null;
	
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
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter = new PrintWriter(socket.getOutputStream());
			
			while(true) {
				// read
				String input = bufferedReader.readLine();
				
				if(input == null) {
					System.out.println("[SERVER] Dissconnected by Client [" + socket.getRemoteSocketAddress() + "]");
					break;
				}
				System.out.println("[SERVER] recieved msg by [" + socket.getRemoteSocketAddress() + "] Client : " + input);
				
				// echo
				printWriter.print(input);
				printWriter.flush();
			}
		} catch (IOException e) {
			System.err.println("[SERVER] error : " + e.toString());
		} finally {
			try {
				if(printWriter != null) {
					printWriter.close();
				}
				
				if(bufferedReader != null) {
					bufferedReader.close();
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
