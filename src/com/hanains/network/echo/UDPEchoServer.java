package com.hanains.network.echo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPEchoServer {
	private static final int PORT = 9090;
	private static final int BUFFER_SIZE = 1024;
	public static void main(String[] args) {
		DatagramSocket datagramSocket = null;
		
		try {
			datagramSocket = new DatagramSocket(PORT);
			
			while(true) {
				log("wait for receive");
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				datagramSocket.receive(receivePacket);
				
				String data = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
				log("received msg : " + data);
				
				
				DatagramPacket sendPacket
					= new DatagramPacket(
							receivePacket.getData(),
							receivePacket.getLength(),
							receivePacket.getAddress(),
							receivePacket.getPort());
				datagramSocket.send(sendPacket);
			}
		} catch (Exception e) {
			log("error : " + e);
		} finally {
			if(datagramSocket != null && !datagramSocket.isClosed()) {
				datagramSocket.close();
			}
		}
	}
	
	public static void log(String message) {
		System.out.println("[UDP Echo Server]" + message);
	}
}
