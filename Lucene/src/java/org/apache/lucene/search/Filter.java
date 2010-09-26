package org.apache.lucene.search;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.DocIdBitSet;

/** 
 *  Abstract base class for restricting which documents may
 *  be returned during searching.
 */
public abstract class Filter implements java.io.Serializable {
  
  /**
   * Creates a {@link DocIdSet} enumerating the documents that should be
   * permitted in search results. <b>NOTE:</b> null can be
   * returned if no documents are accepted by this Filter.
   * <p>
   * Note: This method will be called once per segment in
   * the index during searching.  The returned {@link DocIdSet}
   * must refer to document IDs for that segment, not for
   * the top-level reader.
   * 
   * @param reader a {@link IndexReader} instance opened on the index currently
   *         searched on. Note, it is likely that the provided reader does not
   *         represent the whole underlying index i.e. if the index has more than
   *         one segment the given reader only represents a single segment.
   *          
   * @return a DocIdSet that provides the documents which should be permitted or
   *         prohibited in search results. <b>NOTE:</b> null can be returned if
   *         no documents will be accepted by this Filter.
   * 
   * @see DocIdBitSet
   */
  public abstract DocIdSet getDocIdSet(IndexReader reader) throws IOException;
}
