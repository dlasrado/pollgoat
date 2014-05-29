/**
* ErrorResponse.java
*
* Used to construct the Error respone JSON. 
*
* Every application built over FASE must use this to create JSON error messages.
* 
* Copyright(c) 2014 Equinix, Inc.  All Rights Reserved.
* This software is the proprietary information of Equinix.
*
*/
package common.resource;

import common.util.AppConstants;
import play.api.mvc.SimpleResult;
import play.libs.Json;
import play.libs.F.*;
import play.mvc.Result;
import scala.concurrent.Future;
import views.html.defaultpages.unauthorized;

public class ErrorResponse {

	private String errorTitle;

	private String errorCode;

	private String developerMessage;

	private String errorMessage;



	public ErrorResponse(String errorTitle, String errorCode,
			String developerMessage, String errorMessage) {
		super();
		this.errorTitle = errorTitle;
		this.errorCode = errorCode;
		this.developerMessage = developerMessage;
		this.errorMessage = errorMessage;
	}
	
	public ErrorResponse(String errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = message;
	}


	public String getErrorTitle() {
		return errorTitle;
	}


	public void setErrorTitle(String errorTitle) {
		this.errorTitle = errorTitle;
	}


	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}


	public String getErrorMessage() {
		return errorMessage;
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * This is a static inner class to build its outer class instance ErrorResponse.
	 * It uses builder pattern to build the object step by step.
	 */
	public static class ErrorResponseBuilder {

		private String errorTitle;

		private String errorCode;

		private String developerMessage;

		private String errorMessage;

		public ErrorResponseBuilder(String errorCode) {
			this.errorCode = errorCode;
		}

		public ErrorResponseBuilder title(String errorTitle) {

			this.errorTitle = errorTitle;

			return this;

		}

		public ErrorResponseBuilder errorMessage(String errorMessage) {

			this.errorMessage = errorMessage;

			return this;

		}

		public ErrorResponseBuilder developerMessage(String developerMessage) {

			this.developerMessage = developerMessage;

			return this;

		}

		public ErrorResponse build() {

			return new ErrorResponse(this);

		}
		
		public Promise<Result> getErrorResult() {
			
			return Promise.promise(
					new Function0<Result>() {
                @Override
                public Result apply() throws Throwable {
                    return new Result() {
						
						@Override
						public Future<SimpleResult> getWrappedResult() {
							// TODO Auto-generated method stub
							return null;
						}
					};
                }
            });
		}

	}


	private ErrorResponse(ErrorResponseBuilder builder) {

		this.errorCode = builder.errorCode;

		this.errorTitle = builder.errorTitle;

		this.developerMessage = builder.developerMessage;

		this.errorMessage = builder.errorMessage;

	}	
	
	

}