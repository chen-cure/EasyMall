package cn.tedu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class RedisController {
    //注入StringRedisTemplate
    @Autowired
    private StringRedisTemplate template;
    @RequestMapping("/redis")
    public String setAndGet(String key, String value){
        //String类型
        ValueOperations<String ,String > stringStringValueOperations = template.opsForValue();
        //Hash类型
        HashOperations<String, Object, Object> stringObjectObjectHashOperations = template.opsForHash();
        //List类型
        ListOperations<String, String> stringStringListOperations = template.opsForList();
        //Set Zset类型
        SetOperations<String , String > setOperations = template.opsForSet();

        stringStringValueOperations.set(key,value);
        return stringStringValueOperations.get(key);
    }
}
