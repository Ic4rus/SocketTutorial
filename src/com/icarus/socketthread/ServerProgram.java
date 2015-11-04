package com.icarus.socketthread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerProgram {
	
	public static void main(String[] args) throws IOException {
		
		ServerSocket listener = null;
		
		System.out.println("Server is waiting to accept user...");
		int clientNumber = 0;
		
		// Mở 1 ServerSocket tại cổng 7777
		// Không thể chọn cổng nhỏ hơn 1023 nếu không là người dùng đặc quyền (privileged users (root))
		try {
			listener = new ServerSocket(7777);
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}
		
		try {
			while (true) {
				// Chấp nhận 1 yêu cầu kết nối từ phía client
				// Đồng thời nhận được 1 đối tượng Socket tại server
				Socket socketOfServer = listener.accept();
				new ServiceThread(socketOfServer, clientNumber++).start();
			}
		} finally {
			listener.close();
		}
	}
	
	private static void log(String message) {
		System.out.println(message);
	}
	
	private static class ServiceThread extends Thread {
		
		private Socket socketOfServer;
		private int clientNumber;
		
		public ServiceThread(Socket socketOfServer, int clientNumber) {
			this.socketOfServer = socketOfServer;
			this.clientNumber = clientNumber;
			// Log
			log("New connection with client# " + this.clientNumber + " at " + this.socketOfServer);
		}

		@Override
		public void run() {
			try {
				// Mở luồng vào/ra trên Socket tại server
				BufferedReader is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
				BufferedWriter os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
				
				while (true) {
					// Đọc dữ liệu tới server (do client gửi tới)
					String line = is.readLine();
					
					// Ghi vào luồng đầu ra của Socket tại server
					// (Gửi dữ liệu tới client)
					os.write(">> " + line);
					// Kết thúc dòng
					os.newLine();
					// Đẩy dữ liệu đi
					os.flush();
					
					// Nếu người dùng gửi tới QUIT (muốn kết thúc trò truyện)
					if (line.equals("QUIT")) {
						os.write(">> OK");
						os.newLine();
						os.flush();
						break;
					}
				}
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}

}
