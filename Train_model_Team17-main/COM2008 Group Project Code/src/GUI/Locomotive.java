package GUI;


public class Locomotive extends Product {
    private DecoderType decoderType;
    private Era era;
    public Locomotive(String productCode,String productName, String brandName, double price, GaugeType gauge,DecoderType decoderType2,Era era) {
        super(productCode, productName, brandName, price, gauge);
        this.decoderType = decoderType2;
        this.era = era;
    }


    public Locomotive() {
        super();
    }
    public DecoderType getDecoderType() {
        return decoderType;
    }
    public void setDecoderType(DecoderType decoderType) {
        this.decoderType = decoderType;
    }
    public Era getEra() {
        return era;
    }
    public void setEra(Era era) {
        this.era = era;
    }




}
