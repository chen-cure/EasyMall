package cn.tedu.service;


import cn.tedu.mapper.CartMapper;
import com.jt.common.pojo.Cart;
import com.jt.common.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private RestTemplate template;

    //查询购物车
    public List<Cart> queryCart(String userId) {
        return cartMapper.selectCart(userId);
    }

    //添加至购物车
    public void addCart(Cart cart) {
        /**
         * 如何判断cart对象对于数据库来说是新增还是更新
         */
        // cart userId productId作为符合主键
        Cart exist = cartMapper.selectCartByUidAndPid(cart);
        if (exist!=null){
            //说明有商品，需要传递的是num
            cart.setNum(cart.getNum()+exist.getNum());
            cartMapper.updateCartByUidAndPid(cart);
        }else{
            //说明没有商品,需要补齐商品数据
            //使用微服务实现系统之间的调用
            String url="http://product-server/product/manage/item/"+cart.getProductId();
            Product product = template.getForObject(url, Product.class);
            //将属性补齐
            cart.setProductName(product.getProductName());
            cart.setProductPrice(product.getProductPrice());
            cart.setProductImage(product.getProductImgurl());
            //将数据添加到数据库
            cartMapper.insertCart(cart);
        }

    }

    //更新数量
    public void updateNum(Cart cart) {
        cartMapper.updateCartByUidAndPid(cart);
    }

    public void deleteCart(Cart cart) {
        cartMapper.deleteCart(cart);
    }
}
