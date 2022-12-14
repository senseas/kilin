package com.kilin.ast.statement;

import com.kilin.ast.Node;
import com.kilin.ast.Stream;
import com.kilin.ast.expression.Expression;
import com.kilin.ast.expression.ParametersExpression;
import com.kilin.ast.lexer.TokenType;

import java.util.List;

public class ForStatement extends Statement {
    private Expression initializer;
    private Expression condition;
    private Expression update;
    private BlockStatement body;

    public ForStatement(Node prarent, Expression initializer, Expression condition, Expression update, BlockStatement body) {
        super(prarent);
        this.initializer = initializer;
        this.condition = condition;
        this.update = update;
        this.body = body;

        this.initializer.setPrarent(this);
        this.condition.setPrarent(this);
        this.update.setPrarent(this);
        this.body.setPrarent(this);

        getChildrens().addAll(initializer, condition, update, body);
    }

    public static void parser(Node node) {
        if (node instanceof ForStatement) return;
        Stream.of(node.getChildrens()).reduce((c, m, n) -> {
            if (m.equals(TokenType.FOR) && (n instanceof ParametersExpression && !n.getChildrens().get(0).getChildrens().stream().anyMatch(e -> e.equals(TokenType.COLON)))) {
                List<Node> nodes = n.getChildrens();
                Node b = node.next();
                if (b instanceof BlockStatement) {
                    //create ForNode and set Prarent，Parameters
                    ForStatement statement = new ForStatement(node, (Expression) nodes.get(0), (Expression) nodes.get(1), (Expression) nodes.get(2), (BlockStatement) b);
                    //remove ForNode and Parameters
                    node.getChildrens().removeAll(m, n);
                    node.getPrarent().replace(node, statement);
                    node.getPrarent().getChildrens().remove(b);
                } else {
                    //remove ForNode and Parameters
                    node.getChildrens().removeAll(m, n);
                    //create BlockNode and set Childrens
                    BlockStatement block = new BlockStatement(null, node.getChildrens());
                    //create ForNode and set Prarent , Parameters
                    ForStatement statement = new ForStatement(node, (Expression) nodes.get(0), (Expression) nodes.get(1), (Expression) nodes.get(2), block);
                    //replace this node whit ForNode
                    node.getPrarent().replace(node, statement);
                }
            }
        });
    }
}