package com.hanains.network.chat.server;

import java.io.IOException;

import com.hanains.network.chat.model.ChatConnection;

public class ChatServerSendThread extends Thread {
	private ChatConnection chatConnection = null;
	private String input = null;
	
	public ChatServerSendThread(ChatConnection chatConnection, String input) {
		this.chatConnection = chatConnection;
		this.input = input;
	}
	
	@Override
	public void run() {
		try {
			// '/'로 시작하는 경우는 명령어 취급
			if(input.charAt(0)=='/') {
				String[] command = input.split(" ", 3);	// 0=명령 1=인자 or 메세지
				
				if(command[0].equalsIgnoreCase("/w")) {
					ChatServerUtil.getChatUtil().whisper(chatConnection, command);
				} else if(command[0].equalsIgnoreCase("/j")) {
					ChatServerUtil.getChatUtil().joinRoom(chatConnection, command);
				} else if(command[0].equalsIgnoreCase("/ls")) {
					ChatServerUtil.getChatUtil().displayRoomList(chatConnection);
				} else if(command[0].equalsIgnoreCase("/exit")) {
					ChatServerUtil.getChatUtil().exit(chatConnection);
				} else {
					chatConnection.getPrintWriter().println("[Error] Unknown command");
				}
			} else {
				ChatServerUtil.getChatUtil().roomcast(chatConnection.getLocale(), (chatConnection.getName() + " : " + input));
			}
		} catch (IOException ioe) {
			System.err.println("[SendThread] error : " + ioe.toString());
		}
	}
}
