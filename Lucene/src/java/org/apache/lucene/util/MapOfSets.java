package org.apache.lucene.util;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * Helper class for keeping Lists of Objects associated with keys. <b>WARNING: THIS CLASS IS NOT THREAD SAFE</b>
 */
public class MapOfSets<K, V> {

  private final Map<K, Set<V>> theMap;

  /**
   * @param m the backing store for this object
   */
  public MapOfSets(Map<K, Set<V>> m) {
    theMap = m;
  }

  /**
   * @return direct access to the map backing this object.
   */
  public Map<K, Set<V>> getMap() {
    return theMap;
  }

  /**
   * Adds val to the Set associated with key in the Map.  If key is not 
   * already in the map, a new Set will first be created.
   * @return the size of the Set associated with key once val is added to it.
   */
  public int put(K key, V val) {
    final Set<V> theSet;
    if (theMap.containsKey(key)) {
      theSet = theMap.get(key);
    } else {
      theSet = new HashSet<V>(23);
      theMap.put(key, theSet);
    }
    theSet.add(val);
    return theSet.size();
  }
   /**
   * Adds multiple vals to the Set associated with key in the Map.  
   * If key is not 
   * already in the map, a new Set will first be created.
   * @return the size of the Set associated with key once val is added to it.
   */
  public int putAll(K key, Collection<? extends V> vals) {
    final Set<V> theSet;
    if (theMap.containsKey(key)) {
      theSet = theMap.get(key);
    } else {
      theSet = new HashSet<V>(23);
      theMap.put(key, theSet);
    }
    theSet.addAll(vals);
    return theSet.size();
  }
 
}
