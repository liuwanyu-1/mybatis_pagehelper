package lwy.study.mybatis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lwy.study.mybatis.pojo.Clazz;
import lwy.study.mybatis.service.IClazzService;
import lwy.study.mybatis.service.impl.ClazzServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet("/queryClazzes")
public class QueryClazzesByPage extends HttpServlet {

    /** Controller 调 Service，Service 调 DAO */
    private final IClazzService clazzService = new ClazzServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 处理参数为空：前端没传页码时默认第 1 页，每页 2 条
        String pageNumStr = req.getParameter("pageNumber");
        String pageSizeStr = req.getParameter("pageSize");
        int pageNum = (pageNumStr == null || "".equals(pageNumStr)) ? 1 : Integer.parseInt(pageNumStr);
        int pageSize = (pageSizeStr == null || "".equals(pageSizeStr)) ? 2 : Integer.parseInt(pageSizeStr);

        // 调用 Service 分页查询拿当页数据，再查总记录数
        List<Clazz> clazzes = clazzService.findClazzesByPage(pageNum, pageSize);
        int totalRecord = clazzService.findTotalRecord();

        // 总页数：能整除直接除，不能整除加 1 —— (totalRecord + pageSize - 1) / pageSize
        int totalPage = (totalRecord + pageSize - 1) / pageSize;

        // 分页数据属于单次请求，存 request 域（不能用 session）
        req.setAttribute("clazzes", clazzes);
        req.setAttribute("pageNum", pageNum);
        req.setAttribute("pageSize", pageSize);
        req.setAttribute("totalRecord", totalRecord);
        req.setAttribute("totalPage", totalPage);

        // 请求转发跳转 JSP（重定向 request 域数据会丢）
        req.getRequestDispatcher("/clazzList.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
