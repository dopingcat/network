package com.hanains.network.chat.server;

public class ChatServer {
	public static void main(String[] args) {
		ChatServerConnectionManager.getChatServerConnectionManager().init();
		new ChatServerConnectionThread().run();
	}
}
