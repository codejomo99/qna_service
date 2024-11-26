package com.exam.qna;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping("/qna")
    @ResponseBody
    public String showHome(){
        return "메인 시작";
    }
}
