package com.example.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private static JSONParser jsonParser = null;
    private static JSONArray jsonArray = null;
    private static List<String> list = null;
    private static URL url = null;

    // providerData메서드
    public static void providerData() throws IOException {
        list = new ArrayList<>();
        jsonParser = new JSONParser();

        // ClassPathResource를 통해 providers.json파일을 읽는다.
        ClassPathResource classPathResource = new ClassPathResource("providers.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()));

        try {
            // json => Object로 변환
            Object obj = jsonParser.parse(br);

            // obj => JSONArray로 변환
            JSONArray jsonArr = (JSONArray) obj;

            // jsonArr 크기만큼 반복수행 (youtube, vimeo, twitter, instagram 4가지)
            for (int i = 0; i < jsonArr.size(); i++) {
                // i번째 위치의 객체를 꺼내서 JSONObject로 타입 캐스팅
                JSONObject provider_url = (JSONObject) jsonArr.get(i);

                // 키가 endpoints인 것을 꺼내어 변수에 저장한다.
                String endpoints = (String) provider_url.get("endpoints").toString();

                // String => Object로 만들고 JSONArray에 0번째 인덱스를 urlData에 담는다.
                Object obj2 = jsonParser.parse(endpoints);
                jsonArray = new JSONArray();
                jsonArray = (JSONArray) obj2;
                JSONObject urlData = (JSONObject) jsonArray.get(0);

                // url값만 추출하고 리스트에 추가
                String url = (String) urlData.get("url");
                list.add(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET > localhost:9090/home
    @GetMapping(value = "/home")
    public String getHome(Model model) throws IOException {
        // providers.json에서 url데이터를 추출하여 리스트에 넣음
        providerData();

        // home.jsp리턴
        return "home";
    }

    // hostCheck
    public String hostCheck(String str) {
        String result = "";
        try {
            // 매개변수로 들어온 str을 가지고 url을 생성
            url = new URL(str);

            // \\.를 기준으로 나누어서 저장
            String[] split = url.getHost().split("\\.");

            // split 데이터가 2인 경우(twitter, vimeo)
            if (split.length == 2) {
                result = split[0];
            }
            // split의 길이가 3인 경우(youtube, instagram)
            else if (split.length == 3) {
                result = split[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // result 리턴(매체의 유형이 담긴다.)
        return result;
    }

    // provider url + format + encode 합친 문자 만들기
    public String createUrl(String host, String encode) {
        String oembedUrl = "";

        // list를 조회하며 저장되어있는 url과 host가 일치하는 것이 있을 경우 멈춘다.
        for (String url : list) {
            if (url.contains(host)) {
                // vimeo의 경우 다르게 처리
                if (url.contains("oembed.")) {
                    // {format}이 있을 경우 json으로 대체한다.
                    if (url.contains("{format}")) {
                        url = url.replace("{format}", "json");
                    }
                    oembedUrl = url + "?url=" + encode;
                }
                // youtube, twitter의 경우
                else {
                    oembedUrl = url + "?format=json&url=" + encode;
                }
                break;
            }
        }
        // oembedUrl을 리턴
        return oembedUrl;
    }

    // GET > localhost:9090/home
    @GetMapping(value = "/oembedSearch")
    public String postSearch(@RequestParam("searchURL") String searchURL, Model model) throws IOException {
        try {
            String result = "";
            String host = hostCheck(searchURL); // youtube, vimeo, instagram, twitter중 하나가 저장된다.
            String encode = URLEncoder.encode(searchURL, StandardCharsets.UTF_8); // url을 인코딩하여 encode변수에 저장한다.
            String oembedUrl = createUrl(host, encode); // createUrl메소드를 호출하고 합쳐진 문자열을 oembedUrl에 저장한다.

            // http client생성
            CloseableHttpClient hc = HttpClients.createDefault();

            // get메서드와 URL설정
            HttpGet httpGet = new HttpGet(oembedUrl);

            // httpGet Header에 content-type 지정
            httpGet.addHeader("Content-Type", "application/json");

            // execute메서드를 통해 request요청해서 response를 받는다.
            CloseableHttpResponse httpResponse = hc.execute(httpGet);
            // 결과 값을 스트링으로 읽어온다.
            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

            // String => Object
            Object obj = jsonParser.parse(result);

            // obj => JSONObject 변환
            JSONObject result_json = (JSONObject) obj;

            // model에 결과 값 추가하여 화면에 출력
            model.addAttribute("title", result_json.get("title"));
            model.addAttribute("author_name", result_json.get("author_name"));
            model.addAttribute("author_url", result_json.get("author_url"));
            model.addAttribute("type", result_json.get("type"));
            model.addAttribute("size", "(" + result_json.get("width") + "/" + result_json.get("height") + ")");
            model.addAttribute("height", result_json.get("height"));
            model.addAttribute("width", result_json.get("width"));
            model.addAttribute("version", result_json.get("version"));
            model.addAttribute("provider_name", result_json.get("provider_name"));
            model.addAttribute("provider_url", result_json.get("provider_url"));
            model.addAttribute("thumbnail_height", result_json.get("thumbnail_height"));
            model.addAttribute("thumbnail_width", result_json.get("thumbnail_width"));
            model.addAttribute("thumbnail_url", result_json.get("thumbnail_url"));
            model.addAttribute("html", result_json.get("html"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "home";
    }
}