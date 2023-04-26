package cn.tedu.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 利用feign组件，配置一个接口类
 * 声明式注解，完成调用功能
 */
@FeignClient("eureka-client")
public interface HiService {
    //被调用的controller中注解怎样使用，这儿就怎样使用
    @RequestMapping(value = "/client/hello",method = RequestMethod.GET)
    public String sayhi(@RequestParam("name") String name);
}
