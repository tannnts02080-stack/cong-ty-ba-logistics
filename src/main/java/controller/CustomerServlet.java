package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import dao.CustomerDAO;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");

        // ===== KIỂM TRA RỖNG =====
        if (name == null || name.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/shipping?tab=price");
            return;
        }

        name = name.trim();

        // ===== KIỂM TRA TRÙNG TÊN =====
        if (CustomerDAO.existsByName(name)) {
            // ❌ Trùng → quay lại tab Bảng giá + báo lỗi
            resp.sendRedirect(
                req.getContextPath() + "/shipping?tab=price&error=duplicateCustomer"
            );
            return;
        }

        // ===== THÊM KHÁCH =====
        CustomerDAO.insert(name);

        // ✅ THÊM XONG → QUAY VỀ TAB BẢNG GIÁ
        resp.sendRedirect(req.getContextPath() + "/shipping?tab=price");
    }
}
