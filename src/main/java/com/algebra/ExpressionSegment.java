package com.algebra;

public class ExpressionSegment {

    public String subExpression;
    public int level;

    public ExpressionSegment(String baseExpression, int level) {

        this.subExpression = baseExpression;
        this.level = level;

    }

}