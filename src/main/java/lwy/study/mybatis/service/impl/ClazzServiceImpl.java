package lwy.study.mybatis.service.impl;

import com.github.pagehelper.PageHelper;
import lwy.study.mybatis.dao.ClazzMapper;
import lwy.study.mybatis.pojo.Clazz;
import lwy.study.mybatis.service.IClazzService;
import lwy.study.mybatis.util.SessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;


public class ClazzServiceImpl implements IClazzService {

    SqlSession session;
    ClazzMapper clazzMapper;

    public ClazzServiceImpl() {
        this.session = SessionUtil.getSession();
        this.clazzMapper = session.getMapper(ClazzMapper.class);
    }

    @Override
    public List<Clazz> selectByPage(int pageNum, int pageSize) {
//        // 计算总页数
//        int total = clazzMapper.selectCount();
//        int totalPages = (total + pageSize - 1) / pageSize;
//        Integer pageStart = (pageNum - 1) * pageSize;
//        List<Clazz> clazzes = clazzMapper.selectByPage(pageStart, pageSize);

        PageHelper.startPage(pageNum, pageSize);
        List<Clazz> clazzes = clazzMapper.selectAll();
        return clazzes;
    }

    /** 手写 limit 版：pageStart=(pageNum-1)*pageSize，公式必须在 Service 层算 */
    @Override
    public List<Clazz> selectByPageManual(int pageNum, int pageSize) {
        Integer pageStart = (pageNum - 1) * pageSize;
        return clazzMapper.selectByPage(pageStart, pageSize);
    }
}
