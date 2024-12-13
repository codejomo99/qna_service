package com.exam.qna.error;

public class SignupUsernameDuplicatedException extends RuntimeException {
    public SignupUsernameDuplicatedException(String message) {
        super(message);
    }
}
