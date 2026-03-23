package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import dao.ShippingPriceDAO;

@WebServlet("/shipping-price/value")
public class ShippingPriceValueServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain");

        int customerId = Integer.parseInt(req.getParameter("customerId"));
        String kgStr = req.getParameter("kg");

        Integer kg = null;
        if (kgStr != null && !kgStr.isBlank()) {
            kg = Integer.parseInt(kgStr);
        }

        int price = ShippingPriceDAO.getPrice(customerId, kg);

        // ⚠️ CHỈ TRẢ SỐ
        resp.getWriter().print(price);
    }
}
