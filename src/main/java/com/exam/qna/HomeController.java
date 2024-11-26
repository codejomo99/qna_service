package com.exam.qna;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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


    // http://localhost:8080/test/mbti/이름
    // PathVariable 사용법
    // switch 문을 변수에 담아서 사용 가능.
    @GetMapping("/test/mbti/{name}")
    @ResponseBody
    public String showMbti(@PathVariable String name){
        String rs = switch (name){
            case "홍길동" -> "INFP";
            case "김하늘" -> "ENFP";
            case "박종민" -> "INTJ";
            case "이진성" -> "INFP";

            default -> "모릅니다.";
        };

        return rs;
    }



    // 세션을 이용해서 값을 저장하기
    @GetMapping("/test/saveSession/{name}/{value}")
    @ResponseBody
    public String saveSession(@PathVariable String name, @PathVariable String value, HttpServletRequest req) {
        HttpSession session = req.getSession();

        // req = > 쿠키 = > JESSIONID => 세션을 얻을 수 있다.

        session.setAttribute(name,value);

        return "세션 변수 %s의 값이 %s(으)로 설정되었습니다.".formatted(name,value);
    }

    @GetMapping("/test/getSession/{name}")
    @ResponseBody
    public String getSession(@PathVariable String name, HttpSession session){
        String value = (String) session.getAttribute(name);

        return "세션 변수의 %s의 값이 %s입니다.".formatted(name,value);
    }

}
