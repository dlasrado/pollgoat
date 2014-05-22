package controllers;

import play.libs.F.Promise;
import play.mvc.Result;
import play.mvc.Controller;
//import views.html.index;

public class UserController  extends Controller {

	
	 public static play.libs.F.Promise<Result> findUsers() {
	        return Promise.pure((Result)ok("Hello").as("plain/text"));
	    }

	 public static play.libs.F.Promise<Result> createUser() {
	        return Promise.pure((Result)ok("Hello").as("plain/text"));
	    }
}
