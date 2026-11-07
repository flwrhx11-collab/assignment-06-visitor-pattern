# Assignment 06 - Visitor Pattern (Advanced File System)

## Description
This project implements an in-memory hierarchical file system using the **Visitor Pattern** and **Double Dispatch** in Java 17.
It supports arbitrary nesting of directories and files, and provides three independent operations over the tree without modifying the element classes:
1. `TotalSizeVisitor` - calculates the total size of files.
2. `SearchVisitor` - searches for files by name criterion and minimum size threshold.
3. `StatisticsVisitor` - collects file count, directory count, and maximum tree depth.

## Prerequisites
- Java 17
- Maven

## How to run tests
All JUnit 5 tests can be executed from the project root using the following command:
```bash
mvn test