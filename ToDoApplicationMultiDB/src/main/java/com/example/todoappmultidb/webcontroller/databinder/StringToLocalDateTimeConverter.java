package com.example.todoappmultidb.webcontroller.databinder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

	@Override
	public LocalDateTime convert(String source) {

		if (source.isEmpty() || source.contentEquals("[]"))
			return LocalDateTime.of(0, 1, 1, 0, 0);

		if (source.startsWith("["))
			source = source.substring(1, source.length());
		if (source.endsWith("]"))

			source = source.substring(0, source.length() - 1);
		if (source.contains("T"))
			return LocalDateTime.parse(source, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			return LocalDateTime.parse(source, formatter);
		}
	}

}
