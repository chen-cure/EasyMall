package cn.tedu.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestTest {
    /**
     * 客户端是通过http访问es集群
     *
     */
    @Test
    public void test01(){
        //新增索引
        RestTemplate template = new RestTemplate();
        String url ="http://10.42.6.60:9200/index20";
        /*增加索引*/
//        template.put(url,null);
        /*删除索引*/
        template.delete(url);
    }

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
    public void test02() throws IOException {
        //创建索引
        CreateIndexRequest request =new CreateIndexRequest("index20");

        request.settings(Settings.builder().put("index.number_of_shards",3));

        Map<String ,String > name = new HashMap<>();
        name.put("type","text");
        name.put("analyzer","ik_max_word");

        Map<String ,Object > properties = new HashMap<>();
        properties.put("name",name);

        Map<String ,Object > mappings = new HashMap<>();
        mappings.put("properties",properties);

        request.mapping(mappings);

        //发送 将request发送给es
        //从链接中拿到一个操作索引的对象
        IndicesClient indices = client.indices();
        //创建索引请求的发送
        CreateIndexResponse createIndexResponse = indices.create(request, RequestOptions.DEFAULT);
        System.out.println("创建索引："+(createIndexResponse.isAcknowledged()?"成功":"失败"));

    }

    @Test
    public void analyzer() throws IOException {//测试分词器
        AnalyzeRequest request = AnalyzeRequest.withGlobalAnalyzer(
               "ik_max_word",
                "山西太原龙城校区"

        );
        IndicesClient indices = client.indices();
        AnalyzeResponse analyze = indices.analyze(request, RequestOptions.DEFAULT);
        //从响应中解析分词计算返回的token，获取分词词项
        List<AnalyzeResponse.AnalyzeToken> tokens = analyze.getTokens();
        for(AnalyzeResponse.AnalyzeToken token:tokens){
            System.out.println("起始的位置："+token.getStartOffset());
            System.out.println("结束的位置："+token.getEndOffset());
            System.out.println("位置："+token.getPosition());
            System.out.println("词项："+token.getTerm());
        }
    }

    @Test
    public void exisIndex() throws IOException {
        GetIndexRequest request =new GetIndexRequest("emindex");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println("索引是否存在："+exists);
    }

    //添加doc数据
    @Test
    public void indexDoc() throws IOException {
        //准备一个文档
        String userJson = "{\"name\":\"张三\",\"age\":18}";
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index("index10");
        indexRequest.id("1");
        indexRequest.source(userJson, XContentType.JSON);
        //发送请求
        //创建文档
        IndexResponse response = client.index(indexRequest,RequestOptions.DEFAULT);
        System.out.println(response.getVersion());
        System.out.println(response.getId());
    }

    //根据id读取信息
    @Test
    public void getDoc() throws IOException {
        GetRequest request = new GetRequest();
        //添加请求的参数
        request.index("index10");
        request.id("1");
        //发起请求
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        /**
         * {
         *     index:
         *          type
         *          version
         *          source{
         *              name
         *              age
         *          }
         * }
         */
        Map<String, Object> source = response.getSource();
        System.out.println("name"+source.get("name"));
        System.out.println(response.getSourceAsString());
    }

}
