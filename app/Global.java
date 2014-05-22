
import java.lang.reflect.Method;

import static common.util.MessageUtil.getErrorResponse;

import play.GlobalSettings;
import play.Logger;
import play.api.Play;
import play.api.i18n.Lang;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Action.Simple;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Http.RequestHeader;
import play.mvc.Results.Status;
import play.mvc.SimpleResult;
import scala.concurrent.Future;

import common.util.Util;
import action.AuthorizeAction;
import common.util.AppConstants;

public class Global extends GlobalSettings {

    @Override
    public Action.Simple onRequest(Request request, Method method) {

    	Logger.debug("Inside onRequest...");
        if (request.path().contains(AppConstants.API_LOGIN)
                || request.path().contains(AppConstants.API_ENCRYPT)) {
            return (Simple) super.onRequest(request, method);
        }

        /*if (request.getHeader(FQAConstants.HTTP_AUTHORIZATION_HEADER) == null) {
            return new AuthorizeAction();
        } else if (request.getQueryString(FQAConstants.QUERYSTRING_NAME_UID) == null) {
            return new RequestValidator();
        } else {*/
            return (Simple) super.onRequest(request, method);
        //}
        
    }

    
    
    @Override
    public Promise<SimpleResult> onBadRequest(RequestHeader arg0, String arg1) {
    	Logger.debug("Inside onBadRequest..."+arg1);
    	
    	if(arg0.path().endsWith("capacity/search")){
	    	String uuid = Util.getUniqueId();
	        Lang lang = AppConstants.DEFAULT_LANG;
	    	
			SimpleResult simpleResult = Controller.badRequest(getErrorResponse("S1004", "", uuid, lang)).as("application/json");
			
			return Promise.pure(simpleResult);
    	}
    	else
    		return super.onBadRequest(arg0, arg1);
    	
    }
}