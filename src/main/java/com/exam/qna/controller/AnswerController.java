package com.exam.qna.controller;

import com.exam.qna.entity.Question;
import com.exam.qna.service.AnswerService;
import com.exam.qna.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable int id, String content){
        Question question = questionService.getQuestion(id);

        answerService.create(question,content);

        return String.format("redirect:/question/detail/%s",id);
    }

}
