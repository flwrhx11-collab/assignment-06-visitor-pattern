package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FileSystemTest {

    @Test
    public void testDoubleDispatchAndVisitMethods() {
        DirectoryNode root = new DirectoryNode("root");
        FileNode file = new FileNode("doc.txt", 100);
        root.addChild(file);

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        root.accept(sizeVisitor);

        assertEquals(100, sizeVisitor.getTotalSize());
    }

    @Test
    public void testSizeAccumulationAcrossNestingLevels() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode folder1 = new DirectoryNode("folder1");
        DirectoryNode folder2 = new DirectoryNode("folder2");

        folder1.addChild(new FileNode("file1.txt", 50));
        folder2.addChild(new FileNode("file2.txt", 150));
        folder1.addChild(folder2);
        root.addChild(folder1);
        root.addChild(new FileNode("file3.txt", 200));

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        root.accept(sizeVisitor);

        assertEquals(400, sizeVisitor.getTotalSize());
    }

    @Test
    public void testSearchCombinedFiltersAndDeterministicPaths() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode docs = new DirectoryNode("docs");
        root.addChild(docs);

        docs.addChild(new FileNode("report_a.pdf", 500));
        docs.addChild(new FileNode("report_b.pdf", 100));
        root.addChild(new FileNode("report_c.pdf", 600));
        root.addChild(new FileNode("image.png", 700));

        SearchVisitor searchVisitor = new SearchVisitor("report", 200);
        root.accept(searchVisitor);

        List<String> results = searchVisitor.getFoundPaths();
        assertEquals(2, results.size());
        assertEquals("root/docs/report_a.pdf", results.get(0));
        assertEquals("root/report_c.pdf", results.get(1));
    }

    @Test
    public void testStatisticsNestingAndMaxDepth() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode level1 = new DirectoryNode("level1");
        DirectoryNode level2 = new DirectoryNode("level2");

        level1.addChild(new FileNode("f1.txt", 10));
        level2.addChild(new FileNode("f2.txt", 20));

        level1.addChild(level2);
        root.addChild(level1);

        StatisticsVisitor statsVisitor = new StatisticsVisitor();
        root.accept(statsVisitor);

        assertEquals(2, statsVisitor.getFileCount());
        assertEquals(3, statsVisitor.getDirectoryCount());
        assertEquals(3, statsVisitor.getMaxDepth());
    }

    @Test
    public void testEmptyStructuresAndNoMatchSearch() {
        DirectoryNode root = new DirectoryNode("emptyRoot");

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        SearchVisitor searchVisitor = new SearchVisitor("ghost", 0);
        StatisticsVisitor statsVisitor = new StatisticsVisitor();

        root.accept(sizeVisitor);
        root.accept(searchVisitor);
        root.accept(statsVisitor);

        assertEquals(0, sizeVisitor.getTotalSize());
        assertTrue(searchVisitor.getFoundPaths().isEmpty());
        assertEquals(0, statsVisitor.getFileCount());
        assertEquals(1, statsVisitor.getDirectoryCount());
        assertEquals(0, statsVisitor.getMaxDepth());
    }

    @Test
    public void testRepeatExecutionsDoNotLeakState() {
        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("data.bin", 1024));

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();

        root.accept(sizeVisitor);
        assertEquals(1024, sizeVisitor.getTotalSize());

        sizeVisitor.reset();
        root.accept(sizeVisitor);
        assertEquals(1024, sizeVisitor.getTotalSize());
    }

    @Test
    public void testInvalidTreeMutationsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("bad.txt", -50));

        DirectoryNode folder = new DirectoryNode("folder");
        folder.addChild(new FileNode("file.txt", 10));

        assertThrows(IllegalArgumentException.class, () -> folder.addChild(new FileNode("file.txt", 20)));
    }
}