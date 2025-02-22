package com.nettruyen.comic.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    USER_NOT_EXISTED(1000, "User is not exists", HttpStatus.NOT_FOUND),
    USER_EXISTED(1000, "User already exists", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1000, "Role is not exists", HttpStatus.NOT_FOUND),
    ROLE_EXISTED(1000, "Role already exists", HttpStatus.BAD_REQUEST),

    EMAIL_EXISTED(1000, "Email already exists", HttpStatus.BAD_REQUEST),
    INVALID_OTP_OR_EXPIRED(1003, "OTP code is invalid or expired", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED(9999, "Uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(1001, "This field must be least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1002, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    VALIDATION_INVALID(1111, "Key validation failed", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1003, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1004, "You do not have permission", HttpStatus.FORBIDDEN),
    USER_ALREADY_ACTIVE(1005, "User already active account", HttpStatus.BAD_REQUEST),

    STORY_ALREADY_EXITS(1111, "Story is existed in your database.", HttpStatus.BAD_REQUEST),
    STORY_NOT_EXITS(1112, "Story is not existed in your database.", HttpStatus.BAD_REQUEST),

    CHAPTER_ALREADY_EXITS(1113, "Chapter is existed in story of your database.", HttpStatus.BAD_REQUEST),
    CHAPTER_NOT_EXITS(1114, "Chapter is not existed in story of your database.", HttpStatus.BAD_REQUEST),

    GENERATE_ALREADY_EXITS(1121, "Generate is existed in your database.", HttpStatus.BAD_REQUEST),
    GENERATE_NOT_EXITS(1122, "Generate is not existed in your database.", HttpStatus.BAD_REQUEST),

    INVALID_EXPIRED_TOKEN(1005, "Token is invalid.", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
