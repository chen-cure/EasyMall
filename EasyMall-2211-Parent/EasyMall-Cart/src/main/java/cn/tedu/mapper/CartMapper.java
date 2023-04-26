package cn.tedu.mapper;

import com.jt.common.pojo.Cart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CartMapper {

    @Select("select * from t_cart where user_id = #{userId};")
    List<Cart> selectCart(String userId);

    //查询判断商品存在与否
    @Select("select * from t_cart where user_id=#{userId} and product_id=#{productId} ")
    Cart selectCartByUidAndPid(Cart cart);

    //更新数量
    @Update("update t_cart set num =#{num} where user_id = #{userId} and product_id = #{productId};")
    void updateCartByUidAndPid(Cart cart);

    //新增
    @Insert("insert into t_cart(user_id,product_id,product_name,product_image,product_price,num) " +
            "values (#{userId},#{productId},#{productName},#{productImage},#{productPrice},#{num})")
    void insertCart(Cart cart);

    //删除
    @Delete("delete from t_cart where user_id=#{userId} and product_id=#{productId};")
    void deleteCart(Cart cart);
}
