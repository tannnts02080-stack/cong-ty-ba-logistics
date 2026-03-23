package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;

import utils.DBConnect;

public class ShippingPriceDAO {

    // ================== THÊM GIÁ ==================
    public static void insert(int customerId, Integer kg, int price) {
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

    // ================== LẤY DANH SÁCH KG ==================
    public static List<Integer> findKgByCustomer(int customerId) {
        List<Integer> list = new ArrayList<>();

        String sql = """
            SELECT kg
            FROM shipping_price
            WHERE customer_id = ?
              AND kg IS NOT NULL
            ORDER BY kg
        """;

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

    // ================== LẤY GIÁ ==================
    public static int getPrice(int customerId, Integer kg) {
        String sql = """
            SELECT price
            FROM shipping_price
            WHERE customer_id = ?
              AND (
                   (kg IS NULL AND ? IS NULL)
                   OR kg = ?
              )
        """;

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            if (kg == null) {
                ps.setNull(2, Types.INTEGER);
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(2, kg);
                ps.setInt(3, kg);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("price");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

 // ================== BẢNG GIÁ GROUP THEO KHÁCH ==================
    public static Map<String, List<Map<String, Object>>> findAllGrouped() {

        Map<String, List<Map<String, Object>>> map = new LinkedHashMap<>();

        String sql = """
            SELECT sp.id,
                   c.name AS customer_name,
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
                String customer = rs.getString("customer_name");

                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));          // ✅ CỰC KỲ QUAN TRỌNG
                row.put("kg", (Integer) rs.getObject("kg"));
                row.put("price", rs.getInt("price"));

                map.computeIfAbsent(customer, k -> new ArrayList<>())
                   .add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    
    
    
 // ================== XOÁ GIÁ ==================
 // ================== XOÁ GIÁ (NẾU LÀ GIÁ CUỐI → XOÁ LUÔN KHÁCH) ==================
    public static void delete(int priceId) {

        String getCustomerSql =
            "SELECT customer_id FROM shipping_price WHERE id = ?";

        String deletePriceSql =
            "DELETE FROM shipping_price WHERE id = ?";

        String countRemainSql =
            "SELECT COUNT(*) FROM shipping_price WHERE customer_id = ?";

        String deleteCustomerSql =
            "DELETE FROM customers WHERE id = ?";

        try (Connection con = DBConnect.getConnection()) {
            con.setAutoCommit(false);

            int customerId = -1;

            // 1️⃣ Lấy customer_id của dòng giá
            try (PreparedStatement ps = con.prepareStatement(getCustomerSql)) {
                ps.setInt(1, priceId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    customerId = rs.getInt("customer_id");
                }
            }

            // Nếu không tìm thấy giá → thoát
            if (customerId == -1) {
                con.rollback();
                return;
            }

            // 2️⃣ Xoá dòng giá
            try (PreparedStatement ps = con.prepareStatement(deletePriceSql)) {
                ps.setInt(1, priceId);
                ps.executeUpdate();
            }

            // 3️⃣ Kiểm tra còn giá nào của khách không
            int remain = 0;
            try (PreparedStatement ps = con.prepareStatement(countRemainSql)) {
                ps.setInt(1, customerId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    remain = rs.getInt(1);
                }
            }

            // 4️⃣ Nếu KHÔNG còn giá → xoá luôn khách
            if (remain == 0) {
                try (PreparedStatement ps = con.prepareStatement(deleteCustomerSql)) {
                    ps.setInt(1, customerId);
                    ps.executeUpdate();
                }
            }

            con.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ================== LẤY 1 GIÁ THEO ID ==================
    public static Map<String, Object> findById(int id) {
        String sql = """
            SELECT sp.id, sp.customer_id, sp.kg, sp.price
            FROM shipping_price sp
            WHERE sp.id=?
        """;

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", rs.getInt("id"));
                m.put("customerId", rs.getInt("customer_id"));
                m.put("kg", (Integer) rs.getObject("kg"));
                m.put("price", rs.getInt("price"));
                return m;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================== CẬP NHẬT GIÁ ==================
    public static void update(int id, Integer kg, int price) {
        String sql = """
            UPDATE shipping_price
            SET kg=?, price=?
            WHERE id=?
        """;

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (kg == null) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, kg);
            }

            ps.setInt(2, price);
            ps.setInt(3, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
 // ================== KIỂM TRA TRÙNG KG ==================
    public static boolean existsKg(int customerId, Integer kg) {

        String sql = """
            SELECT 1
            FROM shipping_price
            WHERE customer_id = ?
              AND (
                   (kg IS NULL AND ? IS NULL)
                   OR kg = ?
              )
        """;

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            if (kg == null) {
                ps.setNull(2, Types.INTEGER);
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(2, kg);
                ps.setInt(3, kg);
            }

            ResultSet rs = ps.executeQuery();
            return rs.next(); // có dữ liệu → trùng

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    
 // ===== SEARCH BẢNG GIÁ THEO TÊN KHÁCH  =====
    public static Map<String, List<Map<String, Object>>> searchGroupedByCustomerName(String keyword) {

        Map<String, List<Map<String, Object>>> map = new LinkedHashMap<>();

        String sql = """
            SELECT c.name, sp.id, sp.kg, sp.price
            FROM shipping_price sp
            JOIN customers c ON sp.customer_id = c.id
            WHERE c.name COLLATE Latin1_General_CI_AI LIKE ?
            ORDER BY c.name, sp.kg
        """;

        try (
            Connection con = DBConnect.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String customerName = rs.getString("name");

                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("kg", rs.getInt("kg"));
                row.put("price", rs.getInt("price"));

                map.computeIfAbsent(customerName, k -> new ArrayList<>()).add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }





    public static Integer findPrice(int customerId, Integer kg) {

        String sql = """
            SELECT price
            FROM shipping_price
            WHERE customer_id = ?
            AND (
                (kg IS NULL AND ? IS NULL)
                OR kg = ?
            )
            LIMIT 1
        """;

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            if (kg == null) {
                ps.setNull(2, Types.INTEGER);
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(2, kg);
                ps.setInt(3, kg);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("price");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



    
    
    
    
}
