package cn.tedu.service;

import com.jt.common.utils.UploadUtil;
import com.jt.common.vo.PicUploadResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImgService {
    /**
     * 主要工作
     * 1.写入磁盘，保存图片，图片的路径   D:/Nginx/nginx-1.21.6/img/upload/**
     * 2.根据图片保存的位置，生成一个url地址并返回
     *
     * 详细步骤：
     * 1.要给图片重命名
     * 2.生成多级目录  upload//**
     * 3.生成多级路径，生成文件夹  upload//** /重命名的图片名称
     * 4.写入生成的路径，返回url
     * 5.根据图片路径，生成地址
     *
     */
    public PicUploadResult picUpdate(MultipartFile pic) {
        //文件的重命名
        String oName = pic.getOriginalFilename();//未命名的名称.jpg
        String nName = System.currentTimeMillis() + oName.substring(oName.lastIndexOf("."));
        //生成多级目录
        //String filename String upload
        //是一个计算多级路径的源数据    字符串   具有业务意义的前缀，可以是项目名
        String path = UploadUtil.getUploadPath(UUID.randomUUID().toString(),"/easymall")+"/";
        //创建多级目录
        String dir = "D:/Nginx/nginx-1.21.6/img/upload"+path;
        File _dir = new File(dir);
        if (!_dir.exists()){
            //如果不存在就创建该目录
            _dir.mkdirs();
        }
        try {
            //transferTo 以二进制的形式写图片
            pic.transferTo(new File(dir+nName));
        } catch (IOException e) {
            e.printStackTrace();
            //失败提示
            PicUploadResult result = new PicUploadResult();
            result.setError(1);
            return result;
        }
        PicUploadResult result = new PicUploadResult();
        String url = "http://image.jt.com/"+path+nName;
        result.setUrl(url);
        return result;
    }
}
