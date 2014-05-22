/**
* GateWayTimeoutException.java
*
* Used to notify Gateway timed out error
*
* Copyright(c) 2014 Equinix, Inc.  All Rights Reserved.
* This software is the proprietary information of Equinix.
*
*/
package common.exception;

@SuppressWarnings("serial")
public class GateWayTimeoutException extends BaseException {
	
	public GateWayTimeoutException(){
		super();
	}
	
	public GateWayTimeoutException(String msg){
		super(msg);
	}

}
