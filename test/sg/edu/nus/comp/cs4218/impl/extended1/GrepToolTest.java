package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.TemporaryFolder;
import sg.edu.nus.comp.cs4218.extended1.IGrepTool;

public class GrepToolTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private IGrepTool grepTool;
//    private File emptyFile;
//    private File oneNewLineFile;
//    private File oneLineFile;

    @Before
    public void before() throws IOException {
        grepTool = new GrepTool(new String[0]);

//        emptyFile = folder.newFile();
//        emptyFile.createNewFile();
//
//        oneNewLineFile = folder.newFile();
//        BufferedWriter oneNewLineBuf = new BufferedWriter(new FileWriter(oneNewLineFile));
//        oneNewLineBuf.write(System.lineSeparator());
//        oneNewLineBuf.close();
//
//        oneLineFile = folder.newFile();
//        BufferedWriter oneLineBuf = new BufferedWriter(new FileWriter(oneLineFile));
//        oneLineBuf.write(String.format("abc%n"));
//        oneLineBuf.close();
    }

    @After
    public void after(){
        grepTool = null;
    }

    @Test
    public void getCountOfMatchingLinesTest() {
        // Test null
        assertEquals(grepTool.getCountOfMatchingLines(null, null), -1);
        assertEquals(grepTool.getStatusCode(), 1);

        // Test empty file
        assertEquals(grepTool.getCountOfMatchingLines("", ""), 0);
        assertEquals(grepTool.getStatusCode(), 0);

        // Test file with one line separator
        assertEquals(grepTool.getCountOfMatchingLines("", System.lineSeparator()), 1);
        assertEquals(grepTool.getStatusCode(), 0);

        // Test regex
        assertEquals(grepTool.getCountOfMatchingLines("a[bc]", String.format("ab%nbc%nac%n")), 2);
        assertEquals(grepTool.getStatusCode(), 0);
    }

    @Test
    public void getOnlyMatchingLinesTest() {
        // Test null
        assertEquals(grepTool.getOnlyMatchingLines(null, null), "");
        assertEquals(grepTool.getStatusCode(), 1);

        // Test empty file
        assertEquals(grepTool.getOnlyMatchingLines("", ""), "");
        assertEquals(grepTool.getStatusCode(), 0);

        // Test file with one line separator
        assertEquals(grepTool.getOnlyMatchingLines("", System.lineSeparator()), System.lineSeparator());
        assertEquals(grepTool.getStatusCode(), 0);

        // Test regex
        assertEquals(grepTool.getOnlyMatchingLines("a[bc]", String.format("ab%nbc%nac%n")), String.format("ab%nac%n"));
        assertEquals(grepTool.getStatusCode(), 0);
    }

    @Test
    public void getMatchingLinesWithTrailingContextTest() {
        // Test null
        assertEquals(grepTool.getMatchingLinesWithTrailingContext(1, null, null), "");
        assertEquals(grepTool.getStatusCode(), 1);

        // Test empty file
        assertEquals(grepTool.getMatchingLinesWithTrailingContext(1, "", ""), "");
        assertEquals(grepTool.getStatusCode(), 0);

        // Test file with one line separator
        assertEquals(grepTool.getMatchingLinesWithTrailingContext(1, "", System.lineSeparator()), System.lineSeparator());
        assertEquals(grepTool.getStatusCode(), 0);

        // Test regex
        assertEquals(grepTool.getMatchingLinesWithTrailingContext(1, "a[bc]", String.format("ab%nbc%nac%n")), String.format("ab%nbc%nac%n"));
        assertEquals(grepTool.getStatusCode(), 0);
    }

    @Test
    public void getMatchingLinesWithLeadingContextTest() {
        // Test null
        assertEquals(grepTool.getMatchingLinesWithLeadingContext(1, null, null), "");
        assertEquals(grepTool.getStatusCode(), 1);

        // Test empty file
        assertEquals(grepTool.getMatchingLinesWithLeadingContext(1, "", ""), "");
        assertEquals(grepTool.getStatusCode(), 0);

        // Test file with one line separator
        assertEquals(grepTool.getMatchingLinesWithLeadingContext(1, "", System.lineSeparator()), System.lineSeparator());
        assertEquals(grepTool.getStatusCode(), 0);

        // Test regex
        assertEquals(grepTool.getMatchingLinesWithLeadingContext(1, "a[bc]", String.format("ab%nbc%nac%n")), String.format("ab%nbc%nac%n"));
        assertEquals(grepTool.getStatusCode(), 0);
    }

    @Test
    public void getMatchingLinesWithOutputContextTest() {
        // Test null
        assertEquals(grepTool.getMatchingLinesWithOutputContext(1, null, null), "");
        assertEquals(grepTool.getStatusCode(), 1);

        // Test empty file
        assertEquals(grepTool.getMatchingLinesWithOutputContext(1, "", ""), "");
        assertEquals(grepTool.getStatusCode(), 0);

        // Test file with one line separator
        assertEquals(grepTool.getMatchingLinesWithOutputContext(1, "", System.lineSeparator()), System.lineSeparator());
        assertEquals(grepTool.getStatusCode(), 0);

        // Test regex
        assertEquals(grepTool.getMatchingLinesWithOutputContext(1, "a[bc]", String.format("ab%nbc%nac%n")), String.format("ab%nbc%nac%n"));
        assertEquals(grepTool.getStatusCode(), 0);
    }

    @Test
    public void getMatchingLinesOnlyMatchingPartTest() {
        // Test null
        assertEquals(grepTool.getMatchingLinesOnlyMatchingPart(null, null), "");
        assertEquals(grepTool.getStatusCode(), 1);

        // Test empty file
        assertEquals(grepTool.getMatchingLinesOnlyMatchingPart("", ""), "");
        assertEquals(grepTool.getStatusCode(), 0);

        // Test file with one line separator
        assertEquals(grepTool.getMatchingLinesOnlyMatchingPart("", System.lineSeparator()), System.lineSeparator());
        assertEquals(grepTool.getStatusCode(), 0);

        // Test regex
        assertEquals(grepTool.getMatchingLinesOnlyMatchingPart("a[bc]", String.format("ab%nbc%nac%n")), String.format("ab%nac%n"));
        assertEquals(grepTool.getStatusCode(), 0);
    }

    @Test
    public void getNonMatchingLinesTest() {
        // Test null
        assertEquals(grepTool.getNonMatchingLines(null, null), "");
        assertEquals(grepTool.getStatusCode(), 1);

        // Test empty file
        assertEquals(grepTool.getNonMatchingLines("", ""), "");
        assertEquals(grepTool.getStatusCode(), 0);

        // Test file with one line separator
        assertEquals(grepTool.getNonMatchingLines("", System.lineSeparator()), "");
        assertEquals(grepTool.getStatusCode(), 0);

        // Test regex
        assertEquals(grepTool.getNonMatchingLines("a[bc]", String.format("ab%nbc%nac%n")), String.format("bc%n"));
        assertEquals(grepTool.getStatusCode(), 0);
    }

    @Test
    public void getHelpTest() {
        assertEquals(grepTool.getHelp(), String.format("grep [-cov] [-A num] [-B num] [-C num] [pattern] [file ...]%n"));
        assertEquals(grepTool.getStatusCode(), 0);
    }
}
