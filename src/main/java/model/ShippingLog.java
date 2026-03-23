package model;

import java.sql.Timestamp;

public class ShippingLog {

    // ====== FIELD ======
    private int id;
    private int customerId;
    private String customer;
    private Integer kg;        // cho phép null
    private int price;
    private Timestamp time;

    // ➕ PHẦN BỔ SUNG (CÒN THIẾU)
    private boolean paid;      // đã thu ship hay chưa
    private int extraCost;     // chi phí phát sinh
    private String note;       // ghi chú

    // ====== GET / SET ======
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomer() {
        return customer;
    }
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Integer getKg() {
        return kg;
    }
    public void setKg(Integer kg) {
        this.kg = kg;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public Timestamp getTime() {
        return time;
    }
    public void setTime(Timestamp time) {
        this.time = time;
    }

    // ====== PHẦN MỚI ======
    public boolean isPaid() {
        return paid;
    }
    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public int getExtraCost() {
        return extraCost;
    }
    public void setExtraCost(int extraCost) {
        this.extraCost = extraCost;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}
