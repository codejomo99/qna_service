package com.exam.qna.service;

import com.exam.qna.entity.Answer;
import com.exam.qna.entity.Question;
import com.exam.qna.entity.SiteUser;
import com.exam.qna.repository.AnswerRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;


    public void create(Question question, String content, SiteUser author){
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);

        // 양방향을 위한
        question.addAnswer(answer);

        answerRepository.save(answer);
    }

}
