package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import dao.CustomerDAO;
import dao.ShippingDAO;
import dao.ShippingLogDAO;
import dao.ShippingPriceDAO;
import model.ShippingLog;

@WebServlet("/shipping")
public class ShippingServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {

	    req.setCharacterEncoding("UTF-8");
	    resp.setCharacterEncoding("UTF-8");

	    HttpSession session = req.getSession();

	    String action = req.getParameter("action");
	    String idParam = req.getParameter("id");

	    /* ================== XOÁ ================== */
	    if ("delete".equals(action) && idParam != null && !idParam.isBlank()) {
	        ShippingLogDAO.delete(Integer.parseInt(idParam));
	        resp.sendRedirect(req.getContextPath() + "/shipping?tab=order");
	        return;
	    }

	    /* ================== SỬA ================== */
	    if ("edit".equals(action) && idParam != null && !idParam.isBlank()) {
	        ShippingLog log = ShippingLogDAO.findById(Integer.parseInt(idParam));
	        session.setAttribute("editLog", log);
	        resp.sendRedirect(req.getContextPath() + "/shipping?tab=order");
	        return;
	    }

	    /* ================== LẤY editLog ================== */
	    if (session.getAttribute("editLog") != null) {
	        req.setAttribute("editLog", session.getAttribute("editLog"));
	        session.removeAttribute("editLog");
	    }

	    /* ================== SEARCH BẢNG GIÁ ================== */
	    String keyword = req.getParameter("keyword");
	    if (keyword != null && !keyword.trim().isEmpty()) {
	        req.setAttribute("priceTable",
	                ShippingPriceDAO.searchGroupedByCustomerName(keyword.trim()));
	        req.setAttribute("keyword", keyword);
	    } else {
	        req.setAttribute("priceTable",
	                ShippingPriceDAO.findAllGrouped());
	    }

	    /* ================== DỮ LIỆU CHUNG ================== */
	 // ===== LẤY NGÀY ĐANG XEM =====
	    String dateParam = req.getParameter("date");
	    LocalDate selectedDate;

	    if (dateParam != null && !dateParam.isBlank()) {
	        selectedDate = LocalDate.parse(dateParam);
	    } else {
	        selectedDate = LocalDate.now();
	    }

	    // ===== LOG + TỔNG TIỀN THEO NGÀY =====
	    req.setAttribute("logs", ShippingLogDAO.findByDate(selectedDate));

	    int totalAmount = ShippingDAO.sumPriceByDate(selectedDate);
	    req.setAttribute("totalAmount", totalAmount);
	    
	    
	    //19h02
	    int extraTotal = ShippingLogDAO.sumExtraCostByDate(selectedDate);
	    req.setAttribute("extraTotal", extraTotal);

	    
	    int monthTotal = ShippingDAO.sumPriceFromMonthStart(selectedDate);
	    req.setAttribute("monthTotal", monthTotal);

	    

	    req.setAttribute("selectedDate",
	            selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	    req.setAttribute("selectedDateValue", selectedDate.toString());
	 // yyyy-MM-dd


	    req.setAttribute("customers", CustomerDAO.findAll());

	    

	    /* ================== FORWARD 1 LẦN DUY NHẤT ================== */
	    req.getRequestDispatcher("/shipping.jsp").forward(req, resp);
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	        throws IOException {

	    req.setCharacterEncoding("UTF-8");

	    // ===== LẤY NGÀY =====
	    String dateParam = req.getParameter("date");
	    LocalDate selectedDate =
	            (dateParam != null && !dateParam.isBlank())
	                    ? LocalDate.parse(dateParam)
	                    : LocalDate.now();

	    // ===== DATA CƠ BẢN =====
	    int customerId = Integer.parseInt(req.getParameter("customer_id"));

	    Integer kg = null;
	    if (req.getParameter("kg") != null && !req.getParameter("kg").isBlank()) {
	        kg = Integer.parseInt(req.getParameter("kg"));
	    }

	    int price = Integer.parseInt(req.getParameter("price"));
	    String idParam = req.getParameter("id");

	    // ===== ĐÃ THU SHIP =====
	    boolean paid = req.getParameter("paid") != null;

	    // ===== CHI PHÍ PHÁT SINH =====
	    int extraCost = 0;
	    if (req.getParameter("extraCost") != null && !req.getParameter("extraCost").isBlank()) {
	        extraCost = Integer.parseInt(req.getParameter("extraCost"));
	    }

	    // ===== GHI CHÚ =====
	    String note = req.getParameter("note");

	    // ===== LƯU / CẬP NHẬT =====
	    if (idParam == null || idParam.isBlank()) {
	        ShippingLogDAO.insert(
	                customerId,
	                kg,
	                price,
	                paid,
	                extraCost,
	                note,
	                selectedDate
	        );
	    } else {
	        ShippingLogDAO.update(
	                Integer.parseInt(idParam),
	                customerId,
	                kg,
	                price,
	                paid,
	                extraCost,
	                note
	        );
	    }

	    // ===== QUAY VỀ TAB =====
	    resp.sendRedirect(req.getContextPath() + "/shipping?tab=order&date=" + selectedDate);
	}

}
