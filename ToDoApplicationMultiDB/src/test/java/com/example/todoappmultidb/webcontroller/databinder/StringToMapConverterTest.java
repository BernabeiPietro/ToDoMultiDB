package com.example.todoappmultidb.webcontroller.databinder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.inOrder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StringToMapConverterTest {

	StringToMapConverter toMap;

	@Test
	public void test_convert_onlyCurvyBracket() {
		toMap = new StringToMapConverter();
		assertThat(toMap.convert("{}")).isEqualTo(Collections.emptyMap());
	}

	@Test
	public void test_convert_emptyInput() {
		toMap = new StringToMapConverter();
		assertThat(toMap.convert("")).isEqualTo(Collections.emptyMap());
	}

	@Test
	public void test_convert_null() {
		toMap = new StringToMapConverter();
		assertThat(toMap.convert(null)).isEqualTo(Collections.emptyMap());
	}

	@Test
	public void test_convert_OneWord() {
		toMap = new StringToMapConverter();
		assertThat(toMap.convert("{first}")).isEqualTo(Collections.emptyMap());
	}

	@Test
	public void test_convert_OnePair() {
		toMap=new StringToMapConverter();
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("first", true);
		assertThat(toMap.convert("{first=true}")).isEqualTo(result);
	}
	@Test
	public void test_convert_OnePair_BadDefined() {
		toMap=new StringToMapConverter();
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("first", false);
		assertThat(toMap.convert("{first=Pippo}")).isEqualTo(result);
	}
	@Test
	public void test_convert_OnePair_NotStartWithCurvyBracket() {
		toMap=new StringToMapConverter();
		assertThat(toMap.convert("first=Pippo}")).isEqualTo(Collections.EMPTY_MAP);
	}
	@Test
	public void test_convert_OnePair_NotEndWithCurvyBracket() {
		toMap=new StringToMapConverter();
		assertThat(toMap.convert("{first=Pippo")).isEqualTo(Collections.EMPTY_MAP);
	}
	
	@Test
	public void test_convert_TwoPair() {
		toMap=new StringToMapConverter();
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("first", false);
		result.put("second",true);
		assertThat(toMap.convert("{first=false,second=true}")).isEqualTo(result);
	}
	@Test
	public void test_convert_TwoPair_oneBadFormed() {
		toMap=new StringToMapConverter();
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("first", false);
		assertThat(toMap.convert("{first=false,second true}")).isEqualTo(result);
	}
}
