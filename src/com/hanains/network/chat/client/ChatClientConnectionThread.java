package com.hanains.network.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import com.hanains.network.chat.model.ChatConnection;

public class ChatClientConnectionThread extends Thread {
	private static final String SERVER_IP = "192.168.1.12";
	private static final int SERVER_PORT = 8324;
	
	@Override
	public void run() {
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
			
			while(true) {
				// 닉네임 등록 요청 받음
				System.out.println(bufferedReader.readLine());
				System.out.print("> ");
				String input = null;
				input = ChatClientUtil.getChatClientUtil().getScanner().nextLine();
				if (input == null){
					System.err.println("input null");
				} else if(input.equals("")) {
					System.err.println("input plz");
					continue;
				}
				
				// 닉네임 전송
				printWriter.println(input);
				// 결과 확인
				String response = bufferedReader.readLine();
				String[] result = null;
				
				// right response = {ok [name] [lobby]}
				if(response != null && !response.equals("")) {
					result = response.split(" ");
					
					// 정상 등록 응답 수신
					if(result[0].equals("ok")) {
						ChatClientUtil.getChatClientUtil().setChatCliet(new ChatConnection(result[1], socket, result[2]));
						// 수/발신 스레드 시작
						new ChatClientReceiveThread().start();
						new ChatClientSendThread().start();
						break;
					}
				}
			}
		} catch (IOException ioe) {
			System.err.println("error : " + ioe.toString());
		}
	}
}
