package com.kilin.ast.statement;

import com.kilin.ast.Node;
import com.kilin.ast.Stream;
import com.kilin.ast.lexer.TokenType;

public class BreakStatement extends Statement {

    public BreakStatement(Node prarent) {
        super(prarent);
    }

    public static void parser(Node node) {
        Stream.of(node.getChildrens()).reduce2((list, a, b) -> {
            Stream.of(a.getChildrens()).reduce2((c, m, n) -> {
                if (m.equals(TokenType.BREAK)) {
                    //create SynchronizedNode and set Prarent，Parameters
                    BreakStatement statement = new BreakStatement(node);
                    //replace this node with SynchronizedNode
                    c.replace(m, statement);
                }
            });
        });
    }
}