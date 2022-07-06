package com.iiplabs.nns.sms.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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

}
