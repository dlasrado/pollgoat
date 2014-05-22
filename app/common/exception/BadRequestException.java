package common.exception;

import com.fasterxml.jackson.databind.JsonNode;

@SuppressWarnings("serial")
public class BadRequestException extends BaseException{

	public BadRequestException(){
		super();
	}
	
	public BadRequestException(String msg){
		super(msg);
	}

	public BadRequestException(JsonNode node) {
    		super.setJsonMessage(node);
	}
	
	
}
