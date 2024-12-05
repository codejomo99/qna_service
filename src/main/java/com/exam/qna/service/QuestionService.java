package com.exam.qna.service;

import com.exam.qna.entity.Question;
import com.exam.qna.error.DataNotFoundException;
import com.exam.qna.repository.QuestionRepository;
import java.util.List;
import java.util.Optional;
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
        Optional<Question> oq = questionRepository.findById(id);

        if(oq.isPresent()){
            return oq.get();
        }

        throw new DataNotFoundException("question not found");
    }
}
