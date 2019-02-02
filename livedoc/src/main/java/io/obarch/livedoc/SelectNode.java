package io.obarch.livedoc;

import org.commonmark.node.Node;

import java.util.List;

public interface SelectNode {

    List<Node> select(List<Node> nodes);
}
