package com.hanains.network.chat.client;

import java.util.Scanner;

import com.hanains.network.chat.model.ChatConnection;

public class ChatClientUtil {
	private static final ChatClientUtil chatClientUtil = new ChatClientUtil();
	private ChatConnection chatConnection = null;
	private Scanner scanner = new Scanner(System.in);
	
	private ChatClientUtil() {
	}
	
	public static ChatClientUtil getChatClientUtil() {
		return chatClientUtil;
	}
	
	public Scanner getScanner() {
		return scanner;
	}
	
	public void setChatCliet(ChatConnection chatClient) {
		this.chatConnection = chatClient;
	}
	
	public ChatConnection getChatClient() {
		return chatConnection;
	}
}
