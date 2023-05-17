package admin;

import main.Main;
import java.util.Vector;
import connection.Connect;
import data.admin.Transactions;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Transaction extends Application implements EventHandler<ActionEvent> {
  Connect c = Connect.getConnect();

  Stage manageUser, manageItem, transaction, logout;
  Scene scene;
  BorderPane bPane;
  MenuItem manageUserMenu, manageItemMenu, transactionMenu, logOutMenu;
  MenuBar menuBar;
  Menu menu;
  TableView<Transactions> transactionTable;
  ObservableList<Transactions> transactionObs;
  Vector<Transactions> transactions;

  @SuppressWarnings("unchecked")
  public void Init() {
    bPane = new BorderPane();
    transactions = new Vector<>();

    menuBar = new MenuBar();
    menu = new Menu("Menu");
    manageUserMenu = new MenuItem("Manage User");
    manageItemMenu = new MenuItem("Manage Item");
    transactionMenu = new MenuItem("Transaction");
    logOutMenu = new MenuItem("Logout");

    menu.getItems().addAll(manageUserMenu, manageItemMenu, transactionMenu, logOutMenu);
    menuBar.getMenus().add(menu);

    transactionTable = new TableView<Transactions>();

    TableColumn<Transactions, String> transactionIdCol = new TableColumn<Transactions, String>("transactionID");
    transactionIdCol.setCellValueFactory(new PropertyValueFactory<Transactions, String>("transactionID"));

    TableColumn<Transactions, String> buyerCol = new TableColumn<Transactions, String>("buyer");
    buyerCol.setCellValueFactory(new PropertyValueFactory<Transactions, String>("buyer"));

    TableColumn<Transactions, String> itemNameCol = new TableColumn<Transactions, String>("Item Name");
    itemNameCol.setCellValueFactory(new PropertyValueFactory<Transactions, String>("itemName"));

    TableColumn<Transactions, String> itemDescriptionCol = new TableColumn<Transactions, String>("Item Description");
    itemDescriptionCol.setCellValueFactory(new PropertyValueFactory<Transactions, String>("itemDescription"));

    TableColumn<Transactions, Integer> priceCol = new TableColumn<Transactions, Integer>("Price");
    priceCol.setCellValueFactory(new PropertyValueFactory<Transactions, Integer>("price"));

    TableColumn<Transactions, Integer> quantityCol = new TableColumn<Transactions, Integer>("Quantity");
    quantityCol.setCellValueFactory(new PropertyValueFactory<Transactions, Integer>("quantity"));

    TableColumn<Transactions, Integer> totalPriceCol = new TableColumn<Transactions, Integer>("Total Price");
    totalPriceCol.setCellValueFactory(new PropertyValueFactory<Transactions, Integer>("totalPrice"));

    TableColumn<Transactions, String> transactionDateCol = new TableColumn<Transactions, String>("transaction date");
    transactionDateCol.setCellValueFactory(new PropertyValueFactory<Transactions, String>("transactionDate"));

    transactionTable.getColumns().addAll(transactionIdCol, buyerCol, itemNameCol, itemDescriptionCol, priceCol,
        quantityCol, totalPriceCol, transactionDateCol);

    RefreshTable();

    scene = new Scene(bPane, 900, 600);
  }

  public void Set() {
    bPane.setTop(menuBar);
    bPane.setCenter(transactionTable);

    transactionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
  }

  public void Handler() {
    manageUserMenu.setOnAction(this);
    manageItemMenu.setOnAction(this);
    transactionMenu.setOnAction(this);
    logOutMenu.setOnAction(this);
  }

  public void Select() {
    String query = "SELECT transaction.transactionID, username AS buyer, ItemName, ItemDescription, price, transactiondetail.quantity, price * transactiondetail.quantity AS totalPrice, DATE_FORMAT(transactionDate, '%d %M %Y') AS transactionDate FROM transaction JOIN transactiondetail ON transactiondetail.transactionID = transaction.transactionID JOIN user ON user.userID = transaction.userID JOIN item ON item.itemID = transactiondetail.itemID";
    try {
      c.ExecuteQuery(query);
      while (c.rs.next()) {
        String transactionID = c.rs.getString("transactionID");
        String buyer = c.rs.getString("buyer");
        String itemName = c.rs.getString("ItemName");
        String itemDescription = c.rs.getString("ItemDescription");
        int price = c.rs.getInt("price");
        int quantity = c.rs.getInt("quantity");
        int totalPrice = c.rs.getInt("totalPrice");
        String transactionDate = c.rs.getString("transactionDate");
        transactions.add(new Transactions(transactionID, buyer, itemName, itemDescription, price, quantity, totalPrice,
            transactionDate));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void RefreshTable() {
    transactions.clear();
    Select();
    transactionObs = FXCollections.observableArrayList(transactions);
    transactionTable.setItems(transactionObs);
  }

  public void ManageUser() {
    transaction = (Stage) bPane.getScene().getWindow();
    transaction.close();
    manageUser = new Stage();
    ManageUser mu = new ManageUser();
    try {
      mu.start(manageUser);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void ManageItem() {
    transaction = (Stage) bPane.getScene().getWindow();
    transaction.close();
    manageItem = new Stage();
    ManageItem mi = new ManageItem();
    try {
      mi.start(manageItem);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Logout() {
    transaction = (Stage) bPane.getScene().getWindow();
    transaction.close();
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
    s.setResizable(false);
    s.setTitle("admin page | transaction");
    s.show();
  }

  @Override
  public void handle(ActionEvent e) {
    if (e.getSource() == manageUserMenu) {
      ManageUser();
    } else if (e.getSource() == manageItemMenu) {
      ManageItem();
    } else if (e.getSource() == logOutMenu) {
      Logout();
    }
  }
}
