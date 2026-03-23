package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

import utils.DBConnect;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String user = req.getParameter("username");
        String pass = req.getParameter("password");

        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Kiểm tra chính xác tuyệt đối (phân biệt hoa thường, dấu) bằng Java
                if (user.equals(rs.getString("username")) && pass.equals(rs.getString("password"))) {
                    HttpSession session = req.getSession();
                    session.setAttribute("user", rs.getString("username"));
                    session.setAttribute("role", rs.getString("role"));
                    session.setAttribute("position", rs.getString("position"));

                    if ("admin".equals(rs.getString("role"))) {
                    	resp.sendRedirect("admin/users");
                    } else {
                        resp.sendRedirect("shipping");
                    }
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        req.setAttribute("error", "Sai tài khoản hoặc mật khẩu");
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
