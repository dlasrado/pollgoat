package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.libs.F.Promise;
//import views.html.index;
import securesocial.core.java.SecureSocial;
import securesocial.core.Identity;

public class Application extends Controller {
    
	@SecureSocial.UserAwareAction
    public static play.libs.F.Promise<Result> index() {
		
		Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
        final String userName = user != null ? user.firstName().toUpperCase() : "GUEST";
        
        return Promise.pure((Result)ok(views.html.index.render(userName)));
        //return Promise.pure((Result)ok("Hello").as("plain/text"));
    }


    
}
