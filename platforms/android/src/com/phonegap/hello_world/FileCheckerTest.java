package com.phonegap.hello_world;

import static org.junit.Assert.*;
import static com.phonegap.hello_world.FileChecker.sfvToHash;
import static com.phonegap.hello_world.FileChecker.compareEntries;
import java.io.File;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

public class FileCheckerTest {
	final File localSfv = new File("./assets/sfv/local.sfv");
	FileChecker sfv = null;
	
	@Before
	public void setUp(){
		this.sfv = new FileChecker(sfvToHash(this.localSfv));
	}
	
	@Test
	public void testConstructor() {
		assertEquals("FB524549", this.sfv.localSfv.get("poker/audio/slots/spin_2.mp3"));
	}
	
	@Test
	public void testSfv(){
		HashMap<String, String> result = sfvToHash(localSfv);
		assertTrue(result.containsKey("poker/audio/slots/spin_2.mp3"));
		assertEquals("FB524549", result.get("poker/audio/slots/spin_2.mp3"));
	}
	
	@Test
	public void compareEntriesTest(){
		assertTrue(1 == compareEntries(sfv.localSfv, changeHash()).size());
	}
	
	@Test
	public void compareWithLocal(){
		sfv.updateLocal(changeHash());
		
		assertNull(sfv.localSfv.get("poker/audio/slots/raise_increment.mp3"));
	}

	private HashMap<String, String> changeHash() {
		HashMap<String, String> updatedHash = sfvToHash(localSfv);
		updatedHash.remove("poker/audio/slots/raise_increment.mp3");
		updatedHash.put("poker/audio/slots/raise_increment.mp3", "HAXOR12");
		return updatedHash;
	}

	
	
}
