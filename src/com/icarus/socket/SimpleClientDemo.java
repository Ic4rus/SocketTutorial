package com.icarus.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleClientDemo {
	
	public static void main(String[] args) {
		
		// Địa chỉ máy chủ
		final String serverHost = "localhost";
		
		Socket socketOfClient = null;
		BufferedWriter os = null;
		BufferedReader is = null;
		
		try {
			// Gửi yêu cầu kết nối tới server đang lắng nghe
			// Trên máy 'localhost' cổng 9999
			socketOfClient = new Socket(serverHost, 9999);
			
			// Tạo luồng đầu ra tại client
			// => Gửi dữ liệu tới server
			os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
			
			// Tạo luồng đầu vào tại client
			// => Nhận dữ liệu từ server
			is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
			
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + serverHost);
			return;
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for connection to " + serverHost);
			return;
		}
		
		try {
			// Ghi dữ liệu vào luồng đầu ra của Socket tại client
			os.write("HELLO");
			os.newLine();
			os.flush();
			os.write("I am Tom Cat");
			os.newLine();
			os.flush();
			os.write("QUIT");
			os.newLine();
			os.flush();
			
			// Đọc dữ liệu trả về từ phía server
			// (Đọc luồng đầu vào của socket tại client)
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
			System.err.println("Trying to connect unknow host " + serverHost);
			return;
		} catch (IOException e) {
			System.err.println("IOException " + serverHost);
			return;
		}
	}

}
