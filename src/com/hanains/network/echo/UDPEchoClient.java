package com.hanains.network.echo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class UDPEchoClient {
	private static final String HOST_ADDRESS = "127.0.0.1";
	private static final int HOST_PORT = 9090;
	private static final int BUFFER_SIZE = 1024;
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		DatagramSocket datagramSocket = null;
		try {
			datagramSocket = new DatagramSocket();
			String data = null;
			
			while(true) {
				System.out.print(">");
				data = scanner.nextLine();
				
				if(data == null || data.equals("")) {
					System.err.println("[Client] input data");
					continue;
				} else if(data.equals("exit")) {
					break;
				}
				
				byte[] sendData = (data).getBytes("UTF-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, new InetSocketAddress(HOST_ADDRESS, HOST_PORT));
				
				datagramSocket.send(sendPacket);
				
				DatagramPacket receivePacket = new DatagramPacket(new byte [BUFFER_SIZE], BUFFER_SIZE);
				datagramSocket.receive(receivePacket);
				
				data = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
				log("echo msg : " + data);
			}
		} catch (Exception e) {
			log("error : " + e);
		} finally {
			if(datagramSocket!=null && !datagramSocket.isClosed()) {
				datagramSocket.close();
			}
			if(scanner != null) {
				scanner.close();
			}
		}
	}
	
	public static void log(String message) {
		System.out.println("[UDP Echo Client]" + message);
	}
}
