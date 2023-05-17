package user;

import main.Main;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class User extends Application implements EventHandler<ActionEvent> {

  Stage user, itemMarket, cartItem, transactionHistory, logout;
  Scene scene;
  BorderPane bPane;
  Menu menu;
  MenuItem itemMarketMenu, cartItemMenu, transactionMenu, logOutMenu;
  MenuBar menuBar;
  Text welcome;

  public void Init() {
    bPane = new BorderPane();
    menu = new Menu("Menu");
    itemMarketMenu = new MenuItem("Item Market");
    cartItemMenu = new MenuItem("Cart Item");
    transactionMenu = new MenuItem("Transaction History");
    logOutMenu = new MenuItem("logout");
    menuBar = new MenuBar();
    menu.getItems().addAll(itemMarketMenu, cartItemMenu, transactionMenu, logOutMenu);
    menuBar.getMenus().add(menu);

    welcome = new Text(Welcome());

    scene = new Scene(bPane, 900, 600);
  }

  public void Set() {

    bPane.setTop(menuBar);
    bPane.setCenter(welcome);

    welcome.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
  }

  public String Welcome() {
    return "Welcome " + Main.usernameTF.getText();
  }

  public void Handler() {
    itemMarketMenu.setOnAction(this);
    cartItemMenu.setOnAction(this);
    transactionMenu.setOnAction(this);
    logOutMenu.setOnAction(this);
  }

  public void ItemMarket() {
    user = (Stage) bPane.getScene().getWindow();
    user.close();
    itemMarket = new Stage();
    ItemMarket im = new ItemMarket();
    try {
      im.start(itemMarket);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void CartItem() {
    user = (Stage) bPane.getScene().getWindow();
    user.close();
    cartItem = new Stage();
    CartItem ci = new CartItem();
    try {
      ci.start(cartItem);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void TransactionHistory() {
    user = (Stage) bPane.getScene().getWindow();
    user.close();
    transactionHistory = new Stage();
    TransactionHistory th = new TransactionHistory();
    try {
      th.start(transactionHistory);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Logout() {
    user = (Stage) bPane.getScene().getWindow();
    user.close();
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
    s.setTitle("user page");
    s.setResizable(false);
    s.show();
  }

  @Override
  public void handle(ActionEvent e) {

    if (e.getSource() == itemMarketMenu) {
      ItemMarket();
    } else if (e.getSource() == cartItemMenu) {
      CartItem();
    } else if (e.getSource() == transactionMenu) {
      TransactionHistory();
    } else if (e.getSource() == logOutMenu) {
      Logout();
    }
  }
}
