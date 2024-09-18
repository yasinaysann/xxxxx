package com.project.projectx.exceptionHandler;

import com.project.projectx.core.model.ApiResponse;
import com.project.projectx.exceptionHandler.exception.AuthenticatedFailedException;
import com.project.projectx.exceptionHandler.exception.UserAlreadyExistException;
import com.project.projectx.exceptionHandler.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler({UserAlreadyExistException.class})
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExist(UserAlreadyExistException exception) {
        ApiResponse<Object> apiResponse = ApiResponse.error(exception.getMessage());
        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNotFoundException exception) {
        ApiResponse<Object> apiResponse = ApiResponse.error(HttpStatus.NOT_FOUND,exception.getMessage());
        return ResponseEntity.status(apiResponse.getStatus())
                .body(apiResponse);
    }


    @ExceptionHandler({AuthenticatedFailedException.class})
    public ResponseEntity<ApiResponse<Object>> handleAuthenticatedFailed(AuthenticatedFailedException exception) {
        ApiResponse<Object> apiResponse = ApiResponse.error(HttpStatus.UNAUTHORIZED,exception.getMessage());
        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }
    //todo kullanıcının giriş yapamadığı her bir durumda 401 kodu dönülecek 403 kodu role veya yetkiye göre verilecek

}
