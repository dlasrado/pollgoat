package action;

import static common.util.MessageUtil.getErrorResponse;
import play.Logger;
import play.api.Play;
import play.api.i18n.Lang;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;




import com.fasterxml.jackson.databind.JsonNode;

import common.util.AppConstants;
import common.util.Util;

public class RequestValidator extends Action.Simple {
    
    @Override
    public Promise<SimpleResult> call(final Context context) throws Throwable {
        
        //MANDATORY UID CHECK
        String uid = context.request().getQueryString(AppConstants.QUERYSTRING_NAME_UID);
        Logger.debug("UID =" + uid);
        Promise<SimpleResult> result = null;
        if (Util.isNullOrEmpty(uid)) {
           
            Promise<JsonNode> res = Promise.promise(new Function0<JsonNode>() {
                public JsonNode apply() {
                    String uuid = Util.getUniqueId();
                    Lang lang = Util.getLanguage(AppConstants.DEFAULT_LANG, context.request()
                            .acceptLanguages(), Lang.availables(Play.current()));
                    return getErrorResponse(AppConstants.MISSING_MANDATORY_REQ_ATTR_ERROR_CODE, AppConstants.QUERYSTRING_NAME_UID, uuid, lang);
                }
            });
            result = res.map(new Function<JsonNode, SimpleResult>() {
                public SimpleResult apply(JsonNode obj) {
                    return badRequest(obj).as(AppConstants.JSON_CONTENT_TYPE);
                }
            });
        }
        return result;

    }

}
