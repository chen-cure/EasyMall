package cn.tedu.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQDeclare {
    @Bean
    public Queue queue01(){
         Queue queue = new Queue(
                 "direct_Q1",
                 false,
                 false,
                 false,
                 null
         );
         return queue;
    }

    //声明交换机，不需要提供类型   direct fanout topic封装了对应的类型
    @Bean
    public DirectExchange ex01(){
        DirectExchange exchange = new DirectExchange("direct_ex");
        return  exchange;
    }

    //绑定交换机
    @Bean
    public Binding bind01(){
        Binding binding = BindingBuilder.bind(queue01()).to(ex01()).with("成都");
        return binding;
    }

}
