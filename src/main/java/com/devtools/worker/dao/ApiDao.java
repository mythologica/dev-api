package com.devtools.worker.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface ApiDao {
	List<HashMap> selectData(Map<String,Object> params);
}
