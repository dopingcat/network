package com.hanains.network.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import com.hanains.network.chat.model.ChatConnection;

public class ChatServerRegistThread extends Thread {
	private Socket socket = null;
	private ChatConnection chatConnection = null;
	public ChatServerRegistThread() {}
	
	public ChatServerRegistThread(Socket socket) {
		if(socket != null) {
			this.socket = socket;
		}
	}
	
	@Override
	public void run() {
		InetSocketAddress inetSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
		String remoteHostAddress = inetSocketAddress.getAddress().getHostAddress();
		int remoteHostPort = inetSocketAddress.getPort();
		System.out.println("[Chat Server ver 0.1] connected from " + remoteHostAddress + ":" + remoteHostPort);
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
			
			// regist loop
			while(true) {
				// 닉네임 등록을 요청
				printWriter.println("[Input your name]");
				// 닉네임 수신
				String name = bufferedReader.readLine();
				// 닉네임 등록 처리
				if(name == null) {
					System.out.println("[Chat Server ver 0.1] Dissconnected by Client [" + socket.getRemoteSocketAddress() + "]");
					break;
				} else if(ChatServerConnectionManager.getChatServerConnectionManager().isNameExist(name)) {
					printWriter.println("This name already exist. retry.");
				} else {
					chatConnection = new ChatConnection(name, socket, "lobby");	// 기본 접속 위치 = lobby
					// 등록 완료
					ChatServerConnectionManager.getChatServerConnectionManager().addConnection(chatConnection);
					// 접속 확인 메세지 전송
					printWriter.println("ok " + chatConnection.getName() + " " + chatConnection.getLocale());
					// 채팅 스레드 시작
					new ChatServerChatThread(chatConnection).start();
					break;
				}
				bufferedReader = null;
				printWriter = null;
			}
		} catch (IOException ioe) {
			System.err.println("[RegistThread] error : " + ioe.toString());
			
			if(chatConnection != null) {
				try {
					ChatServerConnectionManager.getChatServerConnectionManager().removeConnection(chatConnection.getName());
				} catch (IOException ioe2) {
					System.err.println("[RegistThread] clean resource error : " + ioe2.toString());
				}
			}
		}
	}
}
