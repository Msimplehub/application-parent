package org.wostore.customer.business.nio.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioTestClient extends Thread {

	private SocketChannel socketChannel;
	private Selector selector;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NioTestClient client = new NioTestClient();
		try {
			client.initClient();
			client.start();
			// client.setDaemon(true);
		} catch (Exception e) {
			e.printStackTrace();
			client.stopServer();
		}
	}

	public void run() {
		while (true) {
			try {
				// 写消息到服务器端
				// writeMessage();

				int select = selector.select();
				if (select > 0) {
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> iter = keys.iterator();
					while (iter.hasNext()) {
						SelectionKey sk = iter.next();
						if (sk.isReadable()) {
							readMessage(sk);
						} else if (sk.isConnectable()) {// 已连接事件
							writeMessage(sk);
						}
//						else if (sk.isWritable()) {
//							writeMessage(sk);
//						}
						iter.remove();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void readMessage(SelectionKey sk) throws IOException, UnsupportedEncodingException {
		SocketChannel curSc = (SocketChannel) sk.channel();
		System.out.println("client readMessage : " + curSc.getRemoteAddress());
		ByteBuffer buffer = ByteBuffer.allocate(16);
		while (curSc.read(buffer) > 0) {
			buffer.flip();
			System.out.println("Receive from server: 001" + new String(buffer.array(), "UTF-8"));
			buffer.clear();
		}
	}

	public void writeMessage(SelectionKey sk) throws IOException {

		SocketChannel client = (SocketChannel) sk.channel();
		System.out.println("client readMessage : " + client.getRemoteAddress());
		try {
			/*
			 * 连接建立事件，已成功连接至服务器
			 */
			if (client.isConnectionPending()) {
				client.finishConnect();
				System.out.println("connect success 001!");
				// 注册读事件
				client.register(selector, SelectionKey.OP_READ);

				String ss = "Server,how are you? 001";
				ByteBuffer buffer = ByteBuffer.wrap(ss.getBytes("UTF-8"));
				while (buffer.hasRemaining()) {
					System.out.println("buffer.hasRemaining() is true. 001");
					client.write(buffer);
				}
			}
		} catch (IOException e) {
			if (client.isOpen()) {
				client.close();
			}
			e.printStackTrace();
		}
	}

	public void initClient() throws IOException, ClosedChannelException {
		InetSocketAddress addr = new InetSocketAddress("172.16.2.157", 2181);
		socketChannel = SocketChannel.open();

		selector = Selector.open();
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);

		// 连接到server
		socketChannel.connect(addr);

		while (!socketChannel.finishConnect()) {
			System.out.println("check finish connection");
		}
	}

	/**
	 * 停止客户端
	 */
	private void stopServer() {
		try {
			if (selector != null && selector.isOpen()) {
				selector.close();
			}
			if (socketChannel != null && socketChannel.isOpen()) {
				socketChannel.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
