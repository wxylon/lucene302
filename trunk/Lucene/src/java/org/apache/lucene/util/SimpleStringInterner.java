package org.apache.lucene.util;

/**
 * Simple lockless and memory barrier free String intern cache that is
 * guaranteed to return the same String instance as String.intern() does.
 */
public class SimpleStringInterner extends StringInterner {

	private static class Entry {
		final private String str;
		final private int hash;
		private Entry next;

		private Entry(String str, int hash, Entry next) {
			this.str = str;
			this.hash = hash;
			this.next = next;
		}
	}

	private final Entry[] cache;
	private final int maxChainLength;

	/**
	 * @param tableSize
	 *            Size of the hash table, should be a power of two.
	 * @param maxChainLength
	 *            Maximum length of each bucket, after which the oldest item
	 *            inserted is dropped.
	 */
	public SimpleStringInterner(int tableSize, int maxChainLength) {
		cache = new Entry[Math.max(1, BitUtil.nextHighestPowerOfTwo(tableSize))];
		this.maxChainLength = Math.max(2, maxChainLength);
	}

	@Override
	public String intern(String s) {
		int h = s.hashCode();
		// In the future, it may be worth augmenting the string hash
		// if the lower bits need better distribution.
		int slot = h & (cache.length - 1);

		Entry first = this.cache[slot];
		Entry nextToLast = null;

		int chainLength = 0;

		for (Entry e = first; e != null; e = e.next) {
			if (e.hash == h && (e.str == s || e.str.compareTo(s) == 0)) {
				// if (e.str == s || (e.hash == h && e.str.compareTo(s)==0)) {
				return e.str;
			}

			chainLength++;
			if (e.next != null) {
				nextToLast = e;
			}
		}

		// insertion-order cache: add new entry at head
		s = s.intern();
		this.cache[slot] = new Entry(s, h, first);
		if (chainLength >= maxChainLength) {
			// prune last entry
			nextToLast.next = null;
		}
		return s;
	}
}