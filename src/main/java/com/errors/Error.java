package com.errors;

public class Error
{
    private String message;
    private String status;
    private int code;

    public static final String EMPTY_FIElD_MESSAGE = "a field is empty";
    public static final String EMPTY_FIElD_STATUS = "400 bad request";

    public static final String DUPLICATED_ENTITY_MESSAGE = "already exist in system";
    public static final String DUPLICATED_ENTITY_STATUS = "409 conflict";
    ///ВСЕ BAD REQUEST

    public Error(){}

    public Error(String message, int code)
    {
        this.message = message;
        this.code = code;
    }

    public Error(String message, String status, int code) {
        this.message = message;
        this.status = status;
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
