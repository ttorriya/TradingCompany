package classes;

public class Supplier {
    private int id;
    private String org_name;
    private String city;
    private String country;
    private String address;
    private int id_basket;

    public Supplier() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId_basket() {
        return id_basket;
    }

    public void setId_basket(int id_basket) {
        this.id_basket = id_basket;
    }
}
