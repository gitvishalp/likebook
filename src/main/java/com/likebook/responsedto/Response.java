package com.likebook.responsedto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Response<T> implements Serializable {

	private static final long serialVersionUID = -4383859172188832914L;
	
	    @JsonProperty("Status")
	    private int status;
	    @JsonProperty("Message")
	    private String message;
	    @JsonProperty("Data")
	    @JsonInclude(value = Include.NON_NULL)
	    private T data;
	    @JsonProperty("Timestamp")
	    private Date timestamp;

	    public static final Response<?> SUCCESS = new Response<>(200, "Success");
	    public static final Response<?> NOT_FOUND = new Response<>(404, "Resource not found");
	    public static final Response<?> BAD_REQUEST = new Response<>(400, "Bad Request");
	    public static final Response<?> SERVER_ERROR = new Response<>(500, "Server Error");

	    public Response(int statusCode, String message, T data) {
	        this();
	        this.status = statusCode;
	        this.message = message;
	        this.data = data;
	    }

	    public Response(int statusCode, String message) {
	        this();
	        this.status = statusCode;
	        this.message = message;
	    }

	    public Response(T data) {
	        this(200, "Success");
	        this.data = data;
	    }

	    public Response(T data, String message) {
	        this(200, message);
	        this.data = data;
	    }

	    public int getStatus() {
	        return status;
	    }

	    public void setStatus(int status) {
	        this.status = status;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public T getData() {
	        return data;
	    }

	    public void setData(T data) {
	        this.data = data;
	    }

	    @Override
	    public String toString() {
	        return "Response [statusCode=" + status + ", message=" + message + ", data=" + data + "]";
	    }

	    public Response() {
	        super();
	        this.timestamp = new Date();
	    }

	    public Date getTimestamp() {
	        return timestamp;
	    }

	    public void setTimestamp(Date timestamp) {
	        this.timestamp = timestamp;
	    }

}
