package com.algebra;

public class Evaluator {
	
	public String evaluate(String expr) {
		
		return new Tokeniser().tokenise(expr).toString();
	}

}
