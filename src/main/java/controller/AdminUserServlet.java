package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

import dao.UserDAO;

@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        String id = req.getParameter("id");

        if ("edit".equals(action) && id != null) {
            req.setAttribute("editUser", UserDAO.findById(Integer.parseInt(id)));
        }

        req.setAttribute("users", UserDAO.findAll());
        req.getRequestDispatcher("/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String action = req.getParameter("action");

        if ("add".equals(action)) {
            UserDAO.insert(
                req.getParameter("username"),
                req.getParameter("password"),
                req.getParameter("role"),
                req.getParameter("position")
            );
        }

        if ("update".equals(action)) {
            UserDAO.update(
                Integer.parseInt(req.getParameter("id")),
                req.getParameter("role"),
                req.getParameter("position")
            );
        }

        if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            String currentUser = (String) req.getSession().getAttribute("user");

            // không cho xoá chính mình
            Map<String, Object> u = UserDAO.findById(id);
            if (u != null && !u.get("username").equals(currentUser)) {
                UserDAO.delete(id);
            }
        }

        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}

