package com.algebra;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class EvalTest {
	
	@Test
	public void testTokenString() {
		assertEquals("Bad token stringification", "453", new Tokeniser.Token(new BigDecimal(453)).toString());
	}

	@Test
	public void testPositiveEval() {
		System.err.println(new Evaluator().evaluate("524+6*(434-45)+5^3"));
	}

	@Test
	public void testNegativeEval() {
		System.out.println(new Evaluator().evaluate("-5452+5*(-25+3)"));		
	}

}
