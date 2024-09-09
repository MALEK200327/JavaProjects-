public class Dvd {
    private String title;
    private double price;
    private double id;



    public String geTitle() {
        return title;
    }

    public double gePrice() {
        return price;
    }

    public double getId() {
        return id;
    }



public void getTitle(String title) {
    this.title= title;
}

public void getPrice(int price) {
    this.price= price;
}

public void getId(int Id) {
    this.id= Id;
}


}
