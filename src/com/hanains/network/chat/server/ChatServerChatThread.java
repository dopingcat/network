package com.hanains.network.chat.server;

import java.io.IOException;

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
					String[] command = input.split(" ", 3);	// 0=명령 1=인자 or 메세지
					
					if(command[0].equalsIgnoreCase("/w")) {	
						ChatUtil.getChatUtil().whisper(chatConnection, command);
					} else if(command[0].equalsIgnoreCase("/j")) {
						ChatUtil.getChatUtil().joinRoom(chatConnection, command);
					} else if(command[0].equalsIgnoreCase("/ls")) {
						ChatUtil.getChatUtil().displayRoomList(chatConnection);
					} else if(command[0].equalsIgnoreCase("/exit")) {
						ChatUtil.getChatUtil().exit(chatConnection);
					} else {
						chatConnection.getPrintWriter().println("[Error] Unknown command");
					}
				} else {
					ChatUtil.getChatUtil().roomcast(chatConnection.getLocale(), (chatConnection.getName() + " : " + input));
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
