package admin;

import main.Main;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class Admin extends Application implements EventHandler<ActionEvent> {

  Stage admin, manageUser, manageItem, transaction, logout;
  Scene scene;
  BorderPane bPane;
  Menu menu;
  MenuItem manageUserMenu, manageItemMenu, transactionMenu, logOutMenu;
  MenuBar menuBar;
  Text welcome;

  public void Init() {
    bPane = new BorderPane();
    menu = new Menu("Menu");
    manageUserMenu = new MenuItem("Manage User");
    manageItemMenu = new MenuItem("Manage Item");
    transactionMenu = new MenuItem("Transaction");
    logOutMenu = new MenuItem("Logout");

    menuBar = new MenuBar();
    menu.getItems().addAll(manageUserMenu, manageItemMenu, transactionMenu, logOutMenu);
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
    manageUserMenu.setOnAction(this);
    manageItemMenu.setOnAction(this);
    transactionMenu.setOnAction(this);
    logOutMenu.setOnAction(this);
  }

  public void ManageUser() {
    admin = (Stage) bPane.getScene().getWindow();
    admin.close();
    manageUser = new Stage();
    ManageUser mu = new ManageUser();
    try {
      mu.start(manageUser);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void ManageItem() {
    admin = (Stage) bPane.getScene().getWindow();
    admin.close();
    manageItem = new Stage();
    ManageItem mi = new ManageItem();
    try {
      mi.start(manageItem);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Transaction() {
    admin = (Stage) bPane.getScene().getWindow();
    admin.close();
    transaction = new Stage();
    Transaction t = new Transaction();
    try {
      t.start(transaction);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Logout() {
    admin = (Stage) bPane.getScene().getWindow();
    admin.close();
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
    s.setTitle("admin page");
    s.setScene(scene);
    s.setResizable(false);
    s.show();
  }

  @Override
  public void handle(ActionEvent e) {
    if (e.getSource() == manageUserMenu) {
      ManageUser();
    } else if (e.getSource() == manageItemMenu) {
      ManageItem();
    } else if (e.getSource() == transactionMenu) {
      Transaction();
    } else if (e.getSource() == logOutMenu) {
      Logout();
    }
  }
}
