package com.jms.scan;

/**
 * 自定义Exception处理
 * 
 * @date 2014-3-26
 */
public class MyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7321580923777060753L;

	private String detailMessage;

	private int error_code;

	private Throwable throwable;

	/**
	 * Constructs a new {@code Exception} that includes the current stack trace.
	 */
	public MyException() {
	}

	/**
	 * Constructs a new {@code Exception} with the current stack trace and the
	 * specified detail message.
	 * 
	 * @param detailMessage
	 *            the detail message for this exception.
	 */
	public MyException(String detailMessage) {
		super(detailMessage);
		this.detailMessage = detailMessage;
	}

	/**
	 * Constructs a new {@code Exception} with the current stack trace, the
	 * specified detail message and the specified cause.
	 * 
	 * @param detailMessage
	 *            the detail message for this exception.
	 * @param throwable
	 *            the cause of this exception.
	 */
	public MyException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		this.detailMessage = detailMessage;
		this.throwable = throwable;
	}

	/**
	 * Constructs a new {@code Exception} with the current stack trace and the
	 * specified cause.
	 * 
	 * @param throwable
	 *            the cause of this exception.
	 */
	public MyException(Throwable throwable) {
		super(throwable);
		this.throwable = throwable;
	}

	public String getDetailMessage() {
		return detailMessage;
	}

	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

}
