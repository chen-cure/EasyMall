package cn.tedu.service;

import cn.tedu.mapper.OrderMapper;
import com.jt.common.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    //新增订单
    public void addOrder(Order order) {
        //缺少三个字段补齐
        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);
        order.setOrderTime(new Date());
        order.setOrderPaystate(0);
        //一次性的插入
        orderMapper.insertOrder(order);
    }


    //订单查询
    public List<Order> queryOrder(String userId) {
        //基于代码做复杂数据的封装
        return orderMapper.selectByUserId(userId);
    }

    //订单删除
    public void deleteOrder(String orderId) {
        orderMapper.deleteOrderByOrderId(orderId);
    }
}
