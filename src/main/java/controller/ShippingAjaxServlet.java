package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import dao.ShippingPriceDAO;

@WebServlet("/shipping-ajax")
public class ShippingAjaxServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setCharacterEncoding("UTF-8");

        int customerId = Integer.parseInt(req.getParameter("customerId"));
        String kgParam = req.getParameter("kg");

        // ===== LOAD KG =====
        if (kgParam == null) {
            List<Integer> kgs = ShippingPriceDAO.findKgByCustomer(customerId);
            resp.setContentType("application/json");
            resp.getWriter().print(new Gson().toJson(kgs));
            return;
        }

        // ===== LOAD GIÁ =====
        Integer kg = null;
        if (!"0".equals(kgParam)) {
            kg = Integer.parseInt(kgParam);
        }

        int price = ShippingPriceDAO.getPrice(customerId, kg);
        resp.getWriter().print(price);
    }
}
