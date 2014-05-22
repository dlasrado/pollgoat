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

public class ServiceUnavailableException extends BaseException {


    private static final long serialVersionUID = 4099567789048236469L;

	public ServiceUnavailableException(){
		super();
	}
	
	public ServiceUnavailableException(String msg){
		super(msg);
	}
	
}
