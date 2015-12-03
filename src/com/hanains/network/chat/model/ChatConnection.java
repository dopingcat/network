package com.hanains.network.chat.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatConnection {
	private String name = null;
	private Socket socket = null;
	private String locale = null;
	
	public ChatConnection() {}
	
	public ChatConnection(String name, Socket socket, String locale) {
		super();
		this.name = name;
		this.socket = socket;
		this.locale = locale;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public String getLocale() {
		return locale;
	}
	
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public BufferedReader getBufferedReader() throws IOException{
		return new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public PrintWriter getPrintWriter() throws IOException {
		return new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
	}

	@Override
	public String toString() {
		return "ChatConnection [name=" + name + ", socket=" + socket + ", locale=" + locale + "]";
	}
	
}
