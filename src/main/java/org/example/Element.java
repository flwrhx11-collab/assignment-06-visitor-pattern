package org.example;

public interface Element {
    void accept(Visitor visitor);
    String getName();
}