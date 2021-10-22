package utils;

import controllers.FrontController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

//This is the file to run in order to start the application
//located at: http://localhost:9002/
public class MainDriver {
	public static void main(String[] args) {
		Javalin app = Javalin.create(
			config->{
				config.addStaticFiles(
						staticFiles->{
							staticFiles.directory="/html-files";
							staticFiles.hostedPath="/";
							staticFiles.location = Location.CLASSPATH;
						}
					);
			}
		).start(9002);
		
		FrontController frontController = new FrontController(app);
	}
}
