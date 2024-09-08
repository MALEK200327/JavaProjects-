package GUI;

public class Controller extends Product {
    private DecoderType decoderType;
    public Controller(String poductCode,String productName, String brandName, double price, GaugeType gauge,DecoderType decoderType) {
        super(poductCode, productName, brandName, price, gauge);
        this.decoderType = decoderType;
    }

    public DecoderType getDecoderType() {
        return decoderType;
    }
    public void setDecoderType(DecoderType decoderType) {
        this.decoderType = decoderType;
    }
}
