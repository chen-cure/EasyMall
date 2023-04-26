package cn.tedu.rabbit;

import com.rabbitmq.client.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 路由模式
 */
public class DirectMode {
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

    private static final String TYPE="direct";
    private static final String EX_NAME=TYPE+"_ex";
    private static final String QUEUE01=TYPE+"_01";
    private static final String QUEUE02=TYPE+"_02";

    @Test
    public void myQueueDeclare() throws IOException {
        channel.queueDeclare(QUEUE01,false,false,false,null);
        channel.queueDeclare(QUEUE02,false,false,false,null);

        channel.exchangeDeclare(EX_NAME,TYPE);

        channel.queueBind(QUEUE01,EX_NAME,"北京");
        channel.queueBind(QUEUE02,EX_NAME,"成都");
    }

    @Test
    public void send() throws IOException {
        String msg = "你好，路由模式";
        byte[] bytes = msg.getBytes();
        channel.basicPublish(EX_NAME,"北京",null,bytes);
    }

    @Test
    public void consume() throws IOException {
        channel.basicConsume(
                QUEUE01,
                false,
                new DeliverCallback() {
                    @Override
                    public void handle(String consumerTag, Delivery message) throws IOException {
                        byte[] body = message.getBody();
                        System.out.println(new String(body));
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
