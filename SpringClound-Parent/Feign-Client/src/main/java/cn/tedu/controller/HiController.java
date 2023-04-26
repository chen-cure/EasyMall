package cn.tedu.controller;



import cn.tedu.api.HiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiController {
    @Autowired
    private HiService hiService;

    @RequestMapping("/feign/hello")
    public String sayHi(String name ){
        System.out.println(hiService.getClass());
        return "Feign:"+hiService.sayhi(name);
    }
}
