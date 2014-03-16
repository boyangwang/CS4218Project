package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

    @Before
    public void before() throws IOException {
        grepTool = new GrepTool(new String[0]);
    }

    @After
    public void after(){
        grepTool = null;
    }

    /**
     * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#getCountOfMatchingLines(java.lang.String, java.lang.String)}.
     */
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

    /**
     * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#getOnlyMatchingLines(java.lang.String, java.lang.String)}.
     */
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

    /**
     * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#getMatchingLinesWithTrailingContext(int, java.lang.String, java.lang.String)}.
     */
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

    /**
     * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#getMatchingLinesWithLeadingContext(int, java.lang.String, java.lang.String)}.
     */
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

    /**
     * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#getMatchingLinesWithOutputContext(int, java.lang.String, java.lang.String)}.
     */
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

    /**
     * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#getMatchingLinesOnlyMatchingPart(java.lang.String, java.lang.String)}.
     */
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

    /**
     * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#getNonMatchingLines(java.lang.String, java.lang.String)}.
     */
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

    /**
     * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#getHelp()}.
     */
    @Test
    public void getHelpTest() {
        assertEquals(grepTool.getHelp(), String.format("grep [-cov] [-A num] [-B num] [-C num] [pattern] [file ...]%n"));
        assertEquals(grepTool.getStatusCode(), 0);
    }
    
    /**
     * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#execute(java.io.File, java.lang.string)}.
     */
    @Test
    public void combinedBeforeAfterExecuteTest() throws IOException {
        File file = folder.newFile();
        Files.write(file.toPath(), "1\n2\n3\n4\n\5\n".getBytes());
        grepTool = new GrepTool(new String[]{"-A", "2", "-B", "1", "2", file.getName()});
        assertEquals("1\n2\n3\n4\n", grepTool.execute(folder.getRoot(), null));
    }
}
