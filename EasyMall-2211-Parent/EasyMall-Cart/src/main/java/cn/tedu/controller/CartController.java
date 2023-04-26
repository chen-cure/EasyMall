package cn.tedu.controller;

import cn.tedu.service.CartService;
import com.jt.common.pojo.Cart;
import com.jt.common.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    //查询商品
    @RequestMapping("/cart/manage/query")
    public List<Cart> queryCart(String userId){
        return cartService.queryCart(userId);
    }

    //新增购物车
    @RequestMapping("/cart/manage/save")    //  url:"http://www.easymall.com/cart/save?userId="+userId+"&productId="+productId+"&num="+num,
    public SysResult addCart(Cart cart){ //cart中只有是哪个属性，其余为空，见上述url。
        try {
            cartService.addCart(cart);
            return SysResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201,"新增购物车失败",null);
        }
    }

    //修改购物车中的数量
    @RequestMapping("/cart/manage/update")  //  url:"http://www.easymall.com/cart/update?productId="+productId+"&userId="+userId+"&num="+num,
    public SysResult updateNum(Cart cart){ //car中只有三个属性，其余为空
        try {
            cartService.updateNum(cart);
            return SysResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201,"更新数量失败",null);
        }
    }

    //删除购物车商品
    @RequestMapping("/cart/manage/delete")
    public SysResult deleteCart(Cart cart){
        try {
            cartService.deleteCart(cart);
            return SysResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201,"删除失败",null);
        }
    }
}
