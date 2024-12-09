package com.exam.qna.service;

import com.exam.qna.entity.Question;
import com.exam.qna.error.DataNotFoundException;
import com.exam.qna.repository.QuestionRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<Question> getList(){
        return questionRepository.findAll();
    }

    public Question getQuestion(int id) {
        return questionRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("no %d question not fund".formatted(id)));
    }

    public void create(String subject, String content) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        questionRepository.save(q);
    }
}
