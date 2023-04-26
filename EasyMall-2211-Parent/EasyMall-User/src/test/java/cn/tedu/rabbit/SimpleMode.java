package cn.tedu.rabbit;

import com.rabbitmq.client.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 完成一个简单模式，一发一接结构
 */
public class SimpleMode {
    //初始化连接对象 短链接
    private Channel channel;

    @Before
    public void channelInit() throws IOException, TimeoutException {
        /**
         * 1.长连接工厂，提供四个属性 ip port tedu 123456
         * 2.获取长连接
         * 3.给成员赋值 channel赋值
          */
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.42.6.60");
        factory.setPort(5672);
        factory.setUsername("tedu");
        factory.setPassword("123456");

        //获取长连接
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
    }

    @Test
    public void myQueueDeclare() throws IOException {
        //声明队列，调用短链接
        channel.queueDeclare(
                "simple", //队列
                false,  //布尔类型，队列是否持久化
                false, //布尔类型，对列是否专属
                //从第一个消费端监听队列开始，到最后一个消费端监听队列为止，
                false,//对列是否自动删除,所有的消费端完成后
                null //map类型，可以固定一些属性
        );
        System.out.println("队列声明成功");
    }

    //生产端
    @Test
    public void send() throws IOException {
        //准备消息
        String msg = "今天天气不错";
        byte[] msgBytes = msg.getBytes();
        //将消息发送给交换机 AMQP
        channel.basicPublish(
                "", //交换机名称
                "simple", //路由key 指的是让交换机发送消息到哪一个队列
                null, //发送消息的时候携带的消息头等
                msgBytes //消息体，消息的主题内容
        );
    }

    //消费端
    @Test
    public void consume() throws IOException {
        //消费消息
        channel.basicConsume(
                "simple",
                true,
//                false,
                /**
                 * 传递调用对象，消息就在这个对象中
                 * consumerTag 当前消费端的id
                 * message 封装消息的对象
                 */
                new DeliverCallback() {
                    @Override
                    public void handle(String consumerTag, Delivery message) throws IOException {
                        byte[] body = message.getBody();
                        System.out.println(new String(body));
//                        channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
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
