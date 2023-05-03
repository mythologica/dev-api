package com.devtools.worker.po;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class ResponsePO {
    public Map<String,String> head = new LinkedHashMap<String,String>();

    public Map<String,List<HashMap>> lists = new HashMap<String,List<HashMap>>();
    
    public String result = "";

    public ResponsePO() {
        this.head.put("success","true");
        this.head.put("code","000000");
        this.head.put("message","");
    }

    public void addList(String listId,List<HashMap> list) {
        if( list == null ) list = new ArrayList<HashMap>();
        this.lists.put(listId,list);
    }

    public String toJsonString(){
        Gson gson = new Gson();
        String rtn = gson.toJson(this).toString();
        System.out.println("return##"+rtn+"##");
        return rtn;
    }
}
