package org.jspringbot.keyword.expression;

import de.odysseus.el.tree.Node;
import de.odysseus.el.tree.impl.ast.AstIdentifier;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class VariableNodeCapture {

    private static void dump(Set<String> capture, Node node, Stack<Node> predecessors) {
        if(AstIdentifier.class.isInstance(node)) {
            capture.add(node.toString());
        }

        predecessors.push(node);
        for (int i = 0; i < node.getCardinality(); i++) {
            dump(capture, node.getChild(i), predecessors);
        }
        predecessors.pop();
    }

    public static Set<String> capture(Node node) {
        Set<String> captured = new HashSet<String>();
        dump(captured, node, new Stack<Node>());

        return captured;
    }
}
