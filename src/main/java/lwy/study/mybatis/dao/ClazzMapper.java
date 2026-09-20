package lwy.study.mybatis.dao;

import lwy.study.mybatis.pojo.Clazz;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ClazzMapper {

    @Select("select * from class")
    List<Clazz> selectAll();

    @Select("select * from class limit #{pageStart}, #{pageSize}")
    List<Clazz> selectByPage(@Param("pageStart") Integer pageStart,
                             @Param("pageSize") Integer pageSize);

}
