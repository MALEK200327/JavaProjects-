package GUI;


class Order {
    
    enum STATUS {
        pending,
        confirmed,
        fulfilled
      }

    private Integer orderID;
    private String order_date;
    private double price_total;
    private STATUS order_status;
    private int userID;
    

    public Order(Integer orderID, String order_date, double price_total, STATUS order_status, int userID) {
        this.orderID = orderID;
        this.order_date = order_date;
        this.price_total = price_total;
        this.order_status = order_status;
        this.userID = userID;
    }

    // Getters and setters

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getOrderDate() {
        return order_date;
    }

    public void setOrderDate(String order_date) {
        this.order_date = order_date;
    }

    public double getPriceTotal() {
        return price_total;
    }

    public void setPriceTotal(double price_total) {
        this.price_total = price_total;
    }

    public STATUS getOrderStatus() {
        return order_status;
    }

    public void setOrderStatus(STATUS order_status) {
        this.order_status = order_status;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    
}
