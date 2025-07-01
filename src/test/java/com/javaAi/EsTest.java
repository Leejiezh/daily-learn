package com.javaAi;
import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;

//@SpringBootTest
public class EsTest {


    private static final String ES_URL = "https://127.0.0.1:9200";

    @Test
    public void operateEs(){

        String username = "elastic"; // 你的用户名
        String password = "your-password";

        String url = ES_URL + "/_cat/indices?v";
        String s = HttpUtil.get(url );

        System.out.println(s);
    }
}
