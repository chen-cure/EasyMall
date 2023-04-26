package cn.tedu;

import com.netflix.servo.util.VisibleForTesting;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

public class template {
    @Test
    public void template(){
        String uri="http://www.baidu.com";
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(uri, String.class);
        System.out.println(forObject);
    }

}
