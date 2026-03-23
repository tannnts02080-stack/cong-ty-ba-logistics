package dao;

import utils.DBConnect;
import java.sql.*;
import java.util.*;

public class CustomerDAO {

    public static List<String> getAllNames() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name FROM customers ORDER BY name";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===== DÙNG CHO JSP (dropdown, form) =====
    public static List<Map<String, Object>> findAll() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT id, name FROM customers ORDER BY name";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> c = new HashMap<>();
                c.put("id", rs.getInt("id"));
                c.put("name", rs.getString("name"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===== THÊM KHÁCH (CÓ KIỂM TRA TRÙNG) =====
    public static boolean insert(String name) {

        if (existsByName(name)) {
            return false;
        }

        String sql = "INSERT INTO customers(name) VALUES(?)";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===== SEARCH KHÁCH HÀNG GẦN ĐÚNG =====
    public static List<Map<String, Object>> searchByName(String keyword) {

        List<Map<String, Object>> list = new ArrayList<>();
        String[] words = keyword.trim().split("\\s+");

        StringBuilder sql = new StringBuilder(
            "SELECT id, name FROM customers WHERE 1=1"
        );

        for (int i = 0; i < words.length; i++) {
            sql.append(" AND name LIKE ?");
        }

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;
            for (String w : words) {
                ps.setString(index++, "%" + w + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> c = new HashMap<>();
                c.put("id", rs.getInt("id"));
                c.put("name", rs.getString("name"));
                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ===== KIỂM TRA TRÙNG =====
    public static boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM customers WHERE name = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    
    
    
    
    
    
    
    public static void deleteById(int id) {
        String sql = "DELETE FROM customers WHERE id = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateName(int id, String name) {
        String sql = "UPDATE customers SET name = ? WHERE id = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    public static void update(int id, String name) {
        String sql = "UPDATE customers SET name=? WHERE id=?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    
}
