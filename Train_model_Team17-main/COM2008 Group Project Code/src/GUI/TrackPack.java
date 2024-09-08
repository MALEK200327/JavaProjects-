package GUI;

import java.util.List;
import java.util.Random;

public class TrackPack extends Product {
    private List<TrackPiece> trackPiecesArray;

    public TrackPack(String productCode,String productName, String brandName, double price,GaugeType gauge,
                     List<TrackPiece> trackPiecesArray) {
        super(productCode, productName, brandName, price, gauge);
        this.trackPiecesArray = trackPiecesArray;
    }

    public List<TrackPiece> getTrackPiecesArray() {
        return trackPiecesArray;
    }

    public void setTrackPiecesArray(List<TrackPiece> trackPiecesArray) {
        this.trackPiecesArray = trackPiecesArray;
    }

    public static String generateProductCode() {
        // Generate a unique product code for each Locomotive object
        int serialNumber = generateRandomSerialNumber(3, 5);
        return "P" + serialNumber;
    }

    public static int generateRandomSerialNumber(int minDigits, int maxDigits) {
        // Generate a random number between 0 and 99999 (five-digit maximum)
        Random random = new Random();
        int range = (int) Math.pow(10, maxDigits) - (int) Math.pow(10, minDigits) + 1;
        return random.nextInt(range) + (int) Math.pow(10, minDigits);
    }
}

