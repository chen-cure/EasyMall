package cn.tedu.controller;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * 直接调用rabbitmqtemplate api中的方法
 */
@RestController
public class RabbitMQController {
    //将自动的创建temple对象注入  测试生产和消费
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/send")
    public String send(String msg){
        //调用rabbitmqTemplate发送消息的api
//        rabbitTemplate.convertAndSend("direct_ex","北京",msg);

        //send方法   需要关心消息对象的封装
        MessageProperties properties = new MessageProperties();
        properties.setContentType("application/json");
        properties.setContentEncoding("utf-8");
        properties.setPriority(100);
        properties.setUserId("tedu");
        Message message = new Message(msg.getBytes(),properties);
        rabbitTemplate.send("direct_ex","成都",message);
        return "success";
    }

    @RequestMapping("/receive")
    public String receive(String queue){
//        String msg = (String) rabbitTemplate.receiveAndConvert(queue);

        //需要传递一个接收queue名称
        Message receive = rabbitTemplate.receive(queue);
        byte[] body = receive.getBody();
        String msg  = new String(body);
        System.out.println(""+new String(body));
        return msg;
    }

}
