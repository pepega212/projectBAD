package admin;

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

public class ManageItem extends Application implements EventHandler<ActionEvent> {
  Connect c = Connect.getConnect();

  Stage manageUser, manageItem, transaction, logout;
  Scene scene;
  BorderPane bPane;
  GridPane gPane;
  FlowPane fPane;
  Text itemIdLabel, itemNameLabel, itemDescLabel, priceLabel, quantityLabel;
  TextField itemIdTF, itemNameTF, itemDescTF;
  Spinner<Integer> priceSpin, quantitySpin;
  Button insertBtn, updateBtn, deleteBtn, clearBtn;
  MenuItem manageUserMenu, manageItemMenu, transactionMenu, logOutMenu;
  MenuBar menuBar;
  Menu menu;
  Vector<Items> items;
  TableView<Items> itemTable;
  ObservableList<Items> itemObs;

  public void Init() {
    bPane = new BorderPane();
    gPane = new GridPane();
    fPane = new FlowPane();
    items = new Vector<Items>();

    itemIdLabel = new Text("itemID");
    itemNameLabel = new Text("item name");
    itemDescLabel = new Text("item description");
    priceLabel = new Text("price");
    quantityLabel = new Text("quantity");

    itemIdTF = new TextField(GenerateItemId());
    itemNameTF = new TextField();
    itemDescTF = new TextField();

    priceSpin = new Spinner<Integer>(0, Integer.MAX_VALUE, 0, 1000);
    quantitySpin = new Spinner<Integer>(0, Integer.MAX_VALUE, 0);

    insertBtn = new Button("Insert Item");
    updateBtn = new Button("Update Item");
    deleteBtn = new Button("Delete Item");
    clearBtn = new Button("Clear Form");

    fPane.getChildren().addAll(insertBtn, updateBtn, deleteBtn, clearBtn);

    menuBar = new MenuBar();
    menu = new Menu("Menu");
    manageUserMenu = new MenuItem("Manage User");
    manageItemMenu = new MenuItem("Manage Item");
    transactionMenu = new MenuItem("Transaction");
    logOutMenu = new MenuItem("Logout");

    menu.getItems().addAll(manageUserMenu, manageItemMenu, transactionMenu, logOutMenu);
    menuBar.getMenus().add(menu);

    SetTable();

    scene = new Scene(bPane, 900, 600);
  }

  public void Set() {
    bPane.setTop(menuBar);
    bPane.setLeft(itemTable);
    bPane.setCenter(gPane);

    itemTable.setPrefWidth(500);
    itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    gPane.setHgap(10);
    gPane.setVgap(10);
    gPane.setPadding(new Insets(20, 40, 20, 40));

    fPane.setHgap(10);
    fPane.setVgap(10);
    fPane.setAlignment(Pos.TOP_CENTER);

    insertBtn.setPrefWidth(100);
    updateBtn.setPrefWidth(100);
    deleteBtn.setPrefWidth(100);
    clearBtn.setPrefWidth(300);

    itemIdTF.setDisable(true);
    updateBtn.setDisable(true);
    deleteBtn.setDisable(true);
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
    manageUserMenu.setOnAction(this);
    manageItemMenu.setOnAction(this);
    transactionMenu.setOnAction(this);
    logOutMenu.setOnAction(this);

    insertBtn.setOnAction(this);
    updateBtn.setOnAction(this);
    deleteBtn.setOnAction(this);
    clearBtn.setOnAction(this);

    itemTable.setOnMouseClicked(TableHandler());
  }

  public String GenerateItemId() {
    String query = "SELECT MAX(itemID) FROM item";
    String itemId = "";
    int id = 0;
    try {
      c.ExecuteQuery(query);
      if (c.rs.next()) {
        itemId = c.rs.getString(1);
      }
      id = Integer.parseInt(itemId.substring(2));
      id++;
      itemId = "IT" + String.format("%03d", id);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return itemId;
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
    String query = "SELECT * FROM item";
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

  public void Insert(String itemID, String itemName, String itemDescription, int price, int quantity) {
    String query = "INSERT INTO item VALUES(? ,? ,? ,? ,?)";
    try {
      c.pst = c.PrepStatement(query);
      c.pst.setString(1, itemID);
      c.pst.setString(2, itemName);
      c.pst.setString(3, itemDescription);
      c.pst.setInt(4, price);
      c.pst.setInt(5, quantity);
      c.pst.executeUpdate();
      c.pst.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void ValidateInsert() {
    Alert insertFailed = new Alert(AlertType.ERROR);
    Alert insertSuccess = new Alert(AlertType.INFORMATION);
    insertFailed.setTitle("ERROR");
    insertFailed.setHeaderText(null);

    String itemID = GenerateItemId();
    String itemName = itemNameTF.getText();
    String itemDescription = itemDescTF.getText();
    int price = priceSpin.getValue();
    int quantity = quantitySpin.getValue();

    if (itemName.length() < 5 || itemName.length() > 100) {
      insertFailed.setContentText("Item Name must be between 5 and 100 characters!");
      insertFailed.showAndWait();
    } else if (itemName.split(" ").length < 2 || !itemName.contains(":")) {
      insertFailed.setContentText(
          "Item Name must consist of at least 2 words containing\nthe game name and item name and separated by (:)! \n\n ex -> dota : iceforged set");
      insertFailed.showAndWait();
    } else if (itemDescription.length() < 10 || itemDescription.length() > 200) {
      insertFailed.setContentText("Item Description must be between 10 - 200 characters!");
      insertFailed.showAndWait();
    } else if (price <= 0) {
      insertFailed.setContentText("Price must be greater than 0!");
      insertFailed.showAndWait();
    } else if (quantity <= 0) {
      insertFailed.setContentText("Quantity must be greater than 0!");
      insertFailed.showAndWait();
    } else {
      insertSuccess.setTitle("SUCCESS");
      insertSuccess.setHeaderText(null);
      insertSuccess.setContentText("Add Item Successfully, Click OK to continue!");
      insertSuccess.showAndWait();
      Insert(itemID, itemName, itemDescription, price, quantity);
      RefreshTable();
      ClearField();
    }
  }

  public void Update(String itemID, String itemName, String itemDescription, int price, int quantity) {
    String query = "UPDATE item SET itemName = ?, itemDescription = ?, price = ?, quantity = ? WHERE itemID = ?";
    try {
      c.pst = c.PrepStatement(query);
      c.pst.setString(1, itemName);
      c.pst.setString(2, itemDescription);
      c.pst.setInt(3, price);
      c.pst.setInt(4, quantity);
      c.pst.setString(5, itemID);
      c.pst.executeUpdate();
      c.pst.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void ValidateUpdate() {
    Alert updateFailed = new Alert(AlertType.ERROR);
    Alert updateSuccess = new Alert(AlertType.INFORMATION);
    updateFailed.setTitle("ERROR");
    updateFailed.setHeaderText(null);
    String itemID = itemIdTF.getText();
    String itemName = itemNameTF.getText();
    String itemDescription = itemDescTF.getText();
    int price = priceSpin.getValue();
    int quantity = quantitySpin.getValue();

    if (itemTable.getSelectionModel().isEmpty()) {
      insertBtn.setDisable(false);
      updateBtn.setDisable(true);
      deleteBtn.setDisable(true);
    } else if (itemName.length() < 5 || itemName.length() > 100) {
      updateFailed.setContentText("Item Name must be between 5 and 100 characters!");
      updateFailed.showAndWait();
    } else if (itemName.split(" ").length < 2 || !itemName.contains(":")) {
      updateFailed.setContentText(
          "Item Name must consist of at least 2 words containing\nthe game name and item name and separated by (:)! \n\n ex -> dota : iceforged set");
      updateFailed.showAndWait();
    } else if (itemDescription.length() < 10 || itemDescription.length() > 200) {
      updateFailed.setContentText("Item Description must be between 10 - 200 characters!");
      updateFailed.showAndWait();
    } else if (price <= 0) {
      updateFailed.setContentText("Price must be greater than 0!");
      updateFailed.showAndWait();
    } else if (quantity <= 0) {
      updateFailed.setContentText("Quantity must be greater than 0!");
      updateFailed.showAndWait();
    } else {
      updateSuccess.setTitle("UPDATE SUCCESS");
      updateSuccess.setHeaderText(null);
      updateSuccess.setContentText("Click OK to continue!");
      updateSuccess.showAndWait();
      Update(itemID, itemName, itemDescription, price, quantity);
      RefreshTable();
      ClearField();
    }
  }

  public void Delete() {
    String query = "DELETE FROM item WHERE itemID = ?";
    try {
      c.pst = c.PrepStatement(query);
      c.pst.setString(1, itemIdTF.getText());
      c.pst.executeUpdate();
      c.pst.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void ValidateDelete() {
    Alert deleteSuccess = new Alert(AlertType.INFORMATION);

    deleteSuccess.setTitle("DELETE SUCCESS");
    deleteSuccess.setHeaderText(null);
    deleteSuccess.setContentText("Click OK to continue!");
    deleteSuccess.showAndWait();
    Delete();
    RefreshTable();
    ClearField();
  }

  public void ClearField() {
    itemIdTF.setText(GenerateItemId());
    itemNameTF.setText("");
    itemDescTF.setText("");
    priceSpin.getValueFactory().setValue(0);
    quantitySpin.getValueFactory().setValue(0);
    itemTable.getSelectionModel().clearSelection();
    insertBtn.setDisable(false);
    updateBtn.setDisable(true);
    deleteBtn.setDisable(true);
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
      public void handle(MouseEvent arg0) {
        TableSelectionModel<Items> itemSelectionModel = itemTable.getSelectionModel();
        itemSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        Items item = itemTable.getSelectionModel().getSelectedItem();
        if (item != null) {
          itemIdTF.setText(item.getItemID());
          itemNameTF.setText(item.getItemName());
          itemDescTF.setText(item.getItemDescription());
          priceSpin.getValueFactory().setValue(item.getPrice());
          quantitySpin.getValueFactory().setValue(item.getQuantity());
          insertBtn.setDisable(true);
          updateBtn.setDisable(false);
          deleteBtn.setDisable(false);
        }
      }
    };
  }

  public void ManageUser() {
    manageItem = (Stage) bPane.getScene().getWindow();
    manageItem.close();
    manageUser = new Stage();
    ManageUser mu = new ManageUser();
    try {
      mu.start(manageUser);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Transaction() {
    manageItem = (Stage) bPane.getScene().getWindow();
    manageItem.close();
    transaction = new Stage();
    Transaction t = new Transaction();
    try {
      t.start(transaction);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Logout() {
    manageItem = (Stage) bPane.getScene().getWindow();
    manageItem.close();
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
    s.setTitle("admin page | manage item");
    s.setResizable(false);
    s.show();
  }

  @Override
  public void handle(ActionEvent e) {
    if (e.getSource() == manageUserMenu) {
      ManageUser();
    } else if (e.getSource() == transactionMenu) {
      Transaction();
    } else if (e.getSource() == logOutMenu) {
      Logout();
    }

    if (e.getSource() == insertBtn) {
      ValidateInsert();
    } else if (e.getSource() == updateBtn) {
      ValidateUpdate();
    } else if (e.getSource() == deleteBtn) {
      ValidateDelete();
    } else if (e.getSource() == clearBtn) {
      ClearField();
    }
  }
}
