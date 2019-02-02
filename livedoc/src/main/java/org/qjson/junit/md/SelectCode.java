package org.qjson.junit.md;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.Node;
import org.commonmark.node.Visitor;

import java.util.ArrayList;
import java.util.List;

class SelectCode {

    private String lang;

    public SelectCode(String lang) {
        this.lang = lang;
    }

    public List<String> select(List<Node> nodes) {
        List<String> selected = new ArrayList<>();
        Visitor visitor = new AbstractVisitor() {
            @Override
            public void visit(FencedCodeBlock code) {
                if (lang == null || lang.equals(code.getInfo())) {
                    selected.add(code.getLiteral());
                }
            }
        };
        for (Node node : nodes) {
            node.accept(visitor);
        }
        return selected;
    }
}
