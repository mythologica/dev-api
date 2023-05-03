package com.devtools.worker.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.devtools.worker.po.ResponsePO;
import com.devtools.worker.service.ApiServiceImpl;
import com.devtools.worker.tools.collection.CollectionTools;
import com.devtools.worker.tools.web.WebTools;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ApiServiceImpl apiService;

	@ResponseBody
	@GetMapping("/api/get/select")
	public String doGet(WebRequest webRequest) {
		logger.info("################################### doGet ###################################[START]");
		HashMap<String, Object> params = WebTools.requestToMap(webRequest);

		CollectionTools.maplog(params);

		ResponsePO resPO = new ResponsePO();
		List<HashMap> list = apiService.select(params);
		resPO.addList("select", list);
		logger.info("################################### doGet ###################################[END]");
		return resPO.toJsonString();
	}

	@ResponseBody
	@PostMapping("http://dev-apigw.hmm21.com/gateway/ptpSchedule/v1")
	public String doPost(@RequestBody String body) {
		logger.info("################################### doPost ###################################[START]");
		logger.info("requestBody:", body);
		System.out.println("반응");
		HashMap params = WebTools.requestToMap(body);

		List<HashMap> list = apiService.select(params);

		ResponsePO resPO = new ResponsePO();
		resPO.addList("select", list);
		logger.info("################################### doPost ###################################[END]");
		return resPO.toJsonString();
	}

	@ResponseBody
	@GetMapping("/api/web/data")
	public String doWeb(WebRequest webRequest) {
		logger.info("################################### doWeb ###################################[START]");
		HashMap<String, Object> params = WebTools.requestToMap(webRequest);

		CollectionTools.maplog(params);

		ResponsePO resPO = new ResponsePO();
//		List<HashMap> list =  apiService.select(params);
		String address = "";
		address = "http://devng.hmm21.com:8080/api/e-service/customer/post/port-to-port-schedule.ic";
//		address = "http://dev-apigw.hmm21.com/port-to-port-schedule";
		String[] headers = {};
		String requestBody = "{ \"test111\": \"4444\", \"fromLocationCode\": \"KRPUS\", \"receiveTermCode\": \"CY\", \"toLocationCode\": \"DEHAM\", \"deliveryTermCode\": \"CY\", \"periodDate\": \"20211203\", \"weekTerm\": \"2\", \"webSort\": \"D\", \"webPriority\": \"A\", }";
		String result = "";
		try {
			result = WebTools.post(address, requestBody, headers);
			logger.info("result:" , result);
		}catch(Exception err) {
			
			logger.error("post error:",err);
		}

		resPO.setResult(result);
		logger.info("################################### doWeb ###################################[END]");
//		return resPO.toJsonString();
		return result;
	}

}
