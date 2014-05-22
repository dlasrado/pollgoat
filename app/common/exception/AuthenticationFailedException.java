/**
* AuthenticationFailedException.java
*
* Used to notify authentication error
*
* Copyright(c) 2014 Equinix, Inc.  All Rights Reserved.
* This software is the proprietary information of Equinix.
*
*/
package common.exception;

@SuppressWarnings("serial")
public class AuthenticationFailedException extends BaseException {
	
	public AuthenticationFailedException(){
		super();
	}
	
	public AuthenticationFailedException(String msg){
		super(msg);
	}

}
