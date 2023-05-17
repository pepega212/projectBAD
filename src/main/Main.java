package main;

import admin.Admin;
import connection.Connect;
import user.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler<ActionEvent> {
  Connect c = Connect.getConnect();

  Stage login, register, admin, user;
  Scene scene;
  BorderPane bPane;
  GridPane gPane;
  FlowPane fPane;
  Text loginTitle, usernameLabel, passwordLabel;
  public static TextField usernameTF;
  PasswordField passwordPF;
  Button loginBtn, registerBtn;

  public void Init() {
    bPane = new BorderPane();
    gPane = new GridPane();
    fPane = new FlowPane();

    loginTitle = new Text("Login");
    usernameLabel = new Text("Username");
    passwordLabel = new Text("Password");
    usernameTF = new TextField();
    passwordPF = new PasswordField();
    loginBtn = new Button("Login");
    registerBtn = new Button("Register Account Page");

    fPane.getChildren().addAll(registerBtn, loginBtn);

    scene = new Scene(bPane, 300, 200);
  }

  public void Build() {
    gPane.add(usernameLabel, 0, 1);
    gPane.add(usernameTF, 1, 1);
    gPane.add(passwordLabel, 0, 2);
    gPane.add(passwordPF, 1, 2);
    gPane.add(fPane, 1, 3);
  }

  public void Set() {
    bPane.setTop(loginTitle);
    bPane.setCenter(gPane);

    BorderPane.setAlignment(loginTitle, Pos.CENTER);
    BorderPane.setMargin(loginTitle, new Insets(20, 0, 0, 0));
    loginTitle.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

    gPane.setHgap(10);
    gPane.setVgap(10);
    gPane.setPadding(new Insets(20));
    fPane.setVgap(10);
    fPane.setHgap(10);
  }

  public void Handler() {
    loginBtn.setOnAction(this);
    registerBtn.setOnAction(this);
  }

  public void Validate() {
    String username = usernameTF.getText();
    String password = passwordPF.getText();
    Alert loginFailed = new Alert(Alert.AlertType.ERROR);
    Alert loginSuccess = new Alert(Alert.AlertType.INFORMATION);
    loginFailed.setTitle("LOGIN FAILED");
    loginFailed.setHeaderText(null);

    if (username.equals("") || password.equals("")) {
      loginFailed.setContentText("Username or Password cannot be null!");
      loginFailed.showAndWait();
    } else {
      String query = "SELECT * FROM user WHERE username = '" + username + "' AND password = '" + password + "'";
      try {
        c.ExecuteQuery(query);
        if (c.rs.next()) {
          loginSuccess.setTitle("LOGIN SUCCESS");
          loginSuccess.setHeaderText(null);
          loginSuccess.setContentText("Click OK to continue");
          loginSuccess.showAndWait();
        } else {
          loginFailed.setContentText("Username or Password is incorrect!");
          loginFailed.showAndWait();
        }
      } catch (Exception err) {
        err.printStackTrace();
      }
    }
    String query = "SELECT * FROM user WHERE username = '" + username + "' AND password = '" + password + "'";
    try {
      c.ExecuteQuery(query);
      if (c.rs.next()) {
        if (c.rs.getString("role").equals("admin")) {
          Admin();
        } else {
          User();
        }
      }
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  public void Admin() {
    login = (Stage) bPane.getScene().getWindow();
    login.close();
    admin = new Stage();
    try {
      new Admin().start(admin);
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  public void User() {
    login = (Stage) bPane.getScene().getWindow();
    login.close();
    user = new Stage();
    try {
      new User().start(user);
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  public void Register() {
    login = (Stage) bPane.getScene().getWindow();
    login.close();
    register = new Stage();
    try {
      new Register().start(register);
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    launch(args);
  }

  @Override
  public void start(Stage s) throws Exception {
    Init();
    Build();
    Set();
    Handler();
    s.setScene(scene);
    s.setTitle("Login Page");
    s.setResizable(false);
    s.show();
  }

  @Override
  public void handle(ActionEvent e) {
    if (e.getSource() == loginBtn) {
      Validate();
    } else if (e.getSource() == registerBtn) {
      Register();
    }
  }
}
