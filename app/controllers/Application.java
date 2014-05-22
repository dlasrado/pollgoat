package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.libs.F.Promise;
//import views.html.index;

public class Application extends Controller {
    
    public static play.libs.F.Promise<Result> index() {
        return Promise.pure((Result)ok("Hello").as("plain/text"));
    }


    
}
