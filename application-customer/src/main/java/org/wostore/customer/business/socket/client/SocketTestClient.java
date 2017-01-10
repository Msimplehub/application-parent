package org.wostore.customer.business.socket.client;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;

public class SocketTestClient {

	public static void main(String[] args) {
		start();
	}

	public static void start() {

		try {

			Socket client = new Socket("172.16.2.235", 8089);

			Writer writer = new OutputStreamWriter(client.getOutputStream());
			Scanner scanner = new Scanner(System.in);
			
			while(true){
				
				
				InputStream inputStream = client.getInputStream();
				
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				char[] buffer = new char[64];
				int len;
				StringBuffer stringBuffer = new StringBuffer();
				if ((len = inputStreamReader.read(buffer)) != -1) {
					stringBuffer.append(new String(buffer, 0, len));
				}
				
				System.out.println(stringBuffer.toString());
				
				
				String nextLine = scanner.nextLine();
				writer.write(nextLine);
				writer.flush();// 写完后要记得flush
				
//				inputStreamReader.close();
//				client.close();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
