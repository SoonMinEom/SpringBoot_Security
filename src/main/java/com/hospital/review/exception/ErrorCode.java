package com.hospital.review.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT,"유저네임 중복"),
    NOT_FOUND(HttpStatus.NOT_FOUND,"ID가 없습니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"");

    private HttpStatus status;
    private String message;

}
