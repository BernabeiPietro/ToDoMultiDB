package com.example.todoappmultidb.webcontroller.databinder;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class StringToMapConverterTest {

	StringToMapConverter toMap;

	@Before
	public void setup() {
		toMap = new StringToMapConverter();
	}

	@Test
	public void test_convert_onlyCurvyBracket() {
		assertThat(toMap.convert("{}")).isEqualTo(Collections.emptyMap());
	}

	@Test
	public void test_convert_emptyInput() {
		assertThat(toMap.convert("")).isEqualTo(Collections.emptyMap());
	}

	@Test
	public void test_convert_null() {
		assertThat(toMap.convert(null)).isEqualTo(Collections.emptyMap());
	}

	@Test
	public void test_convert_OneWord() {
		assertThat(toMap.convert("{first}")).isEqualTo(Collections.emptyMap());
	}

	@Test
	public void test_convert_OnePair() {
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("first", true);
		assertThat(toMap.convert("{first=true}")).isEqualTo(result);
	}

	@Test
	public void test_convert_OnePair_BadDefined() {
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("first", false);
		assertThat(toMap.convert("{first=Pippo}")).isEqualTo(result);
	}

	@Test
	public void test_convert_OnePair_NotStartWithCurvyBracket() {
		assertThat(toMap.convert("first=Pippo}")).isEqualTo(Collections.EMPTY_MAP);
	}

	@Test
	public void test_convert_OnePair_NotEndWithCurvyBracket() {
		assertThat(toMap.convert("{first=Pippo")).isEqualTo(Collections.EMPTY_MAP);
	}

	@Test
	public void test_convert_TwoPair() {
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("first", false);
		result.put("second", true);
		assertThat(toMap.convert("{first=false,second=true}")).isEqualTo(result);
	}

	@Test
	public void test_convert_TwoPair_oneBadFormed() {
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("first", false);
		assertThat(toMap.convert("{first=false,second true}")).isEqualTo(result);
	}
}
