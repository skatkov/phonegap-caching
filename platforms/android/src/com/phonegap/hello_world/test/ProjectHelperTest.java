package com.phonegap.hello_world.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;

import org.junit.Test;

import static com.phonegap.hello_world.ProjectHelper.getUrlPath;
import static com.phonegap.hello_world.ProjectHelper.sfvToHash;
public class ProjectHelperTest {
	final File localSfv = new File("./assets/sfv/local.sfv");
	
	@Test
	public void testGetUrlPath() {
		assertEquals("css/main.css", getUrlPath("http://phonegap-test.herokuapp.com/css/main.css"));
	}
	
	@Test
	public void testHttpsUrlPath(){
		assertEquals("css/main.css", getUrlPath("https://phonegap-test.herokuapp.com/css/main.css"));
	}
	
	@Test
	public void testEmptyPath(){
		assertEquals(null, getUrlPath("https://phonegap-test.herokuapp.com/"));
	}
	
	@Test
	public void testBullshitUrl(){
		assertEquals(null,getUrlPath("hp://bulshitUrl.com"));
		assertEquals(null,getUrlPath(""));
	}
	
	@Test
	public void testSfvToHash(){
		HashMap<String, String> result = sfvToHash(localSfv);
		assertTrue(result.containsKey("poker/audio/slots/spin_2.mp3"));
		assertEquals("FB524549", result.get("poker/audio/slots/spin_2.mp3"));
	}
}
