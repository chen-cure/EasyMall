package cn.tedu.controller;

import cn.tedu.service.HiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiController {
    @Autowired
    private HiService hiService;
    //访问ribbon的地址
    @RequestMapping("/ribbon/hello")
    public String sayHi(String name){
        //假设用户访问ribbon工程，ribbon
        return "RIBBON "+hiService.sayHi(name);
    }
}
