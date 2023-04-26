package cn.tedu.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HiService {
    /**
     * RestTemplate是springmvc提供的一个类，可以实现http请求
     * 可以在代码中发起http请求，包含了很多种：get,post,put等
     *  getForObject(URI url, Class<T> responseType)
     *  使用http请求，向uri发起请求，获取相应的使用responseType反射类型解析数据
     */
    @Autowired
    private RestTemplate restTemplate;
    //通过resttemplate调用API方法，实现eureka-client负载均衡
    @HystrixCommand(fallbackMethod = "error")
    public String sayHi(String name) {
        String url="http://eureka-client/client/hello?name="+name;
        String forObject = restTemplate.getForObject(url, String.class);

        return forObject;
    }

    @HystrixCommand(fallbackMethod = "sayHi")
    public String error(String name) {
        String url="http://eureka-client/client/hello?name="+name;
        String forObject = restTemplate.getForObject(url, String.class);
        return forObject;
    }
}
