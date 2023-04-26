package cn.tedu.service;

import cn.tedu.config.RabbitMQDeclare;
import cn.tedu.mapper.SeckillMapper;
import com.jt.common.pojo.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeckillService {
    @Autowired
    private SeckillMapper seckillMapper;


    public List<Seckill> list() {
        return seckillMapper.selectList();
    }

    public Seckill detail(long seckillId) {
        return seckillMapper.selectOneById(seckillId);
    }
}
