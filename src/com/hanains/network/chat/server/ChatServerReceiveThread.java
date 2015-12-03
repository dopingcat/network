package com.hanains.network.chat.server;

import java.io.IOException;

import com.hanains.network.chat.model.ChatConnection;

public class ChatServerReceiveThread extends Thread {
	private ChatConnection chatConnection = null;
	
	public ChatServerReceiveThread(ChatConnection chatConnection) {
		if(chatConnection != null) {
			this.chatConnection = chatConnection;
		}
	}
	
	@Override
	public void run() {
		try {
			chatConnection.getPrintWriter().println("============ Welcome '"+ chatConnection.getName() +"' ============");
			
			while(true) {
				String input = chatConnection.getBufferedReader().readLine();
				
				if(input == null) {
					System.out.println("[ReceiveThread] Dissconnected by Client [" + chatConnection.getSocket().getRemoteSocketAddress() + "]");
					ChatServerConnectionManager.getChatServerConnectionManager().removeConnection(chatConnection.getName());
					break;
				}
				
				new ChatServerSendThread(chatConnection, input).start();
			}
		} catch (IOException ioe) {
			System.err.println("[ReceiveThread] error : " + ioe.toString());
			try {
				ChatServerConnectionManager.getChatServerConnectionManager().removeConnection(chatConnection.getName());
			} catch (IOException ioe2) {
				System.err.println("[ReceiveThread] clean resource error : " + ioe2.toString());
			}
		} catch (NullPointerException ne) {
			System.err.println("[ReceiveThread] error : " + ne.toString());
		}
	}
}
