package com.algebra;

import java.util.ArrayList;

public class Expression {

    public ArrayList<ExpressionSegment> segments;

    public void addElement(String baseExpression, int level) {

        segments.add(new ExpressionSegment(baseExpression, level));

    }

    public ExpressionSegment getElement(int index) {

        return segments.get(index);
        
    }

}