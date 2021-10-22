package controllers;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Dispatcher {

	public Dispatcher(Javalin app) {
		setupUserPaths(app);
	}
	
	//Sets up user paths for the user controller
	public static void setupUserPaths(Javalin app) {
		app.routes(()->{
			path("/api/user", ()->{
				
				path("/login", ()->{
					post(UserController::login);
				});
				
				path("/logout", ()->{
					get(UserController::logout);
				});
				
				path("/info", ()->{
					get(UserController::getUserInfo);
				});
				
				path("/tickets", ()->{
					get(UserController::getTickets);
				});
				
				path("/new-ticket", ()->{
					post(UserController::createTicket);
				});
			});
			
			path("/api/tickets", ()->{
				get(UserController::getTicketsAll);
				
			});
			
			path("/api/ticket", ()->{
				get(UserController::getTicket);
				
			});
			
			path("/api/update-ticket", ()->{
				put(UserController::updateTicket);
				
			});
		});
	}
}
