package com.devtools.worker.tools.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class ObjectToJson {
    private static ObjectToJson INSTANCE = new ObjectToJson();
    private static Gson gsonObjectBuilder;
    private static Gson gsonStringBuilder;

    private ObjectToJson() {
        GsonBuilder gbString = new GsonBuilder();
        gbString.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gbString.registerTypeAdapter(BigDecimal.class, new BigDecimalSerializer());
        gbString.serializeNulls();
        gsonStringBuilder = gbString.create();

        GsonBuilder gbObject = new GsonBuilder();
        gbObject.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gbObject.registerTypeAdapter(java.sql.Date.class, new DateDeSerializer());
        gbObject.registerTypeAdapter(BigDecimal.class, new BigDecimalDeSerializer());
        gbObject.registerTypeAdapter(BigDecimal.class, new BigDecimalSerializer());
        gbObject.serializeNulls();
        gsonObjectBuilder = gbObject.create();
    }

    @SuppressWarnings("unchecked")
    public static ObjectToJson getInstance(){
        return INSTANCE;
    }

    /**
     * Desc : object를 json형태의 String으로 변환하여 반환한다.
     * @Method Name : paraseString
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public String paraseString(Object obj) {
        return gsonStringBuilder.toJson(obj);
    }

    /**
     * json string을 object로 리턴한다.
     * @param str
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object parseObject(String str, Class clazz){
        return gsonObjectBuilder.fromJson(str, clazz);
    }

    /**
     * json string을 object로 리턴한다.
     * @param str
     * @param tp
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object parseObject(String str, Type tp){
        return gsonObjectBuilder.fromJson(str, tp);
    }
}
