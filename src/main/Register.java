package main;

import connection.Connect;
import javafx.application.Application;
import javafx.scene.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Register extends Application implements EventHandler<ActionEvent> {
  Connect c = Connect.getConnect();

  Stage register, login;
  Scene scene;
  BorderPane bPane;
  GridPane gPane;
  FlowPane fPaneGender, fPaneBtn;
  Text registerTitle, userIdLabel, usernameLabel, passwordLabel, emailLabel, phoneLabel, ageLabel, genderLabel;
  TextField userIdTF, usernameTF, emailTF, phoneTF;
  PasswordField passwordPF;
  RadioButton male, female;
  ToggleGroup genderGroup;
  Spinner<Integer> ageSpin;
  Button registerBtn, loginBtn;

  public void Init() {
    bPane = new BorderPane();
    gPane = new GridPane();
    fPaneGender = new FlowPane();
    fPaneBtn = new FlowPane();

    registerTitle = new Text("Register");
    userIdLabel = new Text("User ID");
    usernameLabel = new Text("Username");
    passwordLabel = new Text("Password");
    emailLabel = new Text("Email");
    phoneLabel = new Text("Phone Number");
    ageLabel = new Text("Age");
    genderLabel = new Text("Gender");

    userIdTF = new TextField();
    userIdTF.setText(GenerateUserId());

    usernameTF = new TextField();
    passwordPF = new PasswordField();
    emailTF = new TextField();
    phoneTF = new TextField();
    ageSpin = new Spinner<Integer>(15, 70, 16);
    male = new RadioButton("Male");
    female = new RadioButton("Female");

    fPaneGender.getChildren().addAll(male, female);

    genderGroup = new ToggleGroup();
    male.setToggleGroup(genderGroup);
    female.setToggleGroup(genderGroup);

    registerBtn = new Button("Register");
    loginBtn = new Button("Login Page");

    fPaneBtn.getChildren().addAll(loginBtn, registerBtn);

    scene = new Scene(bPane, 300, 375);
  }

  public void Set() {
    bPane.setTop(registerTitle);
    bPane.setCenter(gPane);

    BorderPane.setAlignment(registerTitle, Pos.CENTER);
    BorderPane.setMargin(registerTitle, new Insets(20, 0, 0, 0));

    registerTitle.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

    gPane.setHgap(10);
    gPane.setVgap(10);
    gPane.setPadding(new Insets(20));

    fPaneBtn.setHgap(10);
    fPaneGender.setHgap(10);
    FlowPane.setMargin(male, new Insets(0, 0, 0, 15));

    ageSpin.setMaxWidth(200);
    userIdTF.setDisable(true);
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
    gPane.add(fPaneBtn, 1, 7);
  }

  public void Handler() {
    registerBtn.setOnAction(this);
    loginBtn.setOnAction(this);
  }

  public void Validate() {
    String userId = GenerateUserId();
    String username = usernameTF.getText();
    String password = passwordPF.getText();
    String gender;
    String email = emailTF.getText();
    String phone = phoneTF.getText();
    int age = ageSpin.getValue();
    String role = "user";

    if (male.isSelected()) {
      gender = male.getText();
    } else {
      gender = female.getText();
    }

    Alert registFailed = new Alert(AlertType.ERROR);
    Alert registSuccess = new Alert(AlertType.INFORMATION);
    registFailed.setTitle("REGISTRATION ERROR");
    registFailed.setHeaderText(null);

    if (username.length() < 5 || username.length() > 20) {
      registFailed.setContentText("Username must be between 5 - 20 characters!");
      registFailed.showAndWait();
    } else if (password.length() < 5 || password.length() > 20) {
      registFailed.setContentText("Password must be between 5 - 20 characters!");
      registFailed.showAndWait();
    } else if (!checkPassword(password)) {
      registFailed.setContentText("Password must be alphanumeric!");
      registFailed.showAndWait();
    } else if (!email.contains("@") || email.indexOf("@") == 0 || !email.endsWith(".com")) {
      registFailed.setContentText(
          "email must consist of '@' character, '@' characters must not be \nin front and must end with '.com!'");
      registFailed.showAndWait();
    } else if (phone.length() < 9 || phone.length() > 12) {
      registFailed.setContentText("Phone number must be between 9 - 12 characters!");
      registFailed.showAndWait();
    } else if (age < 17 || age > 60) {
      registFailed.setContentText("Age range must be between 17 - 60!");
      registFailed.showAndWait();
    } else if (genderGroup.getSelectedToggle() == null) {
      registFailed.setContentText("Gender must be selected, either 'Male' or 'Female'!");
      registFailed.showAndWait();
    } else {
      registSuccess.setTitle("REGISTRATION SUCCESS");
      registSuccess.setHeaderText(null);
      registSuccess.setContentText("Click OK to continue");
      registSuccess.showAndWait();
      Insert(userId, username, password, gender, email, phone, age, role);
      Login();
    }
  }

  public boolean checkPassword(String password) {
    boolean hasLetter = false;
    boolean hasNumber = false;
    for (int i = 0; i < password.length(); i++) {
      if (Character.isLetter(password.charAt(i))) {
        hasLetter = true;
      }
      if (Character.isDigit(password.charAt(i))) {
        hasNumber = true;
      }
    }
    return hasLetter && hasNumber;
  }

  public String GenerateUserId() {
    String query = "SELECT MAX(userID) FROM user";
    String userId = "";
    int id = 0;
    try {
      c.ExecuteQuery(query);
      if (c.rs.next()) {
        userId = c.rs.getString(1);
        id = Integer.parseInt(userId.substring(2));
        id++;
        userId = "US" + String.format("%03d", id);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return userId;
  }

  public void Insert(String userId, String username, String password, String gender, String email, String phone,
      int age, String role) {
    String query = "INSERT INTO user VALUES (?,?,?,?,?,?,?,?)";
    try {
      c.pst = c.PrepStatement(query);
      c.pst.setString(1, userId);
      c.pst.setString(2, username);
      c.pst.setString(3, password);
      c.pst.setString(4, gender);
      c.pst.setString(5, email);
      c.pst.setString(6, phone);
      c.pst.setInt(7, age);
      c.pst.setString(8, role);
      c.pst.executeUpdate();
      c.pst.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void Login() {
    register = (Stage) bPane.getScene().getWindow();
    register.close();

    login = new Stage();
    try {
      new Main().start(login);
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  @Override
  public void start(Stage s) throws Exception {
    Init();
    Set();
    Build();
    Handler();
    s.setScene(scene);
    s.setTitle("Register Page");
    s.setResizable(false);
    s.show();
  }

  @Override
  public void handle(ActionEvent e) {
    if (e.getSource() == registerBtn) {
      Validate();
    } else if (e.getSource() == loginBtn) {
      Login();
    }
  }
}
