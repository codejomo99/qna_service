package com.exam.qna.error;

public class SignupEmailDuplicatedException extends RuntimeException {
    public SignupEmailDuplicatedException(String message) {
        super(message);
    }
}
