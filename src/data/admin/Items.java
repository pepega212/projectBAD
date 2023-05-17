package data.admin;

public class Items {
  private String itemID;
  private String itemName;
  private String itemDescription;
  private int price;
  private int quantity;

  public Items(String itemID, String itemName, String itemDescription, int price, int quantity) {
    this.itemID = itemID;
    this.itemName = itemName;
    this.itemDescription = itemDescription;
    this.price = price;
    this.quantity = quantity;
  }

  public String getItemID() {
    return this.itemID;
  }

  public void setItemID(String itemID) {
    this.itemID = itemID;
  }

  public String getItemName() {
    return this.itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public String getItemDescription() {
    return this.itemDescription;
  }

  public void setItemDescription(String itemDescription) {
    this.itemDescription = itemDescription;
  }

  public int getPrice() {
    return this.price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
