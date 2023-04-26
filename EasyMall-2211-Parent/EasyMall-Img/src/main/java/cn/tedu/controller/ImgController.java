package cn.tedu.controller;

import cn.tedu.service.ImgService;
import com.jt.common.vo.PicUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImgController {
    //上传图片
    @Autowired
    private ImgService imgService;

    @RequestMapping("/pic/uploadImg")
    public PicUploadResult picUpload(MultipartFile pic){
        return imgService.picUpdate(pic);
    }
}
