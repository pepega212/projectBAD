package connection;

import java.sql.*;

public class Connect {
  private final String username = "root";
  private final String pass = "";
  private final String url = "jdbc:mysql://localhost:3306/fteammarket_db";

  public Connection con;
  Statement st;
  public ResultSet rs;
  public PreparedStatement pst;
  ResultSetMetaData rsmd;

  private static Connect connect;

  public static Connect getConnect() {
    if (connect == null) {
      connect = new Connect();
    }
    return connect;
  }

  private Connect() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      con = DriverManager.getConnection(url, username, pass);
      st = con.createStatement();
      System.out.println("Connected to Database");
    } catch (Exception err) {
      System.out.println("Not Connected to Database");
    }
  }

  public ResultSet ExecuteQuery(String query) {
    try {
      rs = st.executeQuery(query);
      rsmd = rs.getMetaData();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return rs;
  }

  public PreparedStatement PrepStatement(String query) {
    try {
      pst = con.prepareStatement(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return pst;
  }
}
