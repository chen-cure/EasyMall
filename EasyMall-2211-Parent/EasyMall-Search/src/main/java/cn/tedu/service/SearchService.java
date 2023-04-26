package cn.tedu.service;

import com.jt.common.pojo.Product;
import com.jt.common.utils.MapperUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    @Autowired
    private RestHighLevelClient client;


    public List<Product> search(String query, Integer page, Integer rows) {
        /**
         * client 从索引中easymindex查询商品数据
         * 构造一个查询的请求 query ProductName
         * 查询返回的结果解析 productList
          */
        //构造查询请求
        SearchRequest request = new SearchRequest();
        //查询条件
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("productName",query);
        //分页 size条数  from 起始位置
        int size = rows;
        int from = (page-1)*rows;
        //SearchSourceBuilder  搜索内容 参数设置的对象
        request.source(new SearchSourceBuilder().query(queryBuilder).size(size).from(from));
        //执行查询 获取结果
        List<Product> products = new ArrayList<>();
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            //source
            SearchHit[] hits = response.getHits().getHits();
            for(SearchHit hit :hits){
                //从hit中获取source的json，
                String sourceAsString = hit.getSourceAsString();
                Product product = MapperUtil.MP.readValue(sourceAsString, Product.class);
                products.add(product);
            }
            return products;
        } catch (IOException e) {
            e.printStackTrace();
            return products;
        }
    }
}
