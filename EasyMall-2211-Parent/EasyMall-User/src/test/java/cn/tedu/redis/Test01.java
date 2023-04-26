package cn.tedu.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import redis.clients.jedis.*;

import java.util.*;


public class Test01 {
    @Test
    public void test01(){
        //创建客户端
        Jedis jedis = new Jedis("10.10.10.11", 8000);
        //写一个字符串
        jedis.set("name","张三");

        //读数据
        String name = jedis.get("name");
        System.out.println("我叫"+name);

        //hash类型

        jedis.hset("user","age","18");
        //读
        String age = jedis.hget("user", "age");
        System.out.println("获取到的user的属性值是"+age);
    }

    @Test
    public void test02(){
        List<Jedis> nodes = new ArrayList<>();
        nodes.add(new Jedis("10.42.6.60", 6379));
        nodes.add(new Jedis("10.42.6.60", 6380));
        nodes.add(new Jedis("10.42.6.60", 6381));

        SharRedis sharRedis = new SharRedis(nodes);
        for (int i=0;i<100;i++){
            String key = UUID.randomUUID().toString();
            String value="value"+i;
            sharRedis.set(key,value);
            System.out.println(sharRedis.get(key));
        }
    }

    @Test
    public void test03(){
        //创建分片对象，收集节点信息
        List<JedisShardInfo> infos = new ArrayList<>();
        infos.add(new JedisShardInfo("10.42.6.60", 6379));
        infos.add(new JedisShardInfo("10.42.6.60", 6380));
        infos.add(new JedisShardInfo("10.42.6.60", 6381));

        //利用list创建分片对象
        ShardedJedis shardedJedis = new ShardedJedis(infos);
        for (int i = 0 ;i<99 ;i++){
            String key = UUID.randomUUID().toString();
            shardedJedis.set(key,"");
        }

    }

    //连接池
    @Test
    public void test04(){
        JedisPool pool = new JedisPool("10.42.6.60", 9000);
        Jedis resource = pool.getResource();
        resource.set("name","lin");
        pool.close();


        List<JedisShardInfo> infos = new ArrayList<>();
        infos.add(new JedisShardInfo("10.42.6.60", 6379));
        infos.add(new JedisShardInfo("10.42.6.60", 6380));
        infos.add(new JedisShardInfo("10.42.6.60", 6381));


        ShardedJedisPool pool1 = new ShardedJedisPool(new GenericObjectPoolConfig(),infos);


    }


    @Test
    public void test05(){
        //此时连接的就不是具体的节点，而是任意的哨兵
        //收集哨兵的信息
        //向哨兵获取连接，然后连接主节点，至于是哪一个哨兵，随机
        Set<String> infos = new HashSet<>();
        infos.add(new HostAndPort("10.42.6.60",26379).toString());
        infos.add(new HostAndPort("10.42.6.60",26380).toString());
        infos.add(new HostAndPort("10.42.6.60",26381).toString());

        JedisSentinelPool pool = new JedisSentinelPool("mymaster",infos);
        System.out.println("当前的主节点："+pool.getCurrentHostMaster());
        Jedis resource = pool.getResource();


    }

    @Test
    public void test06(){
        //8000~8005 连接的时候连接任意的节点
        Set<HostAndPort> infos = new HashSet<>();
        infos.add(new HostAndPort("10.10.10.11",8000));
        //构造jedis
        JedisCluster jedisCluster = new JedisCluster(infos);
        jedisCluster.set("name","chen");
        System.out.println(jedisCluster.get("name"));
    }

}
