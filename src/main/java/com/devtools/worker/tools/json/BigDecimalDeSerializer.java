package com.devtools.worker.tools.json;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class BigDecimalDeSerializer implements JsonDeserializer<BigDecimal> {

	@Override
	public BigDecimal deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {

		BigDecimal bd=null;

		if (!"".equals(arg0.getAsString().trim())) {
			bd = arg0.getAsBigDecimal();
		}
		return bd;
	}

}
