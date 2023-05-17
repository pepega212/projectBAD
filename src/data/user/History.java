package data.user;

public class History {
  private String transactionID;
  private String itemName;
  private String itemDescription;
  private int price;
  private int quantity;
  private int totalPrice;
  private String transactionDate;

  public History(String transactionID, String itemName, String itemDescription, int price, int quantity,
      int totalPrice, String transactionDate) {
    this.transactionID = transactionID;
    this.itemName = itemName;
    this.itemDescription = itemDescription;
    this.price = price;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
    this.transactionDate = transactionDate;
  }

  public String getTransactionID() {
    return this.transactionID;
  }

  public void setTransactionID(String transactionID) {
    this.transactionID = transactionID;
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

  public int getTotalPrice() {
    return this.totalPrice;
  }

  public void setTotalPrice(int totalPrice) {
    this.totalPrice = totalPrice;
  }

  public String getTransactionDate() {
    return this.transactionDate;
  }

  public void setTransactionDate(String transactionDate) {
    this.transactionDate = transactionDate;
  }

}
