package com.exam.qna.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/** 유효성 검사 **/
@Getter
@Setter
public class UserCreateForm {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    private String username;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인을 해주세요")
    private String password2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email(message = "올바른 형식으로 입력해주세요")
    private String email;
}
