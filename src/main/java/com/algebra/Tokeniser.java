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
			String s = "";
			if (operands == null || operands.length == 0) return new String(new char[] {Symbol.symbolFor(type)});
			for (Token t : operands) {
				s += t;
				s += Symbol.symbolFor(type);
			}
			return s.substring(0, s.length() - new String(new char[] {Symbol.symbolFor(type)}).length());
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
				currentNum = "";
				tokens.add(new Token(new Token[0], Symbol.valueOf(expression.charAt(i))));
			}
		}

		int first = -1, last = -1;

		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).type.equals(Symbol.BRACKET_OPEN) && first != -1) first = i;
			else if (tokens.get(i).type.equals(Symbol.BRACKET_CLOSE)) last = i;
		}

		if (first != -1 && last != -1) {

			Token[] parentheses = new Token[last + 1 - first];
			for (int i = 0; i <= last + 1 - first; i++) {
				parentheses[i] = tokens.get(i + first - 1);
				tokens.remove(i + first - 1);
			}
			String newExpression = "";
			for (Token t : parentheses) newExpression += t.toString();
			tokens.add(first, tokenise(newExpression));
		}

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
		for (int i = 0; i < tokens.size(); ++i) {

			System.out.println(i);
			if (tokens.get(i).type == null) continue;

			if (tokens.get(i).type.priority == level) {
				System.out.println(i);
				tokens.set(i, new Token(new Token[] {tokens.get(i - 1), tokens.get(i + 1)}, tokens.get(i).type));
				tokens.remove(i + 1);
				tokens.remove(i - 1);
				i--;
				// break;
			}

		}
	}

}
