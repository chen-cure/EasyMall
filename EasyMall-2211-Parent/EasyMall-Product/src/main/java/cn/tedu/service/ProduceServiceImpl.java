package cn.tedu.service;

import cn.tedu.mapper.ProductMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.pojo.Product;
import com.jt.common.vo.EasyUIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ProduceServiceImpl implements ProduceService{
    @Autowired
    private ProductMapper pm;
    @Autowired
    private StringRedisTemplate template;
    //分页查询，生成一个返回easyui对象，封装两个属性
    @Override
    public EasyUIResult queryByPage(Integer page, Integer rows) {
        EasyUIResult result = new EasyUIResult();
        //查询商品的总数量
        int total = pm.selectProductCount();
        result.setTotal(total);
        //封装第二个方法
        int start = (page-1)*rows;
        List<Product> pList = pm.selectProductByPage(start,rows);
        //写到result属性中
        result.setRows(pList);
        return result;
    }

    //单个商品的查询
    @Override
    public Product queryOneProduct(String productId) {
        ObjectMapper mapper = new ObjectMapper();
        //TODO 引入Redis，减缓数据库的压力
        /**
         * 在所有的缓存逻辑之前做判断，锁判断
         */
        String lock = productId+".lock";
        if (template.hasKey(lock)){
            //锁存在不可以操作缓存数据，先去数据库读
            return pm.selectProductById(productId);
        }
        //生成数据对应的key，保证唯一
        String productKey = "product_query" + productId;
        //判断key是否存在
        if (template.hasKey(productKey)){
            try {
                //直接使用缓存数据
                String productjson = template.opsForValue().get(productKey);
                //反序列化
                Product product = mapper.readValue(productjson, Product.class);
                return product;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //调用持久层查询商品
            Product product = pm.selectProductById(productId);
            try {
                //存储在redis中的数据value对应的是当前查询到的的商品
                String productjson = mapper.writeValueAsString(product);
                template.opsForValue().set(productKey,productjson,60*60*2,TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pm.selectProductById(productId);



        //查询redis中是否存在数据，存在的话，从当中获取数据
/*        if (template.hasKey(productId)){
            try {
                String productjson = template.opsForValue().get(productId);
                Product product1 = mapper.readValue(productjson, Product.class);
                System.out.println(product);
                return product1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

/*        if (product!=null){
            try {
                //存储在redis中的数据value对应的是当前查询到的的商品
                String productjson = mapper.writeValueAsString(product);
                System.out.println(productjson);
                template.opsForValue().set(productId,productjson,60*60*2,TimeUnit.SECONDS);
                return product;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }*/

    }

    //商品新增
    @Override
    public void addProduct(Product product) {
        //缺少商品IP，需要手动生成
        String productId = UUID.randomUUID().toString();
        System.out.println("商品id:"+productId);
        product.setProductId(productId);
        //写入缓存
        try {
            ObjectMapper mapper = new ObjectMapper();
            String productJson = mapper.writeValueAsString(product);
            String productKey = "product_query"+productId;
            template.opsForValue().set(productKey,productJson,2,TimeUnit.DAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //调用持久层方法
        pm.insertProduct(product);
    }

    //商品的修改
    @Override
    public void updateProduct(Product product) {
        //添加锁
        String lock = product.getProductId()+".lock";
        template.opsForValue().set(lock,"");
        //删除缓存数据
        String productKey = "product_query"+product.getProductId();
        template.delete(productKey);

        //TODO 和Redis中的数据保持一致
        pm.updateProductById(product);
        //释放锁
        template.delete(lock);
    }
}
