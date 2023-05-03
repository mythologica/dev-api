package com.devtools.worker.service;

import com.devtools.worker.dao.ApiDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiServiceImpl implements ApiService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ApiDao apiDao;

	@Override
	public List<HashMap> select(Map<String, Object> params) {
//		logger.info("service.select");
		return apiDao.selectData(params);
	}
}
