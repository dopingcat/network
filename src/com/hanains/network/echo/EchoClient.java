package com.hanains.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	private static final String SERVER_IP = "192.168.1.12";
	private static final int SERVER_PORT = 8324;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Socket socket = null;
		BufferedReader bufferdReader = null;
		PrintWriter printWriter = null;
		
		try {
			socket = new Socket();

			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[CLIENT] connected");

			bufferdReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter = new PrintWriter(socket.getOutputStream());

			while (true) {
				System.out.print("> ");
				String input = scanner.nextLine();
				
				if(input == null || input.equals("")) {
					System.err.println("[CLIENT] input data");
				} else if(input.equals("exit")) {
					break;
				}
				
				// write
				printWriter.print(input);
				printWriter.flush();
				
				// read
				String recieveData = bufferdReader.readLine();
				System.out.println("[CLIENT] received : " + recieveData);
			}
		} catch (IOException ioe) {
			System.err.println("[CLIENT] error : " + ioe.toString());
		} finally {
			try {
				if (printWriter != null) {
					printWriter.close();
				}

				if (bufferdReader != null) {
					bufferdReader.close();
				}

				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
				
				if(scanner != null) {
					scanner.close();
				}
			} catch (IOException ioe) {
				System.err.println("[CLIENT] error : " + ioe.toString());
			}
		}
	}
}
