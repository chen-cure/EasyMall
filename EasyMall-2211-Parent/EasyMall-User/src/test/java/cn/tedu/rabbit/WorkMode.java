package cn.tedu.rabbit;

import com.rabbitmq.client.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 完成一发多抢结构
 */
public class WorkMode {
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
    @Test
    public void myQueueDeclare() throws IOException {
        //调用channel的方法 声明队列
        channel.queueDeclare(
                "work",
                false,
                false,
                false,
                null
        );
        System.out.println("队列声明成功");
    }

    //生产端
    @Test
    public void send() throws IOException {
        String msg = "今天星期四";
        byte[] bytes = msg.getBytes();
        //将消息发送给默认的交换机
        channel.basicPublish(
                "",
                "work",
                null,
                bytes
        );
    }

    //消费端
    @Test
    public void consume01() throws IOException {
        //消费消息
        channel.basicConsume(
                "work",
                false,
                /**
                 * 获取消息
                 */
                new DeliverCallback() {
                    @Override
                    public void handle(String consumerTag, Delivery message) throws IOException {
                        byte[] body = message.getBody();
                        System.out.println("这是消费者1 "+new String(body));
                        //如果false 说明消息消费完毕，确认
                        channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
                    }
                },
                new CancelCallback() {
                    /**
                     * 当连接对象channel主动关闭消费端连接的时候，cannel才回被调用
                     * @param consumerTag 当前的消费端id
                     */
                    @Override
                    public void handle(String consumerTag) throws IOException {
                    }
                }
        );
        //添加死循环
        while(true);
    }

    @Test
    public void consume02() throws IOException {
        //消费消息
        channel.basicConsume(
                "work",
                false,
                /**
                 * 获取消息
                 */
                new DeliverCallback() {
                    @Override
                    public void handle(String consumerTag, Delivery message) throws IOException {
                        byte[] body = message.getBody();
                        System.out.println("这是消费者2 "+new String(body));
                        //如果false 说明消息消费完毕，确认
                        channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
                    }
                },
                new CancelCallback() {
                    /**
                     * 当连接对象channel主动关闭消费端连接的时候，cannel才回被调用
                     * @param consumerTag 当前的消费端id
                     */
                    @Override
                    public void handle(String consumerTag) throws IOException {
                    }
                }
        );
        //添加死循环
        while(true);
    }

}
