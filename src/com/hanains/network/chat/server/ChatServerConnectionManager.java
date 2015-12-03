package com.hanains.network.chat.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import com.hanains.network.chat.model.ChatConnection;

public class ChatServerConnectionManager {
	private final static ChatServerConnectionManager connectionManager = new ChatServerConnectionManager();
	private final static Hashtable<String, ChatConnection> connectionPool = new Hashtable<>();
	private final static Hashtable<String, LinkedList<String>> roomInfo = new Hashtable<>();
	
	private ChatServerConnectionManager() {
	}
	
	public static ChatServerConnectionManager getChatServerConnectionManager() {
		return connectionManager;
	}
	
	public void init() {
		roomInfo.put("lobby", new LinkedList<String>());
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
		joinRoom("lobby", chatConnection.getName());
	}
	
	public void removeConnection(String name) throws IOException {
		if(connectionPool.containsKey(name)) {
			if(connectionPool.get(name).getSocket() != null && !connectionPool.get(name).getSocket().isClosed()) {
				connectionPool.get(name).getSocket().close();
			}
			connectionPool.remove(name);
		}
	}
	
	public ChatConnection selectConnection(String name) {
		return connectionPool.get(name);
	}
	
	public List<ChatConnection> getCurrentConnections(String roomName) {
		ArrayList<ChatConnection> list = new ArrayList<>();
		
		for(String str : roomInfo.get(roomName)) {
			list.add(connectionPool.get(str));
		}
		
		return list;
	}
	
	public List<ChatConnection> getAllConnections() {
		ArrayList<ChatConnection> list = new ArrayList<>();
		connectionPool.forEach((k, v) -> list.add(v));
		
		return list;
	}
	
	public void createRoom(String roomName) {
		roomInfo.put(roomName, new LinkedList<String>());
	}
	
	public void joinRoom(String roomName, String userName) {
		if(!roomInfo.containsKey(roomName)) {
			createRoom(roomName);
		}
		roomInfo.get(connectionPool.get(userName).getLocale()).remove(userName); // 로비에서 삭제
		roomInfo.get(roomName).add(userName);	// 참여방에 추가
		connectionPool.get(userName).setLocale(roomName);	// 장소 변경
	}
	
	public void exitRoom(String roomName, String userName) {
		if(roomInfo.containsKey(roomName)) {
			roomInfo.get(connectionPool.get(userName).getLocale()).remove(userName);	// 참여방에서 삭제
			roomInfo.get("lobby").add(userName);	// 로비에 추가
			connectionPool.get(userName).setLocale("lobby");	// 장소 변경
			
			// 빈방 삭제
			if(!roomName.equalsIgnoreCase("lobby") && roomInfo.get(roomName).isEmpty()) {
				roomInfo.remove(roomName);
			}
		}
	}
	
	public List<String> getAllRoomInfo() {
		List<String> list = new ArrayList<>();
		roomInfo.forEach((k, v) -> list.add(k + "\t|\t" + v.size() + "명 참여중"));
		
		return list;
	}
}
