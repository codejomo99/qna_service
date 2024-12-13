package com.exam.qna.service;

import com.exam.qna.entity.Question;
import com.exam.qna.entity.SiteUser;
import com.exam.qna.error.DataNotFoundException;
import com.exam.qna.repository.QuestionRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Page<Question> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        sorts.add(Sort.Order.desc("id"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAll(pageable);
    }

    public Question getQuestion(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("no %d question not fund".formatted(id)));
    }

    public void create(String subject, String content, SiteUser author) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setAuthor(author);
        q.setCreateDate(LocalDateTime.now());
        questionRepository.save(q);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        questionRepository.save(question);
    }
}
