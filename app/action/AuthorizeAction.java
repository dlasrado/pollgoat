/**
 * AuthorizeAction.java
 *
 * Action class to make pre-request checks 
 *
 * Copyright(c) 2014 Equinix, Inc.  All Rights Reserved.
 * This software is the proprietary information of Equinix.
 *
 */

package action;

import play.Logger;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;

import common.resource.ErrorResponse;
import common.util.JSONBuilder;
import common.util.AppConstants;
import common.util.Util;
import com.fasterxml.jackson.databind.JsonNode;

public class AuthorizeAction extends Action.Simple {

    private static final JSONBuilder<ErrorResponse> JSON_BUILDER = new JSONBuilder<ErrorResponse>();

    @Override
    public Promise<SimpleResult> call(Context context) {
        String authHeader = context.request().getHeader("Authorization");
        Logger.debug("Auth header =" + authHeader);
        Promise<SimpleResult> result = null;
        if (Util.isNullOrEmpty(authHeader)) {
            final ErrorResponse error = new ErrorResponse(AppConstants.BLANK,
            		AppConstants.AUTHENTICATION_FAILED_ERROR_CODE,
            		AppConstants.DEVELOPER_INVALID_REQUEST,
            		AppConstants.AUTHORIZATION_FAILED);
            Promise<JsonNode> res = Promise.promise(new Function0<JsonNode>() {
                public JsonNode apply() {
                    return JSON_BUILDER.convertObjectToJSON(error);
                }
            });
            result = res.map(new Function<JsonNode, SimpleResult>() {
                public SimpleResult apply(JsonNode obj) {
                    return unauthorized(obj);
                }
            });
        }
        return result;
    }

}
