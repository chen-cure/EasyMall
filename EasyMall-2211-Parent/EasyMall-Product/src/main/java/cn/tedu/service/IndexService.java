package cn.tedu.service;

import cn.tedu.mapper.ProductMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jt.common.pojo.Product;
import com.jt.common.utils.MapperUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexService {
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private ProductMapper productMapper;

    //判断索引是否存在
    public boolean exists(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        return exists;
    }

    //准备mapping
    public Map<String ,Object> getMapping(){
        //定义filed类型和分词器
        Map<String,Object> productName = new HashMap<>();
        productName.put("type","text");
        productName.put("analyzer","ik_max_word");

        Map<String,Object> prodecuDescription = new HashMap<>();
        prodecuDescription.put("type","text");
        prodecuDescription.put("analyzer","ik_max_word");

        Map<String,Object> productImgurl = new HashMap<>();
        productImgurl.put("type","keyword");

        Map<String,Object> productId = new HashMap<>();
        productId.put("type","keyword");

        Map<String , Object > productCategory = new HashMap<>();
        productCategory.put("type","keyword");

        Map<String ,Object> productPrice = new HashMap<>();
        productPrice.put("type","double");

        Map<String ,Object> productNum = new HashMap<>();
        productNum.put("type","integer");

        Map<String ,Object> properties = new HashMap<>();
        properties.put("productName",productName);
        properties.put("prodecuDescription",prodecuDescription);
        properties.put("productImgurl",productImgurl);
        properties.put("productId",productId);
        properties.put("productCategory",productCategory);
        properties.put("productPrice",productPrice);
        properties.put("productNum",productNum);

        Map<String,Object> mappings = new HashMap<>();
        mappings.put("properties",properties);

        return mappings;

    }

    //创建索引
    public void createIndex(String indexName) throws IOException {
        IndicesClient indices = client.indices();
        Map<String ,Object> mappings = getMapping();
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        //指定setting和分片
        request.settings(Settings.builder().put("index.number_of_shards",3));
        //设置mapping
        request.mapping(mappings);
        CreateIndexResponse response = indices.create(request, RequestOptions.DEFAULT);
    }

    //将数据批量导入
    public void bulkproduct(List<Product> productList,String indexName) throws Exception {
        //获取批量导入的对象
        BulkRequest request = new BulkRequest();
        //转化，写入request
        for(Product product:productList){
            //将product对象转化为json
            String dataJson = MapperUtil.MP.writeValueAsString(product);
            request.add(new IndexRequest(indexName).id(product.getProductId()).source(dataJson, XContentType.JSON));
            client.bulk(request,RequestOptions.DEFAULT);
        }
    }

    public void index() throws Exception {
        /**
         * 1.定义索引名 emindex
         * 2.判断索引名是否存在
         * 3.读取数据库，封装json，写入es
         */
        String indexName = "easymindex";
        if (!exists(indexName)){
            //不存在，创建索引
            createIndex(indexName);
        }

        //读取数据库数据 t_product
        List<Product> products = productMapper.selectProductByPage(0, 100);
        bulkproduct(products,indexName);


    }
}
