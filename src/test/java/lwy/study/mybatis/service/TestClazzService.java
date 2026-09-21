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

    /** 手写 limit 版：按页码查询，公式在 Service 层算 */
    @Test
    public void testSelectByPageManual() {
        List<Clazz> clazzes = clazzService.selectByPageManual(1, 2);
        for (Clazz clazz : clazzes) {
            System.out.println(clazz);
        }
    }
}
