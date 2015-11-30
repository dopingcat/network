package com.hanains.network.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String domain = null;
		
		while (true) {
			System.out.print("> ");
			domain = scanner.nextLine();
			if(domain == null || domain.length() == 0) {
				System.err.println("[SYSTEM] input domain name.");
			} else if(domain.equals("exit")) {
				break;
			} else if(domain != null && domain.length() != 0) {
				System.out.println(domain);
				try {
					for(InetAddress address : InetAddress.getAllByName(domain)) {
						System.out.println(address.getHostName() + ":" + address.getHostAddress());
					}
				} catch (UnknownHostException e) {
					//e.printStackTrace();
					System.err.println("[EXCEPTION] " + e.toString());
				}
				// Q. 루프 안의 자원을 반환하는 방법?
			}
		}
		if(scanner != null) {
			scanner.close();
		}
		System.err.println("[SYSTEM] terminated.");
	}
}
