package com.exam.qna;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/qna")
    @ResponseBody
    public String showHome() {
        return "메인 시작";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String showTest() {
        return """
                <h1> 안녕하세요. </h1>
                <input type="text" placeholder = "입력하세요"/>
                """;
    }


    @GetMapping("/test/page1")
    @ResponseBody
    public String showGet() {
        return """
                <form method = "POST" action="/test/page2">
                    <input type="number" name = "age" placeholder = "나이 입력" />
                    <input type="submit" value = "page2로 Post 방식" />
                </form>
                """;
    }

    // RequestParam defaultValue 를 통해 0 값의 오류를 막는다.
    // http://localhost:8080/test/page2?age=10 -(GET)
    // http://localhost:8080/test/page2 -(POST)
    @GetMapping("/test/page2")
    @ResponseBody
    public String showPageGet(@RequestParam(defaultValue = "0") int age) {
        return """
                <h1>입력된 나이 : %d</h1>
                <h1> GET 방식 </h1>
                """.formatted(age);
    }

    @PostMapping("/test/page2")
    @ResponseBody
    public String showPagePost(@RequestParam(defaultValue = "0") int age) {
        return """
                <h1>입력된 나이 : %d</h1>
                <h1> POST 방식 </h1>
                """.formatted(age);
    }

    // http://localhost:8080/test/plus?a=1&b=13
    // GET 요청으로 14가 나온다.
    @GetMapping("/test/plus")
    @ResponseBody
    public int plus(@RequestParam(defaultValue = "0") int a, @RequestParam(defaultValue = "0")  int b){
        return a + b;
    }

    // 서블릿 방식으로 값을 받아오는 법
    @GetMapping("/test/plus2")
    @ResponseBody
    public void plusServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int a = Integer.parseInt(req.getParameter("a"));
        int b = Integer.parseInt(req.getParameter("b"));
        resp.getWriter().append(a+b+"");
    }




    // 함수안에 지역변수로 두면 함수 생성주기로 값이 초기화 되기 때문에
    // 전역변수로 둬 호출될때마다 값이 증가하게 해준다.
    private int increaseNum;
    public HomeController(){
        increaseNum = -1;
    }

    @GetMapping("/test/increase")
    @ResponseBody
    public int increase(){
        increaseNum++;
        return increaseNum;
    }

    @GetMapping("/test/gugudan")
    @ResponseBody
    public String gugudan(Integer dan, Integer limit){

        String result = "";

        if(dan == null) dan = 9;
        if(limit == null) limit = 9;

        for(int i = 1; i <= limit; i++){
            result += "%d * %d = %d<br>".formatted(dan,i,dan*i);
        }
        return result;
    }


}
