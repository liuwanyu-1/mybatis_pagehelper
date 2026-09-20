package lwy.study.mybatis.service;

import lwy.study.mybatis.pojo.Clazz;
import lwy.study.mybatis.service.impl.ClazzServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/** Service 层分页测试 */
public class TestClazzService {

    private IClazzService clazzService;

    @Before
    public void init() {
        clazzService = new ClazzServiceImpl();
    }

    @Test
    public void testSelectByPage() {
        List<Clazz> clazzes = clazzService.selectByPage(1, 2);
        for (Clazz clazz : clazzes) {
            System.out.println(clazz);
        }
    }
}
