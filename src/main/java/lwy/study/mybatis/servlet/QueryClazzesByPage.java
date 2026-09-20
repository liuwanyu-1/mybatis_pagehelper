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

    private IClazzService clazzService;

    public QueryClazzesByPage() {
        clazzService = new ClazzServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageNumStr = req.getParameter("pageNum");
        String pageSizeStr = req.getParameter("pageSize");
        int pageNum = 1;
        int pageSize = 5;
        if (pageNumStr != null && !pageNumStr.equals("")) {
            pageNum = Integer.parseInt(pageNumStr);
        }
        if (pageSizeStr != null && !pageSizeStr.equals("")) {
            pageSize = Integer.parseInt(pageSizeStr);
        }

        List<Clazz> clazzes = clazzService.selectByPage(pageNum, pageSize);
        req.setAttribute("clazzes", clazzes);
        req.setAttribute("pageNum", pageNum);
        req.setAttribute("pageSize", pageSize);
        req.getRequestDispatcher("/clazzList.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
