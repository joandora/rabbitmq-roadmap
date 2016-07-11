/**   
 * @Title: Send.java
 * @Package com.joandora.rabbitmq
 * @Description: 消息生产者
 * @author JOANDORA   
 * @date 2015年8月30日 下午10:03:00
 * @version V1.0   
 */

package com.joandora.rabbitmq.manualAck;

import com.joandora.tool.properties.ApacheConfigureProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.configuration.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName: Send
 * @Description: 消息生产者-手动ack
 * 一个简单的rabbitmq消息发送和接受的例子
 * @author JOANDORA
 * @date 2015年8月30日 下午10:03:00
 */
public class Send {
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args) throws Exception {
        Configuration config = new ApacheConfigureProperties("env/rabbitmq.properties" );
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.getString("rabbitmq.host"));
        factory.setUsername(config.getString("rabbitmq.username"));
        factory.setPassword(config.getString("rabbitmq.password"));
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
		String message = "i will not ack";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
		System.out.println(" [x] Sent '" + message + "'");

//		channel.close();
//		connection.close();
	}
}
