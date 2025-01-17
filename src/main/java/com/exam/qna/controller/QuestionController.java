package com.exam.qna.controller;

import com.exam.qna.dto.AnswerForm;
import com.exam.qna.dto.QuestionForm;
import com.exam.qna.entity.Question;
import com.exam.qna.entity.User;
import com.exam.qna.error.DataNotFoundException;
import com.exam.qna.service.QuestionService;
import com.exam.qna.service.SiteUserService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor // 생성자 주입
public class QuestionController {

    // @Autowired // <- 필드 주입
    private final QuestionService questionService;
    private final SiteUserService userService;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "") String kw, @RequestParam(defaultValue = "") String searchType, Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Question> paging = questionService.getList(kw,searchType,page);

        // 미리 실행된 question_list.html 에서
        // questionList 라는 이름으로 questionList 변수를 사용 할 수 있다.
        model.addAttribute("paging", paging);
        model.addAttribute("kw",kw);
        model.addAttribute("searchType",searchType);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id, AnswerForm answerForm) {
        Question question = questionService.getQuestion(id);

        model.addAttribute("question", question);

        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(Principal principal, @Valid QuestionForm questionForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        User user = userService.getUser(principal.getName());

        questionService.create(questionForm.getSubject(), questionForm.getContent(), user);
        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Long id, Principal principal) {
        Question question = this.questionService.getQuestion(id);

        if (question == null) {
            throw new DataNotFoundException("%d번 질문은 존재하지 않습니다.".formatted(id));
        }

        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(@PathVariable("id") Long id, Principal principal){
        Question question = questionService.getQuestion(id);

        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        questionService.delete(question);

        return "redirect:/";

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Long id) {
        Question question = questionService.getQuestion(id);
        User user = userService.getUser(principal.getName());
        questionService.vote(question, user);
        return String.format("redirect:/question/detail/%s", id);
    }



}
