package cn.tedu.controller;

import cn.tedu.service.IndexService;
import com.jt.common.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @Autowired
    private IndexService indexService;

    @RequestMapping("/product/manage/index")
    public SysResult index(){
        try {
            indexService.index();
            return SysResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201,"创建索引失败",null);
        }
    }
}
