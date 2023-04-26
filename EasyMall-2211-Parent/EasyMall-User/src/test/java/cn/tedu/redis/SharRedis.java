package cn.tedu.redis;

import redis.clients.jedis.Jedis;

import java.util.List;

public class SharRedis {
    private List<Jedis> nodes;
    public SharRedis(List<Jedis> nodes){
        this.nodes=nodes;
    }

    //获取jedis对象
    public Jedis hashKey(String key){
        int result = (key.hashCode()&Integer.MAX_VALUE)%nodes.size();
        return nodes.get(result);
    }

    //存储数据
    public void set(String key ,String value){
        Jedis jedis = hashKey(key);
        jedis.set(key,value);
    }

    //获取数据
    public String get(String key){
        Jedis jedis = hashKey(key);
        return jedis.get(key);
    }
}
