package com.exam.qna.controller;

import com.exam.qna.entity.Question;
import com.exam.qna.service.QuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor // 생성자 주입
public class QuestionController {

    // @Autowired // <- 필드 주입
    private final QuestionService questionService;
    @GetMapping("/list")
    public String list(Model model){
        List<Question> questionsList = questionService.getList();

        // 미리 실행된 question_list.html 에서
        // questionList 라는 이름으로 questionList 변수를 사용 할 수 있다.
        model.addAttribute("questionList",questionsList);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable int id){
        Question question = questionService.getQuestion(id);

        model.addAttribute("question",question);

        return "question_detail";
    }

    @GetMapping("/create")
    public String questionCreate(){
        return "question_form";
    }

    @PostMapping("/create")
    public String questionCreate(Model model, QuestionForm questionForm){

       boolean hasError = false;

        if(questionForm.getSubject() == null || questionForm.getSubject().trim().isEmpty()){
            model.addAttribute("subjectErrorMsg", "제목이 없어용");
            hasError = true;
        }

        if(questionForm.getContent() == null || questionForm.getContent().trim().isEmpty()){
            model.addAttribute("contentErrorMsg", "내용이 없어용");
            hasError = true;
        }

        if(hasError){
            model.addAttribute("questionForm",questionForm);
            return "question_form";
        }

        questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list";
    }
}
