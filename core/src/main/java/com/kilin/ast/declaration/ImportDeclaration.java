package com.kilin.ast.declaration;

import com.kilin.ast.Node;
import com.kilin.ast.Stream;
import com.kilin.ast.expression.Name;
import com.kilin.ast.lexer.TokenType;

public class ImportDeclaration extends Declaration {
    private Name name;

    public ImportDeclaration(Node prarent, Name name) {
        super(prarent);
        this.name = name;
        this.name.setPrarent(this);
        getChildrens().add(name);
    }

    public Node getName() {
        return name;
    }

    public static void parser(Node node) {
        Stream.of(node.getChildrens()).reduce(((list, a, b) -> {
            if (a.equals(TokenType.IMPORT) && b.equals(TokenType.MUL)) {
                ImportDeclaration declare = new ImportDeclaration(node.getPrarent(), new Name(b.getTokenType()));
                node.getPrarent().replace(node, declare);
                node.getChildrens().remove(a);
                list.clear();
            } else if (a.equals(TokenType.IMPORT) && b instanceof Name) {
                ImportDeclaration declare = new ImportDeclaration(node.getPrarent(), (Name) b);
                node.getPrarent().replace(node, declare);
                node.getChildrens().remove(a);
                list.clear();
            }
        }));
    }

}