package data.user;

import data.admin.Items;

public class Cart extends Items {

  private int totalPrice;

  public Cart(String itemID, String itemName, String itemDescription, int price, int quantity, int totalPrice) {
    super(itemID, itemName, itemDescription, price, quantity);
    this.totalPrice = totalPrice;
  }

  public int getTotalPrice() {
    return this.totalPrice;
  }

  public void setTotalPrice(int totalPrice) {
    this.totalPrice = totalPrice;
  }

}
