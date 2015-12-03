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
		System.out.println("[Chat Server ver 0.0] connected from " + remoteHostAddress + ":" + remoteHostPort);
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
			
			// regist loop
			while(true) {
				printWriter.println("input your name : ");
				String name = bufferedReader.readLine();
				if(name == null) {
					System.out.println("[Chat Server ver 0.0] Dissconnected by Client [" + socket.getRemoteSocketAddress() + "]");
					break;
				} else if(ChatServerConnectionManager.getChatServerConnectionManager().isNameExist(name)) {
					printWriter.println("err/This name already exist. retry.");
				} else {
					// Q. 소켓을 넘기는것이 괜찮은 방법인지...? 괜찮다고 하면 자원 관리는 어떻게 하는지..
					chatConnection = new ChatConnection(name, socket, "lobby");	// 기본 접속 위치 = lobby
					ChatServerConnectionManager.getChatServerConnectionManager().addConnection(chatConnection);
					System.out.println("[RegistThread] connection add complete");
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
