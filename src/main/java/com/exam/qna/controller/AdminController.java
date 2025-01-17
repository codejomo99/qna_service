//package com.exam.qna.controller;
//
//import com.exam.qna.dto.QuestionForm;
//import com.exam.qna.entity.SiteUser;
//import com.exam.qna.service.QuestionService;
//import com.exam.qna.service.SiteUserService;
//import jakarta.validation.Valid;
//import java.security.Principal;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/admin")
//@RequiredArgsConstructor
//public class AdminController {
//    private final QuestionService questionService;
//    private final SiteUserService userService;
//
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PostMapping("/create")
//    public String createQuestion(Principal principal, @Valid QuestionForm questionForm, BindingResult bindingResult){
//        if(bindingResult.hasErrors()){
//            return "question_form_admin";
//        }
//
//        SiteUser siteUser = userService.getUser(principal.getName());
//        questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
//        return "redirect:/question/list";
//    }
//
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @GetMapping("/create")
//    public String getQuestion(QuestionForm questionForm){
//        return "question_form_admin";
//    }
//}
