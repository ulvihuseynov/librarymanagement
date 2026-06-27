package com.librarymanagementsystem.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
   private PaginationResponse<T> paginationResponse;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, LocalDateTime.now(),null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, LocalDateTime.now(),null);
    }

    public static <T> ApiResponse<T> validateError(String message ,T data) {
        return new ApiResponse<>(false,message, data, LocalDateTime.now(),null);
    }

    public static <T> ApiResponse<T> paginationResponse(String message ,T data,PaginationResponse<T> paginationResponse) {
        return new ApiResponse<>(false,message, data, LocalDateTime.now(),paginationResponse);
    }
}
