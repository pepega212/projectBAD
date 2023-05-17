package user;

import main.Main;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import connection.Connect;
import data.user.Cart;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CartItem extends Application implements EventHandler<ActionEvent> {
  Connect c = Connect.getConnect();

  Stage itemMarket, cartItem, transactionHistory, logout;
  Scene scene;
  BorderPane bPane;
  FlowPane fPane;
  Button removeBtn, checkoutBtn;
  Menu menu;
  MenuItem itemMarketMenu, cartItemMenu, transactionMenu, logOutMenu;
  MenuBar menuBar;
  Vector<Cart> cart;
  TableView<Cart> cartTable;
  ObservableList<Cart> cartObs;

  public void Init() {
    bPane = new BorderPane();
    fPane = new FlowPane();
    cart = new Vector<>();

    removeBtn = new Button("Remove From Cart");
    checkoutBtn = new Button("Checkout");
    fPane.getChildren().addAll(removeBtn, checkoutBtn);

    menu = new Menu("Menu");
    itemMarketMenu = new MenuItem("Item Market");
    cartItemMenu = new MenuItem("Cart Item");
    transactionMenu = new MenuItem("Transaction History");
    logOutMenu = new MenuItem("logout");
    menu.getItems().addAll(itemMarketMenu, cartItemMenu, transactionMenu, logOutMenu);
    menuBar = new MenuBar();
    menuBar.getMenus().add(menu);

    SetTable();

    scene = new Scene(bPane, 900, 600);
  }

  public void Set() {
    bPane.setTop(menuBar);
    bPane.setCenter(cartTable);
    bPane.setBottom(fPane);

    cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    cartTable.setPrefWidth(900);
    fPane.setHgap(10);
    fPane.setVgap(10);
    fPane.setPadding(new Insets(60, 0, 25, 0));
    fPane.setAlignment(Pos.BOTTOM_CENTER);

    removeBtn.setPrefSize(150, 37.5);
    checkoutBtn.setPrefSize(150, 37.5);

    removeBtn.setDisable(true);
    checkoutBtn.setDisable(true);
  }

  public void Handler() {
    itemMarketMenu.setOnAction(this);
    cartItemMenu.setOnAction(this);
    transactionMenu.setOnAction(this);
    logOutMenu.setOnAction(this);

    removeBtn.setOnAction(this);
    checkoutBtn.setOnAction(this);

    cartTable.setOnMouseClicked(TableHandler());
  }

  @SuppressWarnings("unchecked")
  public void SetTable() {
    cartTable = new TableView<>();

    TableColumn<Cart, String> itemIDCol = new TableColumn<>("itemID");
    itemIDCol.setCellValueFactory(new PropertyValueFactory<>("itemID"));

    TableColumn<Cart, String> itemNameCol = new TableColumn<>("Item Name");
    itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));

    TableColumn<Cart, String> itemDescCol = new TableColumn<>("Item Description");
    itemDescCol.setCellValueFactory(new PropertyValueFactory<>("itemDescription"));

    TableColumn<Cart, Integer> priceCol = new TableColumn<>("Price");
    priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    TableColumn<Cart, Integer> quantityCol = new TableColumn<>("Quantity");
    quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

    TableColumn<Cart, Integer> totalCol = new TableColumn<>("Total Price");
    totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

    cartTable.getColumns().addAll(itemIDCol, itemNameCol, itemDescCol, priceCol, quantityCol, totalCol);
    CheckTable();
    RefreshTable();
  }

  public void Select() {
    String query = "SELECT item.itemID, itemName, itemDescription, price, cart.quantity, price * cart.quantity AS totalPrice FROM cart JOIN item ON cart.itemID = item.itemID JOIN user ON cart.userID = user.userID WHERE user.userID = '"
        + GetUserID() + "'";
    try {
      c.ExecuteQuery(query);
      while (c.rs.next()) {
        String itemID = c.rs.getString("itemID");
        String itemName = c.rs.getString("itemName");
        String itemDescription = c.rs.getString("itemDescription");
        int price = c.rs.getInt("price");
        int quantity = c.rs.getInt("quantity");
        int totalPrice = c.rs.getInt("totalPrice");
        cart.add(new Cart(itemID, itemName, itemDescription, price, quantity, totalPrice));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void CheckTable() {
    String query = "SELECT * FROM cart WHERE userID = '" + GetUserID() + "'";
    try {
      c.ExecuteQuery(query);
      if (c.rs.next()) {
        removeBtn.setDisable(false);
        checkoutBtn.setDisable(false);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void CheckOut() {
    Alert checkoutFailed = new Alert(AlertType.ERROR);
    Alert checkoutSuccess = new Alert(AlertType.INFORMATION);

    checkoutFailed.setTitle("ERROR");
    checkoutFailed.setHeaderText(null);
    checkoutSuccess.setTitle("SUCCESS");
    checkoutSuccess.setHeaderText(null);

    String itemID = cartTable.getSelectionModel().getSelectedItem().getItemID();
    String itemName = cartTable.getSelectionModel().getSelectedItem().getItemName();
    int quantity = cartTable.getSelectionModel().getSelectedItem().getQuantity();
    String query = "SELECT quantity FROM item WHERE itemID = '"
        + itemID + "'";
    try {
      c.ExecuteQuery(query);
      while (c.rs.next()) {
        int stock = c.rs.getInt("quantity");
        if (quantity > stock) {
          checkoutFailed.setContentText(
              "The item you ordered (" + itemName + ") with the itemID (" + itemID + ") is out of stock!");
          checkoutFailed.showAndWait();
        } else {
          String reduceQuery = "UPDATE item SET quantity = quantity - ? WHERE itemID = ?";
          c.pst = c.PrepStatement(reduceQuery);
          c.pst.setInt(1, quantity);
          c.pst.setString(2, itemID);
          c.pst.executeUpdate();
          c.pst.close();
          checkoutSuccess.setContentText("Successfully checkout items with item name (" + itemName + ") and id ("
              + itemID + "), click OK to continue");
          checkoutSuccess.showAndWait();
          Insert();
          DeleteFromCart();
          InsertDetail();
          RefreshTable();
          Clear();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void DeleteFromCart() {
    String itemID = cartTable.getSelectionModel().getSelectedItem().getItemID();
    String query = "DELETE FROM cart WHERE itemID = ? ";
    try {
      c.pst = c.PrepStatement(query);
      c.pst.setString(1, itemID);
      c.pst.executeUpdate();
      c.pst.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Insert() {
    String transactionID = GenerateTransactionID();
    String userID = GetUserID();
    Date date = new Date();
    String transactionDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
    String query = "INSERT INTO transaction VALUES(?, ?, ?)";
    try {
      c.pst = c.PrepStatement(query);
      c.pst.setString(1, transactionID);
      c.pst.setString(2, userID);
      c.pst.setString(3, transactionDate);
      c.pst.executeUpdate();
      c.pst.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void InsertDetail() {
    String transactionID = GetTransactionID();
    String itemID = cartTable.getSelectionModel().getSelectedItem().getItemID();
    int quantity = cartTable.getSelectionModel().getSelectedItem().getQuantity();
    String query = "INSERT INTO transactiondetail VALUES(?, ?, ?)";
    try {
      c.pst = c.PrepStatement(query);
      c.pst.setString(1, transactionID);
      c.pst.setString(2, itemID);
      c.pst.setInt(3, quantity);
      c.pst.executeUpdate();
      c.pst.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Remove() {
    Alert removeSuccess = new Alert(AlertType.INFORMATION);
    removeSuccess.setTitle("SUCCESS");
    removeSuccess.setHeaderText(null);
    String itemID = cartTable.getSelectionModel().getSelectedItem().getItemID();
    String userID = GetUserID();
    String query = "DELETE FROM cart WHERE itemID = ? AND userID = ?";
    try {
      c.pst = c.PrepStatement(query);
      c.pst.setString(1, itemID);
      c.pst.setString(2, userID);
      c.pst.executeUpdate();
      c.pst.close();
      removeSuccess.setContentText("Successfully removed item from cart, click OK to continue");
      removeSuccess.showAndWait();
      removeBtn.setDisable(true);
      checkoutBtn.setDisable(true);
      RefreshTable();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String GetUserID() {
    String userID = "";
    String query = "SELECT userID FROM user WHERE username = '" + Main.usernameTF.getText() + "'";
    try {
      c.ExecuteQuery(query);
      while (c.rs.next()) {
        userID = c.rs.getString("userID");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return userID;
  }

  public String GenerateTransactionID() {
    String query = "SELECT MAX(transactionID) FROM transaction";
    String transactionID = "";
    int id = 0;
    try {
      c.ExecuteQuery(query);
      if (c.rs.next()) {
        transactionID = c.rs.getString(1);
        id = Integer.parseInt(transactionID.substring(2));
        id++;
        transactionID = "TR" + String.format("%03d", id);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return transactionID;
  }

  public String GetTransactionID() {
    String transactionID = "";
    String query = "SELECT transactionID FROM transaction WHERE userID = '" + GetUserID() + "'";
    try {
      c.ExecuteQuery(query);
      while (c.rs.next()) {
        transactionID = c.rs.getString("transactionID");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return transactionID;
  }

  public void RefreshTable() {
    cart.clear();
    Select();
    cartObs = FXCollections.observableArrayList(cart);
    cartTable.setItems(cartObs);
  }

  public void Clear() {
    cartTable.getSelectionModel().clearSelection();
    removeBtn.setDisable(true);
    checkoutBtn.setDisable(true);
  }

  public EventHandler<MouseEvent> TableHandler() {

    return new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        TableSelectionModel<Cart> cartSelectionModel = cartTable.getSelectionModel();
        cartSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        Cart c = cartSelectionModel.getSelectedItem();
        if (c != null) {
          removeBtn.setDisable(false);
          checkoutBtn.setDisable(false);
        }
      }
    };
  }

  public void ItemMarket() {
    cartItem = (Stage) bPane.getScene().getWindow();
    cartItem.close();
    itemMarket = new Stage();
    ItemMarket im = new ItemMarket();
    try {
      im.start(itemMarket);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void TransactionHistory() {
    cartItem = (Stage) bPane.getScene().getWindow();
    cartItem.close();
    transactionHistory = new Stage();
    TransactionHistory th = new TransactionHistory();
    try {
      th.start(transactionHistory);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Logout() {
    cartItem = (Stage) bPane.getScene().getWindow();
    cartItem.close();
    logout = new Stage();
    Main m = new Main();
    try {
      m.start(logout);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void start(Stage s) throws Exception {
    Init();
    Set();
    Handler();
    s.setScene(scene);
    s.setTitle("user page | cart item");
    s.setResizable(false);
    s.show();
  }

  @Override
  public void handle(ActionEvent e) {
    if (e.getSource() == itemMarketMenu) {
      ItemMarket();
    } else if (e.getSource() == transactionMenu) {
      TransactionHistory();
    } else if (e.getSource() == logOutMenu) {
      Logout();
    }

    if (e.getSource() == removeBtn) {
      Remove();
    } else if (e.getSource() == checkoutBtn) {
      CheckOut();
    }
  }
}
