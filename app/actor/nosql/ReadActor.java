package actor.nosql;

import java.util.ArrayList;

import play.api.libs.json.JsArray;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Result;
import scala.collection.Iterator;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import common.exception.RecordNotFoundException;
import models.FormParamBean;
import common.exception.InvalidConfigurationException;
import common.resource.ErrorResponse;
import common.util.Util;
import services.MongoUtil;

public class ReadActor extends UntypedActor {

	@Override
	public void onReceive(Object request) throws Exception {
		if(request instanceof FormParamBean){
			final ActorRef sender = getSender();
			FormParamBean 
			requestBean = (FormParamBean)request;
			
        	try {
        		ArrayList<String> al = Util.getDBConfig(requestBean.getConnectionName());
        		final reactivemongo.api.MongoConnection connection = MongoUtil.connect(al.get(0));
              
                //query data.
        		Promise.wrap(MongoUtil.query(MongoUtil.connect(connection,al.get(1),requestBean.getCollectionName()),requestBean.getCriteria())).map(new Function<JsArray, Result>(){

        			@Override
        			public Result apply(JsArray responseList) throws Throwable {
        				connection.close();
        				Iterator<Object> itr = responseList.productIterator();
        				boolean isEmpty = false;
        				
    					Object result = itr.next();
    					if(result instanceof scala.collection.immutable.Nil$) 
    						if(((scala.collection.immutable.Nil$)result).length() == 0) isEmpty = true;
        				
        				if(!isEmpty){
        					sender.tell(responseList,getSelf());
        				}
        				else{
        					throw new RecordNotFoundException("No records found for the search criteria provided.");
        				}
        				return null;
        			}
        			
        		}).recover(new Function<Throwable,Result>(){

					@Override
					public Result apply(Throwable error) throws Throwable {
						connection.close();
						error.printStackTrace();
						if(error instanceof RecordNotFoundException){
							sender.tell(new ErrorResponse("P1903",error.getMessage()),getSelf());
						}
						else{
							sender.tell(new ErrorResponse("P1000",error.getMessage()),getSelf());
						}
						return null;
					}
        			
        		});
                
			}
        	catch(InvalidConfigurationException e){
        		sender.tell(new ErrorResponse("P1902",e.getMessage()),getSelf());
        	}
        	catch (Exception e) {
				e.printStackTrace();
				sender.tell(new ErrorResponse("P1000",e.getMessage()),getSelf());
			}
		}
		else{
			unhandled(request);
		}
		
	}

}
