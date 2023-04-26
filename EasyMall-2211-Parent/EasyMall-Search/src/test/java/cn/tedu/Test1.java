package cn.tedu;

import com.jt.common.pojo.Product;
import com.jt.common.utils.MapperUtil;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class Test1 {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private RestHighLevelClient client=null;

    @Before
    public void init(){
        client=new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("10.42.6.60",9200,"http"),
                        new HttpHost("10.42.177.70",9200,"http"),
                        new HttpHost("10.42.73.110",9200,"http")
                )
        );
    }

    @Test
    public void  search(){
        try {
            SearchRequest request = new SearchRequest();
            MatchQueryBuilder queryBuilders = QueryBuilders.matchQuery("productName","滑雪套装");
            request.source(new SearchSourceBuilder().query(queryBuilders));

            SearchResponse search = client.search(request, RequestOptions.DEFAULT);
            System.out.println(search);
            TimeValue took = search.getTook();
            System.out.println(took);
            //source
            SearchHit[] hits = search.getHits().getHits();
//            for(SearchHit hit:hits){
//                System.out.println(hit.getSourceAsString());
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
