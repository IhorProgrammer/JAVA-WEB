package step.learning.servlets;

import com.google.inject.Singleton;
import step.learning.dal.dao.PromotionalDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class PromotionalServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PromotionalDao promotionalDao = new PromotionalDao();
        req.setAttribute("promotionals", promotionalDao.getPromotional());
        req.setAttribute("page-body", "promotional");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req,resp);
    }
}
