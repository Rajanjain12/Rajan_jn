package com.kyobee.dto.common;

/**
 * This is a generic class that sets the web service response that needs to be
 * sent to Client. The class contains status of response, service identifier for
 * requested service, result, error code and error description in case any error
 * occurs.
 * 
 * @author rohit
 * 
 * @param <T>
 */
public class Response<T> {
	
	/**
	 * This enum indicates the status of web service response whether its (success/failure/partial/sessionTimeout).
	 * @author sshrivastav4
	 *
	 */
	public enum STATUS {
		SUCCESS("SUCCESS"), FAILURE("FAILURE"), PARTIAL("PARTIAL"),SESSIONTIMEOUT("SESSIONTIMEOUT");

		private String status;

		private STATUS(String status) {
			this.status = status;
		}

		@Override
		public String toString() {
			return this.status;
		}
	}

	private String status;

	private String errorCode;

	private String errorDescription;
	
	private String serviceResponseDescription;

	private String serviceIdentifier;

	private T serviceResult;

	public Response() {
	}

	/**
	 * This is a generic constructor that sets the result of requested web service.
	 * @param serviceResult is the web service response that needs to be sent to the client.
	 */
	public Response(T serviceResult) {
		this.serviceResult = serviceResult;
	}

	/**
	 * This is a getter method that gives the status of web service response(success/failure/partial).
	 * @return String the status of response.
	 */
	public String getStatus() {
		return status;
	}

	/**This method sets the status of web service response(success/failure/partial).
	 * @param status indicates whether web service response is success/failure/partial.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * This method is used to get the error code if any error occurs.
	 * @return String which is error code of the error.
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * This method sets the error code if in case any error occurs.
	 * @param errorCode of the error.
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * This method provides a brief description of the error that occurs.
	 * @return String which is error description.
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * This method sets the error description of the error that occurs.
	 * @param errorDescription of the error.
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * This method provides the service identifier of the requested web service.
	 * @return String which tells the identity of the requested service. 
	 */
	public String getServiceIdentifier() {
		return serviceIdentifier;
	}

	/**
	 * This method sets the service identifier of the requested web service.
	 * @param serviceIdentifier tells the identity of the requested service.
	 */
	public void setServiceIdentifier(String serviceIdentifier) {
		this.serviceIdentifier = serviceIdentifier;
	}

	/**
	 * This is a generic type method that is used to get the response of any web service.
	 * @return T which indicates response of any web service.
	 */
	public T getServiceResult() {
		return serviceResult;
	}

	/**
	 * This is a generic method that is used to set the response of any web service.
	 * @param serviceResult which can be the response of any web service. 
	 */
	public void setServiceResult(T serviceResult) {
		this.serviceResult = serviceResult;
	}

	public String getServiceResponseDescription() {
		return serviceResponseDescription;
	}

	public void setServiceResponseDescription(String serviceResponseDescription) {
		this.serviceResponseDescription = serviceResponseDescription;
	}

}
