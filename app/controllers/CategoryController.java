/**
 * 
 */
package controllers;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import services.MongoUtil;

import play.api.i18n.Lang;
import common.util.AppConstants;
import common.util.AppLogger;
import common.util.Util;
import play.Logger;
import play.libs.F.Promise;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.Future;
import play.libs.F.Function;
import play.api.Play;
import play.api.libs.json.JsArray;

/**
 * @author dlasrado
 *
 */
public class CategoryController extends NoSqlController {

	private final static String collectionName = "category";
	private final static String connectionName = "pollgoatConnection";
	/**
	 * Get the categories JSON
	 */
	public static Promise<Result> findCategories() {
		
		Logger.debug("Getting categories list from Mongo...");
		
		final String uuid = request().getHeader(X_REQUEST_UUID);
        final Lang lang = Util.getLanguage(DEFAULT_LANG, request()
                .acceptLanguages(), Lang.availables(Play.current()));
        AppLogger.debug(uuid, "The request language is - " + lang.code());
    	
    	Map<String, String[]> formData = request().body().asFormUrlEncoded();

    	String[] query = formData.get("data");
    	
    	return read(connectionName, collectionName, query, uuid, lang);
	}
	
	public static Promise<Result> addCategory() {
		
		Logger.debug("Adding category to the list in Mongo...");
		
		final String uuid = request().getHeader(X_REQUEST_UUID);
        final Lang lang = Util.getLanguage(DEFAULT_LANG, request()
                .acceptLanguages(), Lang.availables(Play.current()));
        AppLogger.debug(uuid, "The request language is - " + lang.code());
    	
    	Map<String, String[]> formData = request().body().asFormUrlEncoded();
    	
    	String[] data = formData.get("data");
    	
    	return create(connectionName, collectionName, data, uuid, lang);
	}
	
}
