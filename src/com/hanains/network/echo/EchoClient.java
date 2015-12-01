package com.hanains.network.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	private static final String SERVER_IP = "192.168.1.12";
	private static final int SERVER_PORT = 8324;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Socket socket = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		int readByteCount;
		byte[] buffer = new byte[256];

		try {
			socket = new Socket();

			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[CLIENT] connected");

			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();

			while (true) {
				System.out.print("> ");
				String input = scanner.nextLine();
				
				if(input == null || input.length() == 0) {
					System.err.println("[CLIENT] input data");
				} else if(input.equals("exit")) {
					break;
				}
				
				// write
				outputStream.write(input.getBytes("UTF-8"));
				
				// read
				readByteCount = inputStream.read(buffer);
				String recieveData = new String(buffer, 0, readByteCount, "UTF-8");
				System.out.println("[CLIENT] received : " + recieveData);
			}
		} catch (IOException ioe) {
			System.err.println("[CLIENT] error : " + ioe.toString());
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}

				if (inputStream != null) {
					inputStream.close();
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
