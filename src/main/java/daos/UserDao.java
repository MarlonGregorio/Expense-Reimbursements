package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import models.User;


public class UserDao {

	final static Logger logger = Logger.getLogger(UserDao.class);
	public static String dbURL = "jdbc:postgresql://35.236.33.138/reimburseDB";
	public static String dbUsername = "postgres";
	public static String dbPassword = "p4ssw0rd";
	
	//Returns a user from the database if the username and password match
	public static User getUser(String username, String hashPassword) {
		try(Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword)){
			String sqlStatement = "SELECT * FROM users WHERE user_name = ? AND user_password = ?;";
			PreparedStatement ps = conn.prepareStatement(sqlStatement);
			ps.setString(1, username);
			ps.setString(2, hashPassword);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				//Extracting user information from result query
				int userId = rs.getInt("user_id");
				int userRoleId = rs.getInt("user_role_id");
				String userEmail = rs.getString("user_email");
				String firstName = rs.getString("user_first_name");
				String lastName = rs.getString("user_last_name");
				
				//Create a user from database values
				User currUser = new User(userId, username, userEmail);
				currUser.setFirstName(firstName);
				currUser.setLastName(lastName);
				currUser.setRole(userRoleId);
				return currUser;
			}
		}
		catch(SQLException e) {
			logger.error("Database connection failure: Account login", e);
		}
		return null;
	}
}