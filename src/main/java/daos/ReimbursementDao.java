package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import models.Reimbursement;


public class ReimbursementDao {

	final static Logger logger = Logger.getLogger(ReimbursementDao.class);
	public static String dbURL = "jdbc:postgresql://35.236.33.138/reimburseDB";
	public static String dbUsername = "postgres";
	public static String dbPassword = "p4ssw0rd";
	
	//Gets tickets relating to a user
	//If the the userId is -1, it will supply all tickets
	public static ArrayList<Reimbursement> getTickets(int userId) {
		try(Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword)){
			String sqlStatement = "SELECT * FROM reimburse_joins WHERE reimburse_author = ?;";
			if(userId == -1) sqlStatement = "SELECT * FROM reimburse_joins";
			
			PreparedStatement ps = conn.prepareStatement(sqlStatement);
			if(userId != -1) ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			logger.info(ps);
			
			ArrayList<Reimbursement> tickets = new ArrayList<>();
			while(rs.next()) {
				
				//Extracting reimbursement information from result query
				double reimburseAmount = rs.getDouble("reimburse_amount");
				Timestamp reimburseSubmitted = rs.getTimestamp("reimburse_submitted");
				Timestamp reimburseResolved = rs.getTimestamp("reimburse_resolved");
				int reimburseAuthor = rs.getInt("reimburse_author");
				int reimburseResolver = rs.getInt("reimburse_resolver");
				int reimburseStatus = rs.getInt("reimburse_status_id");
				int reimburseType = rs.getInt("reimburse_type_id");
				int reimburseId = rs.getInt("reimburse_id");
				String authorName = rs.getString("authorName");
				String resolverName = rs.getString("resolverName");
				String typeName = rs.getString("reimburse_type");
				String statusName = rs.getString("reimburse_status");
				String reimburseDescription = rs.getString("reimburse_description");
				
				//Create a reimbursement from database values
				Reimbursement reim = new Reimbursement(reimburseType, reimburseAmount, reimburseDescription);
				reim.setId(reimburseId);
				reim.setSubmitted(reimburseSubmitted);
				reim.setResolved(reimburseResolved);
				reim.setDescription(reimburseDescription);
				reim.setAuthor(reimburseAuthor);
				reim.setResolver(reimburseResolver);
				reim.setStatus(reimburseStatus);
				reim.setAuthorName(authorName);
				reim.setResolverName(resolverName);
				reim.setTypeName(typeName);
				reim.setStatusName(statusName);
				
				tickets.add(reim);
			}			
			return tickets;
		}
		catch(SQLException e) {
			logger.error("Database connection failure: Reimbursement query", e);
		}
		return null;
	}
	
	//An employee will insert a ticket into the database
	public static boolean insertTicket(Reimbursement reim) {
		try(Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword)){
			String sqlStatement = "INSERT INTO reimbursements(reimburse_type_id,"
					+ "reimburse_amount, reimburse_description, reimburse_author,"
					+ "reimburse_status_id, reimburse_submitted) VALUES(?,?,?,?,?,?)";
			
			PreparedStatement ps = conn.prepareStatement(sqlStatement);
			ps.setInt(1, reim.getType());
			ps.setDouble(2, reim.getAmount());
			ps.setString(3, reim.getDescription());
			ps.setInt(4, reim.getAuthor());
			ps.setInt(5, reim.getStatus());
			ps.setTimestamp(6, reim.getSubmitted());
			ps.execute();
			logger.info(ps);
			return true;
		}
		catch(SQLException e) {
			logger.error("Database connection failure: Reimbursement insert", e);
		}
		
		return false;
	}

	//A finance manager will update a ticket with its new status and related information
	public static boolean updateTicket(int reimId, boolean toApprove, int resolverId, Timestamp ts) {
		try(Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword)){
			String sqlStatement = "UPDATE reimbursements SET reimburse_resolver = ?,"
					+ "reimburse_resolved = ?,  reimburse_status_id = ? WHERE "
					+ "reimburse_id = ?";
			
			int newStatus = 1; //denied
			if(toApprove) {
				newStatus = 2; //approved
			}
			PreparedStatement ps = conn.prepareStatement(sqlStatement);
			ps.setInt(1, resolverId);
			ps.setTimestamp(2, ts);
			ps.setInt(3, newStatus);
			ps.setInt(4, reimId);
			ps.executeUpdate();
			logger.info(ps);
			return true;
		}
		catch(SQLException e) {
			logger.error("Database connection failure: Reimbursement update", e);
		}
		
		return false;
	}
}
