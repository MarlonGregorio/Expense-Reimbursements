package controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class FrontController {

	Javalin app;
	Dispatcher dispatcher;
	
	public FrontController(Javalin app) {
		this.app = app;
		app.before("/api/*", FrontController::checkAllRequests);
		app.before("/*.html", FrontController::checkPages);
		this.dispatcher = new Dispatcher(app);
	}
	
	//Middleware to ensure a valid user exists for the api calls
	public static void checkAllRequests(Context context) {
		if(context.path().equals("/api/user/login")) {
			return;
		}
		if(context.sessionAttribute("currUser") == null) {
			context.redirect("/index.html");
		}
	}
	
	//Middleware to ensure a valid user exists to visit the html pages
	public static void checkPages(Context context) {
		if(context.sessionAttribute("currUser") == null && !context.path().equals("/index.html")) {
			context.redirect("/index.html");
		}
	}
}
