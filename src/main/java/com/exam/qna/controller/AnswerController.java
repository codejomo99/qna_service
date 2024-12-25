package com.exam.qna.controller;

import com.exam.qna.dto.AnswerForm;
import com.exam.qna.entity.Answer;
import com.exam.qna.entity.Question;
import com.exam.qna.entity.SiteUser;
import com.exam.qna.service.AnswerService;
import com.exam.qna.service.QuestionService;
import com.exam.qna.service.SiteUserService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

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

        Answer answer = answerService.create(question,answerForm.getContent(),siteUser);

        return String.format("redirect:/question/detail/%s#answer_%s",id,answer.getId());
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Long id, Principal principal) {
        Answer answer = answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        answerForm.setContent(answer.getContent());
        return "answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(Principal principal,@PathVariable Long id, @Valid AnswerForm answerForm, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "ansewr_form";
        }
        Answer answer = answerService.getAnswer(id);
        if(!answer.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
        }

        answerService.modify(answer, answerForm.getContent());

        return String.format("redirect:/question/detail/%s#answer_%s",
                answer.getQuestion().getId(), answer.getId());

    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Long id) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }

}
