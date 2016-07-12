/**   
 * @Title: Send.java
 * @Package com.joandora.rabbitmq
 * @Description: 消息生产者
 * @author JOANDORA   
 * @date 2015年8月30日 下午10:03:00
 * @version V1.0   
 */

package com.joandora.rabbitmq.publishConfirm;

import com.joandora.tool.properties.ApacheConfigureProperties;
import com.rabbitmq.client.*;
import org.apache.commons.configuration.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: Send
 * @Description: 消息生产者-手动ack
 * 一个简单的rabbitmq消息发送和接受的例子
 * @author JOANDORA
 * @date 2015年8月30日 下午10:03:00
 */
public class Send {
	private final static String QUEUE_NAME = "hello2";

	public static void main(String[] args) throws Exception {
        Configuration config = new ApacheConfigureProperties("env/rabbitmq.properties" );
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.getString("rabbitmq.host"));
        factory.setUsername(config.getString("rabbitmq.username"));
        factory.setPassword(config.getString("rabbitmq.password"));
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
        channel.confirmSelect();

        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
		String message = "i will not ack";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
		System.out.println(" [x] Sent '" + message + "'");


        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("handleAck："+deliveryTag);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("handleAck："+ deliveryTag);
            }
        });

        while(true){

        }
//		channel.close();
//		connection.close();
	}
}
