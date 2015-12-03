package com.hanains.network.chat.server;

import java.io.IOException;
import java.util.List;

import com.hanains.network.chat.model.ChatConnection;

public class ChatServerChatThread extends Thread {
	private ChatConnection chatConnection = null;
	// 사용자 입력 메세지는 Base64로 인코딩
	public ChatServerChatThread() {}
	
	public ChatServerChatThread(ChatConnection chatConnection) {
		if(chatConnection != null) {
			this.chatConnection = chatConnection;
		}
	}
	
	public void roomcast(String msg) throws IOException {
		for(ChatConnection chatConnection : ChatServerConnectionManager.getChatServerConnectionManager().getCurrentConnections(chatConnection.getLocale())) {
			chatConnection.getPrintWriter().println(msg);
		}
	}
	
	public void broadcast(String msg) throws IOException {
		for(ChatConnection chatConnection : ChatServerConnectionManager.getChatServerConnectionManager().getAllConnections()) {
			chatConnection.getPrintWriter().println(msg);
		}
	}
	
	@Override
	public void run() {
		try {
			// for console test
			chatConnection.getPrintWriter().println("============ Welcome '"+ chatConnection.getName() +"' ============");
			
			while(true) {
				String input = chatConnection.getBufferedReader().readLine();
				
				if(input == null) {
					System.out.println("[SERVER] Dissconnected by Client [" + chatConnection.getSocket().getRemoteSocketAddress() + "]");
					ChatServerConnectionManager.getChatServerConnectionManager().removeConnection(chatConnection.getName());
					break;
				}
				
				// '/'로 시작하는 경우는 명령어 취급
				if(input.charAt(0)=='/') {
					String[] cmd = input.split(" ", 3);	// 0=명령 1=인자 or 메세지
					
					if(cmd[0].equalsIgnoreCase("/w")) {	// 귓속말 fomat = {/w [userName] msg}
						if(cmd.length < 3) {
							chatConnection.getPrintWriter().println("인자값이 부족합니다.");
						} else {
							ChatServerConnectionManager.getChatServerConnectionManager().selectConnection(cmd[1]).getPrintWriter()
								.println("'" + chatConnection.getName() + "'님의 귓속말 : " + cmd[2]);
						}
					} else if(cmd[0].equalsIgnoreCase("/j")) {	// 방 개설 및 참여 fomat = {/c [roomName]}
						if(cmd.length < 2) {
							chatConnection.getPrintWriter().println("인자값이 부족합니다.");
						} else {
							ChatServerConnectionManager.getChatServerConnectionManager().joinRoom(cmd[1], chatConnection.getName());
							chatConnection.getPrintWriter().println("[" + cmd[1] + "] 방에 참여하였습니다.");
						}
					} else if(cmd[0].equalsIgnoreCase("/ls")) {	// 전체 방 정보 보기 format {/ls}
						List<String> list = ChatServerConnectionManager.getChatServerConnectionManager().getAllRoomInfo();
						chatConnection.getPrintWriter().println("======================== Current Rooms ========================");
						for(String str : list) {
							chatConnection.getPrintWriter().println(str);
						}
						chatConnection.getPrintWriter().println("===============================================================");
					} else {
						chatConnection.getPrintWriter().println("[Error] Unknown command");
					}
				} else {
					roomcast(chatConnection.getName() + " : " + input);
				}
			}
		} catch (IOException ioe) {
			System.err.println("[ChatThread] error : " + ioe.toString());
			try {
				ChatServerConnectionManager.getChatServerConnectionManager().removeConnection(chatConnection.getName());
			} catch (IOException ioe2) {
				System.err.println("[ChatThread] clean resource error : " + ioe2.toString());
			}
		} catch (NullPointerException ne) {
			System.err.println("[ChatThread] error : " + ne.toString());
		}
	}
}
