package cn.tedu.rabbit;

import com.rabbitmq.client.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MqTest {
    private Channel channel;
    private ConnectionFactory connectionFactory;
    @Before
    public void initConnect() throws IOException, TimeoutException {
        connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("10.42.6.60");
        connectionFactory.setPort(5);
        connectionFactory.setUsername("tedu");
        connectionFactory.setPassword("123456");

        Connection connection = connectionFactory.newConnection();
        channel = connection.createChannel();
    }

    @Test
    public void InitQue() throws IOException {
        channel.queueDeclare(
                "a",
                false,
                false,
                false,
                null
        );
    }

    @Test
    public void sendMessage() throws IOException {
        String msg = "今天天气真好";
        byte[] bytes = msg.getBytes();
        channel.basicPublish(
                "",
                "simple",
                null,
                bytes
        );
    }

    @Test
    public void consume() throws IOException {
        channel.basicConsume(
                "",
                false,
                new DeliverCallback() {
                    @Override
                    public void handle(String consumerTag, Delivery message) throws IOException {
                        byte[] body = message.getBody();
                        System.out.println(body);
                        channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
                    }
                },
                new CancelCallback() {
                    @Override
                    public void handle(String consumerTag) throws IOException {

                    }
                }
        );
    }
}
