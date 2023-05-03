package com.devtools.worker.tools.json;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DateDeSerializer implements JsonDeserializer<java.sql.Date> {

	@Override
	public Date deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {

		Date rtnDate = null;

		if(!"".equals(arg0.getAsString().trim())){
			DateFormat formater = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
			java.util.Date parsedUtilDate;
			try {
				parsedUtilDate = formater.parse(arg0.getAsString());
			} catch (ParseException e) {
				throw new JsonParseException(e);
			}

			rtnDate = new Date(parsedUtilDate.getTime());
		}

		return rtnDate;
	}


}
