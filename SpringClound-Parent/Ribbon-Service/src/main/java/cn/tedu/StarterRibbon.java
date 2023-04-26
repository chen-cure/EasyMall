package cn.tedu;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.WeightedResponseTimeRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class StarterRibbon {
    public static void main(String[] args) {
        SpringApplication.run(StarterRibbon.class,args);
    }

    //创建一个容器的bean对象
    @Bean //此时RestTemplate作为一个容器bean对象给控制反转，可以注入到任意位置
    @LoadBalanced  //添加ribbon的拦截逻辑，如果没有就无法实现服务端的调用；为当前的restTemplate添加一个拦截器，将hostname转成ip:post形式
    public RestTemplate initRestTemplate(){
        return new RestTemplate();
    }

//    @Bean
//    public IRule initRule(){
//        return new WeightedResponseTimeRule();  //按时间权重来
////        return new RandomRule(); //随机
//    }
}
