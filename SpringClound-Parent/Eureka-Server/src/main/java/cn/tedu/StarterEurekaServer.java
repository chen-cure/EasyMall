package cn.tedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer  //注册中心
public class StarterEurekaServer {
    public static void main(String[] args) {
        SpringApplication.run(StarterEurekaServer.class,args);
    }
}
