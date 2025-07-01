package com.dailylearn.fileUpload.resp;

public class FileUploadResponse {
    private boolean success;
    private String message;
    private Object data;

    public FileUploadResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static FileUploadResponse success(String message, Object data) {
        return new FileUploadResponse(true, message, data);
    }

    public static FileUploadResponse success(String message) {
        return new FileUploadResponse(true, message, null);
    }

    public static FileUploadResponse error(String message) {
        return new FileUploadResponse(false, message, null);
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
} 