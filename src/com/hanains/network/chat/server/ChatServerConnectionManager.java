package com.hanains.network.chat.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import com.hanains.network.chat.model.ChatConnection;

public class ChatServerConnectionManager {
	private final static ChatServerConnectionManager connectionManager = new ChatServerConnectionManager();
	private final static Hashtable<String, ChatConnection> connectionPool = new Hashtable<>();
	private final static Hashtable<String, List<String>> roomInfo = new Hashtable<>();
	
	private ChatServerConnectionManager() {
		roomInfo.put("lobby", new LinkedList<>());
	}
	
	public static ChatServerConnectionManager getChatServerConnectionManager() {
		return connectionManager;
	}
	
	public boolean isNameExist(String name) {
		if(connectionPool.containsKey(name)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void displayConnections() {
		System.out.println("============================= current connections =============================");
		connectionPool.forEach((k,v) -> System.out.println(v));
		System.out.println("===============================================================================");
	}
	
	public void addConnection(ChatConnection chatConnection) {
		connectionPool.put(chatConnection.getName(), chatConnection);
		roomInfo.get("lobby").add(chatConnection.getName());
	}
	
	public void removeConnection(String name) throws IOException {
		if(connectionPool.containsKey(name)) {
			if(connectionPool.get(name).getSocket() != null && !connectionPool.get(name).getSocket().isClosed()) {
				connectionPool.get(name).getSocket().close();
			}
			connectionPool.remove(name);
		}
	}
	
	public ChatConnection selectConnection(String name) throws NullPointerException {
		return connectionPool.get(name);
	}
	
	public List<Socket> getAllConnections() {
		ArrayList<Socket> list = new ArrayList<>();
		connectionPool.forEach((k, v) -> list.add(v.getSocket()));
		
		return list;
	}
	
	public boolean createRoom(String roomName) {
		if(roomInfo.containsKey(roomName)) {
			return false;
		}
		return true;
	}
}
