package dao;

import java.sql.*;
import java.util.*;
import model.ShippingLog;
import utils.DBConnect;
import java.time.LocalDate;

public class ShippingLogDAO {

	// ================== THÊM CHUYẾN ==================
	public static void insert(int customerId, Integer kg, int price, boolean paid, int extraCost, String note,
			LocalDate date) {

		String sql = """
				    INSERT INTO shipping_log
				    (customer_id, kg, price, is_paid, extra_cost, note, created_at)
				    VALUES (?, ?, ?, ?, ?, ?, ?)
				""";

		try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, customerId);

			if (kg == null) {
				ps.setNull(2, Types.INTEGER);
			} else {
				ps.setInt(2, kg);
			}

			ps.setInt(3, price);
			ps.setBoolean(4, paid);
			ps.setInt(5, extraCost);
			ps.setString(6, note);
			ps.setTimestamp(7, Timestamp.valueOf(date.atStartOfDay()));

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ================== DANH SÁCH ==================
	public static List<ShippingLog> findAll() {
		List<ShippingLog> list = new ArrayList<>();

		String sql = """
				    SELECT sl.id,
				           c.name AS customer_name,
				           sl.kg,
				           sl.price,
				           sl.created_at
				    FROM shipping_log sl
				    JOIN customers c ON sl.customer_id = c.id
				    ORDER BY sl.created_at DESC
				""";

		try (Connection con = DBConnect.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				ShippingLog log = new ShippingLog();
				log.setId(rs.getInt("id"));
				log.setCustomer(rs.getString("customer_name"));
				log.setKg(rs.getObject("kg") == null ? null : rs.getInt("kg"));
				log.setPrice(rs.getInt("price"));
				log.setTime(rs.getTimestamp("created_at"));
				list.add(log);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// ================== TÌM THEO ID (SỬA) ==================
	public static ShippingLog findById(int id) {
		String sql = """
				    SELECT sl.id,
				           sl.customer_id,
				           c.name AS customer_name,
				           sl.kg,
				           sl.price
				    FROM shipping_log sl
				    JOIN customers c ON sl.customer_id = c.id
				    WHERE sl.id = ?
				""";

		try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				ShippingLog log = new ShippingLog();
				log.setId(rs.getInt("id"));
				log.setCustomerId(rs.getInt("customer_id"));
				log.setCustomer(rs.getString("customer_name"));
				log.setKg(rs.getObject("kg") == null ? null : rs.getInt("kg"));
				log.setPrice(rs.getInt("price"));
				return log;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ================== CẬP NHẬT ==================
	public static void update(int id, int customerId, Integer kg, int price, boolean paid, int extraCost, String note) {

		String sql = """
				    UPDATE shipping_log
				    SET customer_id = ?,
				        kg = ?,
				        price = ?,
				        is_paid = ?,
				        extra_cost = ?,
				        note = ?
				    WHERE id = ?
				""";

		try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, customerId);

			if (kg == null) {
				ps.setNull(2, Types.INTEGER);
			} else {
				ps.setInt(2, kg);
			}

			ps.setInt(3, price);
			ps.setBoolean(4, paid);
			ps.setInt(5, extraCost);
			ps.setString(6, note);
			ps.setInt(7, id);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ================== XOÁ ==================
	public static void delete(int id) {
		String sql = "DELETE FROM shipping_log WHERE id=?";

		try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<ShippingLog> findByDate(LocalDate date) {

		List<ShippingLog> list = new ArrayList<>();

		String sql = """
								    SELECT sl.id,
				       c.name AS customer,
				       sl.kg,
				       sl.price,
				       sl.created_at,
				       sl.is_paid,
				       sl.extra_cost,
				       sl.note
				FROM shipping_log sl
				JOIN customers c ON sl.customer_id = c.id
				WHERE CAST(sl.created_at AS DATE) = ?
				ORDER BY sl.created_at DESC

								""";

		try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setDate(1, java.sql.Date.valueOf(date));
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ShippingLog l = new ShippingLog();
				l.setId(rs.getInt("id"));
				l.setCustomer(rs.getString("customer"));
				Integer kg = rs.getObject("kg") == null ? null : rs.getInt("kg");
				l.setKg(kg);

				l.setPrice(rs.getInt("price"));
				l.setTime(rs.getTimestamp("created_at"));

				l.setPaid(rs.getBoolean("is_paid"));
				l.setExtraCost(rs.getInt("extra_cost"));
				l.setNote(rs.getString("note"));

				list.add(l);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	
	// ================== TỔNG CHI PHÍ PHÁT SINH THEO NGÀY ==================
	public static int sumExtraCostByDate(LocalDate date) {

	    String sql = """
	        SELECT COALESCE(SUM(extra_cost), 0)
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


}
