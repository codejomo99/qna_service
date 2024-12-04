package com.exam.qna.controller;

import com.exam.qna.entity.Question;
import com.exam.qna.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor // 생성자 주입
public class QuestionController {

    // @Autowired // <- 필드 주입
    private final QuestionRepository questionRepository;
    @GetMapping("/question/list")
    public String list(Model model){
        List<Question> questionsList = questionRepository.findAll();

        // 미리 실행된 question_list.html 에서
        // questionList 라는 이름으로 questionList 변수를 사용 할 수 있다.
        model.addAttribute("questionList",questionsList);


        return "question_list";
    }
}
