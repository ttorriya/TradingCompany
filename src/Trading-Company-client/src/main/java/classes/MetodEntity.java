package classes;

public class MetodEntity {
    private int id;
    private float num;
    private float price;
    private String goalNum;
    private String goalPrice;

    public MetodEntity(){}

    public String getGoalNum() {
        return goalNum;
    }

    public void setGoalNum(String goalNum) {
        this.goalNum = goalNum;
    }

    public String getGoalPrice() {
        return goalPrice;
    }

    public void setGoalPrice(String goalPrice) {
        this.goalPrice = goalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
