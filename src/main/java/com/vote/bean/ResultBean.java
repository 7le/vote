package com.vote.bean;

/**
 * 结果bean
 */
public class ResultBean {
    protected boolean success;
    protected String message;
    protected String token;
    protected Object data;

    public ResultBean() {
    }

    public ResultBean(boolean success,String token, String message,Object data) {
        this.success = success;
        this.token=token;
        this.message = message;
        this.data=data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
