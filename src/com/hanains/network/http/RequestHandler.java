package com.hanains.network.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class RequestHandler extends Thread {
	
	private Socket socket;
	
	public RequestHandler( Socket socket ) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		BufferedReader bufferedReader = null;
		OutputStream outputStream = null;
		
		try {
			// get IOStream
			bufferedReader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
			outputStream = socket.getOutputStream();

			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			SimpleHttpServer.consolLog( "connected from " + inetSocketAddress.getHostName() + ":" + inetSocketAddress);

			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
			// outputStream.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
			// outputStream.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
			// outputStream.write( "\r\n".getBytes() );
			// outputStream.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );
			
			// * Print header+body
			// SimpleHttpServer.consolLog("=================================== request info =====================================");
			ArrayList<String> requestList = new ArrayList<>();
			while(true) {
				String line = bufferedReader.readLine();
				
				if(line == null || line.equals("")) {
					break;
				}
				
				requestList.add(line);
				// SimpleHttpServer.consolLog(line);
			}
			// SimpleHttpServer.consolLog("======================================================================================");
			
			// handle request
			String[] token = requestList.get(0).split(" ");
			if(token[0].equals("GET")) {
				responseStaticResource(outputStream, token[1], token[2]);
			} else {
				// 400 error
				response400Error(outputStream, token[2]);
			}
		} catch( Exception ex ) {
			SimpleHttpServer.consolLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( bufferedReader != null ) {
					bufferedReader.close();
				}
				
				if( outputStream != null ) {
					outputStream.close();
				}
				
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
				
			} catch( IOException ex ) {
				SimpleHttpServer.consolLog( "error:" + ex );
			}
		}
	}
	
	private void response400Error(OutputStream outputStream, String protocol) {
		try {
			File file = new File("./webapp/error/400.html");
			Path path = file.toPath();
			byte[] body = Files.readAllBytes(path);
			outputStream.write("HTTP/1.1 200 OK\r\n".getBytes("UTF-8"));
			outputStream.write("Content-Type:text/html; charset=utf-8\r\n".getBytes("UTF-8"));
			outputStream.write("\r\n".getBytes());
			outputStream.write(body);
		} catch (IOException e) {
			SimpleHttpServer.consolLog("[SERVER] error : " + e.toString());
		}
	}
	
	private void response404Error(OutputStream outputStream, String protocol) {
		try {
			File file = new File("./webapp/error/404.html");
			Path path = file.toPath();
			byte[] body = Files.readAllBytes(path);
			outputStream.write("HTTP/1.1 200 OK\r\n".getBytes("UTF-8"));
			outputStream.write("Content-Type:text/html; charset=utf-8\r\n".getBytes("UTF-8"));
			outputStream.write("\r\n".getBytes());
			outputStream.write(body);
		} catch (IOException e) {
			SimpleHttpServer.consolLog("[SERVER] error : " + e.toString());
		}
	}

	private void responseStaticResource(OutputStream outputStream, String url, String protocol) throws IOException {
		// handle default html
		if(url.equals("/")) {
			url = "/index.html";
		}
		
		// create file instance
		File file = new File("./webapp" + url);
		
		if(!file.exists()) {
			response404Error(outputStream, protocol);
			return;
		}
		
		Path path = file.toPath();
		byte[] body = Files.readAllBytes(path);
		String mimeType = Files.probeContentType(path);
		
		if(mimeType.toLowerCase().equals("text/html")) {
			mimeType = mimeType + "; charset=utf-8";
		}
		
		// send response
		outputStream.write("HTTP/1.1 200 OK\r\n".getBytes("UTF-8"));
		outputStream.write(("Content-Type:" + mimeType + ";\r\n").getBytes("UTF-8"));
		outputStream.write("\r\n".getBytes());
		outputStream.write(body);
	}
}
