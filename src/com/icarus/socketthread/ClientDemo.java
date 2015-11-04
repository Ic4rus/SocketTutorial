package com.icarus.socketthread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class ClientDemo {
	
	public static void main(String[] args) {
		
		// Địa chỉ máy chủ
		final String serverHost = "localhost";
		
		Socket socketOfClient = null;
		BufferedWriter os = null;
		BufferedReader is = null;
		
		try {
			// Gửi yêu cầu kết nối tới server đang lắng nghe
			// trên máy 'localhost' cổng 7777
			socketOfClient = new Socket(serverHost, 7777);
			
			// Tạo luồng đầu ra tại client (gửi dữ liệu tới server)
			os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
			
			// Tạo luồng đầu vào tại client (đọc dữ liệu từ server)
			is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
			
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + serverHost);
			return;
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + serverHost);
			return;
		}
		
		try {
			os.write("HELLO! now is " + new Date());
			os.newLine();
			os.flush();
			os.write("I am Tom Cat ");
			os.newLine();
			os.flush();
			os.write("QUIT");
			os.newLine();
			os.flush();
			
			// Đọc dữ liệu trả về từ phía server
			// Bằng cách đọc luồng đầu vào của socket tại client
			String responseLine;
			while ((responseLine = is.readLine()) != null) {
				System.out.println("Server: " + responseLine);
				if (responseLine.indexOf("OK") != -1) {
					break;
				}
			}
			os.close();
			is.close();
			socketOfClient.close();
		} catch (UnknownHostException e) {
			System.err.println("Trying to connect to unknown host: " + e);
		} catch (IOException e) {
			System.err.println("IOException: " + e);
		}
		
	}

}
