package cn.tedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//开启feign组件
@EnableFeignClients
public class StarterFeign {
    public static void main(String[] args) {
        SpringApplication.run(StarterFeign.class , args);
    }
}
