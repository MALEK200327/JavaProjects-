package GUI;



public class TrackPiece extends Product {

    private TrackType trackType;
    public TrackPiece(String productCode,String productName, String brandName, double price,GaugeType gauge,TrackType trackType) {
        super(productCode, productName, brandName, price, gauge);
        this.trackType = trackType;
    }

    public TrackType getTrackType() {
        return trackType;
    }
    public void setTrackType(TrackType trackType) {
        this.trackType = trackType;
    }
}
