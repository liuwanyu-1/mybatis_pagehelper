package lwy.study.mybatis.service.impl;

import lwy.study.mybatis.dao.ClazzMapper;
import lwy.study.mybatis.pojo.Clazz;
import lwy.study.mybatis.service.IClazzService;
import lwy.study.mybatis.util.SessionUtil;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class ClazzServiceImpl implements IClazzService {

    SqlSession session;
    ClazzMapper clazzMapper;

    public ClazzServiceImpl() {
        session = SessionUtil.getSession();
        clazzMapper = session.getMapper(ClazzMapper.class);
    }

    @Override
    public List<Clazz> findClazzesByPage(Integer pageNum, Integer pageSize) {
        //pageCount = recordCount % pageSize == 0 ? recordCount / pageSize : recordaCount / pageSize + 1
        //实现分页业务
//        Integer pageStart = (pageNum - 1) * pageSize;
//        List<Clazz> clazzes = clazzMapper.selectByPage(pageStart, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<Clazz> clazzes = clazzMapper.selectAll();

        return clazzes;
    }

    @Override
    public Integer findTotalRecord() {
        return clazzMapper.selectCount();
    }
}
