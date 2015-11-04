package com.icarus.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServerProgram {
	
	public static void main(String[] args) {
		
		ServerSocket listener = null;
		String line;
		BufferedReader is;
		BufferedWriter os;
//		Socket socketOfServer = null;
		
		// Mở ServerSocket tại cổng 9999
		// Không thể chọn cổng nhỏ hơn 1023 nếu không là người dùng đặc quyền (privillegd users(root))
		try {
			listener = new ServerSocket(9999);
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		
		try {
			System.out.println("Server is waiting to accept user...");
			
			// Chấp nhận 1 yêu cầu kết nối từ phía client
			// Đồng thời nhận được 1 đối tượng Socket tại server
			Socket socketOfServer = listener.accept();
			System.out.println("Accept a client");
			
			// Mở luồng vào/ra trên Socket tại server
			is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
			os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
			
			// Nhận dữ liệu từ người dùng và gửi lại trả lời
			while (true) {
				// Đọc dữ liệu tới server do client gửi tới
				line = is.readLine();
				
				// Ghi vào luồng ra của Socket tại server
				// ==> gửi tới client
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
			
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		System.out.println("Server stopped!");
	}

}
