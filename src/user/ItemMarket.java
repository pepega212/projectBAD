package user;

import main.Main;
import java.util.Vector;
import connection.Connect;
import data.admin.Items;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ItemMarket extends Application implements EventHandler<ActionEvent> {
  Connect c = Connect.getConnect();

  Stage itemMarket, cartItem, transactionHistory, logout;
  Scene scene;
  BorderPane bPane;
  GridPane gPane;
  FlowPane fPane;
  Text itemIdLabel, itemNameLabel, itemDescLabel, priceLabel, quantityLabel;
  TextField itemIdTF, itemNameTF, itemDescTF;
  Spinner<Integer> priceSpin, quantitySpin;
  Button clearBtn, addBtn;
  Menu menu;
  MenuItem itemMarketMenu, cartItemMenu, transactionMenu, logOutMenu;
  MenuBar menuBar;
  Vector<Items> items;
  TableView<Items> itemTable;
  ObservableList<Items> itemObs;

  public void Init() {
    bPane = new BorderPane();
    gPane = new GridPane();
    fPane = new FlowPane();
    items = new Vector<>();

    itemIdLabel = new Text("itemID");
    itemNameLabel = new Text("item name");
    itemDescLabel = new Text("item description");
    priceLabel = new Text("price");
    quantityLabel = new Text("quantity");

    itemIdTF = new TextField();
    itemNameTF = new TextField();
    itemDescTF = new TextField();

    priceSpin = new Spinner<Integer>(0, Integer.MAX_VALUE, 0);
    quantitySpin = new Spinner<Integer>(0, Integer.MAX_VALUE, 0);

    clearBtn = new Button("Clear Form");
    addBtn = new Button("Add To Cart");

    fPane.getChildren().addAll(clearBtn, addBtn);

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
    bPane.setLeft(itemTable);
    bPane.setCenter(gPane);

    gPane.setHgap(10);
    gPane.setVgap(10);
    gPane.setPadding(new Insets(30, 55, 30, 55));
    itemTable.setPrefWidth(550);
    itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    fPane.setHgap(10);
    fPane.setVgap(10);
    fPane.setAlignment(Pos.CENTER);

    clearBtn.setPrefWidth(100);
    addBtn.setPrefWidth(100);

    clearBtn.setDisable(true);
    addBtn.setDisable(true);

    itemIdTF.setDisable(true);
    itemNameTF.setDisable(true);
    itemDescTF.setDisable(true);
    priceSpin.setDisable(true);
  }

  public void Build() {
    gPane.add(itemIdLabel, 0, 0);
    gPane.add(itemIdTF, 1, 0);
    gPane.add(itemNameLabel, 0, 1);
    gPane.add(itemNameTF, 1, 1);
    gPane.add(itemDescLabel, 0, 2);
    gPane.add(itemDescTF, 1, 2);
    gPane.add(priceLabel, 0, 3);
    gPane.add(priceSpin, 1, 3);
    gPane.add(quantityLabel, 0, 4);
    gPane.add(quantitySpin, 1, 4);
    gPane.add(fPane, 0, 5, 2, 1);
  }

  public void Handler() {
    itemMarketMenu.setOnAction(this);
    cartItemMenu.setOnAction(this);
    transactionMenu.setOnAction(this);
    logOutMenu.setOnAction(this);

    clearBtn.setOnAction(this);
    addBtn.setOnAction(this);

    itemTable.setOnMouseClicked(TableHandler());
  }

  @SuppressWarnings("unchecked")
  public void SetTable() {
    itemTable = new TableView<Items>();

    TableColumn<Items, String> itemIDCol = new TableColumn<Items, String>("ItemID");
    itemIDCol.setCellValueFactory(new PropertyValueFactory<Items, String>("itemID"));

    TableColumn<Items, String> itemNameCol = new TableColumn<Items, String>("Item Name");
    itemNameCol.setCellValueFactory(new PropertyValueFactory<Items, String>("itemName"));

    TableColumn<Items, String> itemDescCol = new TableColumn<Items, String>("Item Description");
    itemDescCol.setCellValueFactory(new PropertyValueFactory<Items, String>("itemDescription"));

    TableColumn<Items, Integer> priceCol = new TableColumn<Items, Integer>("Price");
    priceCol.setCellValueFactory(new PropertyValueFactory<Items, Integer>("price"));

    TableColumn<Items, Integer> quantityCol = new TableColumn<Items, Integer>("Quantity");
    quantityCol.setCellValueFactory(new PropertyValueFactory<Items, Integer>("quantity"));

    itemTable.getColumns().addAll(itemIDCol, itemNameCol, itemDescCol, priceCol, quantityCol);
    RefreshTable();
  }

  public void Select() {
    String query = "SELECT * FROM item WHERE quantity > 0";
    try {
      c.ExecuteQuery(query);
      while (c.rs.next()) {
        String itemID = c.rs.getString("itemID");
        String itemName = c.rs.getString("itemName");
        String itemDescription = c.rs.getString("itemDescription");
        int price = c.rs.getInt("price");
        int quantity = c.rs.getInt("quantity");
        items.add(new Items(itemID, itemName, itemDescription, price, quantity));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Validate() {
    int quantity = quantitySpin.getValue();
    Alert addFailed = new Alert(AlertType.ERROR);
    Alert addSuccess = new Alert(AlertType.INFORMATION);
    addFailed.setTitle("ERROR");
    addFailed.setHeaderText(null);

    if (quantity <= 0) {
      addFailed.setContentText("Quantity must be greater than 0!");
      addFailed.showAndWait();
    } else if (quantity > itemTable.getSelectionModel().getSelectedItem().getQuantity()) {
      addFailed.setContentText("The Quantity you input cannot exceed the existing quantity!");
      addFailed.showAndWait();
    } else {
      addSuccess.setTitle("SUCCESS");
      addSuccess.setHeaderText(null);
      addSuccess.setContentText("Suceessfully added item to cart, Click OK to continue!");
      addSuccess.showAndWait();
      CheckCart();
      ClearField();
    }
  }

  public void UpdateCart() {
    String userID = GetUserID();
    String itemID = itemTable.getSelectionModel().getSelectedItem().getItemID();
    int quantity = quantitySpin.getValue();
    String query = "UPDATE cart SET quantity = quantity + ? WHERE userID = ? AND itemID = ?";
    try {
      c.pst = c.PrepStatement(query);
      c.pst.setInt(1, quantity);
      c.pst.setString(2, userID);
      c.pst.setString(3, itemID);
      c.pst.executeUpdate();
      c.pst.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean CheckCart() {
    boolean check = false;
    String userID = GetUserID();
    String itemID = itemTable.getSelectionModel().getSelectedItem().getItemID();
    String query = "SELECT * FROM cart WHERE userID = '" + userID + "' AND itemID = '" + itemID + "'";
    try {
      c.ExecuteQuery(query);
      if (c.rs.next()) {
        UpdateCart();
      } else {
        AddToCart();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return check;
  }

  public void AddToCart() {
    String userID = GetUserID();
    String itemID = itemTable.getSelectionModel().getSelectedItem().getItemID();
    int quantity = quantitySpin.getValue();
    String query = "INSERT INTO cart VALUES (?, ?, ?)";
    try {
      c.pst = c.PrepStatement(query);
      c.pst.setString(1, userID);
      c.pst.setString(2, itemID);
      c.pst.setInt(3, quantity);
      c.pst.executeUpdate();
      c.pst.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String GetUserID() {
    String query = "SELECT userID FROM user WHERE username = '" + Main.usernameTF.getText() + "'";
    String userID = "";
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

  public void ClearField() {
    itemIdTF.clear();
    itemNameTF.clear();
    itemDescTF.clear();
    priceSpin.getValueFactory().setValue(0);
    quantitySpin.getValueFactory().setValue(0);
    itemTable.getSelectionModel().clearSelection();
    clearBtn.setDisable(true);
    addBtn.setDisable(true);
  }

  public void RefreshTable() {
    items.clear();
    Select();
    itemObs = FXCollections.observableArrayList(items);
    itemTable.setItems(itemObs);
  }

  public EventHandler<MouseEvent> TableHandler() {

    return new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        TableSelectionModel<Items> itemSelectionModel = itemTable.getSelectionModel();
        itemSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        Items item = itemTable.getSelectionModel().getSelectedItem();
        if (item != null) {
          itemIdTF.setText(item.getItemID());
          itemNameTF.setText(item.getItemName());
          itemDescTF.setText(item.getItemDescription());
          priceSpin.getValueFactory().setValue(item.getPrice());
          clearBtn.setDisable(false);
          addBtn.setDisable(false);
        }
      }
    };
  }

  public void CartItem() {
    itemMarket = (Stage) bPane.getScene().getWindow();
    itemMarket.close();
    cartItem = new Stage();
    CartItem ci = new CartItem();
    try {
      ci.start(cartItem);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void TransactionHistory() {
    itemMarket = (Stage) bPane.getScene().getWindow();
    itemMarket.close();
    transactionHistory = new Stage();
    TransactionHistory th = new TransactionHistory();
    try {
      th.start(transactionHistory);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Logout() {
    itemMarket = (Stage) bPane.getScene().getWindow();
    itemMarket.close();
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
    Build();
    Handler();
    s.setScene(scene);
    s.setTitle("user page | item market");
    s.setResizable(false);
    s.show();
  }

  @Override
  public void handle(ActionEvent e) {
    if (e.getSource() == cartItemMenu) {
      CartItem();
    } else if (e.getSource() == transactionMenu) {
      TransactionHistory();
    } else if (e.getSource() == logOutMenu) {
      Logout();
    }

    if (e.getSource() == clearBtn) {
      ClearField();
    } else if (e.getSource() == addBtn) {
      Validate();
    }
  }
}
