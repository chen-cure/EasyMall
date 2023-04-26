package cn.tedu.service;

import cn.tedu.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.pojo.User;
import com.jt.common.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    //重复性校验
    public boolean checkUsername(String username) {
        User user = userMapper.selectByUsername(username);
        return user!=null;
    }

    //注册
    public void doRegister(User user) {
        try {
            user.setUserId(UUID.randomUUID().toString());
            user.setUserPassword(MD5Util.md5(user.getUserPassword()));
            userMapper.insertUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Autowired
    private StringRedisTemplate template;

    //登录
    public String doLogin(User user) {
        //验证user合法性
        //对user密码加密
        user.setUserPassword(MD5Util.md5(user.getUserPassword()));
        User exist = userMapper.selectUserNameAndPassword(user);
        String ticket="";
        if (exist==null){
            //说明用户没有查到
            return ticket;
        }else{
            /**
             * exist不为空 用户名和密码存在  在redis中set数据
             * 计算出ticket 作为当前用户的key
             * 不同的用户，key不能一样 在不同的地方不同的时间，key不可以一样
             */
            ticket="EM_TICKET_"+user.getUserName()+System.currentTimeMillis();
        }
        //添加一个以用户名为key
        String loginkey = "Login_"+user.getUserName();
        if (template.hasKey(loginkey)){
            //有这个值，登录过，获取上一次登录的key   ticket
            String ticketlast = template.opsForValue().get(loginkey);
            //删除上一次登录的ticket
            template.delete(ticketlast);
        }

        try {
            //准备userjson  使用jackson转化将user对象转化为Jason对象
            ObjectMapper mapper = new ObjectMapper();
            //存储在redis中的数据value对应的是当前登录的用户
            String userjson = mapper.writeValueAsString(exist);
            template.opsForValue().set(ticket,userjson,60*60*2, TimeUnit.SECONDS);
            //将loginkey保存好本次登录生成的ticket，方便后续顶替
            template.opsForValue().set(loginkey,ticket,60*60*2, TimeUnit.SECONDS);
            return ticket;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    //从redis中获取用户状态
    public String queryUserJson(String ticket) {
        //获取用户在redis中超时时间
/*        Long time = template.getExpire(ticket,TimeUnit.SECONDS);
        if (time<60*60){
            template.expire(ticket,60*60*2,TimeUnit.SECONDS);
        }*/
        template.expire(ticket,60*60*2,TimeUnit.SECONDS);
        return template.opsForValue().get(ticket);
    }

    //登出
    public void doLogout(String ticket) {
         template.delete(ticket);
    }
}
