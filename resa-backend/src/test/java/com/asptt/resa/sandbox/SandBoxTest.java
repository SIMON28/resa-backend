package com.asptt.resa.sandbox;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SandBoxTest {
	
	
	@Test
	public void whenHashCodeIsCalledOnPut_thenCorrect() {
	    MyKey key = new MyKey(1);
	    Map<MyKey, String> map = new HashMap<>();
	    map.put(key, "val");
	}
	
	@Test
	public void whenHashCodeIsCalledOnGet_thenCorrect() {
	    MyKey key = new MyKey(1);
	    Map<MyKey, String> map = new HashMap<>();
	    map.put(key, "val");
	    map.get(key);
	}
}
