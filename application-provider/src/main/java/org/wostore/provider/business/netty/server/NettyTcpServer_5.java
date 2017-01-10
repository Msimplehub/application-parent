package org.wostore.provider.business.netty.server;

import java.io.UnsupportedEncodingException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty 5.0版本
 * @className  NettyTcpServer_5 
 * @author  M.simple 
 * @datetime  2016年12月29日 下午6:05:31
 */
public class NettyTcpServer_5{
	
private int port;

public NettyTcpServer_5(int port) {
	this.port = port;
	bind();
}

private void bind() {

	EventLoopGroup boss = new NioEventLoopGroup();
	EventLoopGroup worker = new NioEventLoopGroup();

	try {

		ServerBootstrap bootstrap = new ServerBootstrap();

		bootstrap.group(boss, worker);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.option(ChannelOption.SO_BACKLOG, 1024); //连接数
		bootstrap.option(ChannelOption.TCP_NODELAY, true);  //不延迟，消息立即发送
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); //长连接
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel)
					throws Exception {
				ChannelPipeline p = socketChannel.pipeline();
				p.addLast(new NettyServerHandler());
			}
		});
		ChannelFuture f = bootstrap.bind(port).sync();
		if (f.isSuccess()) {
			System.out.println("启动Netty服务成功，端口号：" + this.port);
		}
		// 关闭连接
		f.channel().closeFuture().sync();

	} catch (Exception e) {
		System.out.println("启动Netty服务异常，异常信息：" + e.getMessage());
		e.getMessage();
		e.printStackTrace();
	} finally {
		boss.shutdownGracefully();
		worker.shutdownGracefully();
	}
}

public static void main(String[] args) throws InterruptedException {
	NettyTcpServer_5 server= new NettyTcpServer_5(9999);
}


public class NettyServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {

		ByteBuf buf = (ByteBuf) msg;

		String recieved = getMessage(buf);
		System.out.println("服务器接收到消息：" + recieved);

		try {
			ctx.writeAndFlush(getSendByteBuf("APPLE"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 从ByteBuf中获取信息 使用UTF-8编码返回
	 */
	private String getMessage(ByteBuf buf) {

		byte[] con = new byte[buf.readableBytes()];
		buf.readBytes(con);
		try {
			return new String(con, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private ByteBuf getSendByteBuf(String message) throws UnsupportedEncodingException {

		byte[] req = message.getBytes("UTF-8");
		ByteBuf pingMessage = Unpooled.buffer();
		pingMessage.writeBytes(req);

		return pingMessage;
	}
}
}