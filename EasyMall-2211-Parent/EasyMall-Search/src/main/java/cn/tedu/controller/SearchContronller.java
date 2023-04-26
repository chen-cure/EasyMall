package cn.tedu.controller;

import cn.tedu.service.SearchService;
import com.jt.common.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchContronller {
    @Autowired
    private SearchService searchService;

    @RequestMapping("/search/manage/query")
    public List<Product> search(String query,Integer page,Integer rows){
        return searchService.search(query,page,rows);
    }
}
