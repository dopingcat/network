package com.hanains.network.chat.client;

public class ChatClient {
	public static void main(String[] args) {
		new ChatClientConnectionThread().start();
	}
}
