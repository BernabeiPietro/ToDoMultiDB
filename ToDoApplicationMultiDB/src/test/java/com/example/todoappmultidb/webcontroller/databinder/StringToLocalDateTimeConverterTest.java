package com.example.todoappmultidb.webcontroller.databinder;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

public class StringToLocalDateTimeConverterTest {

	private StringToLocalDateTimeConverter toDate;

	@Before
	public void setup() {
		toDate = new StringToLocalDateTimeConverter();
	}

	@Test
	public void testEmptySquareBrackets() {
		assertThat(toDate.convert("[]")).isEqualTo(LocalDateTime.of(0, 1, 1, 0, 0));

	}

	@Test
	public void testEmptyString() {
		assertThat(toDate.convert("")).isEqualTo(LocalDateTime.of(0, 1, 1, 0, 0));

	}

	@Test
	public void testIsoLocalDateTime() {
		assertThat(toDate.convert("2007-06-01T00:00")).isEqualTo(LocalDateTime.of(2007, 06, 1, 0, 0));
	}

	@Test
	public void testIsoLocalDateTime_withSquareBracket() {
		assertThat(toDate.convert("[2007-06-01T00:00]")).isEqualTo(LocalDateTime.of(2007, 06, 1, 0, 0));
	}

	@Test
	public void testIsoLocalDateTime_withLeftBracket() {
		assertThat(toDate.convert("[2007-06-01T00:00")).isEqualTo(LocalDateTime.of(2007, 06, 1, 0, 0));
	}

	@Test
	public void testIsoLocalDateTime_withRightBracket() {
		assertThat(toDate.convert("2007-06-01T00:00]")).isEqualTo(LocalDateTime.of(2007, 06, 1, 0, 0));
	}

	@Test
	public void testDateTimeSplit_withSquareBracket() {
		assertThat(toDate.convert("[2007-06-01 00:00:00]")).isEqualTo(LocalDateTime.of(2007, 06, 1, 0, 0));
	}

	@Test
	public void testDateTimeSplit() {
		assertThat(toDate.convert("2007-06-01 00:00:00")).isEqualTo(LocalDateTime.of(2007, 06, 1, 0, 0));
	}

}
