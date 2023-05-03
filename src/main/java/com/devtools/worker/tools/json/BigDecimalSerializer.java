package com.devtools.worker.tools.json;


import java.lang.reflect.Type;
import java.math.BigDecimal;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class BigDecimalSerializer implements JsonSerializer<BigDecimal> {

	@Override
	public JsonElement serialize(BigDecimal arg0, Type arg1, JsonSerializationContext arg2) {

		if(arg0 == null){
			return new JsonPrimitive("");
		}

		return new JsonPrimitive(arg0.toString());
	}
}
