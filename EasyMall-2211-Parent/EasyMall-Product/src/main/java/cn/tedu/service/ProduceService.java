package cn.tedu.service;

import com.jt.common.pojo.Product;
import com.jt.common.vo.EasyUIResult;

public interface ProduceService {
    EasyUIResult queryByPage(Integer page, Integer rows);

    Product queryOneProduct(String productId);

    void addProduct(Product product);

    void updateProduct(Product product);
}
