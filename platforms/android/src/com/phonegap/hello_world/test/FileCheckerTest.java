package com.phonegap.hello_world.test;

import static org.junit.Assert.*;
import static com.phonegap.hello_world.ProjectHelper.sfvToHash;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

import com.phonegap.hello_world.FileChecker;

public class FileCheckerTest {
	InputStream localSfv;
	FileChecker sfv = null;
	
	@Before
	public void setUp() throws FileNotFoundException{
		this.localSfv = new FileInputStream("./assets/sfv/local.sfv");
		this.sfv = new FileChecker(sfvToHash(this.localSfv));
	}
	
	@Test
	public void testConstructor() {
		assertTrue(this.sfv.useCached("poker/audio/slots/spin_2.mp3"));
	}
		
	@Test
	public void testCompareWithLocal(){
		sfv.updateLocal(changeHash());
		
		assertFalse(sfv.useCached("poker/audio/slots/raise_increment.mp3"));
	}
	
	@Test
	public void testUseCached(){
		assertFalse(sfv.useCached("missing/path.mp3"));
		assertTrue(sfv.useCached("poker/audio/slots/raise_increment.mp3"));
		assertFalse(sfv.useCached(null));
	}
	
	private HashMap<String, String> changeHash() {
		HashMap<String, String> updatedHash = sfvToHash(localSfv);
		updatedHash.remove("poker/audio/slots/raise_increment.mp3");
		updatedHash.put("poker/audio/slots/raise_increment.mp3", "HAXOR12");
		return updatedHash;
	}
}
