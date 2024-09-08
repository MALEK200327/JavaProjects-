package GUI;

public class Product {
    private String productCode;
    private String productName;
    private String brandName;
    private double price;
    private GaugeType gauge;

    public Product(String productCode, String productName, String brandName, double price, GaugeType gauge) {
        this.productCode = productCode;
        this.productName = productName;
        this.brandName = brandName;
        this.price = price;
        this.gauge = gauge;
    }
    public Product() {
        
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public GaugeType getGauge() {
        return gauge;
    }
    public void setGauge(GaugeType gauge) {
        this.gauge = gauge;
    }
}

