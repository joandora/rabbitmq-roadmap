/**   
* @Title: Recv.java
* @Package com.joandora.rabbitmq
* @Description: 消息消费者
* @author JOANDORA   
* @date 2015年8月30日 下午11:34:53
* @version V1.0   
*/


package com.joandora.rabbitmq.manualAck;

import com.joandora.tool.properties.ApacheConfigureProperties;
import com.rabbitmq.client.*;
import org.apache.commons.configuration.Configuration;

import java.io.IOException;

/**
 * @ClassName: Recv
 * @Description: 消息消费者-手动ack
 * @author JOANDORA
 * @date 2015年8月30日 下午11:34:53
 */
public class Recv {
	private final static String QUEUE_NAME = "hello";
	public static void main(String[] argv) throws Exception {
        Configuration config = new ApacheConfigureProperties("env/rabbitmq.properties" );
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(config.getString("rabbitmq.host"));
        factory.setUsername(config.getString("rabbitmq.username"));
        factory.setPassword(config.getString("rabbitmq.password"));
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    channel.queueDeclare(QUEUE_NAME, false, false, true, null);
	    System.out.println(" [*] Waiting for messages.");

        //创建队列消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        //指定消费队列
        channel.basicConsume(QUEUE_NAME, false, queueingConsumer);
        while (true){
            //nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            long deliveryTag = delivery.getEnvelope().getDeliveryTag();
            System.out.println(" [x] deliveryTag:"+deliveryTag);
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
            if(message.equals("i will not ack")){
                channel.basicNack(deliveryTag,false,true);
                System.out.println(" [x] already unack:"+message);
            }
        }
    }
}
