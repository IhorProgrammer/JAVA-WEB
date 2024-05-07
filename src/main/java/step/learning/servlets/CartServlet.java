package step.learning.servlets;

import step.learning.dal.dao.CartDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CartDao cartDao = new CartDao();
        req.setAttribute("carts", cartDao.getCarts());
        req.setAttribute("page-body", "cart");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req,resp);
    }
}
