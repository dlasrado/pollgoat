package common.exception;

import common.exception.BaseException;

public class InvalidConfigurationException extends BaseException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4411036793266571145L;

	
	public InvalidConfigurationException(){
		super();
	}
	
	public InvalidConfigurationException(String message){
		super(message);
	}
	
}
