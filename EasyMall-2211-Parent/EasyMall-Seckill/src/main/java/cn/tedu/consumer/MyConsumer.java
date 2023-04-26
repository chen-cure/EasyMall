package cn.tedu.consumer;

import cn.tedu.mapper.SeckillMapper;
import com.jt.common.pojo.Success;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class MyConsumer {
    private static final Map<Long,Long > numberMap = new HashMap<>();
    static{
        //利用seckill做key   利用number库存做value
        //准备商品和库存
//        numberMap.put();
    }

    @Autowired
    private SeckillMapper seckillMapper;
    //引入redis就是为了将大量的无用请求以及失败的请求拦截到外面
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 获取rabbitmq推送的消息
     * 参数就是消息
     * 获取消息就可以在方法内部编写一些业务逻辑
     * 给这个方法添加注解，添加监听队列的标识
     * direct_01 收到消息，就会推送给这个对象MyConsumer
     */
    @RabbitListener(queues="direct_Q1")
    public void consumer(String msg){
        /**
         * 执行消费逻辑
         * 1 先解析userPhone和seckillId
         * 2 通过数据来减少库存，返回1/0 判断是否成功
         * 3 如果成功 将成功的对象入库，封装 ，电话，商品入库
         */
//        System.out.println("消费端发送的消息   "+msg);
        String[] split = msg.split("/");
        long userPhone= Long.parseLong(split[0]);
        long seckillId = Long.parseLong(split[1]);


        //解析数据之后获取到了seckill和电话，利用redis将库存执行
        //减库存的拦截到这里
        Long number = numberMap.get(seckillId);  //对应的商品数量
        //做key值  incr操作
        String number_key = "number_"+seckillId;
        Long step = redisTemplate.boundValueOps(number_key).increment();
        if (step>number){
            //秒杀结束。不可以在访问数据库减库存
            System.out.println("redis记录的库存数量用完，不会执行到数据库");
            return;
        }

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(date);
//        int result = seckillMapper.updateNumber(seckillId,time);
        int result = seckillMapper.updateNumber(seckillId,date);
//        System.out.println(result);
        if (result==0){
            System.out.println("减库存失败，卖完了");
            return;
        }
/*        if (result>0){
            seckillMapper.insertseckill(seckillId,userPhone,time);
            return;
        }*/
        Success success = new Success();
        success.setuserPhone(userPhone);
        success.setCreateTime(date);
        success.setSeckillId(seckillId);
        //写入成功的数据库
        seckillMapper.insertSuccess(success);
        System.out.println("消费端消费了消息");


    }
}
