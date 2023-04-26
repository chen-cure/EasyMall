package cn.tedu.rabbit;

import com.rabbitmq.client.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 完成一发多接结构
 */
public class FanoutMode {
    private Channel channel;

    @Before
    public void channelInit() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.42.6.60");
        factory.setPort(5672);
        factory.setUsername("tedu");
        factory.setPassword("123456");

        Connection connection = factory.newConnection();
        channel = connection.createChannel();
    }

    //声明组件
    //准备交换机  队列的名称和相关属性
    private static final String TYPE="fanout";
    private static final String EX_NAME=TYPE+"_ex";
    private static final String QUEUE01=TYPE+"_01";
    private static final String QUEUE02=TYPE+"_02";

    @Test
    public void declare() throws IOException {
        /**
         * queue 队列的名称
         * durable 是否要持久化
         * exclusive 是否排它
         * autoDelete 队列是否自动删除
         * arguments 可以设置队列其他一些参数
         */
        //声明队列
        channel.queueDeclare(QUEUE01,false,false,false,null);
        channel.queueDeclare(QUEUE02,false,false,false,null);
        //声明交换机
        channel.exchangeDeclare(EX_NAME,TYPE);
        channel.queueBind(QUEUE01,EX_NAME,"北京");
        channel.queueBind(QUEUE02,EX_NAME,"成都");
    }

    //生产端
    @Test
    public void send() throws IOException {
        String msg = "你好，发布订阅模式";
        byte[] bytes = msg.getBytes();
        channel.basicPublish(EX_NAME,"成都",null,bytes);
    }

    //消费端
    @Test
    public void consume() throws IOException {
        //消费消息
        channel.basicConsume(
                QUEUE01,
                true,
                new DeliverCallback() {
                    @Override
                    public void handle(String consumerTag, Delivery message) throws IOException {
                        byte[] body = message.getBody();
                        System.out.println(new String(body));
                    }
                },
                new CancelCallback() {
                    @Override
                    public void handle(String consumerTag) throws IOException {
                    }
                }
        );
        //添加死循环
        while(true);
    }
}
