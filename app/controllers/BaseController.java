/**
 * 
 */
package controllers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import com.fasterxml.jackson.databind.JsonNode;

import common.exception.AuthenticationFailedException;
import common.exception.BadRequestException;
import common.exception.BaseException;
import common.exception.GateWayTimeoutException;
import common.exception.RecordNotFoundException;
import common.exception.ServiceUnavailableException;
import common.resource.ErrorResponse;
import common.util.AppConfig;
import common.util.AppConstants;
import common.util.AppLogger;
import common.util.JSONBuilder;
import common.util.Util;
import play.api.i18n.Lang;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;
/**
 * @author dlasrado
 *
 */
public class BaseController extends Controller {

	
	protected static final String                     S1001_GATEWAYTIMEOUT      = "S1001";
    protected static final String                     S1002_SERVICEUNAVAILABLE  = "S1002";
    protected static final String                     S1000_INTERNALSERVERERROR = "S1000";
    protected static final String                     S1004_BADREQUEST          = "S1004";

    protected static final String                     JSON_NODE_RECORDS         = "records";
    protected static final int                        TIMEOUT_IN_MILLIS         = 60000;
    // protected static Duration duration = new FiniteDuration(
    // TIMEOUT_IN_MILLIS,
    // TimeUnit.MILLISECONDS);

    protected static final AppConfig                 CONFIGLOADER              = AppConfig
                                                                                        .getInstance();

    protected static final JSONBuilder<ErrorResponse> JSONBUILDERERROR          = new JSONBuilder<ErrorResponse>();

    protected static boolean                          authenticated;

    protected static final Boolean                    OAUTH                     = Boolean
                                                                                        .parseBoolean(CONFIGLOADER
                                                                                                .get(AppConstants.APP_OAUTH_ENABLED));

    protected static final Lang                       DEFAULT_LANG              = new Lang(
                                                                                        CONFIGLOADER
                                                                                                .get(AppConstants.CONFIG_LANGUAGE_CODE),
                                                                                        CONFIGLOADER
                                                                                                .get(AppConstants.CONFIG_LANGUAGE_COUNTRY));
    
    private static final String X_FASE_REQUEST_UUID         = "X-FASE-REQUEST-UUID";
    

    protected static Result authorize() {
        
        final String uuid = request().getHeader(X_FASE_REQUEST_UUID);
        AppLogger.info(uuid, "OAUTH ENABLED :" + OAUTH);
        if (!OAUTH) {
            return null;
        }
        String authHeader = request().getHeader("Authorization");

        AppLogger.debug(uuid, "AUTH HEADER :" + authHeader);

        if (Util.isNullOrEmpty(authHeader)) {
            ErrorResponse failure = new ErrorResponse(AppConstants.BLANK,
                    AppConstants.AUTHENTICATION_FAILED_ERROR_CODE,
                    AppConstants.DEVELOPER_INVALID_REQUEST,
                    AppConstants.AUTHORIZATION_FAILED);
            return unauthorized(JSONBUILDERERROR.convertObjectToJSON(failure));
        }

        return null;

    }

    /**
     * Gets the fields list from request
     * 
     * @param name
     * @return
     */
    protected static Set<String> getFieldSet(String name) {
        Map<String, String[]> queryParams = request().queryString();
        String[] array = queryParams.get(name);
        HashSet<String> fields = new HashSet<String>();

        // tokenize the first element.
        StringTokenizer st = null;

        if (array != null) {
            st = new StringTokenizer(array[0], ",");

            while (st.hasMoreTokens()) {
                fields.add(st.nextToken());
            }
        }
        return fields;
    }

    /**
     * Creates a webservice request
     * 
     * @param url
     * @return
     */
    protected static WS.WSRequestHolder prepareRequest(String url, Lang lang) {
        
        final String uuid = request().getHeader(X_FASE_REQUEST_UUID);
        WS.WSRequestHolder ws = new WS.WSRequestHolder(url);

        includeQueryParameters(ws);

        includeHeaders(ws, lang);

        AppLogger.debug(uuid, "Backend URL : " + ws.getUrl());

        return ws;

    }

    /**
     * Include headers for FASE service call
     * 
     * @param ws
     */
    protected static void includeHeaders(WSRequestHolder ws, Lang lang) {

        ws.setHeader(AppConstants.HTTP_AUTHORIZATION_HEADER, request()
         .getHeader(AppConstants.HTTP_AUTHORIZATION_HEADER));

        ws.setHeader(AppConstants.HTTP_LANGUAGE_HEADER, lang.code());
        /*ws.setHeader("instance_url", "https://equinix--ECOUAT.cs30.my.salesforce.com");
        ws.setHeader("Authorization", "Bearer 00Dn0000000D48C!AR8AQHSUHh20D5KAfFBj97YYPKfTya2jnDVUCqqrAL8fk6VjqKtZmtszSdQAQHewjgTYf0f.0epcNeS87ZcjjTNPOSxBPvtX");
        */
        ws.setHeader(X_FASE_REQUEST_UUID, request().getHeader(X_FASE_REQUEST_UUID));
        
    }

    /**
     * Includes all query parameters to be passed to FASE
     * 
     * @param ws
     */
    private static void includeQueryParameters(WS.WSRequestHolder ws) {
        final String uuid = request().getHeader(X_FASE_REQUEST_UUID);
        Map<String, String[]> queryStringMap = request().queryString();

        Iterator<Entry<String, String[]>> i = queryStringMap.entrySet()
                .iterator();

        while (i.hasNext()) {
            Entry<String, String[]> entry = (Entry<String, String[]>) i.next();
            String key = entry.getKey();
            String[] vals = entry.getValue();
            for (String s : vals) {
                AppLogger.debug(uuid, key + "=" + s);
                ws.setQueryParameter(key, s);
            }
        }

    }

    /**
     * Get response from FASE service as byte[]. This request is not cached.
     * 
     * @param url
     * @return
     * 
     *         protected static Promise<byte[]> getByteArray(String url, Lang
     *         lang) { WS.WSRequestHolder req = prepareRequest(url, lang);
     *         Promise<Response> result = getResponseAsPromise(req);
     * 
     *         Promise<byte[]> finalResult = result .map(new Function<Response,
     *         byte[]>() { public byte[] apply(Response response) throws
     *         FaseException { handleResponseError(response, null); return
     *         response.asByteArray(); } });
     * 
     *         return finalResult; }
     */
    /**
     * Get response from FASE service
     * 
     * @param url
     * @return
     */
    // private static Promise<Response> getResponseAsPromise(
    // WS.WSRequestHolder request) {

    // return request.get();

    // }

    /**
     * Get response from FASE service as JsonNode
     * 
     * @param url
     * @return
     * 
     *         protected static Promise<JsonNode> getJsonNode(String url, final
     *         String node, Lang lang) {
     * 
     *         final long startDateTime = System.currentTimeMillis(); boolean
     *         cached = false; WS.WSRequestHolder req = prepareRequest(url,
     *         lang); // Check cache final CacheClient cache = new
     *         CacheClient(request().uri(), request() .getQueryString("uid"),
     *         req.getQueryParameters(), request() .headers(), lang);
     *         Promise<Response> result = cache.getFromCache(); if (result ==
     *         null) { result = getResponseAsPromise(req); } else { cached =
     *         true; } final boolean fetchFromCache = cached;
     * 
     *         Promise<JsonNode> finalResult = result .map(new
     *         Function<Response, JsonNode>() { public JsonNode apply(Response
     *         response) throws FaseException { JsonNode jnode =
     *         response.asJson(); if (Logger.isDebugEnabled()) { long
     *         endDateTime = System.currentTimeMillis(); long totalTime =
     *         endDateTime - startDateTime;
     *         Logger.debug("FASE API Response time (MilliSec) : " + totalTime);
     *         } if (!fetchFromCache) { handleResponseError(response, jnode); //
     *         Add to cache cache.addToCache(jnode); } return jnode.get(node); }
     *         });
     * 
     *         return finalResult; }
     */
    /**
     * Get response from FASE as JsonNode
     * 
     * @param url
     * @param node
     * @return
     * 
     *         protected static Promise<JsonNode> getJsonNode(String url, Lang
     *         lang) { String node = JSON_NODE_RECORDS; return getJsonNode(url,
     *         node, lang); }
     */
    /**
     * Creates appropriate Exception based on response
     * 
     * @param wsResponse
     * @return
     * @throws BaseException
     */
    static void handleResponseError(Response wsResponse, JsonNode resp)
            throws BaseException {
        final String uuid = request().getHeader(X_FASE_REQUEST_UUID);
        AppLogger.debug(uuid, "Response status from FASE : " + wsResponse.getStatus());
        int status = wsResponse.getStatus();
        /*if (status != OK && resp != null) {
            Logger.error("Error Status Message :: "
                    + wsResponse.getStatusText());
            Logger.error("Error Response :: " + resp);
        }*/
        switch (status) {

        case OK:
            return;

        case UNAUTHORIZED:
            throw new AuthenticationFailedException(
                    AppConstants.AUTHENTICATION_FAILED);

        case NOT_FOUND:
            JsonNode nf = logErrorJson(wsResponse,resp);
            throw new RecordNotFoundException(nf);

        case GATEWAY_TIMEOUT:
            throw new GateWayTimeoutException(AppConstants.GATEWAY_TIMEOUT);

        case BAD_REQUEST:
            JsonNode br = logErrorJson(wsResponse,resp);
            throw new BadRequestException(br);

        case SERVICE_UNAVAILABLE:            
            throw new ServiceUnavailableException(
                    AppConstants.SERVICE_UNAVAILABLE);

        default:
            throw new BaseException(AppConstants.INTERNAL_SERVER_ERROR);

        }
    }
    
    private static JsonNode logErrorJson(Response wsResponse, JsonNode resp) {
        final String uuid = request().getHeader(X_FASE_REQUEST_UUID);
        AppLogger.error(uuid, "Error Status Message :: "
                + wsResponse.getStatusText());
        AppLogger.error(uuid, "Error Response :: " + resp);
        return resp;
    }

    /**
     * Query FASE SFDC connect
     * 
     * @param query
     * @return
     */
    /*protected static Promise<JsonNode> queryFase(String query, Lang lang) {
        
        final String uuid = request().getHeader(X_FASE_REQUEST_UUID);
        final long startDateTime = System.currentTimeMillis();

        String url = AppConfig.getInstance().get(AppConstants.FASE_QUERY_URL);
        WS.WSRequestHolder request = new WSRequestHolder(url);
        request.setQueryParameter("query", query);
        request.setTimeout(TIMEOUT_IN_MILLIS);
        includeHeaders(request, lang);
        // boolean cached = false;
        // Check cache
        // final CacheClient cache = new CacheClient(request().uri(), request()
        // .getQueryString("uid"), request.getQueryParameters(), request()
        // .headers(), lang);
        // Promise<Response> optyResult = cache.getFromCache();
        // if (optyResult == null) {
        // // Get from SFDC connect
        // optyResult = request.get();
        // } else {
        // cached = true;
        // }
        // final boolean fetchFromCache = cached;
        Promise<JsonNode> result = request.get().map(
                new Function<Response, JsonNode>() {
                    public JsonNode apply(Response response)
                            throws BaseException {
                        JsonNode jnode = response.asJson();
                        if (AppLogger.isDebugEnabled()) {
                            long endDateTime = System.currentTimeMillis();
                            long totalTime = endDateTime - startDateTime;
                            AppLogger.debug(uuid, "FASE API Response time (MilliSec) queryFase : "
                                    + totalTime);
                        }
                        // if (!fetchFromCache) {
                        handleResponseError(response, jnode);
                        // add to cache
                        // cache.addToCache(jnode);
                        // }
                        return jnode.get(JSON_NODE_RECORDS);
                    }
                });

        return result;
    }*/

    /**
     * Checks if the query is a search query
     */
    /*protected static boolean isSearchQuery() {
        String searchTerm = request().getQueryString(
        		AppConstants.REQUEST_PARAM_SEARCH_TERM);
        String searchString = request().getQueryString(
        		AppConstants.REQUEST_PARAM_SEARCH_STRING);

        if (!Utils.isNullOrEmpty(searchTerm)
                && !Utils.isNullOrEmpty(searchString)) {
            return true;
        } else {
            return false;
        }

    }*/


}
