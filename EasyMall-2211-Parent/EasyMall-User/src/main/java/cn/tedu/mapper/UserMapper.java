package cn.tedu.mapper;

import com.jt.common.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    @Select("select * from t_user where user_name=#{username};")
    User selectByUsername(String username);

    //用户插入
    @Insert("insert into t_user values(#{userId},#{userName},#{userPassword}," +
            "#{userNickname},#{userEmail},#{userType});")
    void insertUser(User user);

    //登录
    @Select("select * from t_user where user_name=#{userName} and user_password=#{userPassword};")
    User selectUserNameAndPassword(User user);
}
