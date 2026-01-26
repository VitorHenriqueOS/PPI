package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conector {
	
static String dbname = "ppi";
static String uname = "root";
static String pword = "8003";
static String url = "jdbc:mysql://localhost:3306/" + dbname + "?useSSL=false&useTimezone=true&serverTimezone=UTC";

public Connection getConexao() {
	try {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection(url, uname, pword);
	}
	catch (SQLException e) {
		throw new RuntimeException(e);
	} catch (ClassNotFoundException e) {
		throw new RuntimeException(e);
	}
}
}
