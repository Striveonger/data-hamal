package com.striveonger.study.leaf.mapper;

import com.mybatisflex.core.BaseMapper;
import com.striveonger.study.leaf.entity.LeafAlloc;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 号段表 Mapper 接口
 * </p>
 *
 * @author Mr.Lee
 * @since 2022-11-23
 */
public interface LeafAllocMapper extends BaseMapper<LeafAlloc> {

    @Select("SELECT biz_tag, max_id, step, update_time FROM alloc")
    @Results(value = {
            @Result(column = "biz_tag", property = "key"),
            @Result(column = "max_id", property = "maxId"),
            @Result(column = "step", property = "step"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<LeafAlloc> getAllLeafAllocs();

    @Select("SELECT biz_tag, max_id, step FROM alloc WHERE biz_tag = #{tag}")
    @Results(value = {
            @Result(column = "biz_tag", property = "key"),
            @Result(column = "max_id", property = "maxId"),
            @Result(column = "step", property = "step")
    })
    LeafAlloc getLeafAlloc(@Param("tag") String tag);

    @Update("UPDATE alloc SET max_id = max_id + step WHERE biz_tag = #{tag}")
    void updateMaxId(@Param("tag") String tag);

    @Update("UPDATE alloc SET max_id = max_id + #{leafAlloc.step} WHERE biz_tag = #{leafAlloc.key}")
    void updateMaxIdByCustomStep(@Param("leafAlloc") LeafAlloc leafAlloc);

    @Select("SELECT biz_tag FROM alloc")
    List<String> getAllTags();

    @Select("SELECT COUNT(1) AS num FROM alloc WHERE biz_tag = #{tag}")
    int count(@Param("tag") String tag);

    @Insert("INSERT INTO alloc ( biz_tag, max_id, step, description )  VALUES  ( #{leafAlloc.key}, #{leafAlloc.maxId}, #{leafAlloc.step}, #{leafAlloc.description} )")
    boolean save(@Param("leafAlloc") LeafAlloc leafAlloc);
}
