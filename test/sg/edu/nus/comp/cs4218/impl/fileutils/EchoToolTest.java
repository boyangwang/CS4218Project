package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;

public class EchoToolTest {
    private IEchoTool echoTool;

    @Before
    public void before() {
        echoTool = new EchoTool(new String[0]);
    }

    @After
    public void after() {
        echoTool = null;
    }

    /**
     * MUT: echo()
     * Should return parameter passed.
     */
    @Test
    public void echoTest() {
        String expected = "foobar";
        String result = echoTool.echo(expected);
        assertEquals(result, expected);
        assertEquals(echoTool.getStatusCode(), 0);
    }

    /**
     * MUT: echo()
     * Should return parameter passed with non-success status code.
     */
    @Test
    public void echoNull() {
        String result = echoTool.echo(null);
        assertNull(result);
        assertNotEquals(echoTool.getStatusCode(), 0);
    }
}
