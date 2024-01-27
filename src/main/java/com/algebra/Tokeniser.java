package com.algebra;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Tokeniser {

	public String NUMBERS = "1234567890.";
	
	public static class Token {

		public Token[] operands;
		public Symbol type;
		public BigDecimal value;

		public String toString() {
			if (!type.equals(Symbol.NUMBER)) {
				String s = "";
				if (operands == null || operands.length == 0) return new String(new char[] {Symbol.symbolFor(type)});
				for (Token t : operands) {
					s += t;
					s += Symbol.symbolFor(type);
				}
				return s.substring(0, s.length() - new String(new char[] {Symbol.symbolFor(type)}).length());
			} else return value.toPlainString();
		}
		public Token(Token[] operands, Symbol type) {
			this.operands = operands;
			this.type = type;
		}
		public Token(BigDecimal value) {
			this.operands = new Token[0];
			this.type = Symbol.NUMBER;
			this.value = value;
		}

	}

	public Token tokenise(String expression) {

		// *=times
		// +=add
		// -=subtract
		// /=divide
		// %= modulo
		// () = brackets, sub-expressions
		// [] = function arguments

		expression = expression.replace(" ", "");

		List<Token> tokens = new ArrayList<>();

		// Single-layer tokenisation
		String currentNum = "";
		for (int i = 0; i < expression.length(); i++) {
			if (Matcher.matches(expression.charAt(i), NUMBERS)) {
				currentNum += expression.charAt(i);
			} else {
				if (currentNum != "") tokens.add(new Token(new BigDecimal(currentNum)));
				System.out.println("Current num not match = " + currentNum);
				currentNum = "";
				tokens.add(new Token(new Token[0], Symbol.valueOf(expression.charAt(i))));
			}
		}
		System.out.println("Current num end = " + currentNum);
		if (currentNum != "") tokens.add(new Token(new BigDecimal(currentNum)));

		int first = -1, last = -1;

		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).type.equals(Symbol.BRACKET) && first == -1) first = i;
			else if (tokens.get(i).type.equals(Symbol.BRACKET_CLOSE)) last = i;
		}

		if (first != -1 && last != -1) {

			Token[] parentheses = new Token[last - first];
			for (int i = first + 1; i <= last - 1; i++) {
				parentheses[i - (first + 1)] = tokens.get(i);
				tokens.remove(i);
			}
			tokens.remove(first);
			tokens.remove(last);
			String newExpression = "";
			for (Token t : parentheses) if (t != null) newExpression += t.toString();
			System.err.println(newExpression);
			tokens.add(first, tokenise(newExpression));
		} else System.err.println("No parentheses");

		System.err.println("Size is " + tokens.size());
		for (Token t : tokens) System.err.println(t);

		// Squash list

		// Example 4*5/(-25+3)
		// Topology starts as:
		//[4] [*] [5] [/] [(] [-25] [+] [3] [)]
		// Squash by doing this:
		// [4] [*] [5] [/] [(] [-25+3] [)]
		// [4] [*] [5] [/] [(-25+3)]
		// [4] [*] [5/(-25+3)]
		// [4*5/(-25+3)]
		// Using alternating brackets to make it clear (<> is enclosing brackets/whole expression, {} is operation, [] is number):
		// < { { [4]*[5] }/<( {[-25]+[3]} )> } >
		// 0011223332333221222344444344432221100 (number = layer, higher = deeper)
		
		tokeniseFor(tokens, 2);
		tokeniseFor(tokens, 1);
		tokeniseFor(tokens, 0);

		return tokens.get(0);

	}

	private void tokeniseFor(List<Token> tokens, int level) {
		int size = tokens.size();
		for (int i = 0; i < size; ++i) {

			if (tokens.get(i).type == null) continue;
			System.err.println("number is " + i + "; value is " + tokens.get(i).toString());

			if (tokens.get(i).type.priority == level) {
				tokens.set(i, new Token(new Token[] {
					tokens.get(i - 1), 
					tokens.get(i + 1)}, 
					tokens.get(i).type)
				);
				if (tokens.size() > i + 1) tokens.remove(i + 1);
				if (i - 1 >= 0) tokens.remove(i - 1);
				size -= 2;
				// break;
			}

		}

	}

}
