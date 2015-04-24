package com.example.testndk;

import java.io.InputStream;

public class Good {
	public static String testcommo1n() {
		System.out.println("Good.testcommo1n()");
		System.out.println("Good.testcommo1n() " + Good.class.getClassLoader());

		InputStream inputStream = Good.class.getClassLoader()
				.getResourceAsStream("AndroidManifest.xml")
		/*
		 * Good.class .getResourceAsStream("/AndroidManifest.xml")
		 */;
		String s = android4me.res.AXMLPrinter.getManifestXMLFromAPK(inputStream);
		System.out.println("Good.testcommo1n() " + s);
		return "testcommon";
		
		
	}

	public static void test2() {
		System.out.println("Good.test2()");
	}

	public String testcommon() {
		System.out.println("Good.testcommon()");
		return "testcommon";
	}

	public native String testx();

	static {
		// PluginCache.initSoLib("testndk");
		System.out.println("Good.enclosing_method() === ");
		System.loadLibrary("testndk");
	}

}
