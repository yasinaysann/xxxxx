package com.project.projectx.core.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
public class ApiResponse <T> {
    private Boolean success;
    private String message;
    private HttpStatus status;
    private int statusCode;
    private T data;

    public ApiResponse(Boolean success, String message, HttpStatus status) {
        this.success = success;
        this.message = message;
        this.status = status;
        this.statusCode = status.value();
    }

    public ApiResponse(Boolean success, String message, HttpStatus status, T data) {
        this(success, message, status);
        this.data = data;
    }



    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(Boolean.TRUE, message, HttpStatus.OK, data);
    }

    public static <T> ApiResponse<T> success(String message, HttpStatus status, T data ) {
        return new ApiResponse<>(Boolean.TRUE, message, status, data);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return new ApiResponse<>(Boolean.FALSE, message, status);
    }

    public static <T> ApiResponse<T> error(String message){
        return new ApiResponse<>(Boolean.FALSE, message, HttpStatus.BAD_REQUEST);
    }
}
