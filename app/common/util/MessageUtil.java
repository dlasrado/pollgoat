/**
 * MessageUtil.java
 *
 * Provides i18n support to user messages.
 *
 * Copyright(c) 2014 Equinix, Inc.  All Rights Reserved.
 * This software is the proprietary information of Equinix.
 *
 */
package common.util;

import java.util.ArrayList;

import play.api.i18n.Lang;
import play.i18n.Messages;

import common.resource.ErrorResponse;
import com.fasterxml.jackson.databind.JsonNode;

public class MessageUtil {

	protected static final JSONBuilder<ErrorResponse> jsonBuilderError = new JSONBuilder<ErrorResponse>();

	/**
	 * Creates error response for an error code
	 * 
	 * @param errorCode
	 * @param uuid
	 * @param lang
	 * @return
	 */
	public static JsonNode getErrorResponse(String errorCode, String uuid,
	        Lang lang) {
		ArrayList<String> msg = getProperty(errorCode, lang);
		return getErrorResponseJson(errorCode + uuid, msg.get(0), msg.get(1),
		        msg.get(2));
	}

	/**
	 * Creates error response after substitution based on the error code
	 * 
	 * @param errorCode
	 * @param id
	 * @param uuid
	 * @param lang
	 * @return
	 */
	public static JsonNode getErrorResponse(String errorCode, String id,
	        String uuid, Lang lang) {
		ArrayList<String> msg = getProperty(errorCode, id, lang);
		return getErrorResponseJson(errorCode + uuid, msg.get(0), msg.get(1),
		        msg.get(2));
	}

	/**
	 * Creates a JSON error response
	 * 
	 * @param errorCode
	 * @param errorTitle
	 * @param errorMsg
	 * @param devMsg
	 * @return
	 */
	public static JsonNode getErrorResponseJson(String errorCode,
	        String errorTitle, String errorMsg, String devMsg) {
		ErrorResponse failure = new ErrorResponse.ErrorResponseBuilder(
		        errorCode).title(errorTitle).errorMessage(errorMsg)
		        .developerMessage(devMsg).build();
		return jsonBuilderError.convertObjectToJSON(failure);
	}

	/**
	 * Gets message from property
	 * 
	 * @param errorCode
	 * @param lang
	 * @return
	 */
	public static ArrayList<String> getProperty(String errorCode, Lang lang) {

		ArrayList<String> al = new ArrayList<String>();

		al.add(Messages.get(lang, errorCode + ".title"));
		al.add(Messages.get(lang, errorCode + ".errorMsg"));
		al.add(Messages.get(lang, errorCode + ".devMsg"));

		return al;
	}

	/**
	 * Gets message from property with substitution
	 * 
	 * @param errorCode
	 * @param id
	 * @param lang
	 * @return
	 */
	public static ArrayList<String> getProperty(String errorCode, String id,
	        Lang lang) {
		ArrayList<String> al = new ArrayList<String>();

		al.add(Messages.get(lang, errorCode + ".title"));
		al.add(Messages.get(lang, errorCode + ".errorMsg"));
		al.add(Messages.get(lang, errorCode + ".devMsg", id));

		return al;
	}

	/**
	 * Constructs message for logging error
	 * @param errorCode
	 * @param uuid
	 * @param msg
	 * @return
	 */
	public static String getLogMessage(String errorCode, String uuid,
	        String msg) {

		return errorCode + uuid + AppConstants.SPACE + msg;
	}

	/**
	 * Constructs message for logging error
	 * @param uuid
	 * @param msg
	 * @return
	 */
	public static String getLogMessage(String uuid, String msg) {
		return uuid + AppConstants.SPACE + msg;
	}
}
