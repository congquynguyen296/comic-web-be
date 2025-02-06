package com.nettruyen.comic.exception;

import com.nettruyen.comic.dto.response.ApiResponse;
import com.nettruyen.comic.validation.MinAge;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Field;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Lỗi này dùng khi không có một message nào xảy ra khi có lỗi
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<String>> exceptionHandler(Exception e) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<String>> appExceptionHandler(AppException e) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(e.getErrorCode().getCode());
        apiResponse.setMessage(e.getErrorCode().getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatusCode())   // Set HttpStatusCode
                .body(apiResponse);
    }

    // Exception xử lý quyền không đủ quyền để truy cập vào một endpoint (AccessDenied)
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> accessDeniedExceptionHandler(AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        // Cách khác so với 3 cách còn lại
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    // Đây là loại Exception khi sai yêu cầu về các field (Json)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<List<String>>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<String> errorMessages = new ArrayList<>();
        ErrorCode errorCode = ErrorCode.VALIDATION_INVALID;

        // Lặp qua tất cả các lỗi validation
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String fieldName = fieldError.getField();
            String detailedMessage = "Validation failed for field '" + fieldName + "'";

            try {
                // Lấy thông tin class mục tiêu
                Class<?> targetClass = e.getBindingResult().getTarget().getClass();
                Field field = targetClass.getDeclaredField(fieldName);

                // Kiểm tra nếu có @MinAge
                MinAge minAgeAnnotation = field.getAnnotation(MinAge.class);
                if (minAgeAnnotation != null) {
                    int minAge = minAgeAnnotation.value();
                    detailedMessage = "Validation failed for field '" + fieldName + "': " +
                            "You must be at least " + minAge + " years old.";
                }

                // Thêm xử lý cho các annotation khác tại đây (nếu cần)
                // Ví dụ: Custom annotation khác
                // AnotherAnnotation anotherAnnotation = field.getAnnotation(AnotherAnnotation.class);
                // if (anotherAnnotation != null) { ... }
                Size sizeAnnotation = field.getAnnotation(Size.class);
                if (sizeAnnotation != null) {
                    int minLength = sizeAnnotation.min();
                    detailedMessage = "Validation failed for field '" + fieldName + "': " +
                            minLength + " characters is invalid.";
                }

            } catch (NoSuchFieldException | SecurityException ignored) {
                detailedMessage += " (Unable to retrieve detailed information)";
            }

            // Thêm thông báo chi tiết vào danh sách
            errorMessages.add(detailedMessage);
        });

        ApiResponse<List<String>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(String.valueOf(errorMessages));

        return ResponseEntity.badRequest().body(apiResponse);
    }

}
