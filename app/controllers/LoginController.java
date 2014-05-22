package controllers;

import static common.util.Util.replaceNull;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidParameterSpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.JSONException;
import org.json.JSONObject;

import play.Logger;
import play.api.Play;
import play.api.i18n.Lang;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.SimpleResult;
//import scala.concurrent.Await;
import scala.concurrent.Future;

//import com.equinix.fase.fqa.stub.login.AuthInfoValidator;
import common.util.AppConstants;
import common.util.MessageUtil;
import common.util.SecurityUtils;
import common.util.Util;
import common.resource.ErrorResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.ning.http.client.Realm.AuthScheme;

public class LoginController extends BaseController {

	
	private static final SecurityUtils securityUtils = new SecurityUtils();

	@BodyParser.Of(BodyParser.Json.class)
	public static Promise<Result> login() {
		response().setContentType(AppConstants.JSON_CONTENT_TYPE);
		JsonNode json = request().body().asJson();
		return verify(json);		
	}

	protected static Promise<Result> verify(JsonNode json) {
		
		final Lang lang = Util.getLanguage(AppConstants.DEFAULT_LANG, request().acceptLanguages(), Lang.availables(Play.current()));

		if (json == null) {

			 return throwError(AppConstants.AUTHENTICATION_FAILED_ERROR_CODE, 
					 AppConstants.DEVELOPER_INVALID_REQUEST, lang);
		}
			
		if (json.findValue("userName") == null) {
							
			return throwError(AppConstants.AUTHENTICATION_FAILED_ERROR_CODE, 
					 AppConstants.DEVELOPER_INVALID_REQUEST, lang);
		}
		String name = json.findValue("userName").asText();
		
		if (json.findValue("password") == null) {
			return throwError(AppConstants.AUTHENTICATION_FAILED_ERROR_CODE, 
					 AppConstants.DEVELOPER_INVALID_REQUEST, lang);
		}
		String jsonPwd = json.findValue("password").asText();
		
		Logger.debug("USER :::::::::::::: "+name+"|"+jsonPwd);
		
		String pwd = null;
		try {
			
			String pwdStr = new String(
					securityUtils.decryptPassword(securityUtils.decodeBase64(jsonPwd.getBytes("UTF-8"))), "UTF-8");
			String encryptedUser = Util.getUser(pwdStr);
			if(!name.equals(encryptedUser)) {

				return throwError(AppConstants.AUTHENTICATION_FAILED_ERROR_CODE, 
						 AppConstants.DEVELOPER_INVALID_CREDENTIALS, lang);
				
				Promise<JsonNode> res = Promise
                        .promise(new Function0<JsonNode>() {
                            public JsonNode apply() {
                                return getErrorResponse(
                                        Constants.ERRCODE_FILTERSLENGTH_EXCEEDED,
                                        "" + Constants.FILTERSLENGTH_LIMIT,
                                        errorId, lang);
                            }
                        });
			}
			pwd = Util.getPassword(pwdStr);
						
		} catch (Exception e) {
			ErrorResponse failure = new ErrorResponse(name,
					AppConstants.AUTHENTICATION_FAILED_ERROR_CODE,
					AppConstants.DEVELOPER_INVALID_CREDENTIALS,
					AppConstants.AUTHENTICATION_FAILED );
			Logger.error("Login : Unable to decrypt the user credentials : "+e.getMessage());
			return unauthorized(Json.toJson(failure)).as(AppConstants.JSON_CONTENT_TYPE);
			
		}
		
		try {
			Object response = validateAuthInfo(name, pwd);
			return ok(response.toString()).as(AppConstants.JSON_CONTENT_TYPE);
		} catch (Exception e) {
			Logger.error("Authentication failed"+e.getMessage());
			ErrorResponse failure = new ErrorResponse(name,AppConstants.AUTHENTICATION_FAILED_ERROR_CODE,AppConstants.AUTHENTICATION_FAILED,AppConstants.AUTHENTICATION_FAILED);
            //TODO - propagate proper message
			return unauthorized(Json.toJson(failure)).as(AppConstants.JSON_CONTENT_TYPE);
			
		}
		
	}
	
	
	private static Promise<Result> throwError(final String errorCode, final String errorId, final Lang lang) {
		
		Promise<JsonNode> res = Promise
                .promise(new Function0<JsonNode>() {
                    public JsonNode apply() {
                        return MessageUtil.getErrorResponse(
                        		errorCode,
                        		errorId,
                                Util.getUniqueId(), lang);
                    }
                });
		
        return res.map(new Function<JsonNode, Result>() {
            public SimpleResult apply(JsonNode obj) {
                return badRequest(obj).as("application/json");
            }
        });
    }
	
	
	
	/**
	 * Authenticates user with API gateway
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static Object validateAuthInfo(String username, String password)
			throws Exception {
		
		Map<String, String> response = null;

			Logger.debug("OAuth Call...");
			long startDateTime = 0;
			if(Logger.isDebugEnabled()){
				startDateTime = System.currentTimeMillis();
			}
			//Actual Authentication call to APIGatweway
			response = parseResponse(connectAPIGateway(
					configLoader.get(AppConstants.FASE_GATEWAY_URL), username,
					password), username);
			if(Logger.isDebugEnabled()) {
				long endDateTime = System.currentTimeMillis();
				long totalTime = endDateTime - startDateTime;
				Logger.debug("fetch time (MilliSec) : "+totalTime);
			}
		
		JSONObject result = new JSONObject(response);
		return result;
	}
	
	/**
	 * Constructs the login call
	 * 
	 * @param url
	 * @return
	 */
	protected static WS.WSRequestHolder prepareRequest(String url,
			String userName, String password) {

		WS.WSRequestHolder ws = new WS.WSRequestHolder(url);
		/*ws.setQueryParameter("grant_type",
				AppConstants.FASE_OAUTH_GRANTTYPE_PASSWORD);
		ws.setQueryParameter("username", userName);
		ws.setQueryParameter("password", password);
		ws.setQueryParameter("scope", AppConstants.FASE_OAUTH_SCOPE);*/

		ws.setAuth(configLoader.get(AppConstants.FASE_OAUTH_CONSUMERKEY),
				configLoader.get(AppConstants.FASE_OAUTH_CONSUMERSECRET),
				AuthScheme.BASIC);
		

		ws.setContentType(AppConstants.FORM_URLENCODED_CONTENT_TYPE);
		return ws;

	}

	/**
	 * Connects to Payment gateway and authenticates user The access token and
	 * expiry time is returned after successfull loginl
	 **/
	protected static JsonNode connectAPIGateway(String url, String user,
			String pass) throws Exception {
		String dataContent = "grant_type="+AppConstants.FASE_OAUTH_GRANTTYPE_PASSWORD+"&username="+user+"&password="+pass+"&scope="+AppConstants.FASE_OAUTH_SCOPE;
		Promise<Response> faseResponse = prepareRequest(url, user, pass).post(
				dataContent).recover(new Function<Throwable, Response>() {
			@Override
			public Response apply(Throwable e) throws Throwable {
				//Logger.debug("Error API Gateway.");
				return this.apply(e);
			}

		});

		Future<Response> futureResponse = faseResponse.wrapped();
		Response wsResponse = Await.result(futureResponse, duration);
		if(wsResponse.getStatus()!=200) {
			Logger.error("AuthenticaTION FAILED");
			throw new Exception(wsResponse.getStatusText());
		}
		Logger.debug("GATEWAY CONNECTION STATUS : " + wsResponse.getStatus());

		JsonNode jsonNode = wsResponse.asJson();
		return jsonNode;
	}

	/**
	 * Parse the Authentication response
	 * 
	 * @param input
	 * @return
	 * @throws JSONException
	 */
	private static Map<String, String> parseResponse(JsonNode node, String username)
			throws JSONException {

		Map<String, String> user = new HashMap<String, String>();

		user.put("tokenTimeout", replaceNull(node.findPath("expires_in")
				.asText()));
		user.put("accessToken", replaceNull(node.findPath("access_token")
				.textValue()));
		user.put("displayName", username+" pdehnert");
		user.put("userName", username);
		user.put("firstName", username);
		user.put("lastName", "pdehnert");
		user.put("emailAddress", "pdehnert@equinix.com.c2cdevint");

		return user;
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result encrypt() throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
		response().setContentType(AppConstants.JSON_CONTENT_TYPE);
		JsonNode json = request().body().asJson();
		
		if (json == null) {
			ErrorResponse failure = new ErrorResponse(AppConstants.BLANK,
					AppConstants.INVALID_DATA,
					AppConstants.DEVELOPER_INVALID_REQUEST,
					AppConstants.DEVELOPER_INVALID_REQUEST, AppConstants.BLANK);
			Logger.error("Invalid request !");
			return badRequest(Json.toJson(failure)).as(AppConstants.JSON_CONTENT_TYPE);

		}
			
		if (json.findValue("userName") == null) {
			ErrorResponse failure = new ErrorResponse(AppConstants.BLANK,
					AppConstants.INVALID_DATA,
					AppConstants.DEVELOPER_INVALID_REQUEST,
					AppConstants.DEVELOPER_INVALID_REQUEST, AppConstants.BLANK);
			Logger.error("Encryption - missing credentials ");				
			return badRequest(Json.toJson(failure)).as(AppConstants.JSON_CONTENT_TYPE);
		}
		String name = json.findValue("userName").asText();
		
		if (json.findValue("password") == null) {
			ErrorResponse failure = new ErrorResponse(name,
					AppConstants.INVALID_DATA,
					AppConstants.DEVELOPER_INVALID_REQUEST,
					AppConstants.DEVELOPER_INVALID_REQUEST, AppConstants.BLANK);
			Logger.error("Encryption - missing credentials ");	
			return badRequest(Json.toJson(failure)).as(AppConstants.JSON_CONTENT_TYPE);
		}
		String pwd = json.findValue("password").asText();
		
		String cred = name+":"+pwd;
		byte[] encpass = securityUtils.encryptPassword(cred);
		String s = new String(encpass, "UTF-8");
		Logger.debug("Ency Pass :"+s);		
		
		byte[] encodedpass = securityUtils.encodeBase64(encpass);		
		String es = new String(encodedpass, "UTF-8");
		System.out.println("Enco Pass :"+es);
		
		String resp = "{ \"Base64(AES 256 (username:password))\" : \""+es+"\" }";
		
		return ok(resp).as(AppConstants.JSON_CONTENT_TYPE);
		
	}

}
