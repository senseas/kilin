package com.kilin.ast;

import com.kilin.ast.declaration.Declaration;
import com.kilin.ast.expression.Expression;
import com.kilin.ast.lexer.Token;
import com.kilin.ast.lexer.TokenType;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Node extends Token {
    private Node prarent;
    private NodeList<Node> childrens;

    public Node() {
        super(null);
    }

    public Node(TokenType type) {
        super(type);
    }

    public Node(Node prarent) {
        super(null);
        this.prarent = prarent;
    }

    public Node getPrarent() {
        return prarent;
    }

    public NodeList<Node> getChildrens() {
        if (Objects.nonNull(childrens)) return childrens;
        return childrens = new NodeList<>();
    }

    public void setChildrens(NodeList<Node> childrens) {
        this.childrens = childrens;
    }

    public void replaceAndRemove(Node node, Node replaceNode, Object removeNode) {
        replace(node, replaceNode);
        getChildrens().remove(removeNode);
    }

    public void replace(Node node, Node replaceNode) {
        int index = childrens.indexOf(node);
        childrens.set(index, replaceNode);
    }

    public List<TokenType> getFieldModifiers() {
        List<Node> modifiers = getChildrens().stream().filter(e -> Declaration.Field_Modifiers.contains(e.getTokenType())).toList();
        getChildrens().removeAll(modifiers);
        return modifiers.stream().map(Node::getTokenType).collect(Collectors.toList());
    }

    public List<TokenType> getMethodModifiers() {
        List<Node> modifiers = getChildrens().stream().filter(e -> Declaration.Method_Modifiers.contains(e.getTokenType())).toList();
        getChildrens().removeAll(modifiers);
        return modifiers.stream().map(Node::getTokenType).collect(Collectors.toList());
    }

    public NodeList split(Node node) {
        int index = childrens.indexOf(node);
        if (index == -1) return null;
        return split(index);
    }

    public NodeList split(TokenType type) {
        Node node = childrens.stream().filter(a -> a.equals(type)).findFirst().orElse(null);
        int index = childrens.indexOf(node);
        if (index == -1) return null;
        return split(index);
    }

    private NodeList split(int index) {
        Node a;
        List<Node> nodea = childrens.subList(0, index);
        if (nodea.size() == 1) {
            a = nodea.get(0);
        } else {
            a = new Expression(null);
            a.setChildrens(new NodeList<>(nodea));
        }
        Node b;
        List<Node> nodeb = childrens.subList(index + 1, childrens.size());
        if (nodeb.size() == 1) {
            b = nodeb.get(0);
        } else {
            b = new Expression(null);
            b.setChildrens(new NodeList<>(nodeb));
        }
        return new NodeList(a, b);
    }

    public boolean endsTypeof(Type clas) {
        return childrens.get(childrens.size() - 1).getClass().equals(clas);
    }

    public boolean isLast(Node node) {
        return getChildrens().last() == node;
    }

    public boolean isFirst(Node node) {
        return getChildrens().first() == node;
    }

    public Node next() {
        if (Objects.nonNull(getPrarent())) {
            NodeList<Node> childrens = getPrarent().getChildrens();
            int index = childrens.indexOf(this) + 1;
            if (index < childrens.size()) {
                return childrens.get(index);
            }
        }
        return null;
    }

    public Node previous() {
        if (Objects.nonNull(getPrarent())) {
            NodeList<Node> childrens = getPrarent().getChildrens();
            int index = childrens.indexOf(this) - 1;
            if (0 <= index) {
                return childrens.get(index);
            }
        }
        return null;
    }

    public void setPrarent(Node prarent) {
        this.prarent = prarent;
    }

    @Override
    public String toString() {
        if (Objects.nonNull(getTokenType())) return getTokenType().toString();
        if (childrens.isEmpty()) return "";
        return childrens.stream().map(Objects::toString).collect(Collectors.joining(" "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return getTokenType() == o;
    }
}