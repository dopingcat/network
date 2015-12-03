package com.hanains.network.chat.server;

import java.io.IOException;
import java.util.List;

import com.hanains.network.chat.model.ChatConnection;

public class ChatServerUtil {
	private final static ChatServerUtil chatUtil = new ChatServerUtil();
	
	private ChatServerUtil() {
	}
	
	public static ChatServerUtil getChatUtil() {
		return chatUtil;
	}
	
	// 해당 방 전체
	public void roomcast(String roomName, String msg) throws IOException {
		for(ChatConnection cConn : ChatServerConnectionManager.getChatServerConnectionManager().getCurrentConnections(roomName)) {
			cConn.getPrintWriter().println(msg);
		}
	}
	
	// 접속자 전체
	public void broadcast(String msg) throws IOException {
		for(ChatConnection chatConnection : ChatServerConnectionManager.getChatServerConnectionManager().getAllConnections()) {
			chatConnection.getPrintWriter().println(msg);
		}
	}
	
	// 귓속말 fomat = {/w [userName] msg}
	public void whisper(ChatConnection chatConnection, String[] command) throws IOException {
		if(command.length < 3) {
			chatConnection.getPrintWriter().println("인자값이 부족합니다.");
		} else {
			ChatServerConnectionManager.getChatServerConnectionManager().selectConnection(command[1]).getPrintWriter()
				.println("'" + chatConnection.getName() + "'님의 귓속말 : " + command[2]);
		}
	}
	
	// 방 개설 및 참여 fomat = {/c [roomName]}
	public void joinRoom(ChatConnection chatConnection, String[] command) throws IOException {
		if(command.length < 2) {
			chatConnection.getPrintWriter().println("인자값이 부족합니다.");
		} else {
			ChatServerConnectionManager.getChatServerConnectionManager().joinRoom(command[1], chatConnection.getName());
			chatConnection.getPrintWriter().println("[" + command[1] + "]에 참여하였습니다.");
		}
	}
	
	// 전체 방 정보 보기 format {/ls}
	public void displayRoomList(ChatConnection chatConnection) throws IOException {
		List<String> list = ChatServerConnectionManager.getChatServerConnectionManager().getAllRoomInfo();
		chatConnection.getPrintWriter().println("======================== Current Rooms ========================");
		for(String str : list) {
			chatConnection.getPrintWriter().println(str);
		}
		chatConnection.getPrintWriter().println("===============================================================");
	}
	
	public void exit(ChatConnection chatConnection) throws IOException {
		if(!chatConnection.getLocale().equalsIgnoreCase("lobby")) {	// 로비아닐 경우
			ChatServerConnectionManager.getChatServerConnectionManager().exitRoom(chatConnection.getLocale(), chatConnection.getName());
			chatConnection.getPrintWriter().println("[lobby]로 나왔습니다.");
		} else {
			chatConnection.getPrintWriter().println("이미 [lobby] 입니다.");
		}
	}
}
