package cn.tedu.controller;

import cn.tedu.service.ProduceService;
import com.jt.common.pojo.Product;
import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ProductController {
    @Autowired
    private ProduceService produceService;

    @RequestMapping("/product/manage/pageManage")
    public EasyUIResult queryByPage(Integer page,Integer rows){
        EasyUIResult result = produceService.queryByPage(page,rows);
        return result;
    }

    //基于商品id查询商品信息
    @RequestMapping("/product/manage/item/{productId}")
    public Product queryOneProduct(@PathVariable String productId){
        return produceService.queryOneProduct(productId);
    }

    //新增商品
    @RequestMapping("/product/manage/save")
    public SysResult addProduct(Product product){
        try {
            produceService.addProduct(product);
            return SysResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201,"新增失败",null);
        }
    }

    //修改商品
    @RequestMapping("/product/manage/update")
    public SysResult updateProduct(Product product){
        try {
            produceService.updateProduct(product);
            return SysResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201,"修改失败",null);
        }
    }


}
