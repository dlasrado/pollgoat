package common.exception;

import com.fasterxml.jackson.databind.JsonNode;

public class BaseException extends Exception {

    private static final long serialVersionUID = -3742342522381296593L;

    private JsonNode          jsonMessage;

    private String            messageText;

    private String            errCode;

    public BaseException() {
        super();
    }

    public BaseException(String msg, String code) {
        this.messageText = msg;
        this.errCode = code;
    }

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(JsonNode node) {
        this.jsonMessage = node;
    }

    /**
     * @return the jsonText
     */
    public JsonNode getJsonMessage() {
        return jsonMessage;
    }

    /**
     * @param jsonText
     *            the jsonText to set
     */
    public void setJsonMessage(JsonNode jsonText) {
        this.jsonMessage = jsonText;
    }

    /**
     * @return the errCode
     */
    public String getErrCode() {
        return errCode;
    }

    /**
     * @return the messageText
     */
    public String getMessageText() {
        return messageText;
    }

}
