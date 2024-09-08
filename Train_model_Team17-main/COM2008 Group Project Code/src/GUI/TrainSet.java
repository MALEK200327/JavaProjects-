package GUI;

import java.util.List;

public class TrainSet extends Product {
    private List<Locomotive> locomotives;
    private List<RollingStock> rollingStock;
    private List<TrackPiece> trackPieces;
    private List<Controller> controller;
    private Era era;

    public TrainSet(String productCode,String productName, String brandName, double price,GaugeType gauge,
                    List<Locomotive> locomotives, List<RollingStock> rollingStock,
                    List<TrackPiece> trackPieces, List<Controller> controller, Era era) {
        super(productCode, productName, brandName, price, gauge);
        this.locomotives = locomotives;
        this.rollingStock = rollingStock;
        this.trackPieces = trackPieces;
        this.controller = controller;
        this.era = era;
    }

    public List<Locomotive> getLocomotives() {
        return locomotives;
    }

    public void setLocomotives(List<Locomotive> locomotives) {
        this.locomotives = locomotives;
    }

    public List<RollingStock> getRollingStock() {
        return rollingStock;
    }

    public void setRollingStock(List<RollingStock> rollingStock) {
        this.rollingStock = rollingStock;
    }

    public List<TrackPiece> getTrackPieces() {
        return trackPieces;
    }

    public void setTrackPieces(List<TrackPiece> trackPieces) {
        this.trackPieces = trackPieces;
    }

    public List<Controller> getController() {
        return controller;
    }

    public void setController(List<Controller> controller) {
        this.controller = controller;
    }

    public Era getEra() {
        return era;
    }

    public void setEra(Era era) {
        this.era = era;
    }
}

