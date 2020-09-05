package in.xplorelogic.inveck.models;

public class LocationListDAO {
    int id;
    int quantity;
    int mileStoneId;
    int stockId;
    String location="";
    public LocationListDAO()
    {}
    public LocationListDAO(int id, int quantity, int mileStoneId, int stockId, String location) {
        this.id = id;
        this.quantity = quantity;
        this.mileStoneId = mileStoneId;
        this.stockId = stockId;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMileStoneId() {
        return mileStoneId;
    }

    public void setMileStoneId(int mileStoneId) {
        this.mileStoneId = mileStoneId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
