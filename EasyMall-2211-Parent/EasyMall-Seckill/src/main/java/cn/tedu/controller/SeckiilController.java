package cn.tedu.controller;

import cn.tedu.service.SeckillService;
import com.jt.common.pojo.Seckill;
import com.jt.common.vo.SysResult;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
public class SeckiilController {
    /**
     * 先做两个功能，都是从seckill表格中读取数据
     * seckill表中存储的都是秒杀商品的数据
     * 一个seckill对象对应的是一行seckill表格数据
     */
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StringRedisTemplate template;


    @RequestMapping("/seckill/manage/list")
    public List<Seckill> list(){
        return seckillService.list();
    }

    /**
     * 返回的list中，选中其中的商品，点击详情
     */
    @RequestMapping("/seckill/manage/detail")
    public Seckill detail(long seckillId){
        return seckillService.detail(seckillId);
    }

    /**
     * 开始秒杀 接收用户秒杀请求 并发送
     */
    @RequestMapping("/seckill/manage/{seckillId}")
    public SysResult doSeckill(@PathVariable long seckillId){
        /**
         * 1 封装准确的消息 描述消息谁秒杀了什么
         *  谁：随机的电话号码
         *  什么：商品idseckillId
         * 2 发送消息到rabbitmq
         * 3 如何限制同一个用户对用一件商品只能秒杀一个
         */
        String userPhone="1889977"+(new Random().nextInt(9000)+1000);
        String msg = userPhone+"/"+seckillId;
        //发送消息之前，判断是否已经参与过秒杀
        //发送之前，想redis发送一次命令拿到结果避免线程安全
        //incr
        Long increment = template.boundValueOps(msg).increment();
        if (increment>1){
            //存在 说明用户已经秒杀过了，拦截
            return SysResult.build(201,"已参与过秒杀",null);
        }
/*        if (template.hasKey(userPhone)){
            //存在 说明用户已经秒杀过了，拦截
            return SysResult.build(201,"已参与过秒杀",null);
        }*/
        rabbitTemplate.convertAndSend("direct_ex","成都",msg);
//        template.boundValueOps(msg).set("");
        return SysResult.ok();


    }

}
