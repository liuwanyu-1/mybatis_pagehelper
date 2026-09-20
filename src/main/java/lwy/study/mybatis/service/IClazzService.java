package lwy.study.mybatis.service;

import lwy.study.mybatis.pojo.Clazz;

import java.util.List;

public interface IClazzService {

    /** PageHelper 插件版分页：startPage 后查全部，插件自动拼 limit */
    List<Clazz> findClazzesByPage(Integer pageNum, Integer pageSize);

    /** 统计总记录数：给 Servlet 算总页数用 */
    Integer findTotalRecord();

}
