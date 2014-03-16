package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoggerTest {
	Logging logger;
	ByteArrayOutputStream baos;
	
	@Before
	public void setUp() throws Exception {
		baos = new ByteArrayOutputStream();
		logger = Logging.logger(baos);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void setLogLevel() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		int expected = 100;
		
		logger.setLevel(expected);
		Field intLevel = logger.getClass().getDeclaredField("level");
		intLevel.setAccessible(true);
		int result = (int) intLevel.get(logger);
		
		assertEquals(expected, result);
	}
}
