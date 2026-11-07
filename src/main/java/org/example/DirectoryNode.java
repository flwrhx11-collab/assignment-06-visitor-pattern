package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectoryNode implements Element {
    private final String name;
    private final List<Element> children;

    public DirectoryNode(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    public List<Element> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void addChild(Element child) {
        for (Element c : children) {
            if (c.getName().equals(child.getName())) {
                throw new IllegalArgumentException("Element with this name already exists: " + child.getName());
            }
        }
        children.add(child);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}