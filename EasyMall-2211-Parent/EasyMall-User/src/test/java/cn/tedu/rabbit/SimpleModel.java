package cn.tedu.rabbit;

import com.rabbitmq.client.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SimpleModel {
    private Channel channel;
    @Before
    public void channelInit() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.42.6.60");
        factory.setPort(5672);
        factory.setUsername("tedu");
        factory.setPassword("123456");

        Connection connection = factory.newConnection();
        channel = connection.createChannel();
    }

    @Test
    public  void  createqueue() throws IOException {
        channel.queueDeclare(
                "simple",
                false,
                false,
                true,
                null
        );
        System.out.println("创建队列成功");
    }

    @Test
    public void  create() throws IOException {
        String msg = "今天天气不错";
        byte[] msgBytes = msg.getBytes();
        channel.basicPublish(
                "",
                "simple",
                null,
                msgBytes
        );
        System.out.println("消息发送成功");
    }

    @Test
    public void consumer() throws IOException {
        channel.basicConsume(
                "simple",
                false,
                new DeliverCallback() {
                    @Override
                    public void handle(String consumerTag, Delivery message) throws IOException {
                        byte[] body = message.getBody();
                        System.out.println(new String(body));
                        channel.basicAck(
                                message.getEnvelope().getDeliveryTag(),false
                        );
                    }
                },
                new CancelCallback() {
                    @Override
                    public void handle(String consumerTag) throws IOException {

                    }
                }

        );
        System.out.println("消息消费成功");
    }
}
