package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchVisitor implements Visitor {
    private final String filenameCriterion;
    private final long minSizeThreshold;
    private final List<String> foundPaths;
    private String currentPath;

    public SearchVisitor(String filenameCriterion, long minSizeThreshold) {
        this.filenameCriterion = filenameCriterion;
        this.minSizeThreshold = minSizeThreshold;
        this.foundPaths = new ArrayList<>();
        this.currentPath = "";
    }

    public List<String> getFoundPaths() {
        Collections.sort(foundPaths);
        return foundPaths;
    }

    public void reset() {
        foundPaths.clear();
        currentPath = "";
    }

    @Override
    public void visit(FileNode file) {
        if (file.getName().contains(filenameCriterion) && file.getSize() >= minSizeThreshold) {
            String separator = currentPath.isEmpty() ? "" : "/";
            foundPaths.add(currentPath + separator + file.getName());
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
        String previousPath = currentPath;

        if (currentPath.isEmpty()) {
            currentPath = directory.getName();
        } else {
            currentPath = currentPath + "/" + directory.getName();
        }

        for (Element child : directory.getChildren()) {
            child.accept(this);
        }

        currentPath = previousPath;
    }
}