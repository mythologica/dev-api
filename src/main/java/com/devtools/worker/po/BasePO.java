package com.devtools.worker.po;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BasePO {

    public abstract Map<String,Object> parseMap(Map<String,Object> params);

    public Map<String,Object> toParameters() {
        Map<String,Object> params = new LinkedHashMap<String,Object>();

        return parseMap(params) ;
    }
}
