/**
* RecordNotFoundException.java
*
* Used to notify an empty record set.
*
* Copyright(c) 2014 Equinix, Inc.  All Rights Reserved.
* This software is the proprietary information of Equinix.
*
*/
package common.exception;

import com.fasterxml.jackson.databind.JsonNode;

@SuppressWarnings("serial")
public class RecordNotFoundException extends BaseException {
	
	public RecordNotFoundException(){
		super();
	}
	
	public RecordNotFoundException(String msg){
		super(msg);
	}

	public RecordNotFoundException(JsonNode node) {
    		super.setJsonMessage(node);
	}

}
