package com.example.todoappmultidb.webcontroller.databinder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToMapConverter implements Converter<String, Map<String, Boolean>> {

	@Override
	public Map<String, Boolean> convert(String source) {

		if (source == null ||!source.startsWith("{")|| !source.endsWith("}"))
			return Collections.emptyMap();
		source = source.substring(1, source.length() - 1);
		HashMap<String, Boolean> result = new HashMap<>();
		String[] pair = source.split(",");
		for (String s : pair) {
			String[] splitted = s.split("=");
			if (splitted.length > 1) {
				result.put(splitted[0], Boolean.valueOf(splitted[1]));
			}
		}
		return result;
	}

}
