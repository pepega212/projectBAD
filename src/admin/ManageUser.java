package admin;

import main.Main;
import java.util.Vector;
import connection.Connect;
import data.admin.Users;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ManageUser extends Application implements EventHandler<ActionEvent> {
  Connect c = Connect.getConnect();

  Stage manageUser, manageItem, transaction, logout;
  Scene scene;
  BorderPane bPane;
  GridPane gPane;
  FlowPane fPaneGender, fPaneBtn;
  Text userIdLabel, usernameLabel, passwordLabel, emailLabel, phoneLabel, ageLabel, genderLabel;
  TextField userIdTF, usernameTF, emailTF, phoneTF;
  PasswordField passwordPF;
  Spinner<Integer> ageSpin;
  RadioButton male, female;
  ToggleGroup genderGroup;
  Button updateBtn, deleteBtn;
  TableView<Users> userTable;
  MenuItem manageUserMenu, manageItemMenu, transactionMenu, logOutMenu;
  MenuBar menuBar;
  Menu menu;
  Vector<Users> users;
  ObservableList<Users> userObs;

  public void Init() {
    bPane = new BorderPane();
    gPane = new GridPane();
    fPaneGender = new FlowPane();
    fPaneBtn = new FlowPane();
    users = new Vector<>();

    userIdLabel = new Text("User ID");
    usernameLabel = new Text("Username");
    passwordLabel = new Text("Password");
    emailLabel = new Text("Email");
    phoneLabel = new Text("Phone Number");
    ageLabel = new Text("Age");
    genderLabel = new Text("Gender");

    menuBar = new MenuBar();
    manageUserMenu = new MenuItem("Manage User");
    manageItemMenu = new MenuItem("Manage Item");
    transactionMenu = new MenuItem("Transaction");
    logOutMenu = new MenuItem("Logout");
    menu = new Menu("Menu");
    menu.getItems().addAll(manageUserMenu, manageItemMenu, transactionMenu, logOutMenu);
    menuBar.getMenus().add(menu);

    userIdTF = new TextField();
    usernameTF = new TextField();
    passwordPF = new PasswordField();
    emailTF = new TextField();
    phoneTF = new TextField();

    ageSpin = new Spinner<Integer>(15, 70, 16);

    male = new RadioButton("Male");
    female = new RadioButton("Female");

    genderGroup = new ToggleGroup();
    male.setToggleGroup(genderGroup);
    female.setToggleGroup(genderGroup);
    fPaneGender.getChildren().addAll(male, female);

    updateBtn = new Button("Update User");
    deleteBtn = new Button("Delete User");
    fPaneBtn.getChildren().addAll(updateBtn, deleteBtn);

    SetTable();

    scene = new Scene(bPane, 900, 600);
  }

  public void Set() {
    bPane.setTop(menuBar);
    bPane.setLeft(userTable);
    bPane.setCenter(gPane);

    userTable.setPrefWidth(600);
    userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    gPane.setHgap(10);
    gPane.setVgap(10);
    gPane.setPadding(new Insets(20));

    fPaneGender.setHgap(10);
    fPaneGender.setVgap(10);

    fPaneBtn.setHgap(10);
    fPaneBtn.setVgap(10);

    updateBtn.setPrefWidth(125);
    deleteBtn.setPrefWidth(125);

    userIdTF.setDisable(true);
    usernameTF.setDisable(true);
    passwordPF.setDisable(true);
  }

  public void Build() {
    gPane.add(userIdLabel, 0, 0);
    gPane.add(userIdTF, 1, 0);
    gPane.add(usernameLabel, 0, 1);
    gPane.add(usernameTF, 1, 1);
    gPane.add(passwordLabel, 0, 2);
    gPane.add(passwordPF, 1, 2);
    gPane.add(emailLabel, 0, 3);
    gPane.add(emailTF, 1, 3);
    gPane.add(phoneLabel, 0, 4);
    gPane.add(phoneTF, 1, 4);
    gPane.add(ageLabel, 0, 5);
    gPane.add(ageSpin, 1, 5);
    gPane.add(genderLabel, 0, 6);
    gPane.add(fPaneGender, 1, 6);
    gPane.add(fPaneBtn, 0, 7, 2, 1);
  }

  public void Handler() {
    manageUserMenu.setOnAction(this);
    manageItemMenu.setOnAction(this);
    transactionMenu.setOnAction(this);
    logOutMenu.setOnAction(this);

    updateBtn.setOnAction(this);
    deleteBtn.setOnAction(this);

    userTable.setOnMouseClicked(TableHandler());
  }

  @SuppressWarnings("unchecked")
  public void SetTable() {
    userTable = new TableView<Users>();

    TableColumn<Users, String> userIdCol = new TableColumn<Users, String>("userID");
    userIdCol.setCellValueFactory(new PropertyValueFactory<Users, String>("userID"));

    TableColumn<Users, String> usernameCol = new TableColumn<Users, String>("username");
    usernameCol.setCellValueFactory(new PropertyValueFactory<Users, String>("username"));

    TableColumn<Users, String> passwordCol = new TableColumn<Users, String>("password");
    passwordCol.setCellValueFactory(new PropertyValueFactory<Users, String>("password"));

    TableColumn<Users, String> emailCol = new TableColumn<Users, String>("email");
    emailCol.setCellValueFactory(new PropertyValueFactory<Users, String>("email"));

    TableColumn<Users, String> phoneCol = new TableColumn<Users, String>("phone number");
    phoneCol.setCellValueFactory(new PropertyValueFactory<Users, String>("phoneNumber"));

    TableColumn<Users, Integer> ageCol = new TableColumn<Users, Integer>("age");
    ageCol.setCellValueFactory(new PropertyValueFactory<Users, Integer>("age"));

    TableColumn<Users, String> genderCol = new TableColumn<Users, String>("gender");
    genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

    userTable.getColumns().addAll(userIdCol, usernameCol, passwordCol, emailCol, phoneCol, ageCol, genderCol);

    RefreshTable();
  }

  public void Select() {
    String query = "SELECT userID, username, password, email, phoneNumber, age, gender FROM user WHERE role = 'user'";
    try {
      c.ExecuteQuery(query);
      while (c.rs.next()) {
        String userID = c.rs.getString("userID");
        String username = c.rs.getString("username");
        String password = c.rs.getString("password");
        String email = c.rs.getString("email");
        String phoneNumber = c.rs.getString("phoneNumber");
        int age = c.rs.getInt("age");
        String gender = c.rs.getString("gender");
        users.add(new Users(userID, username, password, email, phoneNumber, age, gender));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Update(String email, String phone, int age, String gender) {
    String query = "UPDATE user SET email = ? , phoneNumber = ? , age = ?, gender = ? WHERE userID = ?";
    try {
      c.pst = c.PrepStatement(query);
      c.pst.setString(1, email);
      c.pst.setString(2, phone);
      c.pst.setInt(3, age);
      c.pst.setString(4, gender);
      c.pst.setString(5, userIdTF.getText());
      c.pst.executeUpdate();
      c.pst.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void ValidateUpdate() {
    Alert updateSuccess = new Alert(AlertType.INFORMATION);
    Alert updateFailed = new Alert(AlertType.ERROR);
    updateFailed.setTitle("UPDATE FAILED");
    updateFailed.setHeaderText(null);

    String email = emailTF.getText();
    String phone = phoneTF.getText();
    int age = ageSpin.getValue();
    String gender;

    if (male.isSelected()) {
      gender = male.getText();
    } else {
      gender = female.getText();
    }

    if (userTable.getSelectionModel().isEmpty()) {
      updateFailed.setContentText("Please select the data you want to update!");
      updateFailed.showAndWait();
    } else {
      if (!email.contains("@") || email.indexOf("@") == 0 || !email.endsWith(".com")) {
        updateFailed.setContentText(
            "email must consist of @ character, @ characters must not be \nin front and must end with .com!");
        updateFailed.showAndWait();
      } else if (phone.length() < 9 || phone.length() > 12) {
        updateFailed.setContentText(
            "Phone number must be between 9 and 12 characters!");
        updateFailed.showAndWait();
      } else if (age < 17 || age > 60) {
        updateFailed.setContentText(
            "AAge range must be between 17 - 60!");
        updateFailed.showAndWait();
      } else if (genderGroup.getSelectedToggle() == null) {
        updateFailed.setContentText("Gender must be selected, either 'Male' or 'Female'!");
        updateFailed.showAndWait();
      } else {
        updateSuccess.setTitle("UPDATE SUCCESS");
        updateSuccess.setHeaderText(null);
        updateSuccess.setContentText("Click OK to continue");
        updateSuccess.showAndWait();
        Update(email, phone, age, gender);
        RefreshTable();
        ClearField();
      }
    }
  }

  public void Delete() {
    String query = "DELETE FROM user WHERE userID = ?";
    try {
      c.pst = c.PrepStatement(query);
      c.pst.setString(1, userIdTF.getText());
      c.pst.executeUpdate();
      c.pst.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void ValidateDelete() {
    Alert deleteSuccess = new Alert(AlertType.INFORMATION);
    Alert deleteFailed = new Alert(AlertType.ERROR);

    if (userTable.getSelectionModel().isEmpty()) {
      deleteFailed.setTitle("DELETE FAILED");
      deleteFailed.setHeaderText(null);
      deleteFailed.setContentText("Please select the data you want to delete!");
      deleteFailed.showAndWait();
    } else {
      deleteSuccess.setTitle("DELETE SUCCESS");
      deleteSuccess.setHeaderText(null);
      deleteSuccess.setContentText("Click OK to continue");
      deleteSuccess.showAndWait();
      Delete();
      RefreshTable();
      ClearField();
    }
  }

  public void RefreshTable() {
    users.clear();
    Select();
    userObs = FXCollections.observableArrayList(users);
    userTable.setItems(userObs);
  }

  public void ClearField() {
    userIdTF.clear();
    usernameTF.clear();
    passwordPF.clear();
    emailTF.clear();
    phoneTF.clear();
    ageSpin.getValueFactory().setValue(16);
    genderGroup.selectToggle(null);
    userTable.getSelectionModel().clearSelection();
  }

  public EventHandler<MouseEvent> TableHandler() {

    return new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        TableSelectionModel<Users> userSelectionModel = userTable.getSelectionModel();
        userSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        Users u = userSelectionModel.getSelectedItem();
        if (u != null) {
          userIdTF.setText(u.getUserID());
          usernameTF.setText(u.getUsername());
          passwordPF.setText(u.getPassword());
          emailTF.setText(u.getEmail());
          phoneTF.setText(u.getPhoneNumber());
          ageSpin.getValueFactory().setValue(u.getAge());
          if (u.getGender().equals("Male")) {
            male.setSelected(true);
          } else {
            female.setSelected(true);
          }
        }
      }
    };
  }

  public void ManageItem() {
    manageUser = (Stage) bPane.getScene().getWindow();
    manageUser.close();
    manageItem = new Stage();
    ManageItem mi = new ManageItem();
    try {
      mi.start(manageItem);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Transaction() {
    manageUser = (Stage) bPane.getScene().getWindow();
    manageUser.close();
    transaction = new Stage();
    Transaction t = new Transaction();
    try {
      t.start(transaction);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Logout() {
    manageUser = (Stage) bPane.getScene().getWindow();
    manageUser.close();
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
    s.setTitle("admin page | manage user");
    s.setResizable(false);
    s.show();
  }

  @Override
  public void handle(ActionEvent e) {
    if (e.getSource() == manageItemMenu) {
      ManageItem();
    } else if (e.getSource() == transactionMenu) {
      Transaction();
    } else if (e.getSource() == logOutMenu) {
      Logout();
    }

    if (e.getSource() == updateBtn) {
      ValidateUpdate();
    } else if (e.getSource() == deleteBtn) {
      ValidateDelete();
    }
  }
}
