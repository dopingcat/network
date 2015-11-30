package com.hanains.network.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {
	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			System.out.println("Host Name : " + inetAddress.getHostName());
			System.out.println("Host IP Address : " + inetAddress.getHostAddress());
			byte[] addresses = inetAddress.getAddress();
			
			for(int b : addresses) {
				System.out.print((b & 0xff) + ".");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
