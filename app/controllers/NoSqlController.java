/**
 * 
 */
package controllers;

import static akka.pattern.Patterns.ask;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import play.api.Play;
import play.api.i18n.Lang;
import play.api.libs.json.JsArray;
import play.libs.Akka;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.util.Timeout;

import common.exception.RecordNotFoundException;
import common.util.AppConfig;
import common.util.AppConstants;
import common.util.AppLogger;
import common.util.Util;
import common.util.MessageUtil;
import actor.nosql.CreateActor;
import actor.nosql.DeleteActor;
import actor.nosql.ReadActor;
import actor.nosql.UpdateActor;
import models.FormParamBean;
import common.resource.ErrorResponse;


public class NoSqlController extends Controller {

	  protected static final AppConfig                 CONFIGLOADER              = AppConfig
              .getInstance();

	  protected static final Lang                       DEFAULT_LANG              = new Lang(
              CONFIGLOADER
                      .get(AppConstants.CONFIG_LANGUAGE_CODE),
              CONFIGLOADER
                      .get(AppConstants.CONFIG_LANGUAGE_COUNTRY));
  	
		/**
		 * The time out for asynchronous akka actor calls.
		 */
		static final Timeout t = new Timeout(Duration.create(60, TimeUnit.SECONDS));

        public static final String X_REQUEST_UUID         = "X-REQUEST-UUID";

        private static <T> Promise<Result> callDB(final String uuid, final Lang lang, ActorRef actorRef, FormParamBean formParam) {
            final String errorId = Util.getUniqueId();
            final long startDateTime = System.currentTimeMillis();
            return Promise.wrap(ask(actorRef, formParam, t)).map(new Function<Object, Result>() {

                @Override
                public Result apply(Object response) throws Throwable {
                	
                	if (AppLogger.isDebugEnabled()) {
                        long endDateTime = System.currentTimeMillis();
                        long totalTime = endDateTime - startDateTime;
                        
                        AppLogger.debug(uuid, " API(NoSql-Connect) Response time (MilliSec) : "
                                + totalTime);
                    }
                	
                    if (response instanceof ErrorResponse) {
                    	ErrorResponse errorResponse = (ErrorResponse) response;
                    	if(errorResponse.getErrorCode().equalsIgnoreCase("P1902")){
                    		AppLogger.error("P1902", uuid,
                    				errorResponse.getErrorMessage());
                    		return badRequest(MessageUtil.getErrorResponse("P1902",errorResponse.getErrorMessage(),errorId,lang)).as("application/json");
                    	}
                    	else if(errorResponse.getErrorCode().equalsIgnoreCase("P1903")){
                    		AppLogger.error("P1903", uuid,errorResponse.getErrorMessage());
							return notFound(MessageUtil.getErrorResponse("P1903",errorId,lang)).as("application/json");
                    	}
                    	else
                    		return internalServerError(MessageUtil.getErrorResponse(
                                    "P1000", errorId, lang));
                        
                    } else if(response instanceof JsArray){
                    	return ok(((JsArray)response).toString()).as("application/json");
                    } else if (response instanceof String) {
                    	return ok("Requested operation completed successfully").as("application/text");
                    } else
                        return internalServerError(MessageUtil.getErrorResponse(
                                "P1000", errorId, lang)).as("application/json");
                }

            }).recover(new Function<Throwable, Result>() {

                @Override
                public Result apply(Throwable e) {
                    AppLogger.error("P1000", uuid, "Error received from actor - " + e);
                    return internalServerError(MessageUtil.getErrorResponse(
                            "P1000", errorId, lang)).as("application/json");
                }

            });
        }
        
	public static Promise<Result> create(String connName, String collName, String[] data, final String uuid, final Lang lang) {

        final String errorId = Util.getUniqueId();
        
        if(connName == null) return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1003","connectionName",errorId,lang)));
    
        if(collName == null) return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1003","collectionName",errorId,lang)));

        if(data == null || data.length == 0){
    		return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1003","data",errorId,lang)));
    	}
        else{
        	
        	FormParamBean formParam = new FormParamBean(connName,collName,"",data[0]);
        	
        	ActorRef actorRef = Akka.system().actorOf(
                    Props.create(CreateActor.class));
        	return callDB(uuid,lang,actorRef, formParam);

        } 
	}


	public static Promise<Result> read(String connName, String collName, String[] query, final String uuid, final Lang lang) {
        final String errorId = Util.getUniqueId();
        
    	
        if(connName == null) return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1003","connectionName",errorId,lang)));
    
        if(collName == null) return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1003","collectionName",errorId,lang)));

        if(query == null || query.length == 0){
    		return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1003","query",errorId,lang)));
    	}
        else{
        	FormParamBean formParam = new FormParamBean(connName,collName,query[0],"");
        	ActorRef actorRef = Akka.system().actorOf(
                    Props.create(ReadActor.class));
        	return callDB(uuid,lang,actorRef, formParam);
        } 
	}


	public static Promise<Result> update(String connName, String collName, String[] newDocument, String[] updateCriteria, final String uuid, final Lang lang) {

        final String errorId = Util.getUniqueId();
    	
        if(connName == null) return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1003","connectionName",errorId,lang)));
    
        if(collName == null) return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1003","collectionName",errorId,lang)));

        
        
        if(updateCriteria == null || updateCriteria.length == 0){
    		return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1003","updateCriteria",errorId,lang)));
    	}
        
        if(newDocument == null || newDocument.length == 0){
    		return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1003","newDocument",errorId,lang)));
    	}
        else{
        	final FormParamBean formParam = new FormParamBean(connName,collName,updateCriteria[0],newDocument[0]);
        	
        	ActorRef readActorRef = Akka.system().actorOf(
                    Props.create(ReadActor.class));
        	
        	final ActorRef actorRef = Akka.system().actorOf(
                    Props.create(UpdateActor.class));
        	
    		return Promise.wrap(ask(readActorRef,formParam,t))
    				.flatMap(new Function<Object, Promise<Result>>(){

	        			@Override
	        			public  Promise<Result> apply(Object response) throws Throwable {

	                        if (response instanceof ErrorResponse) {
	                        	ErrorResponse errorResponse = (ErrorResponse) response;
	                        	if(errorResponse.getErrorCode().equalsIgnoreCase("P1902")){
	                        		AppLogger.error("P1902", uuid,
	                        				errorResponse.getErrorMessage());
	                        		return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1902",errorResponse.getErrorMessage(),errorId,lang)).as("application/json"));
	                        	}
	                        	else if(errorResponse.getErrorCode().equalsIgnoreCase("P1903")){
	                        		AppLogger.error("P1903", uuid,errorResponse.getErrorMessage());
	    							return Promise.pure((Result)notFound(MessageUtil.getErrorResponse("P1903",errorId,lang)).as("application/json"));
	                        	}
	                        	else
	                        		return Promise.pure((Result)internalServerError(MessageUtil.getErrorResponse(
	                                        "P1000", errorId, lang)).as("application/json"));

	                        } else if(response instanceof JsArray){
	                        	return callDB(uuid,lang,actorRef, formParam);
	                        } 
	                        else
	                        	return Promise.pure((Result)internalServerError(MessageUtil.getErrorResponse(
                                        "P1000", errorId, lang)).as("application/json"));
	                    }
    				}).recover(new Function<Throwable,Result>(){
							@Override
							public Result apply(Throwable error) throws Throwable {
								error.printStackTrace();
								if(error instanceof RecordNotFoundException){
									AppLogger.error("P1903", uuid,error.getMessage());
									return notFound(MessageUtil.getErrorResponse("P1903",errorId,lang)).as("application/json");
								}
								else{
					        		AppLogger.error("P1000", uuid,error.getMessage());
									return internalServerError(MessageUtil.getErrorResponse("P1000",errorId,lang));
								}
							}        			
    					});
        }
	}

	public static Promise<Result> delete(String connName, String collName, String[] deleteCriteria, final String uuid, final Lang lang) {

        final String errorId = Util.getUniqueId();

        if(connName == null) return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1003","connectionName",errorId,lang)));
    
        if(collName == null) return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1003","collectionName",errorId,lang)));

        if(deleteCriteria == null || deleteCriteria.length == 0){
    		return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1003","deleteCriteria",errorId,lang)));
    	}
        else{
        	final FormParamBean formParam = new FormParamBean(connName,collName,deleteCriteria[0],"");
        	
        	ActorRef readActorRef = Akka.system().actorOf(
                    Props.create(ReadActor.class));
        	
        	final ActorRef actorRef = Akka.system().actorOf(
                    Props.create(DeleteActor.class));
        	
    		return Promise.wrap(ask(readActorRef,formParam,t))
    				.flatMap(new Function<Object, Promise<Result>>(){

	        			@Override
	        			public  Promise<Result> apply(Object response) throws Throwable {

	                        if (response instanceof ErrorResponse) {
	                        	ErrorResponse errorResponse = (ErrorResponse) response;
	                        	if(errorResponse.getErrorCode().equalsIgnoreCase("P1902")){
	                        		AppLogger.error("P1902", uuid,
	                        				errorResponse.getErrorMessage());
	                        		return Promise.pure((Result)badRequest(MessageUtil.getErrorResponse("P1902",errorResponse.getErrorMessage(),errorId,lang)).as("application/json"));
	                        	}
	                        	else if(errorResponse.getErrorCode().equalsIgnoreCase("P1903")){
	                        		AppLogger.error("P1903", uuid,errorResponse.getErrorMessage());
	    							return Promise.pure((Result)notFound(MessageUtil.getErrorResponse("P1903",errorId,lang)).as("application/json"));
	                        	}
	                        	else
	                        		return Promise.pure((Result)internalServerError(MessageUtil.getErrorResponse(
	                                        "P1000", errorId, lang)).as("application/json"));

	                        } else if(response instanceof JsArray){
	                        	return callDB(uuid,lang,actorRef, formParam);
	                        } 
	                        else
	                        	return Promise.pure((Result)internalServerError(MessageUtil.getErrorResponse(
                                        "P1000", errorId, lang)).as("application/json"));
	                    }
    				}).recover(new Function<Throwable,Result>(){
							@Override
							public Result apply(Throwable error) throws Throwable {
								error.printStackTrace();
								if(error instanceof RecordNotFoundException){
									AppLogger.error("P1903", uuid,error.getMessage());
									return notFound(MessageUtil.getErrorResponse("P1903",errorId,lang)).as("application/json");
								}
								else{
					        		AppLogger.error("P1000", uuid,error.getMessage());
									return internalServerError(MessageUtil.getErrorResponse("P1000",errorId,lang));
								}
							}        			
    					});
        } 

	}

}
