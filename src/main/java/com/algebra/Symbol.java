package com.algebra;

public enum Symbol {
	NUMBER(Integer.MIN_VALUE),
	ADD(0),
	SUBTRACT(0),
	MULTIPLY(1),
	DIVIDE(1),
	MODULO(1),
	POWER(2),
	BRACKET_OPEN(3),
	BRACKET_CLOSE(3);
	public final int priority;
	private Symbol(int priority) {
		this.priority = priority;
	}

	public static Symbol valueOf(char symbol) {
		if (symbol == '^') return POWER;
		else if (symbol == '%') return MODULO;
		else if (symbol == '/') return DIVIDE;
		else if (symbol == '*') return MULTIPLY;
		else if (symbol == '+') return ADD;
		else if (symbol == '-') return SUBTRACT;
		else if (symbol == '(') return BRACKET_OPEN;
		else if (symbol == ')') return BRACKET_CLOSE;
		else return null;
	}

	public static char symbolFor(Symbol symbol) {
		if (symbol == POWER) return '^';
		else if (symbol == MODULO) return '%';
		else if (symbol == DIVIDE) return '/';
		else if (symbol == MULTIPLY) return '*';
		else if (symbol == ADD) return '+';
		else if (symbol == SUBTRACT) return '-';
		else if (symbol == BRACKET_OPEN) return '(';
		else if (symbol == BRACKET_CLOSE) return ')';
		else return '\0';
	}
}
