package com.exam.qna.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;
    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT") // 본문
    private String content;
    private LocalDateTime createDate;


    // ALL : 부모엔티티에 관련된 데이터가 저장될때 자식 엔티티도 저장을 할까 ?
    @OneToMany(mappedBy = "question", cascade = {CascadeType.REMOVE, CascadeType.ALL})
    private List<Answer> answerList = new ArrayList<>();

    @ManyToOne
    private SiteUser author;

    public void addAnswer(Answer answer){
        answer.setQuestion(this);
        getAnswerList().add(answer);
    }
}
