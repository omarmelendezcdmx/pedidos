package com.empresa.pedidos.infrastructure.adapter.in.web.dto;

public class ApiResponse<T> {

    private T data;
    private int code;
    private String description;
    private boolean success;

    public ApiResponse(
            T data,
            int code,
            String description,
            boolean success) {

        this.data = data;
        this.code = code;
        this.description = description;
        this.success = success;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                data,
                200,
                "",
                true
        );
    }

    public static <T> ApiResponse<T> error(
            int code,
            String description) {

        return new ApiResponse<>(
                null,
                code,
                description,
                false
        );
    }

    public T getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSuccess() {
        return success;
    }
}