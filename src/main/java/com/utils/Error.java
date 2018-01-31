package com.utils;

public class Error
{
    private String message;
    private String code;
    private Integer status;

    public Error(){}

    public Error(String message, String code, Integer status)
    {
        this.message = message;
        this.code = code;
        this.status = status;
    }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }
}
