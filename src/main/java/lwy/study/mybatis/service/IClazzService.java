package lwy.study.mybatis.service;

import lwy.study.mybatis.pojo.Clazz;

import java.util.List;


public interface IClazzService {

    List<Clazz> selectByPage(int pageNum, int pageSize);
}
