package classes;

public class Good {
    private int barcode;
    private String product_name;
    private String unit_measurement;
    private float price;
    private float priceRealise;
    private int quantity;
    private float occupied_space;
    private float ratingNum;
    private float ratingPrice;

    public Good() {
        barcode = 0;
        price = 0;
        priceRealise = 0;
        ratingNum = 0;
        ratingPrice = 0;
    }

    public float getPriceRealise() {
        return priceRealise;
    }

    public void setPriceRealise(float priceRealise) {
        this.priceRealise = priceRealise;
    }

    public float getRatingNum() {
        return ratingNum;
    }

    public void setRatingNum(float ratingNum) {
        this.ratingNum = ratingNum;
    }

    public float getRatingPrice() {
        return ratingPrice;
    }

    public void setRatingPrice(float ratingPrice) {
        this.ratingPrice = ratingPrice;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getUnit_measurement() {
        return unit_measurement;
    }

    public void setUnit_measurement(String unit_measurement) {
        this.unit_measurement = unit_measurement;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getOccupied_space() {
        return occupied_space;
    }

    public void setOccupied_space(float occupied_space) {
        this.occupied_space = occupied_space;
    }
}
