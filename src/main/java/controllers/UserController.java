package controllers;


import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import models.Reimbursement;
import models.User;
import services.ReimbursementService;
import services.UserService;

public class UserController {

	//Logs in a user. Puts the user object in the session.
	public static void login(Context context){
		User currUser;
		
		try {
			final ObjectMapper objectMapper = new ObjectMapper();
			String[] loginInfo = objectMapper.readValue(context.body(), String[].class);
			currUser = UserService.getUser(loginInfo[0], loginInfo[1]);
		}
		catch(Exception e) {
			currUser = null;
		}
		
		if(currUser != null) {
			context.sessionAttribute("currUser", currUser);
			context.redirect("/home.html");
		}
	}
	
	//Removes a user from the session
	public static void logout(Context context) {
		context.sessionAttribute("currUser", null);
		context.sessionAttribute("reims", null);
		context.redirect("/index.html");
	}
	
	//Gets a user based on the session
	public static void getUserInfo(Context context) {
		User currUser = context.sessionAttribute("currUser");
		context.json(currUser);
	}
	
	//Gets a ticket based on its id
	public static void getTicket(Context context) {
		User currUser = context.sessionAttribute("currUser");
		ArrayList<Reimbursement> reims = context.sessionAttribute("reims");
		String reimIdQuery = context.queryParam("id");
		
		//check if user is finance manager
		if(currUser.getRole() == 1) {
			if(reims == null) {
				reims = ReimbursementService.getTickets(-1);
				context.sessionAttribute("reims", reims);
			}
			
			int reimId = Integer.parseInt(reimIdQuery);
			
			for(Reimbursement tempReim: reims) {
				if(tempReim.getId() == reimId) {
					context.json(tempReim);
					return;
				}
			}
			
			context.contentType("application/json");
			context.result("{}").status(404);
		}
		else {
			context.contentType("application/json");
			context.result("{}").status(403);
		}
	}
	
	//Gets the tickets of the currently signed in user(employee)
	public static void getTickets(Context context) {
		User currUser = context.sessionAttribute("currUser");
		ArrayList<Reimbursement> reims = context.sessionAttribute("reims");
		
		if(reims == null) {
			reims = ReimbursementService.getTickets(currUser.getId());
			context.sessionAttribute("reims", reims);
		}
		context.json(reims);
	}
	
	//Gets all the tickets in the database
	public static void getTicketsAll(Context context) {
		ArrayList<Reimbursement> reims = context.sessionAttribute("reims");
		
		if(reims == null) {
			reims = ReimbursementService.getTickets(-1);
			context.sessionAttribute("reims", reims);
		}
		context.json(reims);
	}
	
	//Creates a reimbursement ticket. Used by an employee
	public static void createTicket(Context context) {
		Reimbursement reim = context.bodyAsClass(Reimbursement.class);
		User currUser = context.sessionAttribute("currUser");
		ReimbursementService.createReimbursement(reim, currUser);
		ArrayList<Reimbursement> reims = ReimbursementService.getTickets(currUser.getId());
		context.sessionAttribute("reims", reims);
		context.redirect("/tickets.html");
	}
	
	//Updates a ticket's status. Used by a finance manager
	public static void updateTicket(Context context){
		User currUser = context.sessionAttribute("currUser");
		int reimId;
		int reimStatus;
		boolean toApprove = false;
		
		try {
			final ObjectMapper objectMapper = new ObjectMapper();
			int[] updateInfo = objectMapper.readValue(context.body(), int[].class);
			reimId = updateInfo[0];
			reimStatus = updateInfo[1];
		}
		catch(Exception e) {
			//Invalid body message for a ticket update
			context.redirect("/tickets.html");
			return;
		}
		
		if(reimStatus == 1) {
			toApprove = true;
		}
		
		if(currUser.getRole() == 1) {
			ReimbursementService.updateTicket(reimId, toApprove, currUser.getId());
			ArrayList<Reimbursement> reims = ReimbursementService.getTickets(-1);
			context.sessionAttribute("reims", reims);
			context.redirect("/tickets.html");
		}
		else {
			//Employee not allowed to alter ticket after creation
			context.redirect("/tickets.html");
		}
	}
	
}
