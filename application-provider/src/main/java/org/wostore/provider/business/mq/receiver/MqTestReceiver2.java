package org.wostore.provider.business.mq.receiver;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MqTestReceiver2 {

	public static void main(String[] args) {
		MqTestReceiverThread2 mqTestReceiverThread = new MqTestReceiverThread2();
		mqTestReceiverThread.start();
	}
}

class MqTestReceiverThread2 extends Thread {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// super.run();
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		ConnectionFactory connectionFactory;
		// Connection ：JMS 客户端到JMS Provider 的连接
		Connection connection = null;
		// Session： 一个发送或接收消息的线程
		Session session;
		// Destination ：消息的目的地;消息发送给谁.
		Destination destination;
		// 消费者，消息接收者
		MessageConsumer consumer;
		connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://172.16.2.80:61616");
		try {
			// 构造从工厂得到连接对象
			connection = connectionFactory.createConnection();
			// 启动
			connection.start();
			// 获取操作连接
			session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
//			destination = session.createQueue("SimpleTest");
			destination = session.createTopic("SimpleTest");
			consumer = session.createConsumer(destination);
			while (true) {
				// 设置接收者接收消息的时间，为了便于测试，这里谁定为100s
				TextMessage message = (TextMessage) consumer.receive(100000);
				if (null != message) {
					System.out.println("MqTestReceiver2收到消息" + message.getText());
				} else {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != connection)
					connection.close();
			} catch (Throwable ignore) {
			}
		}
	}
}
