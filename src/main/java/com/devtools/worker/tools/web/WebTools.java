package com.devtools.worker.tools.web;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.WebRequest;

import com.devtools.worker.tools.json.ObjectToJson;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebTools {
	private static final Logger logger = LoggerFactory.getLogger(WebTools.class);
	
	
	public static void setEncoding(HttpServletRequest request) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			// Ignore Exception
		}
	}

	public static String removeXss(String str) {
		StringBuffer sb = new StringBuffer();
		// todo: cross site script를 제거하는 로직을 넣어야 함

		sb.append(str);

		return sb.toString();
	}

	public static HashMap requestToMap(WebRequest webRequest) {
		HashMap params = new HashMap();
		Map<String, String[]> allparams = null;
		if (webRequest != null && (allparams = webRequest.getParameterMap()) != null) {
			Iterator kset = allparams.keySet().iterator();
			while (kset.hasNext()) {
				String k = (String) kset.next();
				String[] values = allparams.get(k);
				if (values.length > 1) {
					params.put(k, values);
				} else {
					params.put(k, values[0]);
				}
			}
		}
		return params;
	}

	public static HashMap requestToMap(String requestBody) {
		HashMap vo = null;
		String json = removeXss(requestBody);
		if (json != null) {
			vo = (HashMap) ObjectToJson.getInstance().parseObject(json, new HashMap().getClass());
		} else {
			vo = new HashMap();
		}
		return vo;
	}

	public static String getRequestBody(Map<?, ?> params) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
	}

	
    public static String get(String address, String[] headers) throws Exception {
        //헤더값은 이런식으로 사용합니다(키, 값)headers => "Content-type","plain/text"  
        HttpClient client = HttpClient.newBuilder().version(Version.HTTP_1_1).build();
        String result = client.sendAsync(
            HttpRequest.newBuilder(
                new URI(address)).GET().headers(headers).build(),  //GET방식 요청
                HttpResponse.BodyHandlers.ofString()  //응답은 문자형태
            ).thenApply(HttpResponse::body)  //thenApply메소드로 응답body값만 받기
            .get();  //get메소드로 body값의 문자를 확인
        System.out.println(result);
        return result;
    }
    
	public static String post(String address, String requestBody, String[] headers) throws Exception {
		logger.info("post-body:",requestBody);
		
		BodyPublisher body = BodyPublishers.ofString(requestBody); // post 파라미터 최종 모습 입니다.

		HttpClient client = HttpClient.newBuilder().version(Version.HTTP_1_1).build();

		URI url = new URI(address);
		String result = "";
		if( headers != null && headers.length > 0 ) {
			result = client.sendAsync(HttpRequest.newBuilder(url).POST(body).headers(headers).build(),
					HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).get();
		} else {
			result = client.sendAsync(HttpRequest.newBuilder(url).POST(body).build(),
					HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).get();
		}
		logger.info("post-result:",result);
        return result;
	}
}
