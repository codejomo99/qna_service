package com.exam.qna;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    private int increaseNum;
    private List<Article> articles;

    public HomeController() {
        increaseNum = -1;
        articles = new ArrayList<>(){{
            add(new Article("제목1","내용1"));
            add(new Article("제목2","내용2"));

        }};
    }

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
    public int plus(@RequestParam(defaultValue = "0") int a, @RequestParam(defaultValue = "0") int b) {
        return a + b;
    }

    // 서블릿 방식으로 값을 받아오는 법
    @GetMapping("/test/plus2")
    @ResponseBody
    public void plusServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int a = Integer.parseInt(req.getParameter("a"));
        int b = Integer.parseInt(req.getParameter("b"));
        resp.getWriter().append(a + b + "");
    }

    // 함수안에 지역변수로 두면 함수 생성주기로 값이 초기화 되기 때문에
    // 전역변수로 둬 호출될때마다 값이 증가하게 해준다.


    @GetMapping("/test/increase")
    @ResponseBody
    public int increase() {
        increaseNum++;
        return increaseNum;
    }

    @GetMapping("/test/gugudan")
    @ResponseBody
    public String gugudan(Integer dan, Integer limit) {

        String result = "";

        if (dan == null) {
            dan = 9;
        }
        if (limit == null) {
            limit = 9;
        }

        for (int i = 1; i <= limit; i++) {
            result += "%d * %d = %d<br>".formatted(dan, i, dan * i);
        }
        return result;
    }


    // http://localhost:8080/test/mbti/이름
    // PathVariable 사용법
    // switch 문을 변수에 담아서 사용 가능.
    @GetMapping("/test/mbti/{name}")
    @ResponseBody
    public String showMbti(@PathVariable String name) {
        String rs = switch (name) {
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

        session.setAttribute(name, value);

        return "세션 변수 %s의 값이 %s(으)로 설정되었습니다.".formatted(name, value);
    }

    @GetMapping("/test/getSession/{name}")
    @ResponseBody
    public String getSession(@PathVariable String name, HttpSession session) {
        String value = (String) session.getAttribute(name);

        return "세션 변수의 %s의 값이 %s입니다.".formatted(name, value);
    }

    /**
     * 데이터 반환 타입과 메서드 공부
     **/

    @GetMapping("/test/returnBoolean")
    @ResponseBody
    public boolean showBoolean() {
        return false;
    }

    @GetMapping("/test/returnDouble")
    @ResponseBody
    public double showDouble() {
        return Math.random();
    }

    @GetMapping("/test/returnIntArr")
    @ResponseBody
    public int[] showIntArr() {
        int[] arr = {1, 3, 5};

        return arr;
    }

    @GetMapping("/test/returnStringList")
    @ResponseBody
    public List<String> showStringList() {
        List<String> list = new ArrayList<>() {{
            add("안녕");
            add("테스트");
            add("입니다");
        }};

        return list;
    }


    @GetMapping("/test/returnMap")
    @ResponseBody
    public Map<String, Object> showMap() {
        Map<String, Object> map = new LinkedHashMap<>() {{
            put("id", 1);
            put("age", 5);
            put("name", "푸바오");
            put("list", new ArrayList<>() {{
                add(2);
                add(3);
                add(4);
            }});
        }};

        return map;
    }


    @GetMapping("/test/returnAnimal")
    @ResponseBody
    public Animal showAnimal() {
        Animal animal = new Animal(1, 3, "도니", new ArrayList<>() {{
            add(2);
            add(3);
            add(4);
        }});

        return animal;
    }

    @GetMapping("/test/returnAnimalMapList")
    @ResponseBody
    public List<Map<String, Object>> showAnimalMap() {
        Map<String, Object> map1 = new LinkedHashMap<>() {{
            put("id", 1);
            put("age", 5);
            put("name", "푸바오");
            put("list", new ArrayList<>() {{
                add(2);
                add(3);
                add(4);
            }});
        }};

        Map<String, Object> map2 = new LinkedHashMap<>() {{
            put("id", 2);
            put("age", 11);
            put("name", "도니");
            put("list", new ArrayList<>() {{
                add(5);
                add(6);
                add(7);
            }});
        }};

        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map1);
        list.add(map2);

        return list;

    }

    @GetMapping("/test/returnAnimalList")
    @ResponseBody
    public List<Animal> showAnimalList() {

        Animal animal1 = new Animal(1, 3, "도니", new ArrayList<>() {{
            add(2);
            add(3);
            add(4);
        }});

        Animal animal2 = new Animal(2, 14, "흰둥이", new ArrayList<>() {{
            add(2);
            add(3);
            add(4);
        }});

        List<Animal> list = new ArrayList<>() {{
            add(animal1);
            add(animal2);
        }};

        return list;
    }

    // 간단한 게시판 구현
    @GetMapping("/addArticle")
    @ResponseBody
    public String addArticle(String title, String body) {
        Article article = new Article(title, body);

        articles.add(article);

        return "%d번 게시물이 추가되었습니다.".formatted(article.getId());
    }

    @GetMapping("/article/list")
    @ResponseBody
    public List<Article> getArticles(){
        return articles;
    }

    @GetMapping("/article/detail/{id}")
    @ResponseBody
    public Object getArticle(@PathVariable int id){
        Article article = articles.stream()
                .filter(a -> a.getId() == id) // 게시물 ID와 내가 입력한 ID 일치하는지 확인
                .findFirst() // 찾은것중 첫번째
                .orElse(null); // 아무것도 없으면 null 반환


        return article;
    }

    @GetMapping("/article/modify/{id}")
    @ResponseBody
    public Object updateArticle(@PathVariable int id, String title, String body){
        Article article = articles.stream()
                .filter(a -> a.getId() == id) // 게시물 ID와 내가 입력한 ID 일치하는지 확인
                .findFirst() // 찾은것중 첫번째
                .orElse(null); // 아무것도 없으면 null 반환

        // 간단한 유효성 검사
        if(title == null) {
            return "제목을 입력해주세요.";
        }
        if(body == null){
            return "내용물을 입력해주세요.";
        }

        article.setTitle(title);
        article.setBody(body);

        return "%d번 게시글을 수정했습니다.".formatted(id);
    }

    @GetMapping("/article/delete/{id}")
    @ResponseBody
    public Object deleteArticle(@PathVariable int id){
        Article article = articles.stream()
                .filter(a -> a.getId() == id) // 게시물 ID와 내가 입력한 ID 일치하는지 확인
                .findFirst() // 찾은것중 첫번째
                .orElse(null); // 아무것도 없으면 null 반환

        articles.remove(article);

        return "%d번 게시글을 삭제했습니다.".formatted(id);
    }

    // action 에서 스프링부트에 의해서 자동으로 조립된 객체 입력받을 수 있다.
    @GetMapping("/addPerson")
    @ResponseBody
    public Object showPerson(Person p){
        return p;
    }


}

// 생성자만들어준다
@AllArgsConstructor
@Data
class Animal {
    private final int id;
    private final int age;

    private String name;

    private final List<Integer> list;
}


@AllArgsConstructor
@Data
class Article {
    private static int lastId;
    private final int id;
    private String title;
    private String body;

    static{
        lastId = 0;
    }

    public Article(String title, String body){
        this(++lastId, title, body); // 메서드 오버로딩
    }
}

@AllArgsConstructor
@Getter
class Person{
    private int id;
    private int age;
    private String name;
}
