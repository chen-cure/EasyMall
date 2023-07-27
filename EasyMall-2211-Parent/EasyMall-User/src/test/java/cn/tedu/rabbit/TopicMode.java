package cn.tedu.rabbit;

import com.rabbitmq.client.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 主机模式
 */
public class TopicMode {
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

    //准备交换机和队列的属性
    private static final String TYPE="topic";
    private static final String EX_NAME=TYPE+"_ex";
    private static final String QUEUE1=TYPE+"_Q1";
    private static final String QUEUE2=TYPE+"_Q2";

    @Test
    public void declare() throws IOException {
        //声明队列
        channel.queueDeclare(QUEUE1,false,false,false,null);
        channel.queueDeclare(QUEUE2,false,false,false,null);

        //绑定交换机
        channel.exchangeDeclare(EX_NAME,TYPE);
        channel.queueBind(QUEUE1,EX_NAME,"中国.北京市.#");
        channel.queueBind(QUEUE1,EX_NAME,"中国.*.*.*.#");
        channel.queueBind(QUEUE2,EX_NAME,"*.上海.#");
    }

    //生产者
    @Test
    public void send() throws IOException {
        String msg = "你好，主题模式";
        byte[] bytes = msg.getBytes();

        channel.basicPublish(EX_NAME,"中国.北京.房山.大学城",null,bytes);
    }

    //消费者
    @Test
    public void consume() throws IOException {
        channel.basicConsume(
                QUEUE1,
                false,   //当autoAck为false时，后面需要手动设置ack确认
                new DeliverCallback() {
                    @Override
                    public void handle(String consumerTag, Delivery message) throws IOException {
                        byte[] body = message.getBody();
                        System.out.println(new String(body));
                        channel.basicAck(message.getEnvelope().getDeliveryTag(),false);  //手动设置ack确定
                    }
                },
                new CancelCallback() {
                    @Override
                    public void handle(String consumerTag) throws IOException {
                    }
                }
        );
        while (true);
    }
}
