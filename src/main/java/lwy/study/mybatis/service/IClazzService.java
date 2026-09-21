package lwy.study.mybatis.service;

import lwy.study.mybatis.pojo.Clazz;

import java.util.List;


public interface IClazzService {

    /** PageHelper 插件版分页：startPage 后查全部，插件自动拼 limit */
    List<Clazz> selectByPage(int pageNum, int pageSize);

    /** 手写 limit 版分页：Service 层算 pageStart=(pageNum-1)*pageSize 再调 DAO */
    List<Clazz> selectByPageManual(int pageNum, int pageSize);
}
