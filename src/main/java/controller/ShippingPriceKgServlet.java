package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

import dao.ShippingPriceDAO;

@WebServlet("/shipping-price/kg")
public class ShippingPriceKgServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        String customerIdStr = req.getParameter("customerId");
        if (customerIdStr == null || customerIdStr.isBlank()) {
            resp.getWriter().print("[]");
            return;
        }

        int customerId = Integer.parseInt(customerIdStr);

        List<Integer> list = ShippingPriceDAO.findKgByCustomer(customerId);

        // trả JSON đơn giản
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            json.append(list.get(i));
            if (i < list.size() - 1) json.append(",");
        }
        json.append("]");

        resp.getWriter().print(json.toString());
    }
}
