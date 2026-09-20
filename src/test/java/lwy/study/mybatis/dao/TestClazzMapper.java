package lwy.study.mybatis.dao;

import lwy.study.mybatis.pojo.Clazz;
import lwy.study.mybatis.util.SessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/** ClazzMapper 接口绑定测试 */
public class TestClazzMapper {

    SqlSession session;
    ClazzMapper clazzMapper;

    @Before
    public void init() throws IOException {
        session = SessionUtil.getSession();
        clazzMapper = session.getMapper(ClazzMapper.class);
    }

    /** 手写 limit 物理分页：按页码换算起始行，第 2 页每页 2 条 */
    @Test
    public void testSelectByPage() {
        Integer pageNum = 2;
        Integer pageSize = 2;
        Integer pageStart = (pageNum - 1) * pageSize;
        List<Clazz> clazzes = clazzMapper.selectByPage(pageStart, pageSize);
        for (Clazz clazz : clazzes) {
            System.out.println(clazz);
        }
    }

    @After
    public void destroy() {
        session.commit();
        session.close();
    }
}
