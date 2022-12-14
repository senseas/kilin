package com.kilin.ast.statement;

import com.kilin.ast.Node;
import com.kilin.ast.Stream;
import com.kilin.ast.expression.Expression;
import com.kilin.ast.lexer.TokenType;

import java.util.List;

public class DoWhileStatement extends Statement {
    private Expression condition;
    private Statement body;

    public DoWhileStatement(Node prarent, Expression condition, Statement body) {
        super(prarent);
        this.condition = condition;
        this.body = body;

        this.condition.setPrarent(this);
        this.body.setPrarent(this);

        getChildrens().addAll(condition, body);
    }

    public static void parser(Node node) {
        if (node instanceof ForStatement) return;
        Stream.of(node.getChildrens()).reduce((list, a, b, c) -> {
            Stream.of(a.getChildrens()).reduce((e, m, n) -> {
                if (m.equals(TokenType.DO) && b instanceof BlockStatement) {
                    Node condition = c.getChildrens().get(1);
                    //create ForNode and set Prarent , Parameters
                    DoWhileStatement statement = new DoWhileStatement(node, (Expression) condition, (Statement) b);
                    //remove ForNode and Parameters
                    node.replace(a, statement);
                    node.getChildrens().removeAll(List.of(b, c));
                    list.removeAll(List.of(b, c));
                }
            });
        });
    }

}