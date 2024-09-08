package GUI;

public class RollingStock extends Product {
    private Era era;
    public RollingStock(String productCode,String productName, String brandName, double price, GaugeType gauge,Era era) {
        super(productCode, productName, brandName, price, gauge);
        this.era = era;
    }
    public Era getEra() {
        return era;
    }
    public void setEra(Era era) {
        this.era = era;
    }
}