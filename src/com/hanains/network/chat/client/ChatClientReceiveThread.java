package com.hanains.network.chat.client;

import java.io.IOException;

public class ChatClientReceiveThread extends Thread {
	@Override
	public void run() {
		try {
			while(true) {
				String msg = ChatClientUtil.getChatClientUtil().getChatClient().getBufferedReader().readLine();
				if(msg == null) {
					System.out.println("[Chat Client ver 0.1] Dissconnected by Server");
					break;
				}
				System.out.println(msg);
			}
		} catch (IOException e) {
			System.err.println("error : " + e.toString());
		}
	}
}
