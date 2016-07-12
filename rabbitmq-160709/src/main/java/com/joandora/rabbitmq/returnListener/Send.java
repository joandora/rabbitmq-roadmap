/**   
 * @Title: Send.java
 * @Package com.joandora.rabbitmq
 * @Description: 消息生产者
 * @author JOANDORA   
 * @date 2015年8月30日 下午10:03:00
 * @version V1.0   
 */

package com.joandora.rabbitmq.returnListener;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.joandora.tool.properties.ApacheConfigureProperties;
import com.rabbitmq.client.*;
import com.rabbitmq.utility.BlockingCell;
import org.apache.commons.configuration.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

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
        // doesn't help if server got out of space
        factory.setRequestedHeartbeat(1);
		Connection connection = factory.newConnection();

        connection.addBlockedListener(new BlockedListener() {
            public void handleBlocked(String reason) throws IOException {
                System.out.println("Connection is now blocked:"+reason);
            }

            public void handleUnblocked() throws IOException {
                System.out.println("Connection is now unblocked");
            }
        });

		Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("[X]Returned message(replyCode:" + replyCode + ",replyText:" + replyText
                        + ",exchange:" + exchange + ",routingKey:" + routingKey + ",body:" + new String(body));
            }
        });

        String message = "i will not ack";
        channel.basicPublish("", "hello3", true,null,message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        while(true){

        }
//		channel.close();
//		connection.close();
	}
}
/***
 *  [x] Sent 'i will not ack'
 [X]Returned message(replyCode:312,replyText:NO_ROUTE,exchange:exchangeName,routingKey:routingKey,body:i will not ack
 */
