package com.exam.qna.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "데이터가 존재하지 않습니다.")
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String questionNotFound) {

    }
}
