package com.exam.qna.service;

import com.exam.qna.entity.Question;
import com.exam.qna.entity.User;
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

    public Page<Question> getList(String kw, String searchType, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("isNotice"));
        sorts.add(Sort.Order.desc("createDate"));
        sorts.add(Sort.Order.desc("id"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); // 10 페이지

        if(kw == null || kw.trim().isEmpty()){
            return questionRepository.findAll(pageable);
        }

        switch (searchType) {
            case "author":
                return questionRepository.findByAuthorUsernameContaining(kw, pageable);
            case "content":
                return questionRepository.findByContentContaining(kw, pageable);
            case "subject":
            default:
                return questionRepository.findBySubjectContains(kw, pageable);
        }

    }

    public Question getQuestion(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("no %d question not fund".formatted(id)));
    }

    public void create(String subject, String content, User author) {

        boolean isNotice = false;
        if(author.getUsername().equals("admin")){
            isNotice = true;
        }

        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setAuthor(author);
        q.setIsNotice(isNotice);
        q.setCreateDate(LocalDateTime.now());
        questionRepository.save(q);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        questionRepository.save(question);
    }

    public void delete(Question question) {
        questionRepository.delete(question);
    }

    public void vote(Question question, User user) {
        question.getVoter().add(user);
        questionRepository.save(question);
    }
}
