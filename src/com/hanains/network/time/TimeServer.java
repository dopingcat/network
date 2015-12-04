package com.hanains.network.time;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer {
	private static final int PORT = 9090;
	private static final int BUFFER_SIZE = 1024;
	
	public static void main(String[] args) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss a");
		DatagramSocket datagramSocket = null;
		
		try {
			datagramSocket = new DatagramSocket(PORT);
			
			while(true) {
				log("wait for receive");
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				datagramSocket.receive(receivePacket);
				
				byte[] sendData = (format.format(new Date())).getBytes("UTF-8");
				DatagramPacket sendPacket
					= new DatagramPacket(
						sendData,
						sendData.length,
						receivePacket.getAddress(),
						receivePacket.getPort());
				datagramSocket.send(sendPacket);
			}
		} catch (Exception e) {
			log("error : " + e.toString());
		} finally {
			if(datagramSocket != null && !datagramSocket.isClosed()) {
				datagramSocket.close();
			}
		}
	}
	
	public static void log(String message) {
		System.out.println("[UDP Time Server] " + message);
	}
}
