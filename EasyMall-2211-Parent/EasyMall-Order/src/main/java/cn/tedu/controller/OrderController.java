package cn.tedu.controller;

import cn.tedu.service.OrderService;
import com.jt.common.pojo.Order;
import com.jt.common.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    //订单的新增
    @RequestMapping("/order/manage/save")
    public SysResult addOrder(Order order){
        try {
            orderService.addOrder(order);
            return SysResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201,"订单新增失败",null);
        }
    }

    //订单的查询
    @RequestMapping("/order/manage/query/{userId}")
    public List<Order> queryOrder(@PathVariable String userId){
        return orderService.queryOrder(userId);
    }

    //订单的删除
    @RequestMapping("/order/manage/delete/{orderId}")
    public SysResult deleteOrder(@PathVariable String orderId){
        try {
            orderService.deleteOrder(orderId);
            return SysResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201,"删除失败",null);
        }
    }
}
