/**
 * 
 */
package controllers;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import services.MongoUtil;



import common.util.AppConstants;
import play.Logger;
import play.libs.F.Promise;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.Future;
import play.libs.F.Function;
import play.api.libs.json.JsArray;

/**
 * @author dlasrado
 *
 */
public class PolletController extends Controller {

	/**
	 * Get the categories JSON
	 */
	public static Promise<Result> findCategories() {
		
		Logger.debug("Getting categories list from Mongo...");
		
		JSONObject queryJson = new JSONObject();
		//queryJson.put("productDefTypeCode", "Customizable");
		
		Future<JsArray> futureList = MongoUtil.query(queryJson.toString());

		Logger.debug("findCategories : Returning promise");
		return Promise.wrap(futureList).map(new Function<JsArray, Result>(){

			@Override
			public Result apply(JsArray responseList) throws Throwable {

				return ok(responseList.toString()).as("application/json");
			}

		});
	}
	
	
}
