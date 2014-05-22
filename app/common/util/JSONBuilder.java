/**
* JSONBuilder.java
*
* Utility methods to convert JSON to any POJO type and vice-versa.
*
* Copyright(c) 2014 Equinix, Inc.  All Rights Reserved.
* This software is the proprietary information of Equinix.
*
*/
package common.util;

import java.util.List;

import org.json.JSONArray;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONBuilder<T> {

	private final ObjectMapper objectMapper;
	
	public JSONBuilder(){
		objectMapper=new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
	}
	
	public T createObjectFromJSON(JsonNode node,Class<T> clazz )throws IllegalArgumentException{
		return objectMapper.convertValue(node,clazz);
	}
	
	public JsonNode convertObjectToJSON(T obj){
		return objectMapper.convertValue(obj,JsonNode.class);
	}
	
	public JSONArray convertListToJSON(List<T>results){
		JSONArray arr=new JSONArray();
	    for(T t:results)
	    	arr.put(t);
	    return arr;       
	        
	}
	
}

