package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import dao.ShippingPriceDAO;

@WebServlet("/shipping-price")
public class ShippingPriceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        // ===== XOÁ =====
        if ("delete".equals(action)) {
            String idParam = req.getParameter("id");

            if (idParam != null && !idParam.isBlank()) {
                try {
                    int id = Integer.parseInt(idParam);
                    ShippingPriceDAO.delete(id);
                } catch (NumberFormatException e) {
                    // không làm gì – tránh 500
                }
            }

            resp.sendRedirect(req.getContextPath() + "/shipping");
            return;
        }

        // ===== SỬA =====
        if ("update".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));

            Integer kg = null;
            String kgParam = req.getParameter("kg");
            if (kgParam != null && !kgParam.isBlank()) {
                kg = Integer.parseInt(kgParam);
            }

            int price = Integer.parseInt(req.getParameter("price"));
            ShippingPriceDAO.update(id, kg, price);

            resp.sendRedirect(req.getContextPath() + "/shipping");
            return;
        }

     // ===== THÊM =====
        int customerId = Integer.parseInt(req.getParameter("customer_id"));

        Integer kg = null;
        String kgParam = req.getParameter("kg");
        if (kgParam != null && !kgParam.isBlank()) {
            kg = Integer.parseInt(kgParam);
        }

        int price = Integer.parseInt(req.getParameter("price"));

        // ❌ CHẶN TRÙNG KG
        if (ShippingPriceDAO.existsKg(customerId, kg)) {
            resp.sendRedirect(req.getContextPath() + "/shipping?error=duplicateKg");
            return;
        }

        ShippingPriceDAO.insert(customerId, kg, price);
        resp.sendRedirect(req.getContextPath() + "/shipping");

    }
}

