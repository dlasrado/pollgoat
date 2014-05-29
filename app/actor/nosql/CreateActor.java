package actor.nosql;

import java.util.ArrayList;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Result;
import reactivemongo.core.commands.LastError;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import models.FormParamBean;
import common.exception.InvalidConfigurationException;
import common.resource.ErrorResponse;
import common.util.Util;
import services.MongoUtil;

public class CreateActor extends UntypedActor {

	@Override
	public void onReceive(Object request) throws Exception {
		
		if(request instanceof FormParamBean){
			final ActorRef sender = getSender();
			FormParamBean requestBean = (FormParamBean)request;
			
        	try {
        		ArrayList<String> al = Util.getDBConfig(requestBean.getConnectionName());
        		final reactivemongo.api.MongoConnection connection = MongoUtil.connect(al.get(0));
                
               Promise.wrap(MongoUtil.save(MongoUtil.connect(connection,al.get(1),requestBean.getCollectionName()), requestBean.getData())).map(new Function<LastError,Result>(){

					@Override
					public Result apply(LastError error)
							throws Throwable {
						connection.close();
						if(!error.inError())
							sender.tell("record inserted successfully",getSelf());
						else{
							sender.tell(new ErrorResponse("P1902",error.getMessage()),getSelf());
						}
						
						return null;
					}
                }).recover(new Function<Throwable,Result>(){

					@Override
					public Result apply(Throwable error)
							throws Throwable {
						connection.close();
						sender.tell(new ErrorResponse("P1902",error.getMessage()),getSelf());
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
