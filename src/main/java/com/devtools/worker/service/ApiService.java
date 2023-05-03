package com.devtools.worker.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ApiService {
    public List<HashMap> select(Map<String, Object> params);
}
