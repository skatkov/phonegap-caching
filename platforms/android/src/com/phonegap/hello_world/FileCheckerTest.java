package com.phonegap.hello_world;

import static org.junit.Assert.*;
import static com.phonegap.hello_world.FileChecker.sfvToHash;
import java.io.File;
import java.util.HashMap;

import org.junit.Test;

public class FileCheckerTest {
	@Test
	public void testNotEmpty() {
		FileChecker sfv = new FileChecker();
        assertTrue(sfv.notEmpty());
	}
	
	@Test
	public void testSfv(){
		File sfv = new File("./assets/sfv/local.sfv");
		HashMap<String, String> result = sfvToHash(sfv);
		assertTrue(result.containsKey("poker/audio/slots/spin_2.mp3"));
		assertEquals("FB524549", result.get("poker/audio/slots/spin_2.mp3"));
	}

}
