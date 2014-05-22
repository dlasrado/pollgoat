/**
* FaseConstants.java
*
* FASE constants used accross all applications
*
* Copyright(c) 2014 Equinix, Inc.  All Rights Reserved.
* This software is the proprietary information of Equinix.
*
*/
package common.util;

import play.api.i18n.Lang;

public class AppConstants {
	
	public static final String SPACE = " ";
	
	//Error codes
	public static final String GATEWAY_TIMEOUT = "1503";
	public static final String INTERNAL_SERVER_ERROR = "2001";
	public static final String SERVICE_UNAVAILABLE = "1002";
	
	//HTTP Headers
	public final static String HTTP_LANGUAGE_HEADER = "Accept-Language";	
	public static final String HTTP_AUTHORIZATION_HEADER="Authorization";
	
	//Error Message - TODO - to be externalized
	public static final String AUTHENTICATION_FAILED = "Authentication failed";	
	public static final String NO_RECORDS_FOUND = "No records found.";
	
	public final static String CONFIG_LANGUAGE_CODE = "default.application.lang.code";
	public final static String CONFIG_LANGUAGE_COUNTRY = "default.application.lang.country";
	
	public final static String TIME_STAMP_FORMAT = "ddhhmmssSSS";
	
	public final static String APP_OAUTH_ENABLED = "true";
	
	public final static String AUTHENTICATION_FAILED_ERROR_CODE = "O1001";
	
	public final static String AUTHORIZATION_FAILED = "AUTHORIZATION_FAILED";
	
	public final static String DEVELOPER_INVALID_REQUEST = "Invalid login";
	public final static String DEVELOPER_INVALID_CREDENTIALS = "Invalid Credentials. The username and password does not match";
	
	public final static String BLANK = " ";
	public final static String API_LOGIN = "";
	public final static String API_ENCRYPT = "";
	
	public static final AppConfig CONFIGLOADER      = AppConfig
            													.getInstance();
	
	public final static Lang DEFAULT_LANG = new Lang(
										            CONFIGLOADER
										            .get(AppConstants.CONFIG_LANGUAGE_CODE),
										    CONFIGLOADER
										            .get(AppConstants.CONFIG_LANGUAGE_COUNTRY));
	
	
	public final static String QUERYSTRING_NAME_UID = "REQUEST_UUID";
	public final static String MISSING_MANDATORY_REQ_ATTR_ERROR_CODE = "REQUEST_UUID";
	public static final String     JSON_CONTENT_TYPE                             = "application/json";

    public static final String     PDF_CONTENT_TYPE                              = "application/pdf";

    public static final String     FORM_URLENCODED_CONTENT_TYPE                  = "application/x-www-form-urlencoded";
}
