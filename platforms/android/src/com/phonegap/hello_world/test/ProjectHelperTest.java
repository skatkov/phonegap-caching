package com.phonegap.hello_world.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import android.util.Log;

import static com.phonegap.hello_world.ProjectHelper.getUrlPath;
import static com.phonegap.hello_world.ProjectHelper.sfvToHash;
public class ProjectHelperTest {
	InputStream localSfv; 
	
	@Before
	public void getStream() throws FileNotFoundException{
		localSfv = new FileInputStream("./assets/sfv/local.sfv");	
	}
	
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
