package com.exam.qna.controller;

import com.exam.qna.dto.AnswerForm;
import com.exam.qna.entity.Question;
import com.exam.qna.entity.SiteUser;
import com.exam.qna.service.AnswerService;
import com.exam.qna.service.QuestionService;
import com.exam.qna.service.SiteUserService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final SiteUserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Principal principal, Model model, @PathVariable Long id, @Valid AnswerForm answerForm, BindingResult bindingResult){

        Question question = questionService.getQuestion(id);

        if(bindingResult.hasErrors()){
            model.addAttribute("question",question);
            return "question_detail";
        }

        SiteUser siteUser = userService.getUser(principal.getName());

        answerService.create(question,answerForm.getContent(),siteUser);

        return String.format("redirect:/question/detail/%s",id);
    }

}
