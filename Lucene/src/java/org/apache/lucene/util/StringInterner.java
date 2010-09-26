package org.apache.lucene.util;

/**
 * Subclasses of StringInterner are required to return the same single String
 * object for all equal strings. Depending on the implementation, this may not
 * be the same object returned as String.intern().
 * 
 * This StringInterner base class simply delegates to String.intern().
 */
public class StringInterner {
	/** Returns a single object instance for each equal string. */
	public String intern(String s) {
		return s.intern();
	}

	/** Returns a single object instance for each equal string. */
	public String intern(char[] arr, int offset, int len) {
		return intern(new String(arr, offset, len));
	}
}
