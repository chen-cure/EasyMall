package cn.tedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@MapperScan("cn.tedu.mapper")
@EnableEurekaClient
public class StarterSeckill {
    public static void main(String[] args) {
        SpringApplication.run(StarterSeckill.class,args);
    }

/*    @LoadBalanced
    @Bean
    public RestTemplate initrestTemple(){
        return new RestTemplate();
    }*/
}
