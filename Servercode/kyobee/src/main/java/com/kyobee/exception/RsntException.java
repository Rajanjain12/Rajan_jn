package com.kyobee.exception;

/**
 * This is RPMException class extending Exception class.
 * RPMException will thrown from Actions and methods.
 * @author rohit
 */
public class RsntException extends Exception {

    private static final long serialVersionUID = 5037827424063864222L;

    private String errorMessage;

    private Integer errorNumber;

    public RsntException(final Throwable cause) {
        super(cause);
    }

    public RsntException() {
        super();
    }

    public RsntException(final String message, final Throwable cause) {
        super(message, cause);
        this.errorMessage = message;
    }

    public RsntException(final String message) {
        super(message);
        this.errorMessage = message;
    }

    public RsntException(final String message, final Integer errNumber) {
        super(message);
        this.errorNumber = errNumber;
        this.errorMessage = message;
    }

    public RsntException(final Exception exception) {
        super(exception.getMessage(), exception);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorNumber(final Integer errorNumber) {
        this.errorNumber = errorNumber;
    }

    public Integer getErrorNumber() {
        return errorNumber;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
