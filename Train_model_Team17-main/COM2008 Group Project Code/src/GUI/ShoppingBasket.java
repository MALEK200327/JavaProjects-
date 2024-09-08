package GUI;
import java.util.*; 

class ShoppingBasket {
    private List<Product> items;
    
    public ShoppingBasket() {
        this.items = new ArrayList<>();
    }

    public void addItem(Product product) {
        items.add(product);
    }

    public double getBasketPrice() {
        double basketPrice = items.stream().mapToDouble(Product::getPrice).sum();
        return basketPrice;
    }

    public void purchaseBasket() {
        //create order / process payment / order confirmation?


        items.clear();
    }


}
