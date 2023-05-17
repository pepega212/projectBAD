package user;

import main.Main;
import java.util.Vector;
import data.user.History;
import connection.Connect;
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

public class TransactionHistory extends Application implements EventHandler<ActionEvent> {
  Connect c = Connect.getConnect();

  Stage itemMarket, cartItem, transactionHistory, logout;
  Scene scene;
  BorderPane bPane;
  Menu menu;
  MenuItem itemMarketMenu, cartItemMenu, transactionMenu, logOutMenu;
  MenuBar menuBar;
  TableView<History> historyTable;
  Vector<History> history;
  ObservableList<History> historyObs;

  @SuppressWarnings("unchecked")
  public void Init() {
    bPane = new BorderPane();
    history = new Vector<>();

    menu = new Menu("Menu");
    itemMarketMenu = new MenuItem("Item Market");
    cartItemMenu = new MenuItem("Cart Item");
    transactionMenu = new MenuItem("Transaction History");
    logOutMenu = new MenuItem("logout");
    menu.getItems().addAll(itemMarketMenu, cartItemMenu, transactionMenu, logOutMenu);
    menuBar = new MenuBar();
    menuBar.getMenus().add(menu);

    historyTable = new TableView<History>();

    TableColumn<History, String> transactionIdCol = new TableColumn<History, String>("TransactionID");
    transactionIdCol.setCellValueFactory(new PropertyValueFactory<History, String>("transactionID"));

    TableColumn<History, String> iteNameCol = new TableColumn<History, String>("Item Name");
    iteNameCol.setCellValueFactory(new PropertyValueFactory<History, String>("itemName"));

    TableColumn<History, String> itemDescriptionCol = new TableColumn<History, String>("Item Description");
    itemDescriptionCol.setCellValueFactory(new PropertyValueFactory<History, String>("itemDescription"));

    TableColumn<History, Integer> priceCol = new TableColumn<History, Integer>("Price");
    priceCol.setCellValueFactory(new PropertyValueFactory<History, Integer>("price"));

    TableColumn<History, Integer> quantityCol = new TableColumn<History, Integer>("Quantity");
    quantityCol.setCellValueFactory(new PropertyValueFactory<History, Integer>("quantity"));

    TableColumn<History, Integer> totalPriceCol = new TableColumn<History, Integer>("Total Price");
    totalPriceCol.setCellValueFactory(new PropertyValueFactory<History, Integer>("totalPrice"));

    TableColumn<History, String> transactionDateCol = new TableColumn<History, String>("transaction date");
    transactionDateCol.setCellValueFactory(new PropertyValueFactory<History, String>("transactionDate"));

    historyTable.getColumns().addAll(transactionIdCol, iteNameCol, itemDescriptionCol, priceCol, quantityCol,
        totalPriceCol, transactionDateCol);
    RefreshTable();

    scene = new Scene(bPane, 900, 600);
  }

  public void Set() {
    bPane.setTop(menuBar);
    bPane.setCenter(historyTable);
    historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
  }

  public void Handler() {
    itemMarketMenu.setOnAction(this);
    cartItemMenu.setOnAction(this);
    transactionMenu.setOnAction(this);
    logOutMenu.setOnAction(this);
  }

  public void Select() {
    String query = "SELECT transaction.transactionID, itemName, itemDescription, price, transactiondetail.quantity,price * transactiondetail.quantity AS totalPrice, DATE_FORMAT(transactionDate, '%d %M %Y') AS transactionDate FROM transactionDetail JOIN transaction ON transactiondetail.transactionID = transaction.transactionID JOIN item ON item.itemID = transactiondetail.itemID JOIN user ON user.userID = transaction.userID WHERE username LIKE '"
        + Main.usernameTF.getText() + "'";
    try {
      c.ExecuteQuery(query);
      while (c.rs.next()) {
        String transactionID = c.rs.getString("transactionID");
        String itemName = c.rs.getString("itemName");
        String itemDescription = c.rs.getString("itemDescription");
        int price = c.rs.getInt("price");
        int quantity = c.rs.getInt("quantity");
        int totalPrice = c.rs.getInt("totalPrice");
        String transactionDate = c.rs.getString("transactionDate");
        history
            .add(new History(transactionID, itemName, itemDescription, price, quantity, totalPrice, transactionDate));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void RefreshTable() {
    history.clear();
    Select();
    historyObs = FXCollections.observableArrayList(history);
    historyTable.setItems(historyObs);
  }

  public void CartItem() {
    transactionHistory = (Stage) bPane.getScene().getWindow();
    transactionHistory.close();
    cartItem = new Stage();
    CartItem ci = new CartItem();
    try {
      ci.start(cartItem);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void ItemMarket() {
    transactionHistory = (Stage) bPane.getScene().getWindow();
    transactionHistory.close();
    itemMarket = new Stage();
    ItemMarket im = new ItemMarket();
    try {
      im.start(itemMarket);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Logout() {
    transactionHistory = (Stage) bPane.getScene().getWindow();
    transactionHistory.close();
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
    s.setTitle("user page | transaction history");
    s.setResizable(false);
    s.show();
  }

  @Override
  public void handle(ActionEvent e) {
    if (e.getSource() == itemMarketMenu) {
      ItemMarket();
    } else if (e.getSource() == cartItemMenu) {
      CartItem();
    } else if (e.getSource() == logOutMenu) {
      Logout();
    }
  }
}
