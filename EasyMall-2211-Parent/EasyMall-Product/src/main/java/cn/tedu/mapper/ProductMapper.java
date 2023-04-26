package cn.tedu.mapper;

import com.jt.common.pojo.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper {
    //查询总数
    @Select("select count(*) from t_product;")
    int selectProductCount();

    //分页查询
    @Select("select * from t_product limit #{start},#{rows}")
    List<Product> selectProductByPage(@Param("start") int start, @Param("rows") Integer rows);

    //单个查询
    @Select("select * from  t_product where product_id=#{productId};")
    Product selectProductById(String productId);

    //新增商品
    @Insert("insert into t_product values(#{productId},#{productName},#{productPrice}," +
            "#{productCategory},#{productImgurl},#{productNum},#{productDescription});")
    void insertProduct(Product product);

    //商品修改
    @Update("update t_product set product_name=#{productName},product_price=#{productPrice}," +
            "product_category=#{productCategory},product_imgurl=#{productImgurl}," +
            "product_num=#{productNum},product_description=#{productDescription} where product_id = #{productId};")
    void updateProductById(Product product);
}
