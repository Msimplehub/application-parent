package org.wostore.customer.business.netty.client;

import java.io.UnsupportedEncodingException;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty 5.0版本
 * @className  NettyTcpClient_5 
 * @author  M.simple 
 * @datetime  2016年12月29日 下午6:04:56
 */
public class NettyTcpClient_5 {

	/*
	 * 服务器端口号
	 */
	private int port;

	/*
	 * 服务器IP
	 */
	private String host;

	public NettyTcpClient_5(int port, String host)
			throws InterruptedException {
		this.port = port;
		this.host = host;
		start();
	}

	private void start() throws InterruptedException {
		
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		
		try {
			
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.group(eventLoopGroup);
			bootstrap.remoteAddress(host, port);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel)
						throws Exception {					
					socketChannel.pipeline().addLast(new NettyClientHandler());
				}
			});
			/**ChannelFuture 异步回调
			 * 关于同步的阻塞和异步的非阻塞可以打一个很简单的比方，A向B打电话，通知B做一件事。
				在同步模式下，A告诉B做什么什么事，然后A依然拿着电话，等待B做完，才可以做下一件事；
				在异步模式下，A告诉B做什么什么事，A挂电话，做自己的事。B做完后，打电话通知A做完了。
			 */
			ChannelFuture future = bootstrap.connect(host, port).sync();
//			future.addListener(new ChannelFutureListener()
//	        {
//	            public void operationComplete(final ChannelFuture future)
//	                throws Exception
//	            {
//	            	System.out.println("connect结束： ");
//	            }
//	            
//	            
//	        });
			if (future.isSuccess()) {
				SocketChannel socketChannel = (SocketChannel) future.channel();
				System.out.println("----------------connect server success----------------");
			}
			future.channel().closeFuture().sync();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		
		NettyTcpClient_5 client = new NettyTcpClient_5(9999,
				"localhost");

	}
}

class NettyClientHandler extends ChannelHandlerAdapter  {
	
	private  ByteBuf firstMessage;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		byte[] data = "服务器，给我一个APPLE".getBytes();
		firstMessage=Unpooled.buffer();
		firstMessage.writeBytes(data);
		ctx.writeAndFlush(firstMessage);
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf buf = (ByteBuf) msg;
	String rev = getMessage(buf);
	System.out.println("客户端收到服务器数据:" + rev);
	}
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
}