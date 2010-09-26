package org.apache.lucene.util;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

/**
 * A serializable Enum class.
 * @deprecated Use Java 5 enum, will be removed in a later Lucene 3.x release.
 */
@SuppressWarnings("serial")
public abstract class Parameter implements Serializable
{
  static Map<String,Parameter> allParameters = new HashMap<String,Parameter>();
  
  private String name;
  
  private Parameter() {
    // typesafe enum pattern, no public constructor
  }
  
  protected Parameter(String name) {
    // typesafe enum pattern, no public constructor
    this.name = name;
    String key = makeKey(name);
    
    if(allParameters.containsKey(key))
      throw new IllegalArgumentException("Parameter name " + key + " already used!");
    
    allParameters.put(key, this);
  }
  
  private String makeKey(String name){
    return getClass() + " " + name;
  }
  
  @Override
  public String toString() {
    return name;
  }
  
  /**
   * Resolves the deserialized instance to the local reference for accurate
   * equals() and == comparisons.
   * 
   * @return a reference to Parameter as resolved in the local VM
   * @throws ObjectStreamException
   */
  protected Object readResolve() throws ObjectStreamException {
    Object par = allParameters.get(makeKey(name));
    
    if(par == null)
      throw new StreamCorruptedException("Unknown parameter value: " + name);
      
    return par;
  }
  
 }
