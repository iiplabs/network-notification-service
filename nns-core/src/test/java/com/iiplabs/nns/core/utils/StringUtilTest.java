package com.iiplabs.nns.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class StringUtilTest {

	@Test
	public void testGoodLastField() {
		String source = "card.expiry";
		assertEquals("expiry", StringUtil.getLastField(source));
	}

	@Test
	public void testBadLastField() {
		String source = "expiry";
		assertEquals("expiry", StringUtil.getLastField(source));
	}

	@Test
	public void testStringSubstitutor() {
		String template = "Sample {$time}. end.";

		Map<String, String> values = new HashMap<>();
		values.put("time", "123");

		StringSubstitutor substitutor = new StringSubstitutor(values, "{$", "}");

		assertEquals("Sample 123. end.", substitutor.replace(template));
	}

	@Test
	public void timeKeyTest() {
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("'${time}'");
		assertEquals("${time}", exp.getValue(String.class));
	}

}
