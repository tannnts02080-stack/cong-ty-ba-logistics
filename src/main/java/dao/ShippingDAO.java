package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import utils.DBConnect;

public class ShippingDAO {

    // LẤY DANH SÁCH KG THEO TÊN KHÁCH
	public static List<Integer> getKgByCustomerId(int customerId) {
	    List<Integer> list = new ArrayList<>();
	    String sql = "SELECT kg FROM shipping_price WHERE customer_id=? ORDER BY kg";

	    try (Connection con = DBConnect.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, customerId);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            list.add(rs.getInt("kg"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}

	public static int getPrice(int customerId, int kg) {
	    String sql = "SELECT price FROM shipping_price WHERE customer_id=? AND kg=?";
	    try (Connection con = DBConnect.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, customerId);
	        ps.setInt(2, kg);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) return rs.getInt("price");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return 0;
	}
	
	
	
	public static void insertPrice(int customerId, Integer kg, int price) {
	    String sql = """
	        INSERT INTO shipping_price(customer_id, kg, price)
	        VALUES (?, ?, ?)
	    """;

	    try (Connection con = DBConnect.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, customerId);

	        if (kg == null) {
	            ps.setNull(2, Types.INTEGER);
	        } else {
	            ps.setInt(2, kg);
	        }

	        ps.setInt(3, price);

	        ps.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	
	
	public static Map<String, List<Map<String, Object>>> getPriceTable() {

	    Map<String, List<Map<String, Object>>> result = new LinkedHashMap<>();

	    String sql = """
	        SELECT c.name AS customer,
	               sp.kg,
	               sp.price
	        FROM shipping_price sp
	        JOIN customers c ON sp.customer_id = c.id
	        ORDER BY c.name, sp.kg
	    """;

	    try (Connection con = DBConnect.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            String customer = rs.getString("customer");

	            Map<String, Object> row = new HashMap<>();
	            row.put("kg", rs.getObject("kg"));   // có thể null
	            row.put("price", rs.getInt("price"));

	            result.computeIfAbsent(customer, k -> new ArrayList<>())
	                  .add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return result;
	}

	
	
	
	
	
	
	
	// 11:32
	public static int sumPriceByDate(LocalDate date) {
	    String sql = """
	        SELECT COALESCE(SUM(price), 0)
	        FROM shipping_log
	        WHERE CAST(created_at AS DATE) = ?
	    """;

	    try (Connection con = DBConnect.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setDate(1, java.sql.Date.valueOf(date));

	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return 0;
	}


	
	
	public static int sumPriceFromMonthStart(LocalDate date) {
	    String sql = """
	        SELECT ISNULL(SUM(price), 0)
	        FROM shipping_log
	        WHERE created_at >= ?
	          AND created_at < ?
	    """;

	    LocalDate firstDay = date.withDayOfMonth(1);
	    LocalDate nextDay = date.plusDays(1);

	    try (Connection con = DBConnect.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setTimestamp(1, Timestamp.valueOf(firstDay.atStartOfDay()));
	        ps.setTimestamp(2, Timestamp.valueOf(nextDay.atStartOfDay()));

	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return 0;
	}

	
	
	
	
	public static int sumExtraCostByDate(LocalDate date) {
	    String sql = """
	        SELECT ISNULL(SUM(extra_cost), 0)
	        FROM shipping_log
	        WHERE CAST(created_at AS DATE) = ?
	    """;

	    try (Connection con = DBConnect.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setDate(1, java.sql.Date.valueOf(date));
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) return rs.getInt(1);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return 0;
	}

	
	
	

}
