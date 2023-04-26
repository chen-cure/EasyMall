package cn.tedu.mapper;

import com.jt.common.pojo.Order;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    //订单新增
    void insertOrder(Order order);

    //订单查询
    List<Order> selectByUserId(String userId);

    //订单删除
    @Delete("delete from t_order where order_id=#{orderId};"+
    "delete from t_order_item where order_id=#{orderId};")
    void deleteOrderByOrderId(String orderId);
}
