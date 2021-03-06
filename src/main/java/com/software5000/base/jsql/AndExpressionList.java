package com.software5000.base.jsql;


import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;

import java.util.List;

/**
 * 过滤条件组装类
 *
 * @author mauotasyouca@gmail.com
 */
public class AndExpressionList {

    private Expression singleExpression = null;
    private AndExpression andExpression = null;

    /**
     * 添加单个过滤条件表达式
     *
     * @param rightExpression 单个过滤条件
     * @return 合并的and表达式
     */
    public AndExpressionList append(Expression rightExpression) {
        if (singleExpression == null) {
            singleExpression = rightExpression;
            return this;
        }

        if (andExpression == null) {
            andExpression = new AndExpression(singleExpression, rightExpression);
        } else {
            andExpression = new AndExpression(andExpression, rightExpression);
        }
        return this;
    }

    /**
     * 添加过滤条件列表
     *
     * @param expressions 过滤条件列表
     * @return  合并的and表达式，并且返回自身用于多级操作
     */
    public AndExpression appendListAndGet(List<Expression> expressions) {
        for (Expression expression : expressions) {
            this.append(expression);
        }
        return andExpression;
    }

    /**
     * 获取最终的完整过滤条件
     *
     * @return 最终的完整过滤条件
     */
    public Expression get() {
        return andExpression == null ? singleExpression: andExpression;
    }

}
