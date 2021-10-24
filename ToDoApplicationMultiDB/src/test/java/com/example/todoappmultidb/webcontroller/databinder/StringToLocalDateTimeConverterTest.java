package com.example.todoappmultidb.webcontroller.databinder;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.Test;

public class StringToLocalDateTimeConverterTest {

	@Test
	public void testEmptySquareBrackets() {
		StringToLocalDateTimeConverter toDate=new StringToLocalDateTimeConverter();
		assertThat(toDate.convert("[]")).isEqualTo(LocalDateTime.of(0, 1, 1, 0, 0));
		
	}
	@Test
	public void testEmptyString() {
		StringToLocalDateTimeConverter toDate=new StringToLocalDateTimeConverter();
		assertThat(toDate.convert("")).isEqualTo(LocalDateTime.of(0, 1, 1, 0, 0));
		
	}

	@Test
	public void testIsoLocalDateTime() {
		StringToLocalDateTimeConverter toDate=new StringToLocalDateTimeConverter();
		assertThat(toDate.convert("2007-06-01T00:00")).isEqualTo(LocalDateTime.of(2007,06,1,0,0));
	}
	@Test
	public void testIsoLocalDateTime_withSquareBracket() {
		StringToLocalDateTimeConverter toDate=new StringToLocalDateTimeConverter();
		assertThat(toDate.convert("[2007-06-01T00:00]")).isEqualTo(LocalDateTime.of(2007,06,1,0,0));
	}
	@Test
	public void testDateTimeSplit_withSquareBracket(){
		StringToLocalDateTimeConverter toDate=new StringToLocalDateTimeConverter();
		assertThat(toDate.convert("[2007-06-01 00:00:00]")).isEqualTo(LocalDateTime.of(2007,06,1,0,0));
	}
	@Test
	public void testDateTimeSplit(){
		StringToLocalDateTimeConverter toDate=new StringToLocalDateTimeConverter();
		assertThat(toDate.convert("2007-06-01 00:00:00")).isEqualTo(LocalDateTime.of(2007,06,1,0,0));
	}
	
}
