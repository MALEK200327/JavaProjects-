package GUI;

public class OrderLine {
    
    private Integer orderLineID;
    private Integer orderID;
    private String product_code;
    private int quantity;

    public OrderLine(Integer orderLineID, Integer orderID, String product_code, int quantity) {
        this.orderLineID = orderLineID;
        this.orderID = orderID;
        this.product_code = product_code;
        this.quantity = quantity;
    }

    public int getOrderLineID() {
        return orderLineID;
    }

    public void setOrderLineID(int orderLineID) {
        this.orderLineID = orderLineID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}
