package cn.tedu.mapper;

import com.jt.common.pojo.Seckill;
import com.jt.common.pojo.Success;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SeckillMapper {
    @Select("select * from seckill;")
    public List<Seckill> selectList();

    @Select("select * from seckill where seckill_id=#{seckillId};")
    Seckill selectOneById(long seckillId);


//    @Update("update seckill set number = number-1 where seckill_id = #{seckillId} and number >0 and #{time} < end_time and #{time} > start_time;")
//    int updateNumber(@Param("seckillId") long seckillId, @Param("time") String time);

    @Insert("insert into success(seckill_id,user_phone,create_time) " +
            " values(#{i},#{seckillId},#{userPhone},#{i1},#{time})")
    void insertseckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone,
                       @Param("time") String time);

    @Insert("insert into success(seckill_id,user_phone,create_time) " +
            "values(#{seckillId},#{userPhone},#{createTime});")
    void insertSuccess(Success success);

    @Update("update seckill set number = number-1 where seckill_id = #{seckillId} and number >0 and #{time} < end_time and #{time} > start_time;")
    int updateNumber(@Param("seckillId") long seckillId, @Param("time") Date date);
}
