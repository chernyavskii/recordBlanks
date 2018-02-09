package com.errors;

public class Error
{
    private String message;
    private String status;
    private int code;

    public static final String ENTITY_NOT_FOUND_MESSAGE = "entity not found";
    public static final String ENTITY_NOT_FOUND_STATUS = "404 not found";

    public static final String LIST_ENTITIES_EMPTY_MESSAGE = "list of entities are empty";
    public static final String LIST_ENTITIES_EMPTY_STATUS = "404 not found";

    public static final String SERVER_ERROR_MESSAGE = "server error";
    public static final String SERVER_ERROR_STATUS = "500 Internal Server Error";

    public static final String EMPTY_FIElD_MESSAGE = "a field is empty";
    public static final String EMPTY_FIElD_STATUS = "400 bad request";

    /*USER SERVICE*/
    public static final String DUPLICATED_ENTITY_MESSAGE = "already exist in system";
    public static final String DUPLICATED_ENTITY_STATUS = "409 conflict";

    public static final String PASSWORD_LENGTH_MESSAGE = "a field must be bellow 8 characters";
    public static final String PASSWORD_LENGTH_STATUS = "400 bad request";

    public static final String USERNAME_LENGTH_MESSAGE = "a field must be bellow 5 characters";
    public static final String USERNAME_LENGTH_STATUS = "400 bad request";

    public static final String LOGIN_INCORRECT_MESSAGE = "login or password is incorrect";
    public static final String LOGIN_INCORRECT_STATUS = "403 forbidden";

    public static final String USER_DO_NOT_LOGGEDIN_MESSAGE = "user do not logged-in";
    public static final String USER_DO_NOT_LOGGEDIN_STATUS = "404 not found";

    /*AGENT SERVICE*/

    public static final String UNAUTHORIZED_MESSAGE = "Unauthorized at system";
    public static final String UNAUTHORIZED_STATUS = "401 unauthorized";

    public static final String UNP_LENGTH_MESSAGE = "a field must be bellow 9 characters";
    public static final String UNP_LENGTH_STATUS = "400 bad request";



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