package com.phonegap.hello_world.test;

import static org.junit.Assert.*;

import org.junit.Test;

import static com.phonegap.hello_world.ProjectHelper.getUrlPath;
public class ProjectHelperTest {
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
}
