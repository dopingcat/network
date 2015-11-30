package com.hanains.network.test.ex;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressExample01 {
	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			System.out.println(inetAddress);

			System.out.println(inetAddress.getHostName());
			
			System.out.println(inetAddress.getHostAddress());
			
			for(int b : inetAddress.getAddress()) {
				System.out.print(b & 0xff);
				System.out.print(".");
			}
			System.out.println();
			
			System.out.println(Integer.toHexString(inetAddress.hashCode()));
			
			System.out.println(InetAddress.getByName("www.naver.com"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
