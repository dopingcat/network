package com.hanains.network.chat.client;

import java.io.IOException;

public class ChatClientSendThread extends Thread {
	@Override
	public void run() {
		while(true) {
			String input = null;
			
			input = ChatClientUtil.getChatClientUtil().getScanner().nextLine();
			
			if(input != null && !input.equals("")) {
				try {
					ChatClientUtil.getChatClientUtil().getChatClient().getPrintWriter().println(input);
				} catch (IOException ioe) {
					System.err.println("error : " + ioe.toString());
				}
			} else if(input.equalsIgnoreCase("/exit")) {
				break;
			}
		}
	}
}
