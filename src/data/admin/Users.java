package data.admin;

public class Users {

  private String userID;
  private String username;
  private String password;
  private String email;
  private String phoneNumber;
  private int age;
  private String gender;

  public Users(String userID, String username, String password, String email, String phoneNumber, int age,
      String gender) {
    this.userID = userID;
    this.username = username;
    this.password = password;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.age = age;
    this.gender = gender;
  }

  public String getUserID() {
    return this.userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public int getAge() {
    return this.age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getGender() {
    return this.gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }
}
