package cn.tedu.controller;

import cn.tedu.service.UserService;
import com.jt.common.pojo.User;
import com.jt.common.utils.CookieUtils;
import com.jt.common.utils.MD5Util;
import com.jt.common.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
/*
    //redis
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    // 微服务之间的调用
    @Autowired
    private RestTemplate restTemplate;*/

    //用户检验
    @RequestMapping("/user/manage/checkUserName")
    public SysResult checkUsername(String userName){
        //返回是boolean类型，信息可用，返回为true
        boolean avaliable = userService.checkUsername(userName);
        if (!avaliable){
            return SysResult.ok();
        }else {
            return SysResult.build(201,"用户名已存在",null);
        }
    }

    //表单提交注册
    @RequestMapping("/user/manage/save")
    public SysResult doRegister(User user){
        try {
            userService.doRegister(user);
            return SysResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201,"用户注册失败",null);
        }
    }

    //表单提交登录
    @RequestMapping("/user/manage/login")
    public SysResult doLogin(User user, HttpServletRequest request, HttpServletResponse response){
        //控制层直接调用业务层   获取对象 返回redis的key值  ticket
        String ticket = userService.doLogin(user);
//        System.out.println(user);
//        System.out.println("登录时:"+ticket);
        if ("".equals(ticket)||ticket==null){
            //ticket没有查到  用户登录失败
            return SysResult.build(201,"登录失败",null);
        }else{
            //说明是查到ticket，业务层将用户存储到redis中
            //还要将ticket存储到cookie中
            //直接调用CookieUtils
            //cookie的名字必须叫 EM_TICKET
            CookieUtils.setCookie(request,response,"EM_TICKET",ticket);
            //返回前端信息
            return SysResult.ok();
        }
    }

    //获取用户
    @RequestMapping("/user/manage/query/{ticket}")
    public SysResult queryUserJson(@PathVariable String ticket){
        String userjson = userService.queryUserJson(ticket);
        //不一定能获取到
        if (userjson==null||"".equals(userjson)){
            return SysResult.build(201,"登录超时，请重新登录",null);
        }else{
            //登录状态可用
            return SysResult.build(200,"登录成功",userjson);
        }
    }

    //登出
    @RequestMapping("/user/manage/logout")
    public SysResult doLogout(HttpServletRequest request,HttpServletResponse response){
        //获取cookie
        String ticket = CookieUtils.getCookieValue(request, "EM_TICKET");
        System.out.println(ticket);
        if (ticket!=null&& !"".equals(ticket)){
            //删除cookie
            CookieUtils.deleteCookie(request,response,"EM_TICKET");
            //删除ticket
            userService.doLogout(ticket);
            return SysResult.ok();
        }else {
            return SysResult.build(201,"失败",null);
        }
    }
}
